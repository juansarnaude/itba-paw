import {createContext, useEffect, useMemo, useState} from "react";
import {useLocation} from "react-router-dom";
import Error404 from "../pages/views/errorViews/error404";
import Error403 from "../pages/views/errorViews/error403";
import Error500 from "../pages/views/errorViews/error500";
import Error409 from "../pages/views/errorViews/error409";


const ErrorStatusContext = createContext({});

export const ErrorHandler = ({children}) => {
    const location = useLocation();
    const [errorStatus, setErrorStatus] = useState();

    useEffect(() => {
        return setErrorStatus(undefined);
    }, [location.pathname]);

    const handleErrors = () => {
        if (errorStatus === 404){
            return <Error404></Error404>
        }
        else if (errorStatus === 403){
            return <Error403></Error403>
        }
        else if (errorStatus === 409){
            return <Error409></Error409>
        }
        else if (errorStatus === 500){
            return <Error500></Error500>
        }
        return children;
    }

    const contextPayload = useMemo(() => ({setErrorStatus}), [setErrorStatus]);

    return(
        <ErrorStatusContext.Provider value={contextPayload}>
            {handleErrors()}
        </ErrorStatusContext.Provider>
    )
}

export default ErrorStatusContext