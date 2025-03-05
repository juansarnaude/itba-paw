import React from 'react';
import {Chip} from "@mui/material";

const ChipsDisplay = ({ title, items, onRemove }) => (
    <div className="container d-flex justify-content-start p-0 flex-wrap">
        {items.length > 0 && (
            <div className={'d-flex flex-wrap align-items-center'}>
                <h4>{title}:</h4>
                {items.map((item, index) => (
                        <Chip className={'m-1'} key={index} label={item.name} variant="outlined" onDelete={() => onRemove(item)} />
                ))}
            </div>
        )}
    </div>
);

export default ChipsDisplay;
