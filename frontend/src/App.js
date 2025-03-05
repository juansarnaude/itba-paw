import {Helmet, HelmetProvider} from "react-helmet-async";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import React, {lazy, Suspense, useEffect, useState, useCallback} from "react";
import Loader from "./pages/Loader";
import Nav from "./pages/components/navBar/navbar";
import {useDispatch} from "react-redux";
import {attemptReconnect, refreshUserData} from "./features/authSlice";
import 'bootstrap/dist/css/bootstrap.min.css';
import LoggedGate from "./pages/components/permissions/LoggedGate";
import RoleGate from "./pages/components/permissions/RoleGate";
import UserRoles from "./api/values/UserRoles";
import ContextProviderWrapper from "./store/contextProviderWrapper";

const views = './pages/views';


const Home = lazy(() => import(views + '/home'));
const Login = lazy(() => import(views + '/authViews/login'));
const Register = lazy(() => import(views + '/authViews/register'));
const Details = lazy(() => import(views + '/details'));
const List = lazy(() => import(views + '/list'));
const CreateList = lazy(() => import(views + '/createListView/createListView'));
const BrowseLists = lazy(() => import(views + '/browseLists'));
const Discover = lazy(() => import(views + '/discover'));
const FeaturedLists = lazy(() => import(views + '/featuredLists'));
const MilkyLeaderboard = lazy(() => import(views + '/milkyLeaderboard'));
const Search = lazy(() => import(views + '/search'));
const Profile = lazy(() => import(views + '/profile'));
const Cast = lazy(() => import(views + '/cast'));
const Healthcheck = lazy(() => import(views + '/healthcheck'));
const Error404 = lazy(() => import(views + '/errorViews/error404'));
const AuthTest = lazy(() => import(views + '/AuthTest')); // Import AuthTest
const ReportsDashboard = lazy(() => import(views + '/reportsDashboard/ReportsDashboard'));
const ConfirmToken = lazy(() => import(views + '/authViews/confirmToken'));
const AwaitEmailValidation = lazy(() => import(views + '/authViews/awaitEmailValidation'));
const ForgotPassword = lazy(() => import(views + '/authViews/forgotPasswordView'));
const ResetPassword = lazy(() => import(views + '/authViews/resetPasswordView'));
const ErrorBanned = lazy(() => import(views + '/errorViews/errorBanned'));

export default function App() {
    const helmetContext = {};
    const dispatch = useDispatch();
    const [isInitialized, setIsInitialized] = useState(false);

    useEffect(() => {
        dispatch(attemptReconnect())
            .finally(() => {
                setIsInitialized(true);
            });
    }, [dispatch]);

    const handleLocationChange = useCallback((location) => {
        if (isInitialized) {
            dispatch(refreshUserData()).catch(console.error);
        }
    }, [dispatch, isInitialized]);

    if (!isInitialized) {
        return <Loader/>;
    }

    return (
        <HelmetProvider context={helmetContext}>
            <Helmet>
                <title>Moovie</title>
            </Helmet>
            <BrowserRouter basename={process.env.PUBLIC_URL}>
                <Suspense fallback={<Loader/>}>
                    <Nav onLocationChange={handleLocationChange}/>
                    <ContextProviderWrapper>
                    <Routes>
                            <Route path='/' element={<RoleGate><Home/></RoleGate>}/>
                            <Route path='/login' element={<LoggedGate protectForLoggedIn={true}><Login/></LoggedGate>}/>
                            <Route path='/passwordRecovery'
                                   element={<LoggedGate protectForLoggedIn={true}><ForgotPassword/></LoggedGate>}/>
                            <Route path='/reset-password'
                                   element={<LoggedGate protectForLoggedIn={true}><ResetPassword/></LoggedGate>}/>
                            <Route path='/register'
                                   element={<LoggedGate protectForLoggedIn={true}><Register/></LoggedGate>}/>
                            <Route path='/register/verify' element={<LoggedGate
                                protectForLoggedIn={true}><AwaitEmailValidation/></LoggedGate>}/>
                            <Route path='/register/confirm'
                                   element={<LoggedGate protectForLoggedIn={true}><ConfirmToken/></LoggedGate>}/>
                            <Route path='/details/:id' element={<RoleGate><Details/></RoleGate>}/>
                            <Route path='/list/:id' element={<RoleGate><List/></RoleGate>}/>
                            <Route path='/discover' element={<RoleGate><Discover/></RoleGate>}/>
                            <Route path='/browseLists' element={<RoleGate><BrowseLists/></RoleGate>}/>
                            <Route path='/createList'
                                   element={<LoggedGate><RoleGate><CreateList/></RoleGate></LoggedGate>}/>
                            <Route path='/featuredLists/:type' element={<RoleGate><FeaturedLists/></RoleGate>}/>
                            <Route path='/leaderboard' element={<RoleGate><MilkyLeaderboard/></RoleGate>}/>
                            <Route path='/profile/:username' element={<RoleGate><Profile/></RoleGate>}/>
                            <Route path='/search/:search' element={<RoleGate><Search/></RoleGate>}/>
                            <Route path='/cast/actor/:id' element={<RoleGate><Cast/></RoleGate>}/>
                            <Route path='/cast/director/:id' element={<RoleGate><Cast/></RoleGate>}/>
                            <Route path='/tvcreators/:id' element={<RoleGate><Cast/></RoleGate>}/>
                            <Route path='/healthcheck' element={<RoleGate><Healthcheck/></RoleGate>}/>
                            <Route path='/authtest' element={<RoleGate><AuthTest/></RoleGate>}/>
                            <Route path='/reports'
                                   element={<RoleGate role={UserRoles.MODERATOR}><ReportsDashboard/></RoleGate>}/>
                            <Route path='*' element={<Error404/>}/>
                    </Routes>
                    </ContextProviderWrapper>
                </Suspense>
            </BrowserRouter>
        </HelmetProvider>
    );
}
