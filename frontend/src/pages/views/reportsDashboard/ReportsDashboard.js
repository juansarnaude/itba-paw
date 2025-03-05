import React, { useState } from 'react';
import CommentReports from '../../components/ReportsLists/CommentReports';
import MoovieListReports from '../../components/ReportsLists/MoovieListReports';
import MoovieListReviewReports from '../../components/ReportsLists/MoovieListReviewReports';
import ReviewReports from '../../components/ReportsLists/ReviewReports';
import BannedUsers from '../../components/ReportsLists/BannedUsers';
import {useTranslation} from "react-i18next";

export default function ReportsDashboard() {
  const [selectedType, setSelectedType] = useState('comments');
  const { t } = useTranslation();

  const tabs = [
    {id: 'comments', label: t('reportsDashboard.comments')},
    {id: 'ml', label: t('reportsDashboard.moovieLists')},
    {id: 'mlReviews', label: t('reportsDashboard.moovieListReviews')},
    {id: 'reviews', label: t('reportsDashboard.reviews')},
    {id: 'banned', label: t('reportsDashboard.bannedUsers')}
  ];

  return (
    <div className="min-h-screen bg-gray-100 p-4">
      <div className="container mx-auto bg-white shadow rounded p-6">
        <h2 className="text-2xl font-bold mb-4 text-center">{t('reportsDashboard.title')}</h2>
        
        {/* Tabs */}
        <div className="flex justify-center space-x-2 mb-6">
          {tabs.map(tab => (
            <button
              key={tab.id}
              onClick={() => setSelectedType(tab.id)}
              className={`px-4 py-2 rounded border 
              ${selectedType === tab.id ? 'bg-blue-600 text-white border-blue-600' : 'bg-white text-gray-700 border-gray-300 hover:bg-gray-100'}`}
            >
              {tab.label}
            </button>
          ))}
        </div>

        {/* Content */}
        <div>
          {selectedType === 'comments' && <CommentReports />}
          {selectedType === 'ml' && <MoovieListReports />}
          {selectedType === 'mlReviews' && <MoovieListReviewReports />}
          {selectedType === 'reviews' && <ReviewReports />}
          {selectedType === 'banned' && <BannedUsers />}
        </div>
      </div>
    </div>
  );
}
