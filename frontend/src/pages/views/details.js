import React, {useEffect, useState} from 'react';
import './details.css';
import "../components/mainStyle.css"
import {useNavigate, useParams} from "react-router-dom";
import Reviews from "../components/ReviewsSection/Reviews";
import AddMediaToListButton from "../components/buttons/addMediaToListButton/AddMediaToListButton";
import CreateReviewButton from "../components/buttons/createReviewButton/CreateReviewButton";
import ReviewForm from "../components/forms/reviewForm/ReviewForm";
import ActorCardList from "../components/actorCards/ActorCardList";
import {useSelector} from "react-redux";
import MediaTag from "../components/detailsSection/mediaTag/mediaTag";
import Popover from 'react-bootstrap/Popover';
import {OverlayTrigger, Spinner} from "react-bootstrap";
import MediaService from "../../services/MediaService";
import GenreService from "../../services/GenreService";
import ProviderService from "../../services/ProviderService";
import mediaService from "../../services/MediaService";
import {useTranslation} from "react-i18next";
import reviewService from "../../services/ReviewService";
import castService from "../../services/CastService";
import Error404 from "./errorViews/error404";
import useErrorStatus from "../../hooks/useErrorStatus";

function Details() {
    const { t } = useTranslation();
    const {id} = useParams();
    const { setErrorStatus } = useErrorStatus();
    //GET VALUES FOR Media
    const [media, setMedia] = useState({});
    const [mediaLoading, setMediaLoading] = useState(true);
    const [mediaError, setMediaError] = useState(null);

    //GET VALUES FOR Genres
    const [genres, setGenres] = useState({});
    const [genresLoading, setGenresLoading] = useState(true);
    const [genreError, setGenreError] = useState(null);

    //GET VALUES FOR PROVIDERS
    const [providers, setProviders] = useState({});
    const [providersLoading, setProvidersLoading] = useState(true);
    const [providersError, setProvidersError] = useState(null);

    //GET VALUES FOR TVCREATORS
    const [tvCreators, setTvCreators] = useState([]);
    const [tvCreatorsLoading, setTvCreatorsLoading] = useState(true);
    const [tvCreatorsError, setTvCreatorsError] = useState(null);


    //GET CURRENT USER REVIEW
    const [userReview,setUserReview] = useState({});
    const [userReviewLoading,setUserReviewLoading] = useState(true);
    const [userReviewError,setUserReviewError] = useState(null);


    const [reload, setReload] = useState(false)
    const {isLoggedIn, user} = useSelector(state => state.auth);

    const [reloadReviews, setReloadReviews] = useState(false); // state to trigger re-render of Reviews component


    const popoverMoovieRating = (<Popover id="popover-basic">
        <Popover.Body as="h6">Moovie rating</Popover.Body>
    </Popover>);

    const popoverUserRating = (<Popover id="popover-basic">
        <Popover.Body as="h6">Users rating</Popover.Body>
    </Popover>);


    const fetchMedia = async () => {
        try {
            const response = await MediaService.getMediaById(id);
            setMedia(response.data);
        } catch (err) {
            setErrorStatus(err.response.status);
            setMediaError(err);
        } finally {
            setMediaLoading(false);
        }
    };


    const fetchGenres = async () => {
        try {
            const response = await GenreService.getGenresForMedia(id);
            setGenres(response.data);
        } catch (err) {
            setGenreError(err)
        } finally {
            setGenresLoading(false)
        }
    }

    const fetchProviders = async () => {
        try {
            const response = await ProviderService.getProvidersForMedia(id);
            setProviders(response.data)
        } catch (err) {
            setProvidersError(err);
        } finally {
            setProvidersLoading(false)
        }
    }


    const fetchTvCreators = async () => {
        try {
            const response = await castService.getTvCreatorsByMediaId(id);
            setTvCreators(response.data);
        } catch (err) {
            setTvCreatorsError(err);

        } finally {
            setTvCreatorsLoading(false);

        }
    }


    const fetchUserReview = async () => {
        try {
            if (isLoggedIn) {
                const response = await reviewService.getReviewsByMediaIdandUserId(id, user.id);
                setUserReview(response.data);
            }
        } catch (err) {
            setUserReviewError(err);
        } finally {
            setUserReviewLoading(false);
        }
    }

    useEffect(() => {
        fetchMedia();
        fetchUserReview();

    }, [id, reload,setErrorStatus]);

    useEffect(() => {

        if (media.type !== "Movie") {
            fetchTvCreators();
        }
        fetchGenres();
        fetchProviders();
    }, [id]);

    const trailerLink = (media.trailerLink === 'None' ? null : media.trailerLink);
    const releaseYear = new Date(media.releaseDate).getFullYear();

    // Buttons for creating reviews
    const [showReviewForm, setShowReviewForm] = useState(false);
    const navigate = useNavigate();

    let detailsColumn = <div/>;
    let info = <div/>;

    if (media.type === "Movie") {
        detailsColumn = <span>

                    <div className="d-flex flex-row align-items-center ">
                        <h5>{t('details.director')}</h5>
                        <MediaTag link={'cast/director'} text={media.director} id={media.directorId}/>
                    </div>


                    {media.budget>0 && (
                    <div className="d-flex flex-row align-items-center ">
                        <h5>{t('details.budget')}</h5>
                        <MediaTag text={new Intl.NumberFormat('en-US', {
                            style: 'currency', currency: 'USD', maximumFractionDigits: 0
                        }).format(media.budget)}/>
                    </div>
                    )}

            {media.revenue>0 && (
                <div className="d-flex flex-row align-items-center ">
                        <h5>{t('details.revenue')}</h5>
                        <MediaTag text={new Intl.NumberFormat('en-US', {
                            style: 'currency', currency: 'USD', maximumFractionDigits: 0
                        }).format(media.revenue)}/>
                    </div>
            )}
        </span>

        info = <h5>{releaseYear} • {media.runtime} m • {t('details.movie')}</h5>


    } else {
        detailsColumn =
            <span>

            <div className="d-flex flex-row align-items-center">
                {tvCreators.length > 0 && (
                    <>
                        <h5>{tvCreators.length > 1 ? "Creators:" : "Creator:"}</h5>
                        {tvCreators.map((creator) => (
                            <MediaTag link="tvcreators" text={creator.creatorName} id={creator.id} key={creator.id}/>
                        ))}
                    </>
                )}
            </div>


                {media.lastAirDate && (
                    <div className="d-flex flex-row align-items-center">
                        <h5>{t('details.lastAirDate')}</h5>
                        <MediaTag
                            text={new Date(media.lastAirDate)
                                .toISOString()
                                .split('T')[0]} // Esto toma solo la parte YYYY-MM-DD
                        />
                    </div>
                )}

                {media.nextEpisodeToAir && (
                    <div className="d-flex flex-row align-items-center ">
                        <h5>{t('details.nextEpisodeToAir')}</h5>
                        <MediaTag
                            text={new Date(media.nextEpisodeToAir)
                                .toISOString()
                                .split('T')[0]} // Esto toma solo la parte YYYY-MM-DD
                        />
                    </div>
                )
                }
        </span>

        info = <h5>{releaseYear} • {t('details.serie')} • {media.numberOfSeasons} {t('details.seasons')} • {media.numberOfEpisodes} {t('details.episodes')}</h5>

    }

    const handleOpenReviewForm = () => {
        if (!isLoggedIn) {
            navigate('/login');
        }
        setShowReviewForm(true);
    };

    const handleCloseReviewForm = () => {
        setShowReviewForm(false);
    };

    const handleReviewSubmit = () => {
        setReloadReviews(prevState => !prevState); // Toggle the state to force re-render of Reviews component
        setReload(!reload);
        handleCloseReviewForm(); // Close the review form after submission
    };

    const handleParentReload = () => {
        setReload(!reload);
    };

    if (mediaLoading || genresLoading || providersLoading || tvCreatorsLoading || userReviewLoading) return <div className={'mt-6 d-flex justify-content-center'}><Spinner/></div>

    return (
        <div className="container my-1">
            <div className="row align-items-center justify-content-center" style={{marginBottom: '20px'}}>

                {/* POSTER COLUMN */}
                <div className="col text-center">
                    <img src={media.posterPath} className="posterStyle" alt={`${media.name} poster`}/>
                </div>

                {/* MEDIA DETAILS COLUMN */}
                <div className="col" style={{marginBottom: '10px'}}>
                    <h1>{media.name}
                        <MediaTag style={{fontSize: '14px'}} link={'status'} text={media.status}/>
                        <MediaTag style={{fontSize: '14px'}} link={'l'} text={media.originalLanguage}/>
                    </h1>

                    {info}

                    <div>
                        <h1 className="d-flex flex-row align-items-center ">
                            <OverlayTrigger trigger={["hover", "focus"]} placement="right" overlay={popoverMoovieRating}>
                                <div className="me-3"><i className={"bi bi-star-fill"}/> {media.tmdbRating}</div>
                            </OverlayTrigger>

                            {(media.voteCount > 0) && (
                                <>
                                    <span>•</span>
                                    <OverlayTrigger trigger={["hover", "focus"]} placement="right" overlay={popoverUserRating}>
                                        <div className="m-3"><i className={"bi bi-star"}/> {media.totalRating}</div>
                                    </OverlayTrigger>
                                </>
                            )
                            }

                        </h1>
                    </div>

                    <div style={{
                        overflowX: providers.length > 5 ? 'auto' : 'visible',
                        whiteSpace: 'nowrap',
                        display: 'flex',
                        width:'500px'
                    }}>
                        {providers.length > 0 ? (
                            providers.map((provider) => (
                                <MediaTag
                                    key={provider.providerId}
                                    link={`providers`}
                                    text={provider.providerName}
                                    image={provider.logoPath}
                                    id={provider.providerId}
                                    object={provider}
                                />
                            ))
                        ) : null}
                    </div>

                    <div className="d-flex flex-row align-items-center ">
                        <h5>{t('details.genres')}:</h5>
                        {genres.length > 0 ? genres.map((genre) => <MediaTag darkmode={true} link={`genres`}
                                                                             text={genre.genreName}
                                                                             id={genre.genreId}
                                                                             object={genre}/>) :
                            <MediaTag darkmode={true} text={t('details.noGenresAvailable')}/>}
                    </div>
                    {detailsColumn}

                    {trailerLink && (<div style={{marginBottom: '5px', marginTop: '5px'}}>
                        <iframe style={{width: '85%', height: '315px'}}
                                src={trailerLink.replace("watch?v=", "embed/")}/>
                    </div>)}
                    <span></span>
                    <p> {media.overview} </p>

                    <div className="flex-row d-flex">
                        <AddMediaToListButton media={media} currentId={id}/>

                        <CreateReviewButton handleOpenReviewForm={handleOpenReviewForm} rated={userReview.rating}/>
                    </div>
                </div>


                {showReviewForm && (
                    <div className="overlay">
                        <ReviewForm
                            mediaName={media.name}
                            mediaId={id}
                            userReview={userReview}
                            closeReview={handleCloseReviewForm}
                            onReviewSubmit={handleReviewSubmit} // Pass the submit handler
                        />
                    </div>
                )}
            </div>

            <div className="row my-8">
                <h2>{t('details.cast')}</h2>
                <hr className="my-8"/>
                <ActorCardList mediaId={id}/>
            </div>

            {/* Pass reloadReviews as a key to force re-render */}
            <div className="row">
                <h2>{t('details.reviews')}</h2>
                <hr className="my-8 mb-4"/>
                <Reviews key={reloadReviews ? id : undefined} id={id} handleParentReload={handleParentReload}
                         source="media"/>
            </div>
        </div>
    );
}

export default Details;
