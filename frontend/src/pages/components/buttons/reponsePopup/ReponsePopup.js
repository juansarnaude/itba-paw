import React, {useEffect} from "react";
import "./responsePopup.css";
import {useTranslation} from "react-i18next";

const ResponsePopup = ({ message, type, onClose }) => {
    const { t } = useTranslation();
    let popupClass = type === "loading" ? "popup loading" : `popup ${type}`;

    useEffect(() => {
        popupClass = type === "loading" ? "popup loading" : `popup ${type}`;
    }, [type]);

    return (
        <div className={popupClass}>
            <button className="popup-close-btn" onClick={onClose}>
                &times;
            </button>
            {type === "loading" ? (
                <div className="loading">
                    <span role="img" aria-label="loading">⏳</span>
                    <span>{t('responsePopup.loading')}</span>
                </div>
            ) : (
                <span>{message}</span>
            )}
        </div>
    );
};

export default ResponsePopup;
