import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import userApi from "../../api/UserApi";
import ProfileHeader from "../components/profileHeader/ProfileHeader";
import ProfileTabNavigation from "../components/profileTabNavigation/profileTabNavigation";
import Reviews from "../components/ReviewsSection/Reviews";
import ProfileTabMediaLists from "../components/profileTab/ProfileTabMediaLists";
import ProfileTabMoovieLists from "../components/profileTab/ProfileTabMoovieLists";
import profileApi from "../../api/ProfileApi";
import ConfirmationModal from "../components/forms/confirmationForm/confirmationModal";
import { useTranslation } from "react-i18next";
import {useSelector} from "react-redux";
import useErrorStatus from "../../hooks/useErrorStatus";

function ProfileTab({ selectedTab, profile }) {
  switch (selectedTab.toLowerCase()) {
    case "watched":
      return <ProfileTabMediaLists username={profile.username} type="watched" />;
    case "watchlist":
      return <ProfileTabMediaLists username={profile.username} type="watchlist" />;
    case "public-lists":
      return <ProfileTabMoovieLists username={profile.username} type="public-lists" />;
    case "private-lists":
      return <ProfileTabMoovieLists username={profile.username} type="private-lists" />;
    case "liked-lists":
      return <ProfileTabMoovieLists username={profile.username} type="liked-lists" />;
    case "followed-lists":
      return <ProfileTabMoovieLists username={profile.username} type="followed-lists" />;
    case "reviews":
      return <Reviews username={profile.username} source="user" />;
    default:
      return <div className="text-center p-4">{selectedTab}</div>;
  }
}

function Profile() {
  const { username } = useParams();
  const {isLoggedIn, user} = useSelector(state => state.auth);
  const { setErrorStatus } = useErrorStatus();
  const isMe = isLoggedIn && user.username === username;

  const [profile, setProfile] = useState({});
  const [profileLoading, setProfileLoading] = useState(true);
  const [profileError, setProfileError] = useState(null);
  const { t } = useTranslation();
  const [showBanModal, setShowBanModal] = useState(false);
  const [showUnbanModal, setShowUnbanModal] = useState(false);
  const [showMakeModModal, setShowMakeModModal] = useState(false);

  const fetchProfile = async () => {
    try {
      const response = await profileApi.getProfileByUsername(username);
      setProfile(response.data);
      setProfileError(null);
    } catch (err) {
      console.error("Error fetching profile:", err);
      setProfileError(err);
      setErrorStatus(err.response.status);
    } finally {
      setProfileLoading(false);
    }
  };

  const handleBanUser = async () => {
    setShowBanModal(true);
  };

  const handleUnbanUser = async () => {
    setShowUnbanModal(true);
  };

  const handleMakeModerator = async () => {
    setShowMakeModModal(true);
  };

  const confirmBanUser = async () => {
    try {
      await userApi.banUser(username);
      fetchProfile();
    } catch (err) {
      console.error("Error banning user:", err);
    } finally {
      setShowBanModal(false);
    }
  };

  const confirmUnbanUser = async () => {
    try {
      await userApi.unbanUser(username);
      fetchProfile();
    } catch (err) {
      console.error("Error unbanning user:", err);
    } finally {
      setShowUnbanModal(false);
    }
  };

  const confirmMakeModerator = async () => {
    try {
      await userApi.makeUserModerator(username);
      fetchProfile();
    } catch (err) {
      console.error("Error making user moderator:", err);
    } finally {
      setShowMakeModModal(false);
    }
  };

  useEffect(() => {
    fetchProfile();
  }, [username,setErrorStatus]);

  const [selectedTab, setSelectedTab] = useState("public-lists");
  const handleTabSelect = (tab) => {
    setSelectedTab(tab);
  };

  if (profileLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="spinner-border text-primary" role="status">
          <span className="sr-only">Loading...</span>
        </div>
      </div>
    );
  }

  if (profileError) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="alert alert-danger" role="alert">
          Error loading profile. Please try again later.
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto p-4">
      <div className="bg-white shadow rounded-lg">
        <ProfileHeader
          profile={profile}
          handleBanUser={handleBanUser}
          handleUnbanUser={handleUnbanUser}
          handleMakeModerator={handleMakeModerator}
        />
        <div className="border-t">
          <ProfileTabNavigation selectedTab={selectedTab} onTabSelect={handleTabSelect} isLoggedIn={isLoggedIn} isMe={isMe}/>
        </div>
        <div className="p-4">
          <ProfileTab selectedTab={selectedTab} profile={profile} />
        </div>
      </div>

      {showBanModal && (
        <ConfirmationModal
          title={t('profile.banUser')}
          message={t('confirmationForm.prompt', { actionName: t('profile.banUser').toLowerCase() })}
          onConfirm={confirmBanUser}
          onCancel={() => setShowBanModal(false)}
        />
      )}

      {showUnbanModal && (
        <ConfirmationModal
          title={t('profile.unbanUser')}
          message={t('confirmationForm.prompt', { actionName: t('profile.unbanUser').toLowerCase() })}
          onConfirm={confirmUnbanUser}
          onCancel={() => setShowUnbanModal(false)}
        />
      )}

      {showMakeModModal && (
        <ConfirmationModal
          title={t('profile.makeUserModerator')}
          message={t('confirmationForm.prompt', { actionName: t('profile.makeUserModerator').toLowerCase() })}
          onConfirm={confirmMakeModerator}
          onCancel={() => setShowMakeModModal(false)}
        />
      )}
    </div>
  );
}

export default Profile;
