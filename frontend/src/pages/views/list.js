import React, {useEffect, useState} from 'react';
import {createSearchParams, useNavigate, useParams, useSearchParams} from "react-router-dom";
import ListHeader from "../components/listHeader/ListHeader";
import OrderBy from "../../api/values/MediaOrderBy";
import SortOrder from "../../api/values/SortOrder";
import "../components/mainStyle.css"
import ListService from "../../services/ListService";
import pagingSizes from "../../api/values/PagingSizes";
import ListContentPaginated from "../components/listContentPaginated/ListContentPaginated";
import Reviews from "../components/ReviewsSection/Reviews";
import ListCard from "../components/listCard/ListCard";
import {ProgressBar, Spinner} from "react-bootstrap"
import ListApi from "../../api/ListApi";
import Error403 from "./errorViews/error403";
import {parsePaginatedResponse} from "../../utils/ResponseUtils";
import {useSelector} from "react-redux";
import {useTranslation} from "react-i18next";
import ConfirmationModal from "../components/forms/confirmationForm/confirmationModal";
import moovieListReviewApi from "../../api/MoovieListReviewApi";
import reportApi from "../../api/ReportApi";
import ReportForm from "../components/forms/reportForm/reportForm";
import profileService from "../../services/ProfileService";
import useErrorStatus from "../../hooks/useErrorStatus";

