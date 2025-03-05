import React, { useState, useEffect } from "react";
import defaultPoster from "../../../images/defaultPoster.png";
import { Link } from "react-router-dom";
import "./listCard.css";
import ProfileImage from "../profileImage/ProfileImage";
import listService from "../../../services/ListService";
import {useSelector} from "react-redux";
import {useTranslation} from "react-i18next";
import profileService from "../../../services/ProfileService";

const ListCard = ({ listCard }) => {
    const { t } = useTranslation();
    const [hasLikedAndFollowed, setHasLikedAndFollowed] = useState({liked:false,followed:false});
    const {isLoggedIn, user} = useSelector(state => state.auth);


    let images = [...listCard.images];
    while (images.length < 4) {
        images.push(defaultPoster);
    }

    useEffect(() => {
        const fetchHasLikedAndFollowed = async () => {
            try {
                const likedAndFollowed = await profileService.currentUserLikeFollowStatus(listCard.id, user.username);
                setHasLikedAndFollowed(likedAndFollowed);
            } catch (error) {
            }
        };

        fetchHasLikedAndFollowed();
    }, [listCard.id]);

    return (

            <div className="list-card">
                <Link to={"/list/" + listCard.id} className="not-link">
                    <div className="image-grid">
                        {images.slice(0, 4).map((image, index) => (
                            <img className="list-card-image" src={image} alt={`Poster ${index}`} key={index} />
                        ))}
                    </div>
                    <div className="list-card-body">
                        <div className="list-card-title" title={listCard.name}>{listCard.name}</div>
                        <div className="list-card-details">
                            <span>{listCard.movieCount} {t('listCard.movies')}</span> • <span>{listCard.mediaCount - listCard.movieCount} {t('listCard.series')}</span>
                        </div>
                        <div className="list-card-footer">
                            <span>
                                {t('listCard.by')} {listCard.createdBy} <ProfileImage username={listCard.createdBy}/>
                            </span>
                            <span className="list-card-likes">
                                👍 {listCard.likes} {hasLikedAndFollowed.liked ? t('listCard.youLikedThis') : ""}
                            </span>
                        </div>
                    </div>
                </Link>
            </div>

    );
};

export default ListCard;
