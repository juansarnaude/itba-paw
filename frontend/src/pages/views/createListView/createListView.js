import { useDispatch, useSelector } from "react-redux";
import { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import mediaTypes from "../../../api/values/MediaTypes";
import mediaOrderBy from "../../../api/values/MediaOrderBy";
import SortOrder from "../../../api/values/SortOrder";
import { Spinner } from "react-bootstrap";
import FiltersGroup from "../../components/filters/filtersGroup/filtersGroup";
import MediaCard from "../../components/media/mediaCard/mediaCard";
import { Pagination } from "@mui/material";
import CreateListForm from "../../components/forms/createListForm/CreateListForm";
import {
    resetList,
    setDescription,
    setIsPrivate,
    setName,
    toggleMediaSelection
} from "../../../features/createListSlice";
import ListService from "../../../services/ListService";
import useMediaList from "../../../hooks/useMediasList";
import MoovieListTypes from "../../../api/values/MoovieListTypes";

const CreateListView = () => {
    const { t } = useTranslation(); // Initialize translation

    // Form States
    const dispatch = useDispatch();
    const { selectedMedia, name, description, isPrivate } = useSelector((state) => state.list);
    const { user } = useSelector((state) => state.auth);

    // Filter States
    const [selectedProviders, setSelectedProviders] = useState([]);
    const [selectedGenres, setSelectedGenres] = useState([]);
    const [searchQuery, setSearchQuery] = useState("");
    const [type, setType] = useState(mediaTypes.TYPE_ALL);
    const [orderBy, setOrderBy] = useState(mediaOrderBy.TOTAL_RATING);
    const [sortOrder, setSortOrder] = useState(SortOrder.DESC);
    const [page, setPage] = useState(1);

    // Filter Memos
    const memoizedProviders = useMemo(() => Array.from(selectedProviders || []), [selectedProviders]);
    const memoizedGenres = useMemo(() => Array.from(selectedGenres || []), [selectedGenres]);

    const handleFilterChange = ({ type, sortOrder, orderBy, search, selectedProviders, selectedGenres }) => {
        setSelectedProviders(selectedProviders);
        setSelectedGenres(selectedGenres);
        setOrderBy(orderBy);
        setSearchQuery(search);
        setType(type);
        setSortOrder(sortOrder);
        setPage(1);
    };

    const handlePaginationChange = (event, value) => {
        setPage(value);
    };

    const onClickCallback = (media) => {
        dispatch(toggleMediaSelection(media));
    };

    const onResetCallback = () => {
        dispatch(resetList());
    };

    const createListCallback = async () => {
        try {
            const response = await ListService.createMoovieList({
                name: name,
                type: isPrivate ? MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.type : MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.type,
                description: description,
            });

            if (!response || !response.data || !response.data.url) {
                throw new Error(t("createList.invalid_response"));
            }

            const urlParts = response.data.url.split("/");
            const listId = urlParts[urlParts.length - 1];

            if (!listId) {
                throw new Error(t("createList.failed_extract_id"));
            }

            if (selectedMedia.length > 0) {
                const mediaIds = selectedMedia.map((media) => media.id);
                await ListService.insertMediaIntoMoovieList({
                    id: listId,
                    mediaIds: mediaIds,
                });
            }

            onResetCallback();
            return { success: true, listId };
        } catch (e) {
            console.error(t("createList.creating_list"), e);
            return { success: false, id: 0 };
        }
    };

    const { medias, mediasLoading, mediasError } = useMediaList({
        type: type,
        page: page,
        sortOrder: sortOrder,
        orderBy: orderBy,
        search: searchQuery,
        selectedProviders: memoizedProviders,
        selectedGenres: memoizedGenres,
    });

    if (mediasLoading) return <div className={'mt-6 d-flex justify-content-center'}><Spinner/></div>;

    return (
        <div className={'d-flex flex-column'}>
            <div className={'m-1'} style={{ width: "100vw", height: "1vh" }}></div>
            <div className={'d-flex flex-row m-2'}>
                <FiltersGroup
                    submitCallback={handleFilterChange}
                    searchBar={true}
                    type={type}
                    orderBy={orderBy}
                    sortOrder={sortOrder}
                    query={searchQuery}
                    initialSelectedGenres={selectedGenres}
                    initialSelectedProviders={selectedProviders}
                />
                <div className={'container d-flex flex-column'}>
                    <div style={{ overflowY: "auto", maxHeight: "80vh", width: "60vw" }} className={'flex-wrap d-flex justify-content-evenly'}>
                        {mediasLoading ? <Spinner /> : medias.data.map(media => (
                            <MediaCard key={media.id}
                                       isSelected={selectedMedia.some((selected) => selected.id === media.id)}
                                       media={media} onClick={() => onClickCallback(media)}
                            />
                        ))}
                        {mediasError && <div>{t("createList.general")}: {mediasError.message}</div>}
                    </div>
                    <div className={'m-1 d-flex justify-center'}>
                        {!mediasLoading &&
                            <Pagination onChange={handlePaginationChange} page={page} count={medias.links.last.page} />
                        }
                    </div>
                </div>
                <div style={{ maxWidth: "22vw" }} className={'container d-flex flex-column'}>
                    <CreateListForm
                        name={name} setName={(value) => dispatch(setName(value))}
                        description={description} setDescription={(value) => dispatch(setDescription(value))}
                        isPrivate={isPrivate} setIsPrivate={(value) => dispatch(setIsPrivate(value))}
                        selectedMedia={selectedMedia}
                        onDeleteCallback={onClickCallback}
                        onResetCallback={onResetCallback}
                        onSubmitCallback={createListCallback}
                    />
                </div>
            </div>
        </div>
    );
};

export default CreateListView;
