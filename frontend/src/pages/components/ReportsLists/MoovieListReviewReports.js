import React, { useEffect, useState } from 'react';
import reportApi from '../../../api/ReportApi';
import ConfirmationModal from '../../components/forms/confirmationForm/confirmationModal';
import api from '../../../api/api';
import moovieListReviewApi from '../../../api/MoovieListReviewApi';
import userApi from '../../../api/UserApi';
import {useTranslation} from "react-i18next";
import ReportTypes from '../../../api/values/ReportTypes';
import moovieListApi from '../../../api/MoovieListApi';
import {Spinner} from "react-bootstrap";

export default function MoovieListReviewReports() {
  const [reviews, setReviews] = useState([]);
  const [reviewsLoading, setReviewsLoading] = useState(true);
  const [selectedAction, setSelectedAction] = useState(null);
  const [reviewsWithLists, setReviewsWithLists] = useState([]);
  const { t } = useTranslation();

  useEffect(() => {
    fetchReviews();
  }, []);

  const fetchReviews = async () => {
    try {
      const response = await reportApi.getReports({ contentType: 'moovieListReview' });
      const reportsData = response.data || [];
      
      // Get unique URLs
      const uniqueUrls = [...new Set(reportsData.map(report => report.url))];
      
      try {
        // Fetch all reviews in parallel
        const reviewPromises = uniqueUrls.map(url => api.get(url));
        const reviewResponses = await Promise.all(reviewPromises);
        const reviews = reviewResponses.map(response => response.data);
        
        try {
          // Fetch all report counts and moovie lists in parallel
          const allPromises = reviews.flatMap(review => {
            const params = { contentType: 'moovieListReview', resourceId: review.id };
            const promises = [
              reportApi.getReportCounts({ ...params, reportType: ReportTypes['Abuse & Harassment'] }),
              reportApi.getReportCounts({ ...params, reportType: ReportTypes.Hate }),
              reportApi.getReportCounts({ ...params, reportType: ReportTypes.Spam }),
              reportApi.getReportCounts({ ...params, reportType: ReportTypes.Privacy }),
              api.get(review.moovieListUrl).catch(() => ({ data: { removed: true } }))
            ];
            return promises;
          });
          
          const allResults = await Promise.all(allPromises);
          
          // Add report counts and list details to reviews
          const reviewsWithDetails = reviews.map((review, index) => {
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
              listDetails: allResults[baseIndex + 4].data
            };
          });

          setReviews(reviewsWithDetails);
        } catch (error) {
          console.error('Error fetching additional details:', error);
          setReviews(reviews);
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
      await moovieListReviewApi.deleteMoovieListReviewById(review.id);
      await fetchReviews();
    } catch (error) {
      console.error('Error deleting review:', error);
    }
  };

  const handleBan = async (review) => {
    try {
      const response = await api.get(review.creatorUrl);
      const user = response.data;
      await userApi.banUser(user.username);
      await fetchReviews();
    } catch (error) {
      console.error('Error banning user:', error);
    }
  };

  const handleResolve = async (review) => {
    try {
      await reportApi.resolveMoovieListReviewReport(review.id);
      await fetchReviews();
    } catch (error) {
      console.error('Error resolving report:', error);
    }
  };

  if (reviewsLoading) return <div className={'mt-6 d-flex justify-content-center'}><Spinner/></div>

  return (
    <div className="container-fluid">
      <h3 className="text-xl font-semibold mb-4">{t('moovieListReviewReports.moovieListReviewReports')}</h3>
      {reviews.length === 0 ? (
        <div className="text-center text-gray-500">{t('moovieListReviewReports.noMoovieListReviewReports')}</div>
      ) : (
        <div className="space-y-4">
          {reviews.map((review, index) => (
            <div key={index} className="bg-white rounded-lg shadow-md p-6">
              <div className="flex justify-between items-start mb-4">
                <div className="space-y-2">
                  <div className="flex items-center space-x-2">
                    <a href={ process.env.PUBLIC_URL + `/profile/${review.username}`} className="text-blue-600 font-bold hover:underline">
                      {review.username}
                    </a>
                    <span className="text-gray-500">
                      {t('reviews.onMedia')}
                    </span>
                    {review.listDetails?.removed ? (
                      <span className="text-gray-600">{t('list.removed')}</span>
                    ) : (
                      <a href={process.env.PUBLIC_URL + `/list/${review.listDetails?.id}`} className="text-blue-600 hover:underline">
                        {review.listDetails?.name}
                      </a>
                    )}
                  </div>

                  <div className="flex items-center space-x-4 text-sm text-gray-600">
                    <span className="flex items-center">
                      <i className="bi bi-film mr-1"></i>
                      {review.listDetails?.movieCount} {t('listCard.movies')}
                    </span>
                    <span className="flex items-center">
                      <i className="bi bi-tv mr-1"></i>
                      {review.listDetails?.seriesCount || 0} {t('listCard.series')}
                    </span>
                    <span className="flex items-center">
                      <i className="bi bi-hand-thumbs-up mr-1"></i>
                      {review.reviewLikes}
                    </span>
                  </div>
                </div>

                <div className="text-right">
                  <div className="text-sm text-gray-600 flex flex-col items-end space-y-1">
                    <span className="flex items-center" title={t('reports.totalReports')}>
                      <i className="bi bi-flag mr-1"></i>{review.totalReports}
                    </span>
                    <div className="flex space-x-3">
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
              </div>

              {review.listDetails?.images && review.listDetails.images.length > 0 && (
                <div className="flex space-x-2 mb-4 overflow-x-auto py-2">
                  {review.listDetails.images.slice(0, 4).map((image, imgIndex) => (
                    <img
                      key={imgIndex}
                      src={image}
                      alt={`List preview ${imgIndex + 1}`}
                      className="h-20 w-36 object-cover rounded-md shadow-sm"
                    />
                  ))}
                </div>
              )}

              <div className="bg-gray-50 rounded p-4 my-4">
                <p className="text-gray-700">{review.reviewContent}</p>
              </div>

              <div className="flex justify-end space-x-3">
                <button
                  onClick={() => setSelectedAction({type:'delete', item:review})}
                  className="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600 transition-colors"
                >
                  <i className="bi bi-trash mr-2"></i>
                  {t('moovieListReviewReports.delete')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'ban', item:review})}
                  className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition-colors"
                >
                  <i className="bi bi-person-x mr-2"></i>
                  {t('moovieListReviewReports.banUser')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'resolve', item:review})}
                  className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition-colors"
                >
                  <i className="bi bi-check2-circle mr-2"></i>
                  {t('moovieListReviewReports.resolve')}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
      {selectedAction && (
        <ConfirmationModal
          title={
            selectedAction.type === 'delete' ? t('reports.confirmReviewDeletionTitle') :
            selectedAction.type === 'ban' ? t('reports.confirmUserBanTitle') :
            t('reports.resolveReport')
          }
          message={
            selectedAction.type === 'delete' ? t('reports.confirmReviewDeletionMessage') :
            selectedAction.type === 'ban' ? t('reports.confirmUserBanMessage') :
            t('reports.confirmResolveReportMessage')
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
