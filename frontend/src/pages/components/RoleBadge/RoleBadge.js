import React from "react";
import defaultBadgePicture from "../../../images/moderator_logo.png";
import "./RoleBadgeStyle.css";

const RoleBadge = ({ role,size }) => {
    // Renderiza la imagen solo si role > 2
    if (role === 2) {
        return (
            <img
                src={defaultBadgePicture}
                alt="Role Badge"
                title="Mod Badge" 
                style={{ height: size, width: size }}
                className="role-badge"
            />
        );

    }

    // Retorna null si role <= 2
    return null;
};

export default RoleBadge;
