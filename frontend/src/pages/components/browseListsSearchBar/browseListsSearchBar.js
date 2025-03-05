import React, { useState } from "react";
import { Form, Button, InputGroup } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import {useTranslation} from "react-i18next";

const BrowseListsSearchBar = () => {
    const [query, setQuery] = useState("");
    const navigate = useNavigate();
    const { t } = useTranslation();

    const handleSearchSubmit = (e) => {
        e.preventDefault();
        if (query.trim()) {

            navigate(`/browselists?search=${query}&page=1`);
        }
    };

    return (
        <Form inline onSubmit={handleSearchSubmit}> {}
            <InputGroup className={"InputGroup z-0"}>
                <Form.Control
                    type="search"
                    placeholder={t('search.search')}
                    className="me-2"
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                />
                <Button variant="outline-success" type="submit" style={{marginRight: "10px", zIndex: "0"}}>
                    <i className={"bi bi-search"} /> {t('search.search')}
                </Button>
            </InputGroup>
        </Form>
    );
};

export default BrowseListsSearchBar;
