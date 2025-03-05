import React, {useEffect, useState} from "react";
import "../buttonStyles.css";
import listService from "../../../../services/ListService";
import profileService from "../../../../services/ProfileService";
import MoovieListTypes from "../../../../api/values/MoovieListTypes";
import CardsListOrderBy from "../../../../api/values/CardsListOrderBy";
import SortOrder from "../../../../api/values/SortOrder";
import ResponsePopup from "../reponsePopup/ReponsePopup";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {Dropdown} from "react-bootstrap";
import {useTranslation} from "react-i18next";
import {addIfNotExists, toggleMediaSelection} from "../../../../features/createListSlice";
import WatchlistWatched from "../../../../api/values/WatchlistWatched";
import listApi from "../../../../api/ListApi";
import {OverlayTrigger, Tooltip} from "react-bootstrap";

const AddMediaToListButton = ({currentId, media}) => {
    const {t} = useTranslation();
    const {isLoggedIn, user} = useSelector(state => state.auth);

    const dispatch = useDispatch();
    const {selectedMedia, name, description} = useSelector((state) => state.list);

    const [lists, setLists] = useState([]);
    const [listsLoading, setListsLoading] = useState(true);
    const [listsError, setListsError] = useState(null);
    const [loading, setLoading] = useState(false);
    const [popupMessage, setPopupMessage] = useState("");
    const [popupType, setPopupType] = useState("");
    const [popupVisible, setPopupVisible] = useState(false);
    const [options, setOptions] = useState([]);
    const [alreadyInList, setAlreadyInList] = useState([]);

    const handleCreateListButton = () => {
        dispatch(addIfNotExists(media))
        navigate('/createList')
    }

    const fetchCurrentUserLists = async () => {
        try {
            const response0 = await listService.getLists({
                search: null,
                ownerUsername: user.username,
                type: MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.type,
                orderBy: CardsListOrderBy.MOOVIE_LIST_ID,
                order: SortOrder.DESC,
                pageNumber: 1,
                pageSize: 3,
            });
            const response1 = await listService.getLists({
                search: null,
                ownerUsername: user.username,
                type: MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.type,
                orderBy: CardsListOrderBy.MOOVIE_LIST_ID,
                order: SortOrder.DESC,
                pageNumber: 1,
                pageSize: 10,
            });
            const response2 = await listService.getLists({
                search: null,
                ownerUsername: user.username,
                type: MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.type,
                orderBy: CardsListOrderBy.MOOVIE_LIST_ID,
                order: SortOrder.DESC,
                pageNumber: 1,
                pageSize: 10,
            });

            const combinedLists = [...response0.data, ...response1.data, ...response2.data];
            setLists(combinedLists);
            const listOptions = combinedLists.map((list) => ({
                name: list.name,
                id: list.id,
            }));
            setOptions(listOptions);
        } catch (err) {
            setListsError(err);
        } finally {
            setListsLoading(false);
        }
    };

    const fetchMediasInList = async () => {
        try {
            for (const list of lists) {
                const response = await listApi.getMediaFromList(list.id, currentId)
                if (response.status === 200) {
                    setAlreadyInList((prev) => [...prev, list.id]);
                }
            }
        } catch (err) {
            console.error(err);
        }
    }

    useEffect(() => {
        if (isLoggedIn) {
            fetchCurrentUserLists();
        }
    }, [isLoggedIn]);

    useEffect(() => {
        if (lists.length > 0) {
            fetchMediasInList();
        }
    }, [lists]);

    const [isOpen, setIsOpen] = useState(false);
    const navigate = useNavigate();

    const handleToggle = () => {
        if (!isLoggedIn) {
            navigate(`/login`);
        }
        setIsOpen(!isOpen);
    };

    const renderTooltip = (props) => (
        <Tooltip id="already-in-list-tooltip" {...props}>
            {t('addMediaToListButton.addedToList')}
        </Tooltip>
    );

    const handleOptionClick = async (option) => {
        if (alreadyInList.includes(option.id)) {
            return; // No hacer nada si el medio ya está en la lista
        }

        setPopupVisible(true);
        setIsOpen(false);
        setLoading(true);
        setPopupMessage("");
        setPopupType("loading");

        try {
            let response;
            if (option.name === "Watchlist") {
                response = await profileService.insertMediaIntoWW(WatchlistWatched.Watchlist, Number(currentId), user.username);
            } else if (option.name === "Watched") {
                response = await profileService.insertMediaIntoWW(WatchlistWatched.Watched, Number(currentId), user.username);
            } else {
                response = await listService.insertMediaIntoMoovieList({
                    id: option.id,
                    mediaIds: [Number(currentId)],
                });
            }

            if (response.status === 200) {
                setPopupType("success");
                setPopupMessage(t('addMediaToListButton.successfullyAddedToList'));
                setAlreadyInList((prev) => [...prev, option.id]);
            } else {
                setPopupType("error");
                setPopupMessage(response.data.message);
            }
        } catch (error) {
            setPopupType("error");
            setPopupMessage(t('addMediaToListButton.errorAddingToList'));
        } finally {
            setLoading(false);
        }
    };

    const handleClosePopup = () => {
        setPopupVisible(false);
    };

    const handleOnClick = () => {
        if (!isLoggedIn) {
            navigate('/login');
        }
    };

    return (
        <div className="dropdown">
            <Dropdown onClick={handleOnClick}>
                <Dropdown.Toggle className="btn btn-dark dropdown-toggle" id="dropdown-basic"
                                 style={{marginRight: '10px'}}>
                    <i className="bi bi-plus-circle-fill"></i> {t('addMediaToListButton.addToList')}
                </Dropdown.Toggle>

                {isLoggedIn && (
                    <Dropdown.Menu>
                        {options.map((option, index) => (
                            alreadyInList.includes(option.id) ? (
                                <OverlayTrigger key={index} placement="right" overlay={renderTooltip}>
                                    <div>
                                        <Dropdown.Item
                                            onClick={() => handleOptionClick(option)}
                                            disabled
                                            className="disabled-option"
                                        >
                                            {option.name}
                                        </Dropdown.Item>
                                    </div>
                                </OverlayTrigger>
                            ) : (
                                <Dropdown.Item key={index} onClick={() => handleOptionClick(option)}>
                                    {option.name}
                                </Dropdown.Item>
                            )
                        ))}
                        <Dropdown.Item onClick={handleCreateListButton}> <i
                            className="bi bi-plus-circle-fill"></i> {t('addMediaToListButton.createNewList')}
                        </Dropdown.Item>
                    </Dropdown.Menu>
                )}
            </Dropdown>

            {popupVisible && (
                <ResponsePopup
                    message={popupMessage}
                    type={popupType}
                    isLoading={loading}
                    onClose={handleClosePopup}
                />
            )}
        </div>
    );
};

export default AddMediaToListButton;