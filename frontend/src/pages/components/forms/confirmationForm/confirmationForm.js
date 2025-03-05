import React, { useState } from "react";
import "../formsStyle.css";
import {useTranslation} from "react-i18next";

const ConfirmationForm = ({
                              service, // Servicio a utilizar
                              actionName, // Nombre de la acción (ej: "Eliminar", "Enviar")
                              onConfirm, // Función a ejecutar después de confirmar
                              onCancel,  // Función para cancelar la acción
                              serviceParams // Parámetros a enviar al servicio
                          }) => {
    const { t } = useTranslation();
    const [postResponse, setPostResponse] = useState("");
    const [responseColor, setResponseColor] = useState("");
    const [finished, setFinished] = useState(false);

    const handleConfirm = async () => {
        try {
            const response = await service(...serviceParams);
            if (response.status === 200) {
                setPostResponse(t('confirmationForm.successMessage'));
                setResponseColor("green");
                onConfirm?.();
            } else {
                setPostResponse(response.data.message || t('confirmationForm.errorMessage'));
                setResponseColor("red");
            }
        } catch (error) {
            setPostResponse(t('confirmationForm.requestError'));
            setResponseColor("red");
        }
        setFinished(true);
    };

    if (!finished) {
        return (
            <div className="overlay">
                <div className="box-review" style={{ textAlign: "center", width: "70%", padding: "3em", margin: "2em auto" }}>
                    <h2 style={{ marginBottom: "1.5em" }}>{t('confirmationForm.prompt', {actionName: actionName})}</h2>
                    <div className="buttons" style={{ display: "flex", justifyContent: "center", gap: "1.5em" }}>
                        <button className="cancel" style={{ padding: "0.8em 1.5em" }} onClick={onCancel}>
                            {t('confirmationForm.cancel')}
                        </button>
                        <button className="submit confirm-button" style={{ padding: "0.8em 1.5em" }} onClick={handleConfirm} >
                            {t('confirmationForm.confirm')}
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="overlay">
            <div className="box-review">
                <h2 style={{ color: responseColor }}>{postResponse}</h2>
                <button className="cancel" onClick={onCancel}>
                    {t('confirmationForm.close')}
                </button>
            </div>
        </div>
    );
};

export default ConfirmationForm;
