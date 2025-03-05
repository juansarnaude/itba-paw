import api from './api.js';
import VndType from "../enums/VndType";

const reportApi = (() => {

    // --------------- REPORTING ---------------

    const reportReview = async ({reviewId, reportedBy, content, type}) => {
        const response = await api.post('/reports',
            {
                type: type
            },
            {params:
                {
                    reviewId: reviewId
                },
                headers: {
                    'Content-Type': VndType.APPLICATION_REPORT_FORM,
                }
            });
        return response;
    }

    const reportComment = async ({commentId, reportedBy, content, type}) => {
        const response = await api.post('/reports',
            {
                type: type
            },
            {params:
                {
                    commentId: commentId
                },
                headers: {
                    'Content-Type': VndType.APPLICATION_REPORT_FORM,
                }
            }
        );
        return response;
    }

    const reportMoovieList = async ({moovieListId, reportedBy, content, type}) => {
        const response = await api.post('/reports',
            {
                type: type
            },
            {params:
                {
                    moovieListId: moovieListId
                },
                headers: {
                    'Content-Type': VndType.APPLICATION_REPORT_FORM,
                }
            }
        );
        return response;
    }

    const reportMoovieListReview = async ({moovieListReviewId, reportedBy, content, type}) => {
        console.log(type);
        const response = await api.post('/reports',
            {
                type: type
            },
            {params:
                {
                    moovieListReviewId: moovieListReviewId
                },
                headers: {
                    'Content-Type': VndType.APPLICATION_REPORT_FORM,
                }
            }
        );
        return response;
    }

    // --------------- GET REPORTS ---------------

    const getReports = async ({contentType}) => {
        const response = await api.get('/reports', {params: {contentType}});
        return response;
    }

    const getReportCounts = async ({contentType, reportType, resourceId} = {}) => {
        const params = {};
        if (contentType) params.contentType = contentType;
        if (reportType) params.reportType = reportType;
        if (resourceId) params.resourceId = resourceId;

        const response = await api.get('/reports/count', {params});
        return response;
    }

    // --------------- ACTIONS ---------------

    const resolveReviewReport = async (reviewId) => {
        const response = await api.delete('/reports',
            {params:
                {
                    reviewId: reviewId
                }
            });
        return response;
    }

    const resolveCommentReport = async (commentId) => {
        const response = await api.delete('/reports',
            {params:
                {
                    commentId: commentId
                }
            }
        );
        return response;
    }

    const resolveMoovieListReport = async (moovieListId) => {
        const response = await api.delete('/reports',
            {params:
                {
                    moovieListId: moovieListId
                }
            });
        return response;
    }

    const resolveMoovieListReviewReport = async (moovieListReviewId) => {
        const response = await api.delete('/reports',
            {params:
                {
                    moovieListReviewId: moovieListReviewId
                }
            });
        return response;
    }

    return {
        reportReview,
        reportComment,
        reportMoovieList,
        reportMoovieListReview,
        getReports,
        getReportCounts,
        resolveReviewReport,
        resolveCommentReport,
        resolveMoovieListReport,
        resolveMoovieListReviewReport
    }
})();

export default reportApi;