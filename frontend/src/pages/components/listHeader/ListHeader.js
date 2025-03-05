import React, { useEffect, useState } from "react";
import "./listHeader.css";
import listService from "../../../services/ListService";
import { useSelector } from "react-redux";
import {NavLink, useNavigate} from "react-router-dom";
import ReviewForm from "../forms/reviewForm/ReviewForm";
import EditListForm from "../forms/editListForm/editListForm";
import {useTranslation} from "react-i18next";
import profileService from "../../../services/ProfileService";
import MoovieListTypes from "../../../api/values/MoovieListTypes";

const ListHeader = ({ 
    list, 
    updateHeader, 
    onDelete, 
    onReport,
    showDeleteConfirmation,
    setShowDeleteConfirmation,
    showReportForm,
    setShowReportForm
}) => {
    const { t } = useTranslation();
    const { isLoggedIn, user } = useSelector((state) => state.auth);
    const navigate = useNavigate();

    const [hasLikedAndFollowed, setHasLikedAndFollowed] = useState({
        liked: false,
        followed: false,
    });

    const [ping, setPing] = useState(false);

    useEffect(() => {
        const fetchHasLikedAndFollowed = async () => {
            try {
                const likedAndFollowed = await profileService.currentUserLikeFollowStatus(list.id, user.username);
                setHasLikedAndFollowed(likedAndFollowed);
            } catch (error) {
                // Handle error
            }
        };

        fetchHasLikedAndFollowed();
    }, [list.id, ping]);

    const handleLike = async () => {
        try {
            if(!isLoggedIn){
                navigate("/login");
            }
            if (hasLikedAndFollowed.liked) {
                await listService.unlikeList(list.id, user.username);
            } else {
                await listService.likeList(list.id, user.username);
            }
            setPing(!ping);
        } catch (error) {
            // Handle error
        }
    };

    const handleFollow = async () => {
        try {
            if(!isLoggedIn){
                navigate("/login");
            }
            if (hasLikedAndFollowed.followed) {
                await listService.unfollowList(list.id, user.username);
            } else {
                await listService.followList(list.id, user.username);
            }
            setPing(!ping);
        } catch (error) {
            // Handle error
        }
    };

    const [editList, setEditList] = useState(false);
    const handleOpenEdit = () => {
        setEditList(true);
    };

    const handleCloseEdit = () => {
        setEditList(false);
    };

    const handleCloseEditSucccess = () => {
        setEditList(false);
        updateHeader();
    }



    return (
        <div className="list-header">
            {list.images && list.images.length > 0 ? (
                <div
                    className="list-header-image"
                    style={{ backgroundImage: `url(${list.images[0]})` }}
                ></div>
            ) : null}
            <div className="list-header-content">
                <div className="list-header-actions">
                    {isLoggedIn && (
                        <>
                            {(user.username !== list.createdBy) && (
                                <button
                                    className="report-button"
                                    onClick={() => setShowReportForm(true)}
                                >
                                    <i className="bi bi-flag"></i>
                                </button>
                            )}
                            {user.username === list.createdBy && (
                                <>
                                    <button 
                                        className="delete-button"
                                        onClick={() => setShowDeleteConfirmation(true)}
                                    >
                                        <i className="bi bi-trash"></i>
                                    </button>
                                    <button className="edit-list-button" onClick={handleOpenEdit}>
                                        {t('listHeader.edit')}
                                    </button>
                                </>
                            )}
                        </>
                    )}
                </div>
                <h1 className="list-header-title">{list.name}</h1>
                <p className="list-header-description">{list.description}</p>
                <span className="list-header-username">{t('listHeader.by')} <div style={{ cursor: "pointer", color: "aliceblue"}} onClick={() => navigate(`/profile/${list.createdBy}`)}>{list.createdBy}</div></span>
                    <div className="list-header-buttons">
                    { (list.type !== MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.type &&
                            list.type !== MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.type)
                        && (
                    <button
                        className={`like-button ${hasLikedAndFollowed.liked ? t('listHeader.liked') : ""}`}
                        onClick={handleLike}
                    >
                        {hasLikedAndFollowed.liked ? t('listHeader.dislike') : t('listHeader.like')}
                    </button>)}
                    { (list.type !== MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.type &&
                            list.type !== MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.type)
                        && (
                    <button
                        className={`follow-button ${hasLikedAndFollowed.followed ? t('listHeader.followed') : ""}`}
                        onClick={handleFollow}
                    >
                        {hasLikedAndFollowed.followed ? t('listHeader.unfollow') : t('listHeader.follow')}
                    </button>)}
                </div>
            </div>
            {editList && (
                <div className="overlay">
                    <EditListForm
                        listName={list.name}
                        listId={list.id}
                        listDescription={list.description}
                        closeEdit={handleCloseEdit}
                        closeEditSuccess={handleCloseEditSucccess}
                    />
                </div>
            )}
        </div>
    );
};

export default ListHeader;
