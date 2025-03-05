import React from 'react';
import MediaCard from '../mediaCard/MediaCard';
import './cardsHorizontalContainer.css';
import Loader from "../../Loader";
import {useTranslation} from "react-i18next";

const CardsHorizontalContainer = ({ mediaList, loading, error}) => {
    const { t } = useTranslation();

    if (loading) {
        return <Loader />;
    }

    if (error) {
        return <div>{t('cardsHorizontalContainer.errorLoadingMedia')}</div>;
    }

    return (
        <div className="cardsHorizontalContainer">
            {Array.isArray(mediaList) && mediaList.length > 0 ? (
                mediaList.map((media) => (
                    <MediaCard key={media.id} media={media} />
                ))
            ) : (
                <div>{t('cardsHorizontalContainer.noMediaFound')}</div>
            )}
        </div>
    );
};

export default CardsHorizontalContainer;
