package ar.edu.itba.paw.webapp.vndTypes;

public class VndType {

    private VndType(){}

    // Tipo de medio para una lista de actores
    public static final String APPLICATION_ACTOR_LIST = "application/vnd.actor-list.v1+json";

    // Tipo de medio para un actor individual
    public static final String APPLICATION_ACTOR = "application/vnd.actor.v1+json";

    // Tipo de medio para un comentario individual
    public static final String APPLICATION_COMMENT = "application/vnd.comment.v1+json";

    // Tipo de medio para crear un comentario individual
    public static final String APPLICATION_COMMENT_FORM = "application/vnd.comment-form.v1+json";

    // Tipo de medio para crear un feedback de un comentario individual
    public static final String APPLICATION_COMMENT_FEEDBACK_FORM = "application/vnd.comment-feedback-form.v1+json";

    // Tipo de medio para una lista de comentarios
    public static final String APPLICATION_COMMENT_LIST = "application/vnd.comment-list.v1+json";

    // Tipo de medio para una lista de directores
    public static final String APPLICATION_DIRECTOR_LIST = "application/vnd.director-list.v1+json";

    // Tipo de medio para un director individual
    public static final String APPLICATION_DIRECTOR = "application/vnd.director.v1+json";

    // Tipo de medio para una lista de generos
    public static final String APPLICATION_GENRE_LIST = "application/vnd.genre-list.v1+json";

    // Tipo de medio para un genero
    public static final String APPLICATION_GENRE = "application/vnd.genre.v1+json";

    // Tipo de medio para una lista de medias
    public static final String APPLICATION_MEDIA_LIST = "application/vnd.media-list.v1+json";

    // Tipo de medio para una media individual
    public static final String APPLICATION_MEDIA = "application/vnd.media.v1+json";

    // Tipo de medio para una lista de MoovieList
    public static final String APPLICATION_MOOVIELIST_LIST = "application/vnd.moovielist-list.v1+json";

    // Tipo de medio para una lista de MoovieList
    public static final String APPLICATION_MOOVIELIST = "application/vnd.moovielist.v1+json";

    // Tipo de medio para feedback de una lista de MoovieList
    public static final String APPLICATION_MOOVIELIST_FEEDBACK_FORM = "application/vnd.moovielist-feedback-form.v1+json";

    // Tipo de medio para operaciones de creación/edición de una MoovieList
    public static final String APPLICATION_MOOVIELIST_FORM = "application/vnd.moovielist-form.v1+json";

    // Tipo de medio para operaciones de seguimiento (Follow/Unfollow)
    public static final String APPLICATION_MOOVIELIST_FOLLOW_FORM = "application/vnd.moovielist-follow-form.v1+json";

    // Tipo de medio para un medio (Media) dentro de una MoovieList
    public static final String APPLICATION_MOOVIELIST_MEDIA = "application/vnd.moovielist-media.v1+json";

    // Tipo de medio para una lista de medios (Media) dentro de una MoovieList
    public static final String APPLICATION_MOOVIELIST_MEDIA_LIST = "application/vnd.moovielist-media-list.v1+json";

    // Tipo de medio para operaciones relacionadas con medios (Media) en una MoovieList
    public static final String APPLICATION_MOOVIELIST_MEDIA_FORM = "application/vnd.moovielist-media-form.v1+json";

    // Tipo de medio para una reseña individual de MoovieList
    public static final String APPLICATION_MOOVIELIST_REVIEW = "application/vnd.moovielist-review.v1+json";

    // Tipo de medio para una lista de reseñas de MoovieList
    public static final String APPLICATION_MOOVIELIST_REVIEW_LIST = "application/vnd.moovielist-review-list.v1+json";

    // Tipo de medio para operaciones de creación/edición de una reseña de MoovieList
    public static final String APPLICATION_MOOVIELIST_REVIEW_FORM = "application/vnd.moovielist-review-form.v1+json";

    // Tipo de medio para un perfil individual
    public static final String APPLICATION_PROFILE = "application/vnd.profile.v1+json";

    // Tipo de medio para una lista de perfiles
    public static final String APPLICATION_PROFILE_LIST = "application/vnd.profile-list.v1+json";

    // Tipo de medio para la lista de "Watched"
    public static final String APPLICATION_WATCHED_LIST = "application/vnd.watched-list.v1+json";

    // Tipo de medio para un medio en la lista de "Watched"
    public static final String APPLICATION_WATCHED_MEDIA = "application/vnd.watched-media.v1+json";

    // Tipo de medio para operaciones de creación/edición de una media de Watched
    public static final String APPLICATION_WATCHED_MEDIA_FORM = "application/vnd.watched-media-form.v1+json";

    // Tipo de medio para la lista de "Watchlist"
    public static final String APPLICATION_WATCHLIST_LIST = "application/vnd.watchlist-list.v1+json";

