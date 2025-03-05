package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.BannedMessage.BannedMessage;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.auth.JwtTokenProvider;

import ar.edu.itba.paw.webapp.dto.in.BanUserDTO;
import ar.edu.itba.paw.webapp.dto.in.TokenDto;
import ar.edu.itba.paw.webapp.dto.in.UserCreateDto;
import ar.edu.itba.paw.webapp.dto.out.BanMessageDTO;
import ar.edu.itba.paw.webapp.dto.in.*;
import ar.edu.itba.paw.webapp.dto.out.CountDto;
import ar.edu.itba.paw.webapp.dto.out.UserDto;

import ar.edu.itba.paw.webapp.exceptions.VerificationTokenNotFoundException;
import ar.edu.itba.paw.webapp.mappers.*;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

@Path("users")
@Component
public class UserController {

    private static int DEFAULT_PAGE_INT = 1;

    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModeratorService moderatorService;
    private final BannedService bannedService;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(final UserService userService,
                          VerificationTokenService verificationTokenService, JwtTokenProvider jwtTokenProvider, ModeratorService moderatorService, BannedService bannedService) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.moderatorService = moderatorService;
        this.bannedService = bannedService;
    }

    @GET
    @Produces(VndType.APPLICATION_USER_LIST)
    public Response getUsers(
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("email") final String email,
            @QueryParam("id") final Integer id,
            @QueryParam("role") final Integer role) {

//        CHECK for invalid role
        if (role != null) {
            UserRoles enumRole = UserRoles.getRoleFromInt(role);
            if (enumRole == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }


        // Si se proporciona un ID, buscar por ID
        if (id != null) {
            try {
                final User user = userService.findUserById(id);
                if (user == null) {
                    LOGGER.info("User with ID {} not found. Returning NOT_FOUND.", id);
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
                return Response.ok(UserDto.fromUser(user, uriInfo)).build();
            } catch (RuntimeException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
            }
        }

        // Buscar por email si se proporciona
        if (email != null && !email.isEmpty()) {
            try {
                final User user = userService.findUserByEmail(email);
                if (user == null) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
                return Response.ok(UserDto.fromUser(user, uriInfo)).build();
            } catch (RuntimeException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
            }
        }

        // Validar número de página
        if (page < DEFAULT_PAGE_INT) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // Listar usuarios paginados
        try {
            final List<User> all;
            if (role == null) {
                all = userService.listAll(page);
            } else {
                all = userService.listAll(role, page);
            }
            if (all.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            List<UserDto> dtoList = UserDto.fromUserList(all, uriInfo);
            return Response.ok(new GenericEntity<List<UserDto>>(dtoList) {
            }).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @POST
    @Produces(VndType.APPLICATION_USER)
    @Consumes(VndType.APPLICATION_USER_FORM)
    public Response createUser(@Valid final UserCreateDto userCreateDto) {
        LOGGER.info("Method: createUser, Path: /users, UserCreateDto: {}", userCreateDto);
        try {
            LOGGER.info("Attempting to create user with username: {}, email: {}", userCreateDto.getUsername(), userCreateDto.getEmail());
            userService.createUser(userCreateDto.getUsername(), userCreateDto.getEmail(), userCreateDto.getPassword());
            final User user = userService.findUserByUsername(userCreateDto.getUsername());
            return Response.created(uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getUserId())).build()).entity(UserDto.fromUser(user, uriInfo)).build();
        }  catch (UnableToCreateUserException e) {
            LOGGER.info("User already exists. Returning CONFLICT.");
            return Response.status(Response.Status.CONFLICT).entity("User already exists").build();
        } catch (RuntimeException e) {
            LOGGER.error("Error creating user: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Consumes(VndType.APPLICATION_USER_TOKEN_FORM)
    @Produces(VndType.APPLICATION_USER_TOKEN)
    public Response verifyUser(@Valid @NotNull final TokenDto tokenDto) {
        String tokenString = tokenDto.getToken();
        LOGGER.info("Method: verifyUser, Path: users, Token: {}", tokenString);
        try {
            final Optional<Token> tok = verificationTokenService.getToken(tokenString);
            if (tok.isPresent()) {
                Token token = tok.get();
                if (userService.confirmRegister(token)) {
                    User user = userService.findUserById(token.getUserId());
                    String jwt = jwtTokenProvider.createToken(user);
                    return Response.ok(UserDto.fromUser(user, uriInfo)).header(HttpHeaders.AUTHORIZATION, jwt).build();
                }
                LOGGER.info("Token validation failed. Returning INTERNAL_SERVER_ERROR.");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            LOGGER.info("Token not found. Returning BAD_REQUEST.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (VerificationTokenNotFoundException e) {
            LOGGER.error("Verification token not found: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(VndType.APPLICATION_RESEND_TOKEN_FORM)
    @Produces(VndType.APPLICATION_USER_TOKEN)
    public Response resendVerificationEmail(@Valid final TokenDto tokenDto) {
        LOGGER.info("Method: resendVerificationEmail, Path: /users/, Token: {}", tokenDto.getToken());

        try {
            final Optional<Token> tokenOptional = verificationTokenService.getToken(tokenDto.getToken());

            if (!tokenOptional.isPresent()) {
                LOGGER.info("Token not found. Returning NOT_FOUND.");
                return Response.status(Response.Status.NOT_FOUND).entity("Token not found").build();
            }
            Token token = tokenOptional.get();
            if (userService.findUserById(token.getUserId()).getRole() > 0) {
                throw new UserVerifiedException("User already verified.");
            }
            userService.resendVerificationEmail(token);

            LOGGER.info("Verification email resent successfully for user ID: {}", token.getUserId());
            return Response.ok().entity("Verification email resent successfully").build();

        } catch (VerificationTokenNotFoundException e) {
            LOGGER.error("Verification token not found: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity("Verification token not found").build();
        } catch (UserVerifiedException e) {
            LOGGER.error("User is already verified. Returning CONFLICT.");
            return Response.status(Response.Status.CONFLICT).entity("User is already verified").build();
        }
        catch (Exception e) {
            LOGGER.error("Error resending verification email: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to resend verification email").build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(VndType.APPLICATION_PASSWORD_TOKEN_FORM)
    public Response createPasswordResetToken(@Valid UserEmailDto userEmailDto) {
        LOGGER.info("Method: createPasswordResetToken, Path: /users, Email: {}", userEmailDto.getEmail());
        try {
            final User user = userService.findUserByEmail(userEmailDto.getEmail());
            final String token = userService.forgotPassword(user);
            return Response.created(uriInfo.getAbsolutePathBuilder().path(token).build()).build();
        } catch (RuntimeException e) {
            LOGGER.error("Error creating password reset token: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }

    }

    @PUT
    @Consumes(VndType.APPLICATION_USER_PASSWORD)
    public Response resetPassword(@Valid UserResetPasswordDto userResetPasswordDto) {
        LOGGER.info("Method: resetPassword, Path: /users, Token: {}", userResetPasswordDto.getToken());
        try {
            final Optional<Token> tokenOptional = verificationTokenService.getToken(userResetPasswordDto.getToken());
            if (!tokenOptional.isPresent()) {
                LOGGER.info("Token not found. Returning NOT_FOUND.");
                return Response.status(Response.Status.NOT_FOUND).entity("Token not found").build();
            }
            Token tok = tokenOptional.get();
            userService.resetPassword(tok, userResetPasswordDto.getPassword());
            return Response.noContent().build();
        } catch (RuntimeException e) {
            LOGGER.error("Error reset passoword: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }

    }

    @GET
    @Path("/count")
    @Produces(VndType.APPLICATION_USER_COUNT)
    public Response getUserCount() {
        LOGGER.info("Method: getUserCount, Path: /users/count");
        try {
            int count = userService.getUserCount();
            LOGGER.info("User count retrieved: {}", count);
            return Response.ok(CountDto.fromCount(count)).build();
        } catch (Exception e) {
            LOGGER.error("Error retrieving user count: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GET
    @Produces(VndType.APPLICATION_USER)
    @Path("/{username}")
    public Response getUserByUsername(@PathParam("username") final String username) {
        LOGGER.info("Method: getUserByUsername, Path: /users/{username}, Username: {}", username);
        final User user = userService.findUserByUsername(username);
        if (user == null) {
            LOGGER.info("User with username {} not found. Returning NOT_FOUND.", username);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(UserDto.fromUser(user, uriInfo)).build();
    }


//    MODERATION

    @GET
    @Path("/{username}/banMessage")
    @PreAuthorize("@accessValidator.isUserAdmin()")
    @Produces(VndType.APPLICATION_USER_BAN_MESSAGE)
    public Response banMessage(@PathParam("username") final String username) {
        try {
            User user = userService.findUserByUsername(username);
            BannedMessage message = bannedService.getBannedMessage(user.getUserId());
            if (message == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            LOGGER.info("RETURNING BAN MESSAGE: " + message.getMessage());
            return Response.ok(BanMessageDTO.fromBannedMessage(message, user.getUsername(), uriInfo)).build();
        }catch (BannedMessageNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PUT
    @Path("/{username}")
    @PreAuthorize("@accessValidator.isUserAdmin()")
    @Consumes(VndType.APPLICATION_USER_BAN_FORM)
    @Produces(VndType.APPLICATION_USER)
    public Response banUser(@PathParam("username") final String username,
                            @Valid @NotNull final BanUserDTO banUserDTO) {
        try {
            if (banUserDTO.getModAction().equals("UNBAN")) {
                User toUnban = userService.findUserByUsername(username);
                try {
                    moderatorService.unbanUser(toUnban.getUserId());
                } catch (InvalidAuthenticationLevelRequiredToPerformActionException e) {
                    return new InvalidAuthenticationLevelRequiredToPerformActionEM().toResponse(e);
                } catch (UnableToChangeRoleException e) {
                    return new UnableToChangeRoleEM().toResponse(e);
                }
                User finalUser = userService.findUserByUsername(username);
                return Response.ok(UserDto.fromUser(finalUser, uriInfo)).build();
            } else if (banUserDTO.getModAction().equals("BAN")) {
                User toBan = userService.findUserByUsername(username);
                try {
                    this.moderatorService.banUser(toBan.getUserId(), banUserDTO.getBanMessage());
                } catch (UnableToBanUserException e) {
                    return new UnableToBanUserEM().toResponse(e);
                } catch (InvalidAuthenticationLevelRequiredToPerformActionException e) {
                    return new InvalidAuthenticationLevelRequiredToPerformActionEM().toResponse(e);
                }
                User finalUser = userService.findUserByUsername(username);
                return Response.ok(UserDto.fromUser(finalUser, uriInfo)).build();
            }
        } catch (UnableToFindUserException e) {
            return new UnableToFindUserEM().toResponse(e);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{username}")
    @PreAuthorize("@accessValidator.isUserAdmin()")
    @Produces(VndType.APPLICATION_USER)
    public Response makeUserMod(@PathParam("username") final String username) {
        User user = null;
        try {
            user = userService.findUserByUsername(username);
            moderatorService.makeUserModerator(user.getUserId());
        } catch (UnableToFindUserException e) {
            return new UnableToFindUserEM().toResponse(e);
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
//        Actualizar el modelo a devolver
//        Llegar aqui implica un exito en la operacion
        user.setRole(UserRoles.MODERATOR.getRole());
        return Response.ok(UserDto.fromUser(user, uriInfo)).build();
    }


}
