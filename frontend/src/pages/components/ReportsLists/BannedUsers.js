import React, { useEffect, useState } from 'react';
import userApi from '../../../api/UserApi';
import profileApi from '../../../api/ProfileApi';
import UserRoles from '../../../api/values/UserRoles';
import ConfirmationModal from '../../components/forms/confirmationForm/confirmationModal';
import {useTranslation} from "react-i18next";
import defaultProfilePicture from "../../../images/defaultProfilePicture.png"
import ProfileImage from "../profileImage/ProfileImage";
import {useNavigate} from "react-router-dom";
import {Spinner} from "react-bootstrap";

export default function BannedUsers() {
  const [users, setUsers] = useState([]);
  const [usersLoading, setUsersLoading] = useState(true);
  const [selectedAction, setSelectedAction] = useState(null);
  const [showUnbanModal, setShowUnbanModal] = useState(false);
  const { t } = useTranslation();
  const navigate = useNavigate();

  useEffect(() => {
    fetchBannedUsers();
  }, []);

  const fetchBannedUsers = async () => {
    try {
      const response = await userApi.listUsers({ role: UserRoles.BANNED });
      const bannedUsers = response.data || [];




      // Fetch ban messages and profile info in parallel for all users
      const detailPromises = bannedUsers.flatMap(user => [
          userApi.getBanMessage(user.username).catch(() => ({ data: {} })),
        profileApi.getProfileByUsername(user.username)
      ]);


      const detailResponses = await Promise.all(detailPromises);
      
      // Combine the results with the user data
      const usersWithDetails = bannedUsers.map((user, index) => {
        const baseIndex = index * 2; // 2 promises per user
        return {
          ...user,
          banInfo: detailResponses[baseIndex].data,
          profile: detailResponses[baseIndex + 1].data
        };
      });
      setUsers(usersWithDetails);
      setUsersLoading(false);
    } catch (error) {
      console.error('Error fetching banned users:', error);
    }
  };

  const handleUnban = async (user) => {
    try {
      await userApi.unbanUser(user.username);
      await fetchBannedUsers();
    } catch (error) {
      console.error('Error unbanning user:', error);
    } finally {
      setShowUnbanModal(false);
    }
  };

  const handleProfilePictureClick = (username) => {
    navigate(`/profile/${username}`);
  }

  if (usersLoading) return <div className={'mt-6 d-flex justify-content-center'}><Spinner/></div>


  return (
    <div>
      <h3 className="text-xl font-semibold mb-4">{t('bannedUsers.bannedUsers')}</h3>
      {users.length === 0 ? (
        <div className="text-center text-gray-500">{t('bannedUsers.noBannedUsers')}</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {users.map((user) => (
            <div key={user.username} className="bg-white p-4 rounded shadow">
              <div className="flex items-center justify-between mb-2">
                <div className="flex items-center space-x-4">
                  <ProfileImage
                      username = {user.username}
                      size="60px"
                      defaultProfilePicture="https://example.com/default-profile.jpg"
                      onClick={() => handleProfilePictureClick(user.username)}
                  />
                  <div>
                    <div className="font-bold"><a href={ process.env.PUBLIC_URL + `/profile/${user.username}`}>{user.username}</a></div>
                    <div className="text-sm text-gray-600">{user.banInfo?.banMessage}</div>
                    <div className="text-sm text-gray-500">{user.email}</div>
                  </div>
                </div>
                <button
                  onClick={() => setShowUnbanModal(user)}
                  className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                >
                  {t('profile.unbanUser')}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
      {showUnbanModal && (
        <ConfirmationModal
          title={t('profile.unbanUser')}
          message={t('confirmationForm.prompt', { actionName: t('profile.unbanUser').toLowerCase() })}
          onConfirm={() => handleUnban(showUnbanModal)}
          onCancel={() => setShowUnbanModal(false)}
        />
      )}
    </div>
  );
}
