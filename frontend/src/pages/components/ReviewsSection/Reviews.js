import React, { useEffect, useState } from "react";
import { createSearchParams, useNavigate, useSearchParams } from "react-router-dom";
import { useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import PaginationButton from "../paginationButton/PaginationButton";
import ProfileImage from "../profileImage/ProfileImage";
import { Divider, Pagination } from "@mui/material";
import reviewService from "../../../services/ReviewService";
import moovieListReviewService from "../../../services/MoovieListReviewService";
import commentApi from "../../../api/CommentApi";
import moovieListReviewApi from "../../../api/MoovieListReviewApi";
import CommentList from "../commentList/CommentList";
import CommentField from "../commentField/CommentField";
import mediaService from "../../../services/MediaService";
import ReviewForm from "../forms/reviewForm/ReviewForm";
import ConfirmationForm from "../forms/confirmationForm/confirmationForm";
import ConfirmationModal from "../forms/confirmationForm/confirmationModal";
import profileService from "../../../services/ProfileService";
import ReportForm from "../forms/reportForm/reportForm";
import reportApi from "../../../api/ReportApi";
import {Spinner} from "react-bootstrap";


const ReviewItem = ({ review, source, isLoggedIn, currentUser, handleReport, reloadReviews }) => {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [reportedReviewId, setReportedReviewId] = useState(null);
    const [commentLoading, setCommentLoading] = useState(false);
    const [showDeleteReview, setShowDeleteReview] = useState(false);
    const [showEditReview, setShowEditReview] = useState(false);
    const [showReportForm, setShowReportForm] = useState(false);

    const [likeRefresh, setLikeRefresh] = useState(false);



    const handleToggleDelete = () => {
        setShowDeleteReview(!showDeleteReview)
    }

    const handleDelete = async () =>{
        try{
            if(source === 'media' || source === 'user' ){
                await reviewService.deleteReviewById(review.id);
                setShowDeleteReview(!showDeleteReview);
            } else {
                await moovieListReviewService.deleteMoovieListReview(review.id);
            }
            reloadReviews();
        } catch (e){

        }
    }

    const handleToggleEdit = () => {
        setShowEditReview(!showEditReview)
    }

    const [reviewContent, setReviewContent] = useState(review.reviewContent);

    const handleEdit = async () => {
        try {
            if (source !== 'media' && source !== 'lists') {
                await moovieListReviewService.editReview(review.moovieListid, reviewContent);
            }
            handleToggleEdit();
            reloadReviews();
        } catch (e) {
            console.error("Failed to edit review:", e);
        }
    };


    const [reloadComments, setReloadComments] = useState(false);

    const handleCommentSubmit = async (reviewId, comment) => {
        try {
            setCommentLoading(true);
            if (source === 'media') {
                await commentApi.createReviewComment(reviewId, comment);
            } else if (source === 'list') {
                await moovieListReviewApi.createListReviewComment(reviewId, comment);
            }
            setReloadComments(!reloadComments);
        } catch (error) {
            console.error("Error creating comment:", error);
        } finally {
            setCommentLoading(false);
        }
    };

    const [media, setMedia] = useState(null);

    useEffect(() => {
        if(source === 'user' || source === 'media'){
            const fetchMedia = async () => {
                try {
                    let response = await  mediaService.getMediaById(review.mediaId)
                    setMedia(response.data);
                } catch (error) {
                    console.error("Error fetching reviews:", error);
                }
            };

            fetchMedia();
        }
    }, [source]);


    const [currentLikeStatus, setCurrentLikeStatus] = useState(false);
    useEffect( async () => {
        try{
            if(source === 'list' ){
                setCurrentLikeStatus( await profileService.currentUserHasLikedMoovieListReview(review.id, currentUser.username));
            } else {
                setCurrentLikeStatus( await profileService.currentUserHasLikedReview(review.id, currentUser.username));
            }
        } catch(e){

        }
    }, [likeRefresh]);

    const handleLikeReview = async () => {
        try{
            if(source === 'media' || source === 'user' ){
                await reviewService.likeReview(currentUser, review.id);
                setLikeRefresh(!likeRefresh);
                if(currentLikeStatus){
                    review.likes = review.likes - 1;
                } else{
                    review.likes = review.likes + 1;
                }
            } else {
                await moovieListReviewService.likeMoovieListReview(currentUser, review.id);
                setLikeRefresh(!likeRefresh);
                if(currentLikeStatus){
                    review.reviewLikes = review.reviewLikes - 1;
                } else{
                    review.reviewLikes = review.reviewLikes + 1;
                }
            }

        } catch(e)
        {
        }
    }

    const handleReportSubmit = async (reportReason) => {
        try {
            let response;
            if (source === 'list') {
                response = await reportApi.reportMoovieListReview({
                    moovieListReviewId: review.id,
                    reportedBy: currentUser.username,
                    type: reportReason
                });
            } else {
                response = await reportApi.reportReview({
                    reviewId: review.id,
                    reportedBy: currentUser.username,
                    type: reportReason
                });
            }
            setShowReportForm(false);
            return response;
        } catch (error) {
            console.error("Error reporting review:", error);
            return error;
        }
    };

    return (
        <div key={review.id} className="review container-fluid bg-white my-3">
            <div className="review-header d-flex align-items-center justify-between">
                <div>
                    <ProfileImage
                        username={review.username}
                        size="100px"
                        onClick={() => navigate(`/profile/${review.username}`)}
                    />
                    <div>
                        <strong>{review.username} </strong>
                        { (source === 'user' && media ) && (
                            <>
                                {t('reviews.onMedia') + " "}
                                <strong>
                                    <a href={process.env.PUBLIC_URL + `/details/${media.id}`} className="media-link">
                                        {media.name}
                                    </a>
                                </strong>
                            </>
                        )}
                        {review.lastModified && (<a> - {t('reviews.lastModified')} {review.lastModified}</a>)}
                    </div>
                </div>
                <div>
                    <div>
                        {source !== 'list' && (
                            <>{review.rating}/5<i className="bi bi-star-fill"/></>
                        )}

                        {isLoggedIn && (
                            <button
                                className="btn btn-warning btn-sm mx-1"
                                onClick={() => setShowReportForm(true)}
                            >
                                <i className="bi bi-flag"></i>
                            </button>
                        )}
                        {(isLoggedIn && review.username === currentUser.username) && (
                            <button
                                className="btn btn-primary btn-sm me-1"
                                onClick={handleToggleEdit}
                            >
                                <i className="bi bi-pencil"></i> Edit
                            </button>
                        )}
                        {(isLoggedIn && review.username === currentUser.username) && (
                            <button
                                className="btn btn-danger btn-sm"
                                onClick={handleToggleDelete}>
                                <i className="bi bi-trash"></i>
                            </button>
                        )}

                    </div>
                    <div className="d-flex align-items-center justify-content-end mt-2">
                            <span className="me-2">
                                Likes: {source === 'list' ? review.reviewLikes : review.likes}
                            </span> {isLoggedIn && (
                            <button
                                className="btn btn-success btn-sm"
                                onClick={handleLikeReview}
                                data-bs-toggle="tooltip"
                                data-bs-placement="top"
                                title={currentLikeStatus ? t('review.unlike') : t('review.like')}>
                                <i className={currentLikeStatus ? "bi bi-hand-thumbs-up-fill" : "bi bi-hand-thumbs-up"}></i>
                            </button>
                        )}
                    </div>
                </div>
            </div>
            <div className="review-content">{review.reviewContent}</div>
            {source === 'media' && (
                <>
                    <CommentList reviewId={review.id} reload={{reloadComments}}/>
                    {isLoggedIn && (
                        <CommentField onSubmit={(comment) => handleCommentSubmit(review.id, comment)}
                                      isLoading={commentLoading}/>
                    )}
                </>
            )}
            <Divider/>
            {showDeleteReview && (

                <div className="overlay">
                    <ConfirmationModal onConfirm={handleDelete} onCancel={handleToggleDelete}
                                       message={t('reviews.aboutToDelete')} title={t('review.confirmDelete')} />
                </div>
            )}
            {showEditReview && (
                source === 'list' ? (
                    <ConfirmationModal
                        title={t('list.writeReview')}
                        message={
                            <textarea
                                className="form-control"
                                value={reviewContent}
                                onChange={(e) => setReviewContent(e.target.value)}
                                rows={4}
                                placeholder={t('list.reviewPlaceholder')}
                            />
                        }
                        onConfirm={handleEdit}
                        onCancel={() => {
                            handleToggleEdit();
                            setReviewContent("");
                        }}
                    />
                ) : (
                    <ReviewForm
                        mediaName={media.name}
                        mediaId={review.mediaId}
                        userReview={review}
                        closeReview={handleToggleEdit}
                        onReviewSubmit={handleEdit}
                    />
                )
            )}

            {showReportForm && (
                <ReportForm
                    onReportSubmit={handleReportSubmit}
                    onCancel={() => setShowReportForm(false)}
                />
            )}
        </div>
    );
};

function Reviews({ id, username, source, handleParentReload, parentReload }) {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const [reviews, setReviews] = useState([]);
    const [page, setPage] = useState(Number(searchParams.get("page")) || 1);
    const { user, isLoggedIn } = useSelector(state => state.auth);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [reload, setReload] = useState(false);
    const [totalPages, setTotalPages] = useState(1);

    const toggleReload = () => {
        setReload(!reload);
    }


    useEffect(() => {
        const fetchReviews = async () => {
            setLoading(true);
            setError(null);
            try {
                let response;
                if (source === 'media') {
                    response = await reviewService.getReviewsByMediaId(id, page);
                } else if (source === 'list') {
                    response = await moovieListReviewService.getMoovieListReviewsByListId(id, page);
                } else if (source === 'user') {
                    response = await reviewService.getMovieReviewsFromUser(username, page);
                }
                setReviews(response.data);
                setTotalPages(response.links?.last?.page || 1);
            } catch (error) {
                console.error("Error fetching reviews:", error);
                setError(error);
            } finally {
                setLoading(false);
            }
        };
        fetchReviews();
    }, [id, page, reload, parentReload]);

    const handlePageChange = (event, newPage) => {
        setPage(newPage);
        if(source === 'user') {
            navigate({
                pathname: `/profile/${username}`,
                search: createSearchParams({page: newPage.toString()}).toString(),
            });
        }
        else {
            navigate({
                pathname: `/${source}/${username}`,
                search: createSearchParams({page: newPage.toString()}).toString(),
            });
        }
    };

    if (loading) return <div className={'mt-6 d-flex justify-content-center'}><Spinner/></div>

    return (
        <div className="reviews-container">
            {error && <p className="error">{t('reviews.error')} {error.message}</p>}
            {!loading && !error && reviews.length > 0 ? (
                reviews.slice().reverse().map(review => (
                    <ReviewItem
                        key={review.id}
                        review={review}
                        source={source}
                        isLoggedIn={isLoggedIn}
                        currentUser={user}
                        reloadComments={() => handleParentReload()}
                        reloadReviews = {()=> toggleReload()}
                    />
                ))
            ) : (
                !loading && !error && <p>{t('reviews.noneFound')}</p>
            )}
            {!loading && !error && totalPages > 1 && (
                <div className="d-flex justify-center pt-4">
                    <Pagination 
                        page={page} 
                        count={totalPages}
                        onChange={handlePageChange}
                    />
                </div>
            )}
        </div>
    );
}


export default Reviews;
