import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import mediaService from "../../../services/MediaService";
import ActorCard from "./ActorCard";
import {useTranslation} from "react-i18next";
import castService from "../../../services/CastService";

const ActorCardList = ({ mediaId }) => {
    const { t } = useTranslation();
    const navigate = useNavigate();

    const [actors, setActors] = useState([]);
    const [actorsLoading, setActorsLoading] = useState(true);
    const [actorsError, setActorsError] = useState(null);

    const handleActorCardClick = (actor) => {
        navigate(`/cast/actor/${actor.actorId}`, { state: { actorName: actor.actorName } });
    };

    useEffect(() => {
        async function fetchActors()  {
            try {
                const response = await castService.getActorsByMediaId(mediaId);
                setActors(response.data);

            } catch (err) {
                setActorsError(err);

            } finally {
                setActorsLoading(false);

            }
        }

        fetchActors();
    }, [mediaId]);

    if (actorsLoading) {
        return <div>{t('actorCardList.loading')}</div>;
    }

    if (actorsError) {
        return <div>{t('actorCardList.loading',{actorsError:actorsError.message})}</div>;
    }

    return (
        <div style={{ display: 'flex', gap: '16px', overflowX: 'auto' }}>
            {actors.map((actor) => (
                <div
                    key={actor.actorId}
                    onClick={() => handleActorCardClick(actor)}
                    style={{ cursor: "pointer" }}
                >
                    <ActorCard
                        name={actor.actorName}
                        image={actor.profilePath}
                    />
                </div>
            ))}
        </div>
    );
};

export default ActorCardList;
