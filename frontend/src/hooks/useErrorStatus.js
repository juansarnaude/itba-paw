import {useContext} from "react";
import ErrorStatusContext from "../store/errorStatusContext";

const useErrorStatus = () => useContext(ErrorStatusContext);

export default useErrorStatus;