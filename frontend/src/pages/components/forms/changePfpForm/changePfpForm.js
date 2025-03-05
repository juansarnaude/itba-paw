import React, { useState } from "react";
import "../formsStyle.css";
import UserService from "../../../../services/UserService";
import { useSelector } from "react-redux";
import {useTranslation} from "react-i18next";
import ProfileService from "../../../../services/ProfileService";

const ChangePfpForm = ({ onCancel }) => {
    const { t } = useTranslation();
    const { user } = useSelector((state) => state.auth); // Fetch logged-in user's details
    const [selectedFile, setSelectedFile] = useState(null);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null); // State to track success message

    const handleFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (!selectedFile) {
            alert("Please select a file first.");
            return;
        }

        try {
            let form = new FormData();
            form.append("image", selectedFile);
            const response = await ProfileService.setPfp({
                username: user.username,
                pfp: form
            });
            if (response.status === 200) {
                setSuccess("Profile picture updated successfully!");

            } else{
                throw new Error();
            }
        } catch (error) {
            setError(error.message || "An error occurred while uploading the profile picture.");
        }
    };

    return (
        <div className="overlay">
            <div className="box-review">
                {!error ? (
                    success ? (
                        <div>
                            <h2 style={{color: "green"}}>{success}</h2>
                            <button type="button" className="cancel" onClick={onCancel}>
                                {t('pfpForm.close')}
                            </button>
                        </div>

                    ) : (
                        <>
                            <h2>{t('pfpForm.changeProfilePicture')}</h2>
                            <form onSubmit={handleSubmit}>
                            <input
                                    type="file"
                                    accept="image/*"
                                    onChange={handleFileChange}
                                />
                                <div className="buttons">
                                    <button type="button" className="cancel" onClick={onCancel}>
                                        {t('pfpForm.cancel')}
                                    </button>
                                    <button type="submit" className="submit">
                                        {t('pfpForm.submit')}
                                    </button>
                                </div>
                            </form>
                        </>
                    )
                ) : (
                    <>
                        <h2 style={{ color: "red" }}>{error}</h2>
                        <button className="cancel" onClick={() => setError(null)}>
                            {t('pfpForm.back')}
                        </button>
                    </>
                )}
            </div>
        </div>
    );
};

export default ChangePfpForm;
