import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import {Image} from "react-bootstrap";
import Logo from '../../../images/logo.png';
import {NavLink, useLocation, useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {logout} from "../../../features/authSlice";
import ProfileImage from "../profileImage/ProfileImage";
import React from "react";
import SearchBar from "../searchBar/SearchBar";
import RoleBadge from "../RoleBadge/RoleBadge";
import {useTranslation} from "react-i18next";
import './navbarStyle.css';
import UserRoles from "../../../api/values/UserRoles";

function NavbarComponent({ onLocationChange }) {

    const dispatch = useDispatch();
    const {isLoggedIn, user} = useSelector(state => state.auth);
    const { t } = useTranslation();
    const location = useLocation();
    const navigate = useNavigate();

    // Add effect to call onLocationChange when location changes
    React.useEffect(() => {
        onLocationChange(location);
    }, [location, onLocationChange]);

    const handleLoginRedirect = () => {
        navigate("/login", { state: { from: location.pathname } });
    };


    const handleLogout = () => {
        dispatch(logout());
    };

    return (
        <Navbar expand="lg"  sticky="top"  className="navbar navbar-expand-lg navbar-light bg-body-tertiary container-nav">
            <Container fluid>
                <Navbar.Brand as={NavLink} to="/" className="d-flex align-items-center">
                    <Image src={Logo} width={50} className="me-2"/> Moovie
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link as={NavLink} to="/discover" activeClassName="active">{t('navBar.discover')}</Nav.Link>
                        <Nav.Link as={NavLink} to="/browseLists" activeClassName="active">{t('navBar.browseLists')}</Nav.Link>
                        {isLoggedIn
                            && <Nav.Link as={NavLink} to="/createList" activeClassName="active">{t('navBar.createList')}</Nav.Link>
                        }
                        <NavDropdown title={t('navBar.topRated')} id="basic-nav-dropdown">
                            <NavDropdown.Item as={NavLink} to="featuredLists/topRatedMedia" activeClassName="active">{t('navBar.media')}</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="featuredLists/topRatedMovies" activeClassName="active">{t('navBar.movies')}</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="featuredLists/topRatedSeries" activeClassName="active">{t('navBar.series')}</NavDropdown.Item>
                        </NavDropdown>
                        <NavDropdown title={t('navBar.mostPopular')} id="basic-nav-dropdown">
                            <NavDropdown.Item as={NavLink} to="featuredLists/mostPopularMedia" activeClassName="active">{t('navBar.media')}</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="featuredLists/mostPopularMovies" activeClassName="active">{t('navBar.movies')}</NavDropdown.Item>
                            <NavDropdown.Item as={NavLink} to="featuredLists/mostPopularSeries" activeClassName="active">{t('navBar.series')}</NavDropdown.Item>
                        </NavDropdown>
                        <Nav.Link as={NavLink} to="/leaderboard" activeClassName="active">{t('navBar.leaderboard')}</Nav.Link>
                    </Nav>
                    <SearchBar/>
                    <Nav className="d-flex nav-item justify-content-center userPic-login">
                        {user && (
                            <div style={{display: 'flex', alignItems: 'center'}}>
                            <RoleBadge role={user.role} size={"50px"}></RoleBadge>
                            <ProfileImage
                                username = {user.username}
                                size="60px" // Adjust size as needed
                                defaultProfilePicture="https://example.com/default-profile.jpg" // Your default image URL
                            />
                            </div>
                        )}
                        {isLoggedIn ? (
                            <div style={{display: 'flex', alignItems: 'center'}}>
                                <NavDropdown
                                    title={`${user.username} `}
                                    id="basic-nav-dropdown"
                                    drop="down"
                                    className="custom-dropdown"
                                >
                                    <NavDropdown.Item as={NavLink}
                                                      to={`/profile/${user.username}`}>{t('navBar.profile')}</NavDropdown.Item>
                                    {user.role === UserRoles.MODERATOR && (
                                        <NavDropdown.Item as={NavLink} to={`/reports`}>{t('navBar.reports')}</NavDropdown.Item>
                                    )}
                                    <NavDropdown.Item onClick={handleLogout}>{t('navBar.logout')}</NavDropdown.Item>
                                </NavDropdown>
                            </div>
                                ) : (
                                    <Nav.Link onClick={handleLoginRedirect} className={'link-primary'}>
                                        {t('navBar.login')}
                                    </Nav.Link>
                                )}
                            </Nav>

                            </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavbarComponent;