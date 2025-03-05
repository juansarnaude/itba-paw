import React, { useState } from "react";
import "../formsStyle.css";
import mediaService from "../../../../services/MediaService";
import {useTranslation} from "react-i18next";
import reviewService from "../../../../services/ReviewService";

const ReviewForm = ({mediaName, closeReview, mediaId, userReview, onReviewSubmit }) => {
    const { t } = useTranslation();
    const [error, setError] = useState(null);
    const [rating, setRating] = useState(userReview.rating || 0);
    const [review, setReview] = useState(userReview.reviewContent||"");

    const handleStarClick = (value) => {
        setRating(value);
    };

    const handleSubmit = async () => {
        try {
            setError(null); // Limpiar errores previos
            let response;
            if (userReview.rating){
                response= await reviewService.editReview(mediaId, rating, review);
            }else {
                response = await reviewService.createReview(mediaId, rating, review);
            }

            if (response.status === 200 || response.status === 201) {
                // Call onReviewSubmit if provided to reload reviews
                onReviewSubmit?.();
                // Close the pop-up
                closeReview();
                return;
            } else {
                // Set error if response is not successful
                setError(error.response?.data?.message || "Error submitting review");
            }
        } catch (error) {
            // Set error for any network or other errors
            setError(error.response?.data?.message || "Error making request");
        }
    };

    return (
        <div className="overlay">
            <div className="box-review">
                {!error ? (
                    <>
                        {userReview.rating ? (<h2>{t('reviewForm.editReview',{mediaName: mediaName})}</h2>) : (
                            <h2>{t('reviewForm.yourReview',{mediaName: mediaName})}</h2>)}
                        <div className="stars">
                            {[1, 2, 3, 4, 5].map((value) => (
                                <span
                                    key={value}
                                    className={`star ${value <= rating ? "selected" : ""}`}
                                    onClick={() => handleStarClick(value)}
                                >
                                ★
                            </span>
                            ))}
                        </div>
                        <textarea
                            placeholder={t('reviewForm.yourReviewPlaceholder')}
                            value={review}
                            onChange={(e) => setReview(e.target.value)}
                            maxLength="500"
                        ></textarea>
                        <p>{review.length}/500</p>
                        <div className="buttons">
                            <button className="cancel" onClick={closeReview}>
                                {t('reviewForm.cancel')}
                            </button>
                            <button
                                className="submit"
                                onClick={handleSubmit}
                                disabled={rating === 0}
                            >
                                {t('reviewForm.submit')}
                            </button>
                        </div>
                    </>
                ) : (
                    <>
                        <h2 style={{color: "red"}}>{error}</h2>
                        <button className="cancel" onClick={() => setError(null)}>
                            {t('reviewForm.back')}
                        </button>
                    </>
                )}
            </div>
        </div>
    );
}

export default ReviewForm;