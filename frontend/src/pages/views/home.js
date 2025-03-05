import React, {useState, useEffect, useCallback} from 'react';
import mediaApi from '../../api/MediaApi'; // Adjust the path if needed
import CardsHorizontalContainer from "../components/cardsHorizontalContainer/CardsHorizontalContainer";
import MediaTypes from "../../api/values/MediaTypes";
import OrderBy from "../../api/values/MediaOrderBy";
import SortOrder from "../../api/values/SortOrder";
import "../components/mainStyle.css"
import "./home.css"
import Button from "react-bootstrap/Button";
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {Divider} from "@mui/material";


function Home() {

    const navigate = useNavigate();

    //GET VALUES FOT Top Rated Movies
    const [topRatedMovies, setTopRatedMovies] = useState([]);
    const [topRatedMoviesLoading, setTopRatedMoviesLoading] = useState(true);
    const [topRatedMoviesError, setTopRatedMoviesError] = useState(null)
    const { t } = useTranslation();

    const fetchTopRatedMovies = async () => {
        try {
            const response = await mediaApi
                .getMedia({ type: MediaTypes.TYPE_MOVIE, orderBy: OrderBy.TMDB_RATING, sortOrder: SortOrder.DESC, page: 1, pageSize: 5});
            setTopRatedMovies(response.data);
        } catch (err) {
            setTopRatedMoviesError(err);
        } finally {
            setTopRatedMoviesLoading(false);
        }
    };

    useEffect(() => {
        fetchTopRatedMovies();
    }, []);

    const handleTopRatedMoviesButtonClick = () => {
        navigate('/featuredLists/topRatedMovies');
    }

    //GET VALUES FOT Mos Popular Movies
    const [mostPopularMovies, setMostPopularMovies] = useState([]);
    const [mostPopularMoviesLoading, setMostPopularMoviesLoading] = useState(true);
    const [mostPopularMoviesError, setMostPopularMoviesError] = useState(null);

    const fetchMostPopularMovies = async () => {
        try {
            const response = await mediaApi
                .getMedia({ type: MediaTypes.TYPE_MOVIE, orderBy: OrderBy.VOTE_COUNT, sortOrder: SortOrder.DESC, page: 1, pageSize: 5});
            setMostPopularMovies(response.data);
        } catch (err) {
            setMostPopularMoviesError(err);
        } finally {
            setMostPopularMoviesLoading(false);
        }
    };

    useEffect(() => {
        fetchMostPopularMovies();
    }, []);

    const handleMostPopularMoviesButtonClick = () => {
        navigate('/featuredLists/mostPopularMovies');
    }

    //GET VALUES FOT Top Rated Series
    const [topRatedSeries, setTopRatedSeries] = useState([]);
    const [topRatedSeriesLoading, setTopRatedSeriesLoading] = useState(true);
    const [topRatedSeriesError, setTopRatedSeriesError] = useState(null);

    const fetchTopRatedSeries = async () => {
        try {
            const response = await mediaApi
                .getMedia({ type: MediaTypes.TYPE_TVSERIE, orderBy: OrderBy.TMDB_RATING, sortOrder: SortOrder.DESC, page: 1, pageSize: 5});
            setTopRatedSeries(response.data);
        } catch (err) {
            setTopRatedSeriesError(err);
        } finally {
            setTopRatedSeriesLoading(false);
        }
    };

    useEffect(() => {
        fetchTopRatedSeries();
    }, []);

    const handleTopRatedSeriesButtonClick = () => {
        navigate('/featuredLists/topRatedSeries');
    }

    //GET VALUES FOT Most Popular Series
    const [mostPopularSeries, setMostPopularSeries] = useState([]);
    const [mostPopularSeriesLoading, setMostPopularSeriesLoading] = useState(true);
    const [mostPopularSeriesError, setMostPopularSeriesError] = useState(null);

    const fetchMostPopularSeries = async () => {
        try {
            const response = await mediaApi
                .getMedia({ type: MediaTypes.TYPE_TVSERIE, orderBy: OrderBy.VOTE_COUNT, sortOrder: SortOrder.DESC, page: 1, pageSize: 5});
            setMostPopularSeries(response.data);
        } catch (err) {
            setMostPopularSeriesError(err);
        } finally {
            setMostPopularSeriesLoading(false);
        }
    };

    useEffect(() => {
        fetchMostPopularSeries();
    }, []);

    const handleMostPopularSeriesButtonClick = () => {
        navigate('/featuredLists/mostPopularSeries');
    }

    return (
        <main className="moovie-default default-container">
            <div className="hero-text">
                <h1>{t('home.immerse')}</h1>
                <p>{t('home.discover')}</p>
            </div>
            <div>
                <div className="container d-flex justify-content-between p-2 position-relative">

                    <div className="hero-text mb-0">
                        <h2>{t('home.topRatedMovies')}</h2>
                    </div>
                    <a
                        onClick={handleTopRatedMoviesButtonClick}
                        className="link-button"
                    >
                        {t('home.seeMore')}
                    </a>
                    <Divider sx={{
                        backgroundColor: "rgba(0, 0, 0, 0.8)",
                        height: "2px",
                        width: "100%",
                        position: "absolute",
                        bottom: 0,
                        left: 0,
                        zIndex: 1,
                    }} />
                </div>
                <div className="cards-container mt-3">
                    <CardsHorizontalContainer
                        mediaList={topRatedMovies}
                        loading={topRatedMoviesLoading}
                        error={topRatedMoviesError}
                    />
                </div>
            </div>

            <div>
                <div className="container d-flex justify-content-between p-2 position-relative">
                    <div className="hero-text mb-0">
                        <h2>{t('home.mostPopularMovies')}</h2>
                    </div>
                    <a
                        onClick={handleMostPopularMoviesButtonClick}
                        className="link-button"
                    >
                        {t('home.seeMore')}
                    </a>
                    <Divider sx={{
                        backgroundColor: "rgba(0, 0, 0, 0.8)",
                        height: "2px",
                        width: "100%",
                        position: "absolute",
                        bottom: 0,
                        left: 0,
                        zIndex: 1,
                    }} />
                </div>
                <div className="cards-container mt-3">
                    <CardsHorizontalContainer mediaList={mostPopularMovies} loading={mostPopularMoviesLoading}
                                                error={mostPopularMoviesError}/>
                </div>
            </div>

            <div>
                <div className="container d-flex justify-content-between p-2 position-relative">
                    <div className="hero-text mb-0">
                        <h2>{t('home.topRatedSeries')}</h2>
                    </div>
                    <a
                        onClick={handleTopRatedSeriesButtonClick}
                        className="link-button"
                    >
                        {t('home.seeMore')}
                    </a>
                    <Divider sx={{
                        backgroundColor: "rgba(0, 0, 0, 0.8)",
                        height: "2px",
                        width: "100%",
                        position: "absolute",
                        bottom: 0,
                        left: 0,
                        zIndex: 1,
                    }} />
                </div>
                <div className="cards-container mt-3">
                    <CardsHorizontalContainer mediaList={topRatedSeries} loading={topRatedSeriesLoading}
                                                error={topRatedSeriesError}/>
                </div>
            </div>

            <div>
                <div className="container d-flex justify-content-between p-2 position-relative">
                    <div className="hero-text mb-0">
                        <h2>{t('home.mostPopularSeries')}</h2>
                    </div>
                    <a
                        onClick={handleMostPopularSeriesButtonClick}
                        className="link-button"
                    >
                        {t('home.seeMore')}
                    </a>
                    <Divider sx={{
                        backgroundColor: "rgba(0, 0, 0, 0.8)",
                        height: "2px",
                        width: "100%",
                        position: "absolute",
                        bottom: 0,
                        left: 0,
                        zIndex: 1,
                    }} />
                </div>
                <div className="cards-container mt-3">
                    <CardsHorizontalContainer mediaList={mostPopularSeries} loading={mostPopularSeriesLoading}
                                              error={mostPopularSeriesError}/>
                </div>
            </div>


        </main>
    );
}

export default Home;
