import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { truncateText } from "../../../../utils/FormatUtils";
import { Alert, Card, Form, OverlayTrigger, Tooltip } from "react-bootstrap";
import Button from "react-bootstrap/Button";
import { Link } from "react-router-dom";
import { Checkbox, FormControlLabel, FormGroup } from "@mui/material";

const CreateListForm = ({
                            selectedMedia,
                            name,
                            setName,
                            description,
                            setDescription,
                            isPrivate,
                            setIsPrivate,
                            onDeleteCallback,
                            onSubmitCallback,
                            onResetCallback
                        }) => {
    const { t } = useTranslation();

    const [showError, setShowError] = useState(false);
    const [showSuccess, setShowSuccess] = useState(false);
    const [listId, setListId] = useState(0);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const { success, listId } = await onSubmitCallback();
        setShowSuccess(success);
        setShowError(!success);
        setListId(listId);
    };

    const handleReset = (e) => {
        e.preventDefault();
        onResetCallback();
    };

    return (
        <Card className="shadow p-3 border border-dark" style={{ height: "83vh" }}>
            <Card.Body>
                {showError && (
                    <Alert variant="danger" dismissible onClose={() => setShowError(false)}>
                        {t('createListForm.errorMessage')}
                    </Alert>
                )}
                {showSuccess && (
                    <Alert variant="success" dismissible onClose={() => setShowSuccess(false)}>
                        {t('createListForm.successMessage')} <Link to={'/list/' + listId}>{t('createListForm.link')}</Link>
                    </Alert>
                )}
                <div className={'d-flex flex-row justify-content-between align-items-center'}>
                    <Card.Title className="text-center">
                        {t("createListForm.title")}
                        <OverlayTrigger placement="top-end" overlay={<Tooltip>{t("createListForm.tooltip")}</Tooltip>}>
                            <span style={{ cursor: 'pointer', marginLeft: '8px' }}>
                                <i className={'bi bi-info-circle-fill'}></i>
                            </span>
                        </OverlayTrigger>
                    </Card.Title>
                    <Button type="reset" variant="outline-dark" onClick={handleReset}>
                        <i className={'bi bi-arrow-counterclockwise'} />
                    </Button>
                </div>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                        <Form.Label>{t("createListForm.nameLabel")}</Form.Label>
                        <Form.Control
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            placeholder={t("createListForm.namePlaceholder")}
                            required
                        />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>{t("createListForm.descriptionLabel")}</Form.Label>
                        <Form.Control
                            as="textarea"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            placeholder={t("createListForm.descriptionPlaceholder")}
                            rows={3}
                            required
                        />
                    </Form.Group>

                    <FormGroup>
                        <FormControlLabel
                            control={<Checkbox color="success" checked={isPrivate} onChange={(e) => setIsPrivate(e.target.checked)} />}
                            label={t("createListForm.privateLabel")}
                        />
                    </FormGroup>

                    <Button variant="success" type="submit" className="w-100 mb-3">
                        {t("createListForm.createButton")}
                    </Button>

                    <div style={{ maxHeight: "35vh", overflowY: "auto" }}>
                        {selectedMedia.map((media) => (
                            <Button key={media.id} variant="light" className="w-100 mb-2 d-flex flex-row align-items-center justify-content-between" onClick={() => onDeleteCallback(media)}>
                                {truncateText(media.name, 10)}
                                <i className={'bi bi-trash'} />
                            </Button>
                        ))}
                    </div>
                </Form>
            </Card.Body>
        </Card>
    );
};

export default CreateListForm;
