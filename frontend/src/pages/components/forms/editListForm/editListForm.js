import React, { useState } from "react";
import "../formsStyle.css";
import listService from "../../../../services/ListService";
import {useTranslation} from "react-i18next";

const EditListForm = ({ listName, listDescription, closeEdit, closeEditSuccess, listId }) => {
    const { t } = useTranslation();
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const [name, setName] = useState(listName);
    const [description, setDescription] = useState(listDescription);

    const handleSubmit = async () => {
        if (!name.trim()) {
            setError("El nombre de la lista no puede estar vacío.");
            return;
        }
        try {
            const response = await listService.editMoovieList(
                {
                    id: listId,
                    name: name,
                    description: description
                }
            );

            if (response.status === 200 || response.status === 201) {
                setSuccess(true);
            } else {
                setError(response.data.message || "Error al actualizar la lista.");
            }
        } catch (error) {
            setError(error.response?.data?.message || "Error al realizar la solicitud.");
        }
    };

    return (
        <div className="overlay">
            <div className="box-review">
                {success ? (
                    <>
                        <h2 style={{ color: "green" }}>{t('editList.listUpdatedSuccessfully')}</h2>
                        <button className="cancel" onClick={closeEditSuccess}>
                            {t('editList.close')}
                        </button>
                    </>
                ) : (
                    !error ? (
                        <>
                            <h2>{t('editList.editList')}</h2>
                            <div className="form-group">
                                <label htmlFor="list-name">{t('editList.listName')}</label>
                                <input
                                    id="list-name"
                                    placeholder={listName}
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                    maxLength="100"
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="list-description">{t('editList.description')}</label>
                                <textarea
                                    id="list-description"
                                    placeholder="Descripción (Opcional)"
                                    value={description}
                                    onChange={(e) => setDescription(e.target.value)}
                                    maxLength="500"
                                ></textarea>
                                <p>{description.length}/500</p>
                            </div>
                            <div className="buttons">
                                <button className="cancel" onClick={closeEdit}>
                                    {t('editList.cancel')}
                                </button>
                                <button
                                    className="submit"
                                    onClick={handleSubmit}
                                    disabled={!name.trim()}
                                >
                                    {t('editList.save')}
                                </button>
                            </div>
                        </>
                    ) : (
                        <>
                            <h2 style={{ color: "red" }}>{error}</h2>
                            <button className="cancel" onClick={() => setError(null)}>
                                {t('editList.back')}
                            </button>
                        </>
                    )
                )}
            </div>
        </div>
    );

};

export default EditListForm;
