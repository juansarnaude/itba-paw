import React, {useEffect, useState} from "react";
import SortOrder from "../../../api/values/SortOrder";
import OrderBy from "../../../api/values/MediaOrderBy";
import ListContentPaginated from "../listContentPaginated/ListContentPaginated";
import MediaService from "../../../services/MediaService";
import profileApi from "../../../api/ProfileApi";
import {Spinner} from "react-bootstrap";

function ProfileTabMediaLists({ type, username }) {
    const [currentOrderBy, setOrderBy] = useState(OrderBy.CUSTOM_ORDER);
    const [currentSortOrder, setSortOrder] = useState(SortOrder.DESC);
    const [page, setPage] = useState(1);

    const [listContent, setListContent] = useState(undefined);
    const [listPagination, setListPagination] = useState(undefined);
    const [listContentLoading, setListContentLoading] = useState(true);
    const [listContentError, setListContentError] = useState(false);


    useEffect(() => {
        async function getData() {
            try {
                const data = await profileApi.getSpecialListFromUser(
                    username,
                    type,
                    currentOrderBy,
                    currentSortOrder,
                    page
                );
                setListPagination(data.data);
                const idList = MediaService.getIdMediaFromObjectList(data.data);
                if (idList.length > 0) {
                    setListContent(await MediaService.getMediaByIdList(idList));
                } else {
                    setListContent([]);
                }
                setListContentLoading(false);
            } catch (error) {
                setListContentError(error);
                setListContentLoading(false);
            }
        }
        getData();
    }, [type, username, currentOrderBy, currentSortOrder, page]);


    if (listContentLoading) return <div className={'mt-6 d-flex justify-content-center'}><Spinner/></div>

    return (
        <ListContentPaginated
            listContent={listContent}
            page={page}
            lastPage={listPagination?.links?.last?.page}
            handlePageChange={setPage}
            currentOrderBy={currentOrderBy}
            setOrderBy={setOrderBy}
            currentSortOrder={currentSortOrder}
            setSortOrder={setSortOrder}
        />
    );
}

export default ProfileTabMediaLists;