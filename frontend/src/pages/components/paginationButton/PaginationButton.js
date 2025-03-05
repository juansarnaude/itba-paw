import React from "react";
import {Pagination} from "@mui/material";

const PaginationButton = (props) =>{
    const handleChange = (event, value) => {
        props.setPage(value);
    };

    return (<Pagination count={parseInt(props.lastPage)} variant="outlined"
                        color="secondary"
                        page={parseInt(props.page)}
                        onChange={handleChange}/>);
}

export default PaginationButton;