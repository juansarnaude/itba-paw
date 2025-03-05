import React, {useEffect, useState} from "react";
import SortOrder from "../../../api/values/SortOrder";
import OrderBy from "../../../api/values/MediaOrderBy";
import UserApi from "../../../api/UserApi";
import ListService from "../../../services/ListService";
import pagingSizes from "../../../api/values/PagingSizes";
import ListContentPaginated from "../listContentPaginated/ListContentPaginated";
import CardsListOrderBy from "../../../api/values/CardsListOrderBy";
import MoovieListTypes from "../../../api/values/MoovieListTypes";
import ListCardsPaginated from "../ListCardsPaginated/ListCardsPaginated";
import UserService from "../../../services/UserService";
import ProfileService from "../../../services/ProfileService";
import {Spinner} from "react-bootstrap";

function ProfileTabMediaLists({ type, username }) {

    const [search, setSearch] = useState(null);
    const [orderBy, setOrderBy] = useState(CardsListOrderBy.LIKE_COUNT);
    const [sortOrder, setSortOrder] = useState(SortOrder.DESC);
    const [page, setPage] = useState(1);

    const [lists, setLists] = useState(undefined);
    const [listsLoading, setListsLoading] = useState(true);
    const [listError, setListError] = useState(false);


    let typeQuery = MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.type;
    let typeString = "null"

    switch (type){
        case("public-lists"):
            typeQuery = MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.type;
            break;
        case("private-lists"):
            typeQuery = MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.type;
            break;
        case("followed-lists"):
            typeQuery = 0;
            typeString = "followed";
            break;
        case("liked-lists"):
            typeQuery = 0;
            typeString = "liked";
            break;
        default:
            typeQuery = -1;
    }

    useEffect(() => {
        async function getData() {
            try {
                setListsLoading(true);
                if (typeQuery === -1) {
                    setListError(true);
                    setListsLoading(false);
                    return;
                }
                let data;
                if (typeQuery !== 0) {
                    const params = {
                        orderBy,
                        ownerUsername: username,
                        pageNumber: page,
                        pageSize: pagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS,
                        search,
                        type: typeQuery,
                        order: sortOrder
                    }
                    data = await ListService.getLists(params);
                } else {
                    const initialData = await ProfileService.getLikedOrFollowedListFromUser(
                        username,
                        typeString,
                        orderBy,
                        sortOrder,
                        page
                    );
                    if (initialData.data && initialData.data.length > 0) {
                        data = await ListService.getListByIdList(ListService.getIdListFromObjectList(initialData.data));
                    } else {
                        data = initialData;
                    }
                }
                setLists(data);
                setListError(false);
            } catch (error) {
                console.error("Error fetching data:", error);
                setLists(null);
                setListError(true);
            } finally {
                setListsLoading(false);
            }
        }
        getData();
    }, [orderBy, sortOrder, page, typeQuery, typeString, username, search, pagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS]);

    if (listsLoading) return <div className={'mt-6 d-flex justify-content-center'}><Spinner/></div>

    return (
        <ListCardsPaginated
            mlcList={lists}
            page={page}
            lastPage={lists?.links?.last?.page}
            handlePageChange={setPage}
            currentOrderBy={orderBy}
            setOrderBy={setOrderBy}
            currentSortOrder={sortOrder}
            setSortOrder={setSortOrder}
        />
    );
}

export default ProfileTabMediaLists;