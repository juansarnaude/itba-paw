import React from 'react';
import altImage from "../../../images/defaultPoster.png";
import './ActorCard.css';

const ActorCard = ({ name, image }) => {



    // Default to altImage if no image is provided
    let imageSrc = image;

    if(!imageSrc || imageSrc.length < 10){
        imageSrc = altImage;
    }



    return (
        <div className='actor-card'>
            <img
                src={imageSrc}
                alt={name || 'Actor'} // Default alt text if name is not provided
                style={{
                    borderRadius: '8px',
                    width: '100%',
                    height: 'auto',
                }}
            />
            <h3 style={{ margin: '8px 0', fontSize: '1.1rem', fontWeight: '600' }}>{name}</h3>
        </div>
    );
};

export default ActorCard;