    // Tipo de medio para un medio en la lista de "Watchlist"
    public static final String APPLICATION_WATCHLIST_MEDIA = "application/vnd.watchlist-media.v1+json";

    // Tipo de medio para un conteo en la lista de "Watchlist"
    public static final String APPLICATION_WATCHLIST_COUNT = "application/vnd.watchlist-count.v1+json";

    // Tipo de medio para operaciones de creación/edición de una media de Watchlist
    public static final String APPLICATION_WATCHLIST_MEDIA_FORM = "application/vnd.watchlist-media-form.v1+json";

    // Tipo de medio para la lista de listas seguidas (Follows)
    public static final String APPLICATION_FOLLOWED_LISTS = "application/vnd.followed-lists.v1+json";

    // Tipo de medio para la lista de listas seguidas (Follows)
    public static final String APPLICATION_FOLLOWED_LISTS_USER_LIST = "application/vnd.followed-lists-user-list.v1+json";

    // Tipo de medio para la lista de likes de listas
    public static final String APPLICATION_LIST_LIKE_LISTS = "application/vnd.list-like-lists.v1+json";

    // Tipo de medio para like de lista
    public static final String APPLICATION_LIST_LIKE = "application/vnd.list-like.v1+json";

    // Tipo de medio para  likes  de review
    public static final String APPLICATION_REVIEW_LIKE = "application/vnd.review-like.v1+json";

    // Tipo de medio para  likes  de review de lista
    public static final String APPLICATION_MOOVIELIST_REVIEW_LIKE = "application/vnd.moovielist-review-like.v1+json";

    // Tipo de medio para  likes  de comments
    public static final String APPLICATION_COMMENT_LIKE = "application/vnd.comment-like.v1+json";

    // Tipo de medio para una lista de proveedores "
    public static final String APPLICATION_PROVIDER_LIST = "application/vnd.provider-list.v1+json";

    // Tipo para providers
    public static final String APPLICATION_PROVIDER = "application/vnd.provider.v1+json";

    // Tipo de medio para una lista de reportes
    public static final String APPLICATION_REPORT_LIST = "application/vnd.report-list.v1+json";

    // Tipo de medio para un reporte individual
    public static final String APPLICATION_REPORT = "application/vnd.report.v1+json";

    // Tipo de medio para operaciones de creación de reportes
    public static final String APPLICATION_REPORT_FORM = "application/vnd.report-form.v1+json";

    // Tipo de medio para operaciones de creación de reportes
    public static final String APPLICATION_REPORT_COUNT = "application/vnd.report-count.v1+json";

    // Tipo de medio para una reseña individual
    public static final String APPLICATION_REVIEW = "application/vnd.review.v1+json";

    // Tipo de medio para una lista de reseñas
    public static final String APPLICATION_REVIEW_LIST = "application/vnd.review-list.v1+json";

    // Tipo de medio para operaciones de creación/edición de una reseña
    public static final String APPLICATION_REVIEW_FORM = "application/vnd.review-form.v1+json";

    // Tipo de medio para un tvcreator individual
    public static final String APPLICATION_TVCREATOR = "application/vnd.tvcreator.v1+json";

    // Tipo de medio para una lista de tvcreators
    public static final String APPLICATION_TVCREATOR_LIST = "application/vnd.tvcreator-list.v1+json";

    // Tipo de medio para un user individual
    public static final String APPLICATION_USER = "application/vnd.user.v1+json";

    public static final String APPLICATION_USER_PASSWORD = "application/vnd.user_password.v1+json";

    // Tipo de medio para una lista de users
    public static final String APPLICATION_USER_LIST = "application/vnd.user-list.v1+json";

    // Tipo de medio para operaciones de creación/edición de un user
    public static final String APPLICATION_USER_FORM = "application/vnd.user-form.v1+json";

    // Tipo de medio para token de un user individual
    public static final String APPLICATION_USER_TOKEN = "application/vnd.user_token.v1+json";

    // Tipo de medio para token de un user individual
    public static final String APPLICATION_USER_COUNT = "application/vnd.user_count.v1+json";

    // Tipo de medio para form de token de un user individual
    public static final String APPLICATION_USER_TOKEN_FORM = "application/vnd.user_token_form.v1+json";

    public static final String APPLICATION_RESEND_TOKEN_FORM = "application/vnd.resend_token_form.v1+json";

    public static final String APPLICATION_PASSWORD_TOKEN_FORM = "application/vnd.password_token_form.v1+json";

    // Tipo de medio para banear o desbanear un user individual
    public static final String APPLICATION_USER_BAN_FORM = "application/vnd.user-ban-form.v1+json";

    public static final String APPLICATION_USER_BAN_MESSAGE = "application/vnd.user-ban-message.v1+json";

}

