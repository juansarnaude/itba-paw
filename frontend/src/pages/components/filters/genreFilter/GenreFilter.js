import React from 'react';
import './genreFilter.css';

const GenreFilter = ({ genres, selectedGenres, setSelectedGenres }) => {
    const handleGenreClick = (genre) => {
        const updatedGenres = new Set(selectedGenres);
        if (updatedGenres.has(genre.genreId)) {
            updatedGenres.delete(genre.genreId);
        } else {
            updatedGenres.add(genre.genreId);
        }
        setSelectedGenres(updatedGenres);
    };

    return (
        <div className="genre-filter">
            {genres && genres.map((genre) => (
                <div
                    key={genre.genreId}
                    className={`genre-item ${selectedGenres.has(genre.genreId) ? 'selected' : ''}`}
                    onClick={() => handleGenreClick(genre)}
                >
                    <span>{genre.genreName}</span>
                    {selectedGenres.has(genre.genreId) && (
                        <span className="selected-indicator">Selected</span>
                    )}
                </div>
            ))}
        </div>
    );
};

export default GenreFilter;
