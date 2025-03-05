import React, { useEffect, useState } from 'react';
import userApi from "../../api/UserApi";
import PagingSizes from "../../api/values/PagingSizes";
import "../components/mainStyle.css";
import "./milkyLeaderboard.css";
import ProfileImage from "../components/profileImage/ProfileImage";
import logo from "../../images/logo.png"
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import profileApi from "../../api/ProfileApi";
import {Spinner} from "react-bootstrap";
import {FaInfoCircle} from "react-icons/fa";

function MilkyLeaderboard() {
    const [milkyLeaderboard, setMilkyLeaderboard] = useState([]);
    const [milkyLeaderboardLoading, setMilkyLeaderboardLoading] = useState(true);
    const [milkyLeaderboardError, setMilkyLeaderboardError] = useState(null);
    const { t } = useTranslation();

    const fetchMilkyLeaderboard = async () => {
        try {
            const response = await profileApi.getMilkyLeaderboard({
                page: 1,
                pageSize: PagingSizes.MILKY_LEADERBOARD_DEFAULT_PAGE_SIZE,
            });
            setMilkyLeaderboard(response.data);
        } catch (err) {
            setMilkyLeaderboardError(err);
        } finally {
            setMilkyLeaderboardLoading(false);
        }
    };

    useEffect(() => {
        fetchMilkyLeaderboard();
    }, []);


    //Alguien que tenga buenos gustos dieñativos que ponga esto en algun lugar
    //<img style={{width: "100px", height: "100px"}} src={logo}
    //                  alt="Milky Logo"/>

    if (milkyLeaderboardLoading) return <div className={'mt-6 d-flex justify-content-center'}><Spinner/></div>

    return (
        <div className="moovie-default default-container">
            <div className="title bold-title"> {t('mpl.title')}</div>

            <div className="milky-leaderboard-table-container">
                <table className="milky-leaderboard-table">
                    <thead>
                    <tr className="milky-leaderboard-header">
                        <th className="col"></th>
                        <th className="col bold-letters">{t('mpl.username')}</th>
                        <th className="col bold-letters">{t('mpl.moovieListCount')}</th>
                        <th className="col bold-letters">{t('mpl.reviewsCount')}</th>
                        <th className="col bold-letters">{t('mpl.points')}<FaInfoCircle
                            title={t('mpl.pointsTooltip')}
                            style={{marginLeft: "5px"}}
                        /></th>
                    </tr>
                    </thead>

                    <tbody>
                    {milkyLeaderboard.map(profile => (
                        <MilkyLeaderboardProfile profile={profile} key={profile.username}/>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default MilkyLeaderboard;

function MilkyLeaderboardProfile({profile}) {

    const navigate = useNavigate();

    const handleUsernameClick = (username) => {
        navigate(`/profile/${username}`);
    }

    return (
        <tr className="milky-leaderboard-profile">
            <td className="col">
                <ProfileImage style={{ cursor: 'pointer' }} onClick={() => handleUsernameClick(profile.username)} image={profile.pictureUrl} username={profile.username} size={75}/>
            </td>
            <td className="col" style={{ cursor: 'pointer' }} onClick={() => handleUsernameClick(profile.username)}>{profile.username}</td>
            <td className="col">{profile.moovieListCount}</td>
            <td className="col">{profile.reviewsCount}</td>
            <td className="col">{profile.milkyPoints}</td>
        </tr>
    );
}
