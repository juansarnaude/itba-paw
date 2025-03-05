import { useState, useEffect } from "react";
import MediaService from "../services/MediaService";
import pagingSizes from "../api/values/PagingSizes";
import ProviderService from "../services/ProviderService";
import GenreService from "../services/GenreService";

const useMediaList = ({ type, page, sortOrder, orderBy, search, selectedProviders, selectedGenres }) => {
    const [medias, setMedias] = useState({ data: [], links: {} });
    const [mediasLoading, setMediasLoading] = useState(true);
    const [mediasError, setMediasError] = useState(null);

    useEffect(() => {
        const getMedias = async () => {
            try {
                setMediasError(null);
                setMediasLoading(true);

                const mediasResponse = await MediaService.getMedia({
                    type: type,
                    page: page,
                    pageSize: pagingSizes.MEDIA_DEFAULT_PAGE_SIZE,
                    orderBy: orderBy,
                    sortOrder: sortOrder,
                    search: search,
                    providers: Array.from(selectedProviders.map((e)=>e.id)),
                    genres: Array.from(selectedGenres.map((e)=>e.id)),
                });

                const { data: medias, links } = mediasResponse;

                const mediasWithProviders = await Promise.all(
                    medias.map(async (media) => {
                        try {
                            const providers = await ProviderService.getProvidersForMedia(media.id);
                            return { ...media, providers };
                        } catch {
                            return { ...media, providers: [] };
                        }
                    })
                );

                const mediasWithGenres = await Promise.all(
                    mediasWithProviders.map(async (media) => {
                        try {
                            const genres = await GenreService.getGenresForMedia(media.id);
                            return { ...media, genres };
                        } catch {
                            return { ...media, genres: [] };
                        }
                    })
                );

                setMedias({ links, data: mediasWithGenres });
            } catch (error) {
                console.error("Error fetching media data:", error);
                setMediasError(error);
            } finally {
                setMediasLoading(false);
            }
        };

        getMedias();
    }, [type, page, sortOrder, orderBy, search, selectedProviders, selectedGenres]);

    return { medias, mediasLoading, mediasError };
};

export default useMediaList;
