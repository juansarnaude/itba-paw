import React from "react";
import {createSearchParams, useNavigate} from 'react-router-dom';

const SearchableMediaTag = ({ image, text, link, id, object }) => {

    const navigate = useNavigate();

    const handleClick = () => {
        if (link === `providers` || link === `genres`) {
            const { providerName, providerId, url, ...rest } = object;
            const providerObject = {
                name: providerName,
                id: providerId
            };
            navigate(`/discover`, {
                state: { selectedProviders: [providerObject] },
            });
        }
        if (link === `genres`) {
            const { genreName, genreId, url, ...rest } = object;
            const genreObject = {
                name: genreName,
                id: genreId
            };
            navigate(`/discover`, {
                state: { selectedGenres: [genreObject] },
            });
        }

        if (link === `tvcreators` || link === `cast/director`) {
            navigate(`/${link}/${id}`,{ state: { actorName: text } });
        }
    };


    return (
        <div onClick={handleClick} style={{display:'inline-flex',alignItems: 'center', cursor: link ? 'pointer' : 'default' }}>
                {image && <img src={image} style={{height:'1.6em',marginRight:'5px',verticalAlign:'middle'}} />}
                {text}
        </div>
    );
}

export default SearchableMediaTag;