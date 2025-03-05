import React from "react";
import "./profileImage.css";
import defaultProfilePicture from "../../../images/defaultProfilePicture.png";
import profileService from "../../../services/ProfileService";
import profileApi from "../../../api/ProfileApi";

const ProfileImage = ({ image, username, size, onClick }) => {

    if(username && !image ){
        image = profileApi.getPfp(username);
    }

    const imgSrc = image || defaultProfilePicture;

    return (
        <img
            id="profile-image"
            className="profileImage"
            style={{ height: size, width: size, cursor: 'pointer' }}
            src={imgSrc}
            alt="Profile"
            onClick={onClick}  // Added onClick handler
            onError={(e) => {
                e.target.src = defaultProfilePicture;
            }}
        />
    );
};

export default ProfileImage;
