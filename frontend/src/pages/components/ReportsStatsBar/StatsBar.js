import React from 'react';
import {
  FlagIcon,
  PersonXMarkIcon,
  EnvelopeExclamationIcon,
  FaceAngryIcon,
  SlashCircleIcon,
  IncognitoIcon
} from './icons';

const StatsBar = ({ stats }) => {
  const { totalReports, totalBanned, spamReports, hateReports, abuseReports, privacyReports } = stats;

  // Stats are displayed in a responsive flex container
  return (
    <div className="flex flex-wrap justify-center gap-4 bg-white p-4 rounded shadow">
      <div className="flex items-center gap-2">
        <FlagIcon />
        <h5 className="font-semibold text-gray-700">Total: {totalReports}</h5>
      </div>
      <div className="flex items-center gap-2">
        <PersonXMarkIcon />
        <h5 className="font-semibold text-gray-700">Banned: {totalBanned}</h5>
      </div>
      <div className="flex items-center gap-2">
        <EnvelopeExclamationIcon />
        <h5 className="font-semibold text-gray-700">Spam: {spamReports}</h5>
      </div>
      <div className="flex items-center gap-2">
        <FaceAngryIcon />
        <h5 className="font-semibold text-gray-700">Hate: {hateReports}</h5>
      </div>
      <div className="flex items-center gap-2">
        <SlashCircleIcon />
        <h5 className="font-semibold text-gray-700">Abuse: {abuseReports}</h5>
      </div>
      <div className="flex items-center gap-2">
        <IncognitoIcon />
        <h5 className="font-semibold text-gray-700">Privacy: {privacyReports}</h5>
      </div>
    </div>
  );
};

export default StatsBar;
