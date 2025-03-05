import "../forms/formsStyle.css";
import React, { useState, useEffect, useCallback } from "react";
import mediaService from "../../../services/MediaService";
import MediaService from "../../../services/MediaService";
import pagingSizes from "../../../api/values/PagingSizes";
import "../../../api/values/MediaTypes";
import MediaTypes from "../../../api/values/MediaTypes";
import SortOrder from "../../../api/values/SortOrder";
import MediaOrderBy from "../../../api/values/MediaOrderBy";
import MediaCard from "../mediaCard/MediaCard";
import moovieListReviewService from "../../../services/MoovieListReviewService";
import listService from "../../../services/ListService";
import { useTranslation } from "react-i18next";

const ListContentPaginatedSearchMode = ({ moovieListId, handleCloseSearchMode, onMediaAdded }) => {
    const [searchQuery, setSearchQuery] = useState('');
    const [mediaList, setMediaList] = useState(null);
    const { t } = useTranslation();
    const [searchTimeout, setSearchTimeout] = useState(null);
    const [successMessage, setSuccessMessage] = useState(null);
    const [errorMessage, setErrorMessage] = useState(null);
    const fetchMedia = useCallback(async (query) => {
        try {
            const mediasResponse = await MediaService.getMedia({
                type: MediaTypes.TYPE_MOVIE,
                page: 1,
                pageSize: pagingSizes.MEDIA_DEFAULT_PAGE_SIZE,
                sortOrder: SortOrder.DESC,
                orderBy: MediaOrderBy.VOTE_COUNT,
                search: query
            });
            setMediaList(mediasResponse);
        } catch (e) {
            setMediaList(null);
        }
    }, []);

    const handleInputChange = (event) => {
        const query = event.target.value;
        setSearchQuery(query);

        // Clear any existing timeout
        if (searchTimeout) {
            clearTimeout(searchTimeout);
        }

        if (query.trim() === "") {
            setMediaList(null);
            return;
        }

        // Set new timeout
        const timeoutId = setTimeout(() => {
            fetchMedia(query);
        }, 300);

        setSearchTimeout(timeoutId);
    };

    // Cleanup timeout on component unmount
    useEffect(() => {
        return () => {
            if (searchTimeout) {
                clearTimeout(searchTimeout);
            }
        };
    }, [searchTimeout]);

    const handleMediaClick = async (media) => {
        try {
            const response = await listService.insertMediaIntoMoovieList({
                id: moovieListId,
                mediaIds: [media.id]
            });
            if (response.status === 200) {
                // Show success message
                setSuccessMessage(media.name);
                // Trigger refresh in parent component
                onMediaAdded();
                // Hide message after 2 seconds
                setTimeout(() => {
                    setSuccessMessage(null);
                }, 5000);
            } else {
                setErrorMessage(media.name);
                setTimeout(() => {
                    setErrorMessage(null);
                }, 5000);
            }
        } catch(e) {
        }
    };

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
            <div className="bg-white dark:bg-gray-800 rounded-lg w-full max-w-4xl shadow-xl flex flex-col relative" style={{ height: '80vh' }}>
            {/* Success Banner */}
            {successMessage && (
                    <div className="absolute top-0 left-0 right-0 bg-green-500 text-white px-4 py-2 rounded-t-lg flex items-center justify-between">
                        <div className="flex items-center">
                            <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7" />
                            </svg>
                            <span>{`${successMessage} ${t('listContentSearch.addedToList')}`}</span>
                        </div>
                    </div>
                )}

            {/* Error Banner */}
            {errorMessage && (
                    <div className="absolute top-0 left-0 right-0 bg-red-500 text-white px-4 py-2 rounded-t-lg flex items-center justify-between">
                        <div className="flex items-center">
                            <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
                            </svg>
                            <span>{`${errorMessage} ${t('listContentSearch.alreadyInList')}`}</span>
                        </div>
                    </div>
                )}

                {/* Header */}
                <div className="p-4 border-b border-gray-200 dark:border-gray-700 flex justify-between items-center">
                    <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
                        {t('listContentSearch.title')}
                    </h3>
                    <button 
                        onClick={handleCloseSearchMode} 
                        className="text-gray-400 hover:text-gray-500 focus:outline-none"
                    >
                        <span className="text-2xl">&times;</span>
                    </button>
                </div>

                {/* Search Input */}
                <div className="p-4 border-b border-gray-200 dark:border-gray-700">
                    <input
                        type="text"
                        value={searchQuery}
                        onChange={handleInputChange}
                        placeholder={t('listContentSearch.placeholder')}
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
                    />
                    <p className="mt-2 text-sm text-gray-500 dark:text-gray-400">
                        {t('listContentSearch.helper')}
                    </p>
                </div>

                {/* Results Grid - Fixed Height Container */}
                <div className="flex-1 overflow-y-auto min-h-0">
                    <div className="p-4">
                        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                            {mediaList?.data?.map((media) => (
                                <div 
                                    key={media.id}
                                    onClick={() => handleMediaClick(media)}
                                    className="cursor-pointer transform transition-transform hover:scale-105"
                                >
                                    <MediaCard
                                        media={media}
                                        size="small"
                                        showWWButtons={false}
                                        disableOnClick={true}
                                    />
                                </div>
                            ))}
                        </div>
                        
                        {mediaList?.data?.length === 0 && (
                            <div className="text-center py-8 text-gray-500 dark:text-gray-400">
                                {t('listContentSearch.noResults')}
                            </div>
                        )}
                        
                        {!mediaList && searchQuery && (
                            <div className="text-center py-8 text-gray-500 dark:text-gray-400">
                                {t('listContentSearch.startTyping')}
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ListContentPaginatedSearchMode;