function List() {
    const [error403, setError403] = useState(false);
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const {t} = useTranslation();
    const { setErrorStatus } = useErrorStatus();

    const {id} = useParams();
    const [currentOrderBy, setOrderBy] = useState(OrderBy.CUSTOM_ORDER);
    const [currentSortOrder, setSortOrder] = useState(SortOrder.ASC);
    const [page, setPage] = useState(Number(searchParams.get("page")) || 1);

    const {isLoggedIn, user} = useSelector(state => state.auth);


    //GET VALUES FOR LIST
    const [list, setList] = useState(undefined);
    const [listLoading, setListLoading] = useState(true);
    const [listError, setListError] = useState(null);


    const handlePageChange = (newPage) => {
        setPage(newPage);
        navigate({
            pathname: `/list/${id}`,
            search: createSearchParams({
                orderBy:currentOrderBy,
                sortOrder: currentSortOrder,
                page: newPage.toString(),
            }).toString(),
        });
    };

    useEffect(() => {
        navigate({
            pathname: `/list/${id}`,
            search: createSearchParams({
                orderBy: currentOrderBy,
                sortOrder: currentSortOrder,
                page: page.toString(),
            }).toString(),
        });
    }, [id, currentOrderBy, currentSortOrder, page, navigate]);

    const [updateListHeader, setUpdateListHeader] = useState(false);

    const handleUpdateList = () =>{
        //To update if edited
        setUpdateListHeader(true);
    }

    useEffect(() => {
        async function getData() {
            try {
                const data = await ListService.getListById(id);
                setList(data);
                setListLoading(false);
                setUpdateListHeader(false);
            } catch (error) {
                setListError(error);
                setListLoading(false);
                setErrorStatus(error.response.status);
            }
        }
        getData();
    }, [id, updateListHeader, setErrorStatus]);

    //GET VALUES FOR LIST CONTENT
    const [listContent, setListContent] = useState(undefined);
    const [listContentLoading, setListContentLoading] = useState(true);
    const [listContentError, setListContentError] = useState(null);
    const [flag, setFlag] = useState(false);
    const Refresh = () => {
        setFlag(!flag);
    }

    useEffect(() => {
        async function getData() {
            try {
                const data = await ListService.getListContentById({
                    id: id,
                    orderBy: currentOrderBy,
                    sortOrder: currentSortOrder,
                    pageNumber: page,
                    pageSize: pagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT
                });
                setListContent(data);
                setListContentLoading(false);
            } catch (error) {
                setListContentError(error);
                setListContentLoading(false);
            }
        }
        getData();
    }, [currentOrderBy,currentSortOrder,page, flag]);

    const [watchedCount, setWatchedCount] = useState(0);
    useEffect( () => {
        async function getWatchedCount() {
            try{
                const data = await profileService.getWatchedCountFromMovieListId(id, user.username);
                setWatchedCount(data.data.count);
            } catch (e){
                setWatchedCount(0);
            }
        }
        getWatchedCount();
    },[id]);

    const [listRecommendations, setListRecommendations] = useState(undefined);
    const [listRecommendationsLoading, setlistRecommendationsLoading] = useState(true);
    const [listRecommendationsError, setlistRecommendationsError] = useState(null);

    useEffect(() => {
        async function getData() {
            try {
                const data = await ListService.getRecommendedLists(id)
                setListRecommendations(data);

                setlistRecommendationsLoading(false);
            } catch (error) {
                setlistRecommendationsError(error);
                setlistRecommendationsLoading(false);
            }
        }
        getData();
    }, [id]);

    const [showReviewForm, setShowReviewForm] = useState(false);
    const [reviewContent, setReviewContent] = useState("");
    const [reviewSubmitting, setReviewSubmitting] = useState(false);

    const handleReviewSubmit = async () => {
        if (!isLoggedIn) {
            navigate('/login');
            return;
        }
        
        setReviewSubmitting(true);
        try {
            await moovieListReviewApi.createMoovieListReview(id, reviewContent);
            setShowReviewForm(false);
            setReviewContent("");
            // Refresh reviews
            setFlag(!flag);
        } catch (error) {
            console.error("Error submitting review:", error);
        }
        setReviewSubmitting(false);
    };

    const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
    const [showReportForm, setShowReportForm] = useState(false);

    const handleDeleteList = async () => {
        try {
            await ListApi.deleteList(id);
            navigate('/');
        } catch (error) {
            console.error("Error deleting list:", error);
        }
    };

    const handleReportList = async (reportReason) => {
        try {
            const response = await reportApi.reportMoovieList({
                moovieListId: id,
                reportedBy: user.username,
                type: reportReason
            });
            setShowReportForm(false);
            return response;
        } catch (error) {
            console.error("Error reporting list:", error);
            return error;
        }
    };

    if(error403){
        return(
            <Error403></Error403>
        )
    }

    if (listLoading || listContentLoading || listRecommendationsLoading) return <div className={'mt-6 d-flex justify-content-center'}><Spinner/></div>

    return (
        <div className="default-container moovie-default">
            <ListHeader
                list={list?.data || []} 
                updateHeader={handleUpdateList}
                onDelete={handleDeleteList}
                onReport={handleReportList}
                showDeleteConfirmation={showDeleteConfirmation}
                setShowDeleteConfirmation={setShowDeleteConfirmation}
                showReportForm={showReportForm}
                setShowReportForm={setShowReportForm}
            />
            
            {showDeleteConfirmation && (
                <ConfirmationModal
                    title={t('list.deleteList')}
                    message={t('list.deleteListConfirmation')}
                    onConfirm={handleDeleteList}
                    onCancel={() => setShowDeleteConfirmation(false)}
                />
            )}

            {showReportForm && (
                <ReportForm
                    onReportSubmit={handleReportList}
                    onCancel={() => setShowReportForm(false)}
                />
            )}

            {(isLoggedIn && list && list.data) && (
                <div style={{marginTop : "5px"}}>
                    <ProgressBar
                        now={list.data.mediaCount === 0 ? 100 : (watchedCount / list.data.mediaCount) * 100}
                        label={t('profile.watched') + ": "  + `${Math.round(list.data.mediaCount === 0 ? 100 : (watchedCount / list.data.mediaCount) * 100)}%`}
                    />
                </div>
            )}



            {listContentLoading ? (
                <p>{t('list.loading')}</p>
            ) : (
                <ListContentPaginated
                    listContent={listContent}
                    page={page}
                    lastPage={listContent?.links?.last?.page}
                    handlePageChange={handlePageChange}
                    currentOrderBy={currentOrderBy}
                    setOrderBy={setOrderBy}
                    currentSortOrder={currentSortOrder}
                    setSortOrder={setSortOrder}
                    setListContent={setListContent}
                    isOwner={isLoggedIn === true && (list?.data.createdBy === user.username)}
                    listId={id}
                    Refresh={Refresh}
                />
            )}

            <h3>{t('list.prompt')}</h3>
            <div style={{ 
                width: '100%',
                display: 'flex',
                justifyContent: 'center',
                overflow: 'hidden' // Contain the scroll area
            }}>
                <div style={{ 
                    maxWidth: '95%',
                    overflowX: 'auto',
                    display: 'flex',
                    justifyContent: 'flex-start',
                    gap: '1rem',
                    padding: '1rem'
                }}>
                    {listRecommendations?.data?.map(list => (
                        <div style={{ flex: '0 0 300px' }}> {/* Fixed width for each card */}
                            <ListCard key={list.id} listCard={list} />
                        </div>
                    ))}
                </div>
            </div>

            <div className="d-flex justify-content-between align-items-center mb-3">
                <h3>{t('list.reviews')}</h3>
                {isLoggedIn && (
                    <button 
                        className="btn btn-primary" 
                        onClick={() => setShowReviewForm(true)}
                    >
                        {t('list.writeReview')}
                    </button>
                )}
            </div>

            {showReviewForm && (
                <ConfirmationModal
                    title={t('list.writeReview')}
                    message={
                        <textarea
                            className="form-control"
                            value={reviewContent}
                            onChange={(e) => setReviewContent(e.target.value)}
                            rows={4}
                            placeholder={t('list.reviewPlaceholder')}
                        />
                    }
                    onConfirm={handleReviewSubmit}
                    onCancel={() => {
                        setShowReviewForm(false);
                        setReviewContent("");
                    }}
                />
            )}

            <Reviews id={id} source={'list'} handleParentReload={() => setFlag(!flag)} parentReload={flag} />

        </div>
    );

}

export default List;