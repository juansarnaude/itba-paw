import React from 'react';
import { Collapse } from 'react-bootstrap';

const FilterSection = ({ title, isOpen, toggleOpen, children }) => (
    <div className="d-flex flex-column m-1">
        <button
            type="button"
            onClick={toggleOpen}
            className="btn btn-success m-1"
            aria-controls={title.toLowerCase()}
            aria-expanded={isOpen}
        >
            {title}
        </button>
        <Collapse in={isOpen}>
            <div id={title.toLowerCase()} className="m-1" style={{ maxHeight: '20vh', overflow: 'auto' }}>
                {children}
            </div>
        </Collapse>
    </div>
);

export default FilterSection;
