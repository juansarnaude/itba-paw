import React, { useEffect, useState } from 'react';
import { useParams, useLocation } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { Divider } from "@mui/material";
import mediaService from "../../services/MediaService";
import castService from "../../services/CastService";
import MediaCard from "../components/mediaCard/MediaCard";
import defaultPoster from "../../images/defaultPoster.png";
import "./cast.css";
import {Spinner} from "react-bootstrap";
import useErrorStatus from "../../hooks/useErrorStatus";

function Cast() {
    const { id } = useParams();
    const location = useLocation();
    const { t } = useTranslation();
    const { setErrorStatus } = useErrorStatus();

    const [actorObject, setActorObject] = useState(null);
    const [actorMedias, setActorMedias] = useState(undefined);
    const [actorMediasLoading, setActorMediasLoading] = useState(true);
    const [actorMediasError, setActorMediasError] = useState(null);
    const [selectedActor, setSelectedActor] = useState(location.state?.actorName || "Unknown Actor");

    const isActor = location.pathname.includes("/cast/actor/");
    const isTvCreator = location.pathname.includes("/tvcreators/");
    const isDirector = location.pathname.includes("/cast/director/");

    useEffect(() => {
        async function getData() {
            try {
                let data;
                if (isActor) {
                    data = await mediaService.getMediasForActor({ id });
                    const actorData = await castService.getActorById(id);
                    setActorObject(actorData.data);
                } else if (isTvCreator) {
                    data = await mediaService.getMediasForTVCreator({ id });
                } else if (isDirector) {
                    data = await mediaService.getMediasForDirector({ id });
                }
                setActorMedias(data);
            } catch (error) {
                console.error("Error fetching actor media:", error);
                setActorMediasError(error);
                setErrorStatus(error.response.status);
            } finally {
                setActorMediasLoading(false);
            }
        }
        getData();
    }, [id,setErrorStatus]);

    if (actorMediasLoading) return <div className={'mt-6 d-flex justify-content-center'}><Spinner/></div>

    return (
        <div className="cast-container">
            {actorMedias?.data?.length > 0 ? (
                <>
                    <div className="actor-image-container">
                        <img
                            src={actorObject ? actorObject.profilePath : defaultPoster}
                            alt={`${selectedActor}'s Image`}
                            className="actor-image"
                        />
                    </div>
                    <h3 className="cast-title">{t('cast.mediasFor', {selectedActor})}</h3>
                    <Divider className="cast-divider"/>
                    <div className="cards-container">
                        {actorMedias.data.map((media) => (
                            <div className="media-card" key={media.id}>
                                <MediaCard media={media}/>
                            </div>
                        ))}
                    </div>
                </>
            ) : (
                <p className="no-media-text">{t('cast.noMediasFound')}</p>
            )}
        </div>
    );
}

export default Cast;