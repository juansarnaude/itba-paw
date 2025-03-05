// src/components/listContentPaginated/ListContentPaginated.js

import React, {useState} from "react";
import ListContent from "../listContent/ListContent";
import PaginationButton from "../paginationButton/PaginationButton";
import DropdownMenu from "../dropdownMenu/DropdownMenu";
import MediaOrderBy from "../../../api/values/MediaOrderBy";
import Button from "react-bootstrap/Button";
import {useSelector} from "react-redux";
import SortOrder from "../../../api/values/SortOrder";
import {useTranslation} from "react-i18next";
import ListContentPaginatedSearchMode from "./ListContentPaginatedSearchMode";

const ListContentPaginated = ({
                                  listContent,
                                  page,
                                  lastPage,
                                  handlePageChange,
                                  currentOrderBy,
                                  setOrderBy,
                                  currentSortOrder,
                                  setSortOrder,
                                  setListContent,
                                  isOwner,
                                  listId,
                                    Refresh
                              }) => {

    const [editMode, setEditMode] = useState(false);
    const {t} = useTranslation();

    const {isLoggedIn, user} = useSelector(state => state.auth);


    const handleEditMode = () =>{
        setEditMode(!editMode);
        setOrderBy(MediaOrderBy.CUSTOM_ORDER);
        setSortOrder(SortOrder.ASC);
    }

    const [searchMediaMode, setSearchMediaMode] = useState(false);
    const handleSearchMediaToAdd = () => {
        setSearchMediaMode(!searchMediaMode);
    }


    return (
        <div>
            <div className="list-actions-container d-flex justify-content-between align-items-center mb-3">
                <div className="d-flex gap-2">
                    {isOwner && (
                        <>
                            <Button 
                                variant={editMode ? "secondary" : "primary"}
                                onClick={handleEditMode}
                            >
                                <i className={`fas fa-${editMode ? 'save' : 'edit'} me-2`}></i>
                                {editMode ? t('listContentPaginated.save') : t('listContentPaginated.edit')}
                            </Button>
                            <Button 
                                variant="success"
                                onClick={handleSearchMediaToAdd}
                            >
                                <i className="fas fa-plus me-2"></i>
                                {t('listContentPaginated.searchMediaToAdd')}
                            </Button>
                        </>
                    )}
                </div>
                
                {!editMode && (
                    <DropdownMenu
                        setOrderBy={setOrderBy}
                        setSortOrder={setSortOrder}
                        currentOrderDefault={currentSortOrder}
                        values={Object.values(MediaOrderBy)}
                    />
                )}
            </div>

            {editMode && (
                <div className="alert alert-info">
                    <i className="fas fa-info-circle me-2"></i>
                    {t('list.dragToModifyOrder')}
                </div>
            )}

            <ListContent 
                listContent={listContent?.data ?? []} 
                editMode={editMode}
                setCurrentSortOrder={setSortOrder} 
                listId={listId} 
                currentPage={page}
                Refresh={Refresh}
                isLoggedIn={isLoggedIn}
                username={isLoggedIn ?  user.username : null}
            />

            <div className="flex justify-center pt-4">
                {listContent?.data?.length > 0 && listContent.links?.last?.page > 1 && (
                    <PaginationButton
                        page={page}
                        lastPage={lastPage}
                        setPage={handlePageChange}
                    />
                )}
            </div>

            {
                searchMediaMode && (
                    <ListContentPaginatedSearchMode 
                        moovieListId={listId} 
                        handleCloseSearchMode={handleSearchMediaToAdd}
                        onMediaAdded={Refresh}
                    />
                )
            }
        </div>
    );
};

export default ListContentPaginated;
