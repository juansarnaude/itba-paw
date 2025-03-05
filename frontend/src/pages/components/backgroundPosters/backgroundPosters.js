import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css'
import './BackgroundPosters.css';
import '../mainStyle.css';

const BackgroundPosters = ({ mediaList }) => {

    return (
        <div
            style={{ zIndex: -1, position: 'absolute', left: 0, top: 0, width: '100%', height: '100%' }}
            className="d-flex flex-row"
        >
            {[...Array(2)].map((_, i) => (
                <div className="d-flex flex-column" key={i}>
                    <div className="d-flex movingContainer" style={{ height: '34%', width: '100vw' }}>
                        {mediaList.slice(0, 7).map((media, j) => (
                            <img
                                key={j}
                                className="cropCenter me-1"
                                src={media.posterPath}
                                alt="poster"
                            />
                        ))}
                    </div>
                    <div className="d-flex mt-1 movingContainer2" style={{ height: '34%' }}>
                        {mediaList.slice(7, 14).map((media, j) => (
                            <img
                                key={j}
                                className="cropCenter me-1"
                                src={media.posterPath}
                                alt="poster"
                            />
                        ))}
                    </div>
                    <div className="d-flex mt-1 movingContainer" style={{ height: '34%' }}>
                        {mediaList.slice(14, 21).map((media, j) => (
                            <img
                                key={j}
                                className="cropCenter me-1"
                                src={media.posterPath}
                                alt="poster"
                            />
                        ))}
                    </div>
                </div>
            ))}
        </div>
    );
};

export default BackgroundPosters;