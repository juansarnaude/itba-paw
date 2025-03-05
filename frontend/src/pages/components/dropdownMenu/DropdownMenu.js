import React, { useState, useEffect } from "react";
import NavDropdown from "react-bootstrap/NavDropdown";
import OrderBy from "../../../api/values/MediaOrderBy";
import SortOrder from "../../../api/values/SortOrder";
import Button from "react-bootstrap/Button";
import {Tooltip as ReactTooltip} from "react-tooltip";
import {useTranslation} from "react-i18next";

const DropdownMenu = ({ setOrderBy, setSortOrder, currentSortOrder, values }) => {
    const [btnState, setBtnState] = useState(currentSortOrder);
    const {t} = useTranslation();

    useEffect(() => {
        setBtnState(currentSortOrder);
    }, [currentSortOrder]);

    const handleSelect = (selectedValue) => {
        setOrderBy(selectedValue);
    };

    const handleClick = () => {
        const newSortOrder = btnState === SortOrder.DESC ? SortOrder.ASC : SortOrder.DESC;
        setBtnState(newSortOrder);
        setSortOrder(newSortOrder);
    };

    return (
        <div style={{ display: "flex" }}>
            <NavDropdown title={t('dropdownMenu.orderBy')}>
                {values.map((value) => (
                    <NavDropdown.Item key={value} onClick={() => handleSelect(value)}>
                        {value}
                    </NavDropdown.Item>
                ))}
            </NavDropdown>
            <ReactTooltip id="tooltip-id" place="bottom" type="dark" effect="solid" />
            <Button className="z-0" onClick={handleClick} data-tooltip-id={"tooltip-id"}
                    data-tooltip-content={t('dropdownMenu.invertOrder')}>
                {btnState === SortOrder.DESC ? "↑" : "↓"}
            </Button>
        </div>
    );
};

export default DropdownMenu;
