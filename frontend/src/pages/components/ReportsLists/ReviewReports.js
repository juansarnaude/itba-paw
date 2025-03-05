import React, { useEffect, useState } from 'react';
import reportApi from '../../../api/ReportApi';
import reviewApi from '../../../api/ReviewApi';
import userApi from '../../../api/UserApi';
import ConfirmationModal from '../../components/forms/confirmationForm/confirmationModal';
import api from '../../../api/api';
import {useTranslation} from "react-i18next";
import ReportTypes from '../../../api/values/ReportTypes';
import mediaApi from '../../../api/MediaApi';
import {Spinner} from "react-bootstrap";

export default function ReviewReports() {
  const [reviews, setReviews] = useState([]);
  const [reviewsLoading, setReviewsLoading] = useState(true);
  const [selectedAction, setSelectedAction] = useState(null);
  const { t } = useTranslation();

  useEffect(() => {
    fetchReports();
  }, []);

  const fetchReports = async () => {
    try {
      const response = await reportApi.getReports({ contentType: 'review' });
      const reportsData = response.data || [];
      
      // Get unique URLs
      const uniqueUrls = [...new Set(reportsData.map(report => report.url))];
      
      try {
        // Fetch all reviews in parallel
        const reviewPromises = uniqueUrls.map(url => api.get(url));
        const reviewResponses = await Promise.all(reviewPromises);
        const reviews = reviewResponses.map(response => response.data);
        
        try {
          // Fetch all report counts and media details in parallel
          const allPromises = reviews.flatMap(review => {
            const params = { contentType: 'review', resourceId: review.id };
            const promises = [
              reportApi.getReportCounts({ ...params, reportType: ReportTypes['Abuse & Harassment'] }),
              reportApi.getReportCounts({ ...params, reportType: ReportTypes.Hate }),
              reportApi.getReportCounts({ ...params, reportType: ReportTypes.Spam }),
              reportApi.getReportCounts({ ...params, reportType: ReportTypes.Privacy })
            ];
            
            if (review.mediaId) {
              promises.push(mediaApi.getMediaById(review.mediaId));
            } else {
              promises.push(Promise.resolve({ data: null }));
            }
            
            return promises;
          });
          
          const allResults = await Promise.all(allPromises);
          
          // Add report counts and media details to reviews
          const reviewsWithReports = reviews.map((review, index) => {
            const baseIndex = index * 5;
            return {
              ...review,
              abuseReports: allResults[baseIndex].data.count,
              hateReports: allResults[baseIndex + 1].data.count,
              spamReports: allResults[baseIndex + 2].data.count,
              privacyReports: allResults[baseIndex + 3].data.count,
              totalReports: allResults[baseIndex].data.count + 
                           allResults[baseIndex + 1].data.count + 
                           allResults[baseIndex + 2].data.count + 
                           allResults[baseIndex + 3].data.count,
              mediaDetails: allResults[baseIndex + 4].data
            };
          });

          setReviews(reviewsWithReports);
        } catch (error) {
          console.error('Error fetching additional details:', error);
          setReviews(reviews); // Set reviews without additional details
        }
      } catch (error) {
        console.error('Error fetching reviews:', error);
        setReviews([]);
      }
    } catch (error) {
      console.error('Error fetching reports:', error);
      setReviews([]);
    } finally {
      setReviewsLoading(false);
    }
  };

  const handleDelete = async (review) => {
    try {
      await reviewApi.deleteReviewById(review.id);
      await fetchReports();
    } catch (error) {
      console.error('Error deleting review:', error);
    }
  };

  const handleBan = async (review) => {
    try {
      const response = await api.get(review.userUrl);
      const user = response.data;
      await userApi.banUser(user.username);
      await fetchReports();
    } catch (error) {
      console.error('Error banning user:', error);
    }
  };

  const handleResolve = async (review) => {
    try {
      await reportApi.resolveReviewReport(review.id);
      await fetchReports();
    } catch (error) {
      console.error('Error resolving report:', error);
    }
  };

  if (reviewsLoading) return <div className={'mt-6 d-flex justify-content-center'}><Spinner/></div>

  return (
    <div className="container-fluid">
      <h3 className="text-xl font-semibold mb-4">
        {t('reviewReports.reviewReports')}
      </h3>
      {reviews.length === 0 ? (
        <div className="text-center text-gray-500">{t('reviewReports.noReviewReports')}</div>
      ) : (
        <div className="space-y-4">
          {reviews.map((review, index) => (
            <div key={index} className="review container-fluid bg-white my-3 p-4 rounded shadow">
              <div className="review-header d-flex align-items-center justify-between">
                <div>
                  <div className="flex items-center space-x-4">
                    <a href={ process.env.PUBLIC_URL + `/profile/${review.username}`} className="text-blue-600 font-bold hover:underline">
                      {review.username}
                    </a>
                    {review.mediaDetails && (
                      <span className="text-gray-600">
                        {t('reviews.onMedia')} <a href={ process.env.PUBLIC_URL + `/details/${review.mediaDetails.id}`} className="text-blue-600 hover:underline">
                          {review.mediaDetails.name}
                        </a>
                      </span>
                    )}
                  </div>
                  {review.lastModified && (
                    <div className="text-sm text-gray-500">
                      {t('reviews.lastModified')} {review.lastModified}
                    </div>
                  )}
                </div>
                <div>
                  <div className="text-sm text-gray-600 flex space-x-3 mb-2">
                    <span className="flex items-center">
                      <i className="bi bi-star-fill text-yellow-500 mr-1"></i>
                      {review.rating}/5
                    </span>
                    <span className="flex items-center">
                      <i className="bi bi-hand-thumbs-up mr-1"></i>
                      {review.likes || 0}
                    </span>
                  </div>
                  <div className="text-sm text-gray-600 flex space-x-3">
                    <span className="flex items-center" title={t('reports.totalReports')}>
                      <i className="bi bi-flag mr-1"></i>{review.totalReports}
                    </span>
                    <span className="flex items-center" title={t('reports.spamReports')}>
                      <i className="bi bi-envelope-exclamation mr-1"></i>{review.spamReports}
                    </span>
                    <span className="flex items-center" title={t('reports.hateReports')}>
                      <i className="bi bi-emoji-angry mr-1"></i>{review.hateReports}
                    </span>
                    <span className="flex items-center" title={t('reports.abuseReports')}>
                      <i className="bi bi-slash-circle mr-1"></i>{review.abuseReports}
                    </span>
                    <span className="flex items-center" title={t('reports.privacyReports')}>
                      <i className="bi bi-incognito mr-1"></i>{review.privacyReports}
                    </span>
                  </div>
                </div>
              </div>
              <div className="review-content my-4 text-gray-700">
                {review.reviewContent}
              </div>
              <div className="flex justify-end space-x-3">
                <button
                  onClick={() => setSelectedAction({type:'delete', item:review})}
                  className="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600"
                >
                  <i className="bi bi-trash mr-2"></i>
                  {t('reviewReports.delete')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'ban', item:review})}
                  className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                >
                  <i className="bi bi-person-x mr-2"></i>
                  {t('reviewReports.banUser')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'resolve', item:review})}
                  className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                  <i className="bi bi-check2-circle mr-2"></i>
                  {t('reviewReports.resolve')}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
      {selectedAction && (
        <ConfirmationModal
          title={
            selectedAction.type === 'delete' ? t('reviewReports.confirmReviewDeletionTitle') :
            selectedAction.type === 'ban' ? t('reviewReports.confirmUserBanTitle') :
            t('reviewReports.resolveReport')
          }
          message={
            selectedAction.type === 'delete' ? t('reviewReports.confirmReviewDeletionMessage') :
            selectedAction.type === 'ban' ? t('reviewReports.confirmUserBanMessage') :
            t('reviewReports.confirmResolveReportMessage')
          }
          onConfirm={async () => {
            if (selectedAction.type === 'delete') await handleDelete(selectedAction.item);
            if (selectedAction.type === 'ban') await handleBan(selectedAction.item);
            if (selectedAction.type === 'resolve') await handleResolve(selectedAction.item);
            setSelectedAction(null);
          }}
          onCancel={() => setSelectedAction(null)}
        />
      )}
    </div>
  );
}
