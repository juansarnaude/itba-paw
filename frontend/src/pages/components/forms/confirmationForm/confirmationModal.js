import React from "react";
import "../formsStyle.css";
import {useTranslation} from "react-i18next";

const ConfirmationModal = ({
    title,
    message,
    onConfirm,
    onCancel
}) => {
    const { t } = useTranslation();

    return (
        <div className="overlay">
            <div className="box-review" style={{ textAlign: "center", width: "70%", padding: "3em", margin: "2em auto" }}>
                <h2 style={{ marginBottom: "1em" }}>{title}</h2>
                <div style={{ marginBottom: "1.5em" }}>{message}</div>
                <div className="buttons" style={{ display: "flex", justifyContent: "center", gap: "1.5em" }}>
                    <button className="cancel" style={{ padding: "0.8em 1.5em" }} onClick={onCancel}>
                        {t('confirmationForm.cancel')}
                    </button>
                    <button className="submit confirm-button" style={{ padding: "0.8em 1.5em" }} onClick={onConfirm}>
                        {t('confirmationForm.confirm')}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ConfirmationModal;