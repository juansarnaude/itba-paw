import React from "react";
import "../buttonStyles.css";
import {useTranslation} from "react-i18next";


const CreateReviewButton = ({ handleOpenReviewForm, rated }) => {
    const { t } = useTranslation();

    return( rated ? (
        <button
            type="button"
            className="btn btn-dark border border-black"
            onClick={handleOpenReviewForm}
        >
            <i className="bi bi-star-fill"></i> {t("reviewButton.rated")}
        </button>
    ) : (
        <button
            type="button"
            className="btn btn-light border border-black"
            onClick={handleOpenReviewForm}
        >
            <i className="bi bi-star-fill"></i> {t("reviewButton.rate")}
        </button>
    )
);
};

export default CreateReviewButton;
