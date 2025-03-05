import React, { useEffect, useState } from 'react';
import './mediaCard.css';
import { useNavigate } from "react-router-dom";
import "../mainStyle.css";
import mediaService from "../../../services/MediaService";
import { useSelector } from "react-redux";
import WatchlistWatched from "../../../api/values/WatchlistWatched";
import { Tooltip as ReactTooltip } from 'react-tooltip'
import {useTranslation} from "react-i18next";
import profileService from "../../../services/ProfileService";
import { BsEye, BsEyeSlash, BsBookmark, BsBookmarkDash } from 'react-icons/bs';

const MediaCard = ({ media, size = 'normal' , showWWButtons = true, disableOnClick = false}) => {
    const releaseDate = new Date(media.releaseDate).getFullYear();

    const [ww, setWW] = useState({ watched: false, watchlist: false });
    const { isLoggedIn, user } = useSelector(state => state.auth);
    const [ping, setPing] = useState(false);
    const { t } = useTranslation();

    useEffect(() => {
        const fetchWW = async () => {
            try {
                const WW = await profileService.currentUserWWStatus(media.id, user.username);
                setWW(WW);
             } catch (error) {
            }
        };

        fetchWW();
    }, [media.id, ping]);

    const navigate = useNavigate();
    const [hovered, setHovered] = useState(false);

    const handleMouseEnter = () => {
        setHovered(true);
    };

    const handleMouseLeave = () => {
        setHovered(false);
    };

    const handleClick = () => {
        if(!disableOnClick){
            navigate(`/details/${media.id}`);
        }
    };

    const handleWatched = async () => {
        try {

            if (!isLoggedIn) {
                navigate('/login');
                return;
            }

            if (ww.watched) {
                await profileService.removeMediaFromWW(WatchlistWatched.Watched, media.id, user.username);
            } else {
                await profileService.insertMediaIntoWW(WatchlistWatched.Watched, media.id, user.username);
            }
            setPing(!ping)
        }catch(error){
        }
    };

    const handleWatchlist = async () => {
        try {

            if (!isLoggedIn) {
                navigate('/login');
                return;
            }

            if (ww.watchlist) {
                await profileService.removeMediaFromWW(WatchlistWatched.Watchlist, media.id, user.username);
            } else {
                await profileService.insertMediaIntoWW(WatchlistWatched.Watchlist, media.id, user.username);
            }
            setPing(!ping)
        }catch(error){
        }
    };

    const sizeClasses = {
        normal: 'media-card',
        small: 'media-card media-card-small'
    };

    return (
        <div
            className={`${sizeClasses[size]} shadow ${hovered ? 'hovered' : ''}`}
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
            onClick={handleClick}
        >
            <div className="media-card-border">
                <img className="media-card-image" src={media.posterPath} alt={media.name} onClick={handleClick} />
                <ReactTooltip id={`watched-tooltip-${media.id}`} place="bottom" type="dark" effect="solid" />
                <ReactTooltip id={`watchlist-tooltip-${media.id}`} place="bottom" type="dark" effect="solid" />                {hovered && (
                    <div className="media-card-overlay">
                        <h4 className="media-card-title">{media.name}</h4>
                        <h5>{releaseDate}</h5>
                        <h5>{media.tmdbRating}⭐</h5>

                        {showWWButtons &&(
                        <div className="media-card-buttons">
                            <button className="media-card-button"
                                    data-tooltip-id={`watched-tooltip-${media.id}`}
                                    data-tooltip-content={ww.watched ? t('mediaCard.removeFromWatched') : t('mediaCard.addToWatched')}
                                    onClick={(e) => { e.stopPropagation(); handleWatched(); }}>
                                {ww.watched ? <BsEyeSlash className="fs-5" /> : <BsEye className="fs-5" />}
                            </button>
                            <button className="media-card-button"
                                    data-tooltip-id={`watchlist-tooltip-${media.id}`}
                                    data-tooltip-content={ww.watchlist ? t('mediaCard.removeFromWatchlist') : t('mediaCard.addToWatchlist')}
                                    onClick={(e) => { e.stopPropagation(); handleWatchlist(); }}>
                                {ww.watchlist ? <BsBookmarkDash className="fs-5" /> : <BsBookmark className="fs-5" />}
                            </button>
                        </div>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};

export default MediaCard;
