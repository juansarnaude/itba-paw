import React, { useEffect, useState } from 'react';
import reportApi from '../../../api/ReportApi';
import ConfirmationModal from '../../components/forms/confirmationForm/confirmationModal';
import api from '../../../api/api';
import userApi from '../../../api/UserApi';
import commentApi from '../../../api/CommentApi';
import {useTranslation} from "react-i18next";
import ReportTypes from '../../../api/values/ReportTypes';
import { Tooltip } from "react-tooltip";
import mediaApi from '../../../api/MediaApi';
import {Spinner} from "react-bootstrap";

export default function CommentReports() {
  const [comments, setComments] = useState([]);
  const [commentsLoading, setCommentsLoading] = useState(true);
  const [selectedAction, setSelectedAction] = useState(null);
  const { t } = useTranslation();
  // selectedAction = {type: 'delete'|'ban'|'resolve', item: comment}

  useEffect(() => {
    fetchComments();
  }, []);

  const fetchComments = async () => {
    try {
      const response = await reportApi.getReports({ contentType: 'comment' });
      const reportsData = response.data || [];
      
      // Get unique URLs
      const uniqueUrls = [...new Set(reportsData.map(report => report.url))];
      
      try {
        // Fetch all comments in parallel
        const commentPromises = uniqueUrls.map(url => api.get(url));
        const commentResponses = await Promise.all(commentPromises);
        const comments = commentResponses.map(response => response.data);
        
        try {
          // Fetch all report counts, reviews, and media details in parallel
          const allPromises = comments.flatMap(comment => {
            const params = { contentType: 'comment', resourceId: comment.id };
            const promises = [
              reportApi.getReportCounts({ ...params, reportType: ReportTypes['Abuse & Harassment'] }),
              reportApi.getReportCounts({ ...params, reportType: ReportTypes.Hate }),
              reportApi.getReportCounts({ ...params, reportType: ReportTypes.Spam }),
              reportApi.getReportCounts({ ...params, reportType: ReportTypes.Privacy }),
              api.get(comment.reviewUrl),
              mediaApi.getMediaById(comment.mediaId)
            ];
            return promises;
          });
          
          const allResults = await Promise.all(allPromises);
          
          // Add report counts, review, and media details to comments
          const commentsWithDetails = comments.map((comment, index) => {
            const baseIndex = index * 6;
            return {
              ...comment,
              abuseReports: allResults[baseIndex].data.count,
              hateReports: allResults[baseIndex + 1].data.count,
              spamReports: allResults[baseIndex + 2].data.count,
              privacyReports: allResults[baseIndex + 3].data.count,
              totalReports: allResults[baseIndex].data.count + 
                           allResults[baseIndex + 1].data.count + 
                           allResults[baseIndex + 2].data.count + 
                           allResults[baseIndex + 3].data.count,
              reviewDetails: allResults[baseIndex + 4].data,
              mediaDetails: allResults[baseIndex + 5].data
            };
          });

          setComments(commentsWithDetails);
        } catch (error) {
          console.error('Error fetching additional details:', error);
          setComments(comments); // Set comments without additional details
        }
      } catch (error) {
        console.error('Error fetching comments:', error);
        setComments([]);
      }
    } catch (error) {
      console.error('Error fetching reports:', error);
      setComments([]);
    } finally {
      setCommentsLoading(false);
    }
  };

  const handleDelete = async (comment) => {
    try {
      await commentApi.deleteComment(comment.id);
      await fetchComments();
    } catch (error) {
      console.error('Error deleting comment:', error);
    }
  };

  const handleBan = async (comment) => {
    try {
      const response = await api.get(comment.userUrl);
      const user = response.data;
      await userApi.banUser(user.username);
      await fetchComments();
    } catch (error) {
      console.error('Error banning user:', error);
    }
  };

  const handleResolve = async (comment) => {
    try {
      await reportApi.resolveCommentReport(comment.id);
      await fetchComments();
    } catch (error) {
      console.error('Error resolving report:', error);
    }
  };

  if (commentsLoading) return <div className={'mt-6 d-flex justify-content-center'}><Spinner/></div>

  return (
    <div className="container-fluid">
      <h3 className="text-xl font-semibold mb-4">{t('commentReports.commentReports')}</h3>
      {comments.length === 0 ? (
        <div className="text-center text-gray-500">{t('commentReports.noCommentReports')}</div>
      ) : (
        <div className="space-y-4">
          {comments.map((comment, index) => (
            <div key={index} className="bg-white rounded-lg shadow-md p-6">
              <div className="flex justify-between items-start mb-4">
                <div className="space-y-2">
                  <div className="flex items-center space-x-2">
                    <a href={ process.env.PUBLIC_URL + `/profile/${comment.username}`} className="text-blue-600 font-bold hover:underline">
                      {comment.username}
                    </a>
                    <span className="text-gray-500">
                      {t('reviews.onMedia')}
                    </span>
                    <a href={process.env.PUBLIC_URL + `/details/${comment.mediaDetails?.id}`} className="text-blue-600 hover:underline">
                      {comment.mediaDetails?.name}
                    </a>
                  </div>
                  
                  <div className="text-sm text-gray-600">
                    {t('reviews.onMedia')} {t('reviews.reviews')}:
                    <span className="ml-2 text-gray-700 italic">
                      "{comment.reviewDetails?.reviewContent?.length > 50 
                        ? `${comment.reviewDetails.reviewContent.substring(0, 50)}...`
                        : comment.reviewDetails?.reviewContent}"
                    </span>
                  </div>

                  <div className="flex items-center space-x-4 text-sm text-gray-600">
                    <span className="flex items-center">
                      <i className="bi bi-hand-thumbs-up mr-1"></i>
                      {comment.commentLikes}
                    </span>
                    <span className="flex items-center">
                      <i className="bi bi-hand-thumbs-down mr-1"></i>
                      {comment.commentDislikes}
                    </span>
                  </div>
                </div>

                <div className="text-right">
                  <div className="text-sm text-gray-600 flex flex-col items-end space-y-1">
                    <span className="flex items-center" title={t('reports.totalReports')}>
                      <i className="bi bi-flag mr-1"></i>{comment.totalReports}
                    </span>
                    <div className="flex space-x-3">
                      <span className="flex items-center" title={t('reports.spamReports')}>
                        <i className="bi bi-envelope-exclamation mr-1"></i>{comment.spamReports}
                      </span>
                      <span className="flex items-center" title={t('reports.hateReports')}>
                        <i className="bi bi-emoji-angry mr-1"></i>{comment.hateReports}
                      </span>
                      <span className="flex items-center" title={t('reports.abuseReports')}>
                        <i className="bi bi-slash-circle mr-1"></i>{comment.abuseReports}
                      </span>
                      <span className="flex items-center" title={t('reports.privacyReports')}>
                        <i className="bi bi-incognito mr-1"></i>{comment.privacyReports}
                      </span>
                    </div>
                  </div>
                </div>
              </div>

              <div className="bg-gray-50 rounded p-4 my-4">
                <p className="text-gray-700">{comment.content}</p>
              </div>

              <div className="flex justify-end space-x-3">
                <button
                  onClick={() => setSelectedAction({type:'delete', item:comment})}
                  className="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600 transition-colors"
                >
                  <i className="bi bi-trash mr-2"></i>
                  {t('commentReports.delete')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'ban', item:comment})}
                  className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition-colors"
                >
                  <i className="bi bi-person-x mr-2"></i>
                  {t('commentReports.banUser')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'resolve', item:comment})}
                  className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition-colors"
                >
                  <i className="bi bi-check2-circle mr-2"></i>
                  {t('commentReports.resolve')}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
      {selectedAction && (
        <ConfirmationModal
          title={
            selectedAction.type === 'delete' ? t('commentReports.confirmCommentDeletionTitle') :
            selectedAction.type === 'ban' ? t('commentReports.confirmUserBanTitle') :
            t('commentReports.resolveReport')
          }
          message={
            selectedAction.type === 'delete' ? t('commentReports.confirmCommentDeletionMessage') :
            selectedAction.type === 'ban' ? t('commentReports.confirmUserBanMessage') :
            t('commentReports.confirmResolveReportMessage')
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
