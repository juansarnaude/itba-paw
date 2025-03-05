import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";

const LoggedGate = ({ children, protectForLoggedIn = false }) => {
    const { isLoggedIn } = useSelector(state => state.auth);
    const navigate = useNavigate();

    useEffect(() => {
        // If logged in, redirect to home page
        if (protectForLoggedIn && isLoggedIn) {
            navigate('/');
        }
        // If not logged in, redirect to login page
        else if (!protectForLoggedIn && !isLoggedIn) {
            navigate('/login');
        }
    }, [isLoggedIn, navigate, protectForLoggedIn]);

    return <>{children}</>;
};

export default LoggedGate;
