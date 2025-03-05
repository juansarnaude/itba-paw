import {ErrorHandler} from "../store/errorStatusContext";


const ContextProviderWrapper = (props) => {

    return (
                <ErrorHandler>
                        {props.children}
                </ErrorHandler>
    );

}
export default ContextProviderWrapper;