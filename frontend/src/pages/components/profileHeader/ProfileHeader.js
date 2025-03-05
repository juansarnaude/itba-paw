import React, {useState} from 'react';
import './profileHeader.css';
import ProfileImage from "../profileImage/ProfileImage";
import { Button } from 'react-bootstrap';
import ChangePfpForm from "../forms/changePfpForm/changePfpForm";
import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import UserRoles from '../../../api/values/UserRoles';
import RoleBadge from "../RoleBadge/RoleBadge";

const ProfileHeader = ({ profile, handleBanUser, handleUnbanUser, handleMakeModerator }) => {
    const { t } = useTranslation();
    const [showPfpPopup, setShowPfpPopup] = useState(false);
    const navigate = useNavigate();
    const { isLoggedIn, user } = useSelector(state => state.auth);

    const handleShowPfpPopup = () => {
        if (!isLoggedIn) {
            navigate('/login');
        }
        if (user.username === profile.username) {
            setShowPfpPopup(true);
        }
    };

    const handleClosePfpPopup = () => setShowPfpPopup(false);

    const isModerator = user?.role === UserRoles.MODERATOR;
    const isNotOwnProfile = user?.username !== profile.username;
    const showModActions = isModerator && isNotOwnProfile;
    const canBeMadeModerator = showModActions && profile.role !== UserRoles.MODERATOR && profile.role !== UserRoles.BANNED;

    return (
        <div className="profile-header">
            <div className="profile-header-info">
                <div className="profile-header-avatar" style={{ cursor: "pointer" }} onClick={handleShowPfpPopup}>
                    <ProfileImage username={profile.username} image={profile.pictureUrl}/>
                </div>
                <div>
                    <h1 className="profile-header-username">
                        {profile.username}
                        {profile.hasBadge && <span title={t('tooltips.badgeTooltip')}> 🏆</span>}
                        {showModActions && (
                            <>{(profile.role !== UserRoles.MODERATOR) ? (
                                <>
                                {(profile.role === UserRoles.BANNED_NOT_REGISTERED || profile.role === UserRoles.BANNED) ? (
                                    <Button
                                        variant="success"
                                        size="sm"
                                        className="ms-2"
                                        onClick={handleUnbanUser}
                                    >
                                        {t('profile.unbanUser')}
                                    </Button>
                                ) : (
                                    <Button
                                        variant="danger"
                                        size="sm"
                                        className="ms-2"
                                        onClick={handleBanUser}
                                    >
                                        {t('profile.banUser')}
                                    </Button>
                                )}
                                {canBeMadeModerator && (
                                    <Button
                                        variant="primary"
                                        size="sm"
                                        className="ms-2"
                                        onClick={handleMakeModerator}
                                    >
                                        {t('profile.makeUserModerator')}
                                    </Button>
                                )}
                                    </>
                            ) : (<RoleBadge role={profile.role} size={"50px"}></RoleBadge>)}
                            </>
                        )}
                    </h1>
                    <p className="profile-header-email">{profile.email}</p>
                </div>
            </div>
            <div className="profile-header-stats">
                <span title={t('tooltips.moovieListTooltip')}>📋 {profile.moovieListCount}</span>
                <span title={t('tooltips.reviewsTooltip')}>⭐ {profile.reviewsCount}</span>
                <span title={t('tooltips.milkyPointsTooltip')}>🐵 {profile.milkyPoints}</span>
            </div>
            {showPfpPopup && (
                <ChangePfpForm onCancel={handleClosePfpPopup} />
            )}
        </div>
    );
};

export default ProfileHeader;
