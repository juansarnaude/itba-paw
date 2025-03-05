import api from "./api";
import VndType from "../enums/VndType";

const profileApi = (() => {

    const getProfileByUsername = (username) =>{
        return api.get(`/profiles/${username}`);
    }



    const getMilkyLeaderboard = ({page, pageSize}) => {
        return api.get('/profiles/milkyLeaderboard',
            {
                params: {
                    'page': page,
                    'pageSize': pageSize
                }
            });
    }


    const getSearchedUsers = ({username,orderBy,sortOrder,page}) => {
        return api.get(`/profiles`,
            {
                params: {
                    'username': username,
                    'orderBy': orderBy,
                    'sortOrder': sortOrder,
                    'pageNumber': page
                }
            }
        );
    }


    const getSpecialListFromUser = (username, type, orderBy, order, pageNumber = 1) => {
            return api.get(`/profiles/${username}/${type}?orderBy=${orderBy}&order=${order}&pageNumber=${pageNumber}`);
    };



    const setPfp = (username, pfp) => {
        return api.put(`/profiles/${username}/image`, pfp);
    }

    const getPfp =  (username) => {
        return process.env.REACT_APP_API_URL +  `/profiles/${username}/image`;
    }

    /*
    LIKES AND FOLLOWED
    */
    const getLikedOrFollowedListFromUser = (username, type, orderBy, sortOrder, pageNumber = 1) => {
            if (type !== "followed" && type !== "liked") {
                throw new Error(`Invalid type: ${type}. Expected "followed" or "liked".`);
            }
            const endpoint = type === "followed" ? "listLikes" : "listFollows";
            return api.get(`/profiles/${username}/${endpoint}`, {
                params: {
                    username,
                    orderBy,
                    sortOrder,
                    pageNumber
                }
            });
        };

    const currentUserHasLikedList = (moovieListId, username) => {
            return api.get(`/profiles/${username}/listLikes/${moovieListId}`);
        }



    const currentUserHasFollowedList = (moovieListId, username) => {
            return api.get(`/profiles/${username}/listFollows/${moovieListId}`);
        }


        //WATCHED AND WATCHLIST (WW)
    const currentUserWW = (ww, username, mediaId) => {
            return api.get(`/profiles/${username}/${ww}/${mediaId}`);
        }

    const insertMediaIntoWW = (ww, mediaId,username) => {
        let contentType = "application/json";

        if (ww === "watched") {
            contentType = VndType.APPLICATION_WATCHED_MEDIA_FORM;
        }else {
            contentType = VndType.APPLICATION_WATCHLIST_MEDIA_FORM;
        }


        return api.post(`/profiles/${username}/${ww}`,

                {"id":mediaId},
            {headers: {
                    'Content-Type': contentType
                }
            });
        }

    const removeMediaFromWW = (ww, username, mediaId) => {
            return api.delete(`/profiles/${username}/${ww}/${mediaId}`);
        }

    const currentUserHasLikedReview = (reviewId, username) => {
        return api.get(`/profiles/${username}/reviewLikes/${reviewId}`);
    }

    const currentUserHasLikedMoovieListReview = (reviewId, username) => {
        return api.get(`/profiles/${username}/moovieListsReviewsLikes/${reviewId}`);
    }

    const currentUserCommentFeedback = (commentId, username) => {
        return api.get(`/profiles/${username}/commentsFeedback/${commentId}`);
    }

    const getWatchedCountFromMovieListId = (movieListId, username) => {
        return api.get(`/profiles/${username}/watched/count?listId=${movieListId}`);
    }




        return {
        getPfp,
        getProfileByUsername,
        getMilkyLeaderboard,
        getSpecialListFromUser,
        getSearchedUsers,
        setPfp,
        currentUserHasLikedList,
        getLikedOrFollowedListFromUser,
        currentUserHasFollowedList,
        currentUserWW,
        insertMediaIntoWW,
        removeMediaFromWW,
        currentUserHasLikedReview,
        currentUserHasLikedMoovieListReview,
        currentUserCommentFeedback,
        getWatchedCountFromMovieListId
    }

}
)();

export default profileApi;
