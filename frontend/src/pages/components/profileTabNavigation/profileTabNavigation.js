import React, {useState} from "react";
import Nav from "react-bootstrap/Nav";
import "./profileTabNavigation.css"
import {useTranslation} from "react-i18next";

function ProfileTabNavigation({ selectedTab, onTabSelect, isLoggedIn, isMe }) {
    const { t } = useTranslation();

    return (
        <Nav
            variant="tabs"
            activeKey={selectedTab}
            onSelect={(selectedKey) => onTabSelect(selectedKey)}
            className="custom-nav"
        >

            <Nav.Item>
                <Nav.Link eventKey="public-lists">{t('profile.public')}</Nav.Link>
            </Nav.Item>

            {
                isMe &&
                <Nav.Item>
                    <Nav.Link eventKey="private-lists">{t('profile.private')}</Nav.Link>
                </Nav.Item>
            }

            {
                isLoggedIn &&
                <Nav.Item>
                    <Nav.Link eventKey="liked-lists">{t('profile.liked')}</Nav.Link>
                </Nav.Item>
            }

            {
                isLoggedIn &&
                <Nav.Item>
                    <Nav.Link eventKey="followed-lists">{t('profile.followed')}</Nav.Link>
                </Nav.Item>
            }

            <Nav.Item>
                <Nav.Link eventKey="reviews">{t('profile.reviews')}</Nav.Link>
            </Nav.Item>

            {
                isMe &&
                <Nav.Item>
                    <Nav.Link eventKey="watched">{t('profile.watched')}</Nav.Link>
                </Nav.Item>

            }

            {
                isMe &&
                <Nav.Item>
                    <Nav.Link eventKey="watchlist">{t('profile.watchlist')}</Nav.Link>
                </Nav.Item>
            }

        </Nav>
    );
}

export default ProfileTabNavigation;

