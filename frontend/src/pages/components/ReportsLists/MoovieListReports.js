import React, { useEffect, useState } from 'react';
import reportApi from '../../../api/ReportApi';
import ConfirmationModal from '../../components/forms/confirmationForm/confirmationModal';
import api from '../../../api/api';
import ListApi from '../../../api/ListApi';
import userApi from '../../../api/UserApi';
import {useTranslation} from "react-i18next";
import ReportTypes from '../../../api/values/ReportTypes';
import {Spinner} from "react-bootstrap";

export default function MoovieListReports() {
  const [lists, setLists] = useState([]);
  const [listsLoading, setListsLoading] = useState(true);
  const [selectedAction, setSelectedAction] = useState(null);
  const { t } = useTranslation();

  useEffect(() => {
    fetchLists();
  }, []);

  const fetchLists = async () => {
    try {
      const response = await reportApi.getReports({ contentType: 'moovieList' });
      const reportsData = response.data || [];
      
      // Get unique URLs
      const uniqueUrls = [...new Set(reportsData.map(report => report.url))];
      
      try {
        // Fetch all lists in parallel
        const listPromises = uniqueUrls.map(url => api.get(url));
        const listResponses = await Promise.all(listPromises);
        const lists = listResponses.map(response => response.data);
        
        try {
          // Fetch all report counts in parallel
          const reportCountPromises = lists.flatMap(list => {
            const params = { contentType: 'moovieList', resourceId: list.id };
            return [
              reportApi.getReportCounts({ ...params, reportType: ReportTypes['Abuse & Harassment'] }),
              reportApi.getReportCounts({ ...params, reportType: ReportTypes.Hate }),
              reportApi.getReportCounts({ ...params, reportType: ReportTypes.Spam }),
              reportApi.getReportCounts({ ...params, reportType: ReportTypes.Privacy })
            ];
          });
          
          const reportCounts = await Promise.all(reportCountPromises);
          
          // Add report counts to lists
          const listsWithReports = lists.map((list, index) => {
            const baseIndex = index * 4;
            return {
              ...list,
              abuseReports: reportCounts[baseIndex].data.count,
              hateReports: reportCounts[baseIndex + 1].data.count,
              spamReports: reportCounts[baseIndex + 2].data.count,
              privacyReports: reportCounts[baseIndex + 3].data.count,
              totalReports: reportCounts[baseIndex].data.count + 
                           reportCounts[baseIndex + 1].data.count + 
                           reportCounts[baseIndex + 2].data.count + 
                           reportCounts[baseIndex + 3].data.count
            };
          });

          setLists(listsWithReports);
        } catch (error) {
          console.error('Error fetching report counts:', error);
          setLists(lists); // Set lists without report counts
        }
      } catch (error) {
        console.error('Error fetching lists:', error);
        setLists([]); 
      }
    } catch (error) {
      console.error('Error fetching reports:', error);
      setLists([]);
    } finally {
      setListsLoading(false);
    }
  };

  const handleDelete = async (ml) => {
    try {
      await ListApi.deleteList(ml.id);
      await fetchLists();
    } catch (error) {
      console.error('Error deleting list:', error);
    }
  };

  const handleBan = async (ml) => {
    try {
      const response = await api.get(ml.creatorUrl);
      const user = response.data;
      await userApi.banUser(user.username);
      await fetchLists();
    } catch (error) {
      console.error('Error banning user:', error);
    }
  };

  const handleResolve = async (ml) => {
    try {
      await reportApi.resolveMoovieListReport(ml.id);
      await fetchLists();
    } catch (error) {
      console.error('Error resolving report:', error);
    }
  };

  if (listsLoading) return <div className={'mt-6 d-flex justify-content-center'}><Spinner/></div>

  return (
    <div className="container-fluid">
      <h3 className="text-xl font-semibold mb-4">{t('moovieListReports.moovieListReports')}</h3>
      {lists.length === 0 ? (
        <div className="text-center text-gray-500">{t('moovieListReports.noMoovieListReports')}</div>
      ) : (
        <div className="space-y-4">
          {lists.map((ml, index) => (
            <div key={index} className="bg-white rounded-lg shadow-md p-6">
              <div className="flex justify-between items-start mb-4">
                <div className="space-y-2">
                  <div className="flex items-center space-x-2">
                    <h4 className="text-lg font-semibold">
                      <a href={process.env.PUBLIC_URL + `/list/${ml.id}`} className="text-blue-600 hover:underline">
                        {ml.name}
                      </a>
                      <span className="text-gray-600 text-sm ml-2">
                        {t('listHeader.by')}{' '}
                        <a href={process.env.PUBLIC_URL + `/profile/${ml.createdBy}`} className="text-blue-600 font-bold hover:underline">
                          {ml.createdBy}
                        </a>
                      </span>
                    </h4>
                  </div>

                  <div className="flex items-center space-x-4 text-sm text-gray-600">
                    <span className="flex items-center">
                      <i className="bi bi-film mr-1"></i>
                      {ml.movieCount} {t('listCard.movies')}
                    </span>
                    <span className="flex items-center">
                      <i className="bi bi-tv mr-1"></i>
                      {ml.mediaCount - ml.movieCount} {t('listCard.series')}
                    </span>
                    <span className="flex items-center">
                      <i className="bi bi-people mr-1"></i>
                      {ml.followers} {t('listHeader.followers')}
                    </span>
                    <span className="flex items-center">
                      <i className="bi bi-hand-thumbs-up mr-1"></i>
                      {ml.likes}
                    </span>
                  </div>
                </div>

                <div className="text-right">
                  <div className="text-sm text-gray-600 flex flex-col items-end space-y-1">
                    <span className="flex items-center" title={t('reports.totalReports')}>
                      <i className="bi bi-flag mr-1"></i>{ml.totalReports}
                    </span>
                    <div className="flex space-x-3">
                      <span className="flex items-center" title={t('reports.spamReports')}>
                        <i className="bi bi-envelope-exclamation mr-1"></i>{ml.spamReports}
                      </span>
                      <span className="flex items-center" title={t('reports.hateReports')}>
                        <i className="bi bi-emoji-angry mr-1"></i>{ml.hateReports}
                      </span>
                      <span className="flex items-center" title={t('reports.abuseReports')}>
                        <i className="bi bi-slash-circle mr-1"></i>{ml.abuseReports}
                      </span>
                      <span className="flex items-center" title={t('reports.privacyReports')}>
                        <i className="bi bi-incognito mr-1"></i>{ml.privacyReports}
                      </span>
                    </div>
                  </div>
                </div>
              </div>

              {ml.images && ml.images.length > 0 && (
                <div className="flex space-x-2 mb-4 overflow-x-auto py-2">
                  {ml.images.slice(0, 4).map((image, imgIndex) => (
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
                <p className="text-gray-700">{ml.description}</p>
              </div>

              <div className="flex justify-end space-x-3">
                <button
                  onClick={() => setSelectedAction({type:'delete', item:ml})}
                  className="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600 transition-colors"
                >
                  <i className="bi bi-trash mr-2"></i>
                  {t('moovieListReports.delete')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'ban', item:ml})}
                  className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition-colors"
                >
                  <i className="bi bi-person-x mr-2"></i>
                  {t('moovieListReports.banUser')}
                </button>
                <button
                  onClick={() => setSelectedAction({type:'resolve', item:ml})}
                  className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition-colors"
                >
                  <i className="bi bi-check2-circle mr-2"></i>
                  {t('moovieListReports.resolve')}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
      {selectedAction && (
        <ConfirmationModal
          title={
            selectedAction.type === 'delete' ? t('reports.confirmListDeletionTitle') :
            selectedAction.type === 'ban' ? t('reports.confirmUserBanTitle') :
            t('reports.resolveReport')
          }
          message={
            selectedAction.type === 'delete' ? t('reports.confirmListDeletionMessage') :
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
