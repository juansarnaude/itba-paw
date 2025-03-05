import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import ChipsDisplay from './chipsDisplay';
import FilterSection from './filterSection';
import FilterList from './filterList';
import FormButtons from './formButtons';
import mediaTypes from '../../../../api/values/MediaTypes';
import mediaOrderBy from '../../../../api/values/MediaOrderBy';
import ProviderService from "../../../../services/ProviderService";
import GenreService from "../../../../services/GenreService";
import SortOrder from "../../../../api/values/SortOrder";
import { CircularProgress } from "@mui/material";

const FiltersGroup = ({
                          type,
                          sortOrder,
                          orderBy,
                          query,
                          searchBar,
                          initialSelectedGenres = [],
                          initialSelectedProviders = [],
                          submitCallback
                      }) => {
    const { t } = useTranslation(); // Initialize translations

    const [openGenres, setOpenGenres] = useState(false);
    const [openProviders, setOpenProviders] = useState(false);
    const [searchGenre, setSearchGenre] = useState('');
    const [searchProvider, setSearchProvider] = useState('');

    const [selectedGenres, setSelectedGenres] = useState(initialSelectedGenres);
    const [selectedProviders, setSelectedProviders] = useState(initialSelectedProviders);
    const [queryInput, setQueryInput] = useState(query);
    const [sortOrderInput, setSortOrderInput] = useState(sortOrder || SortOrder.DESC);
    const [mediaTypeInput, setMediaTypeInput] = useState(type || mediaTypes.TYPE_ALL);
    const [mediaOrderByInput, setMediaOrderByInput] = useState(orderBy || mediaOrderBy.TOTAL_RATING);

    const [genresList, setGenresList] = useState([]);
    const [providersList, setProvidersList] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function getProviders() {
            try {
                setLoading(true);
                const response = await ProviderService.getAllProviders();
                const providerList = response.data.map(provider => ({
                    name: provider.providerName,
                    id: provider.providerId
                }));
                setProvidersList(providerList);
            } catch (error) {
                setError(t("filters.error.fetch_providers"));
                setLoading(false);
            } finally {
                setLoading(false);
            }
        }

        async function getGenres() {
            try {
                setLoading(true);
                const response = await GenreService.getAllGenres();
                const genreList = response.data.map(genre => ({
                    name: genre.genreName,
                    id: genre.genreId
                }));
                setGenresList(genreList);
                console.log("genreList", genreList);
                console.log("selectedGenres", selectedGenres);
            } catch (error) {
                setError(t("filters.error.fetch_genres"));
                setLoading(false);
            } finally {
                setLoading(false);
            }
        }

        getProviders();
        getGenres();
    }, []);

    const handleChipRemove = (setFunction, item) => {
        setFunction((prev) => prev.filter((i) => i !== item));
    };

    const handleFilterSubmit = (e) => {
        e.preventDefault();
        submitCallback({
            type: mediaTypeInput,
            sortOrder: sortOrderInput,
            orderBy: mediaOrderByInput,
            search: queryInput,
            selectedProviders: selectedProviders,
            selectedGenres: selectedGenres
        });
    };

    const handleReset = () => {
        setSelectedGenres([]);
        setSelectedProviders([]);
        setQueryInput("");
        setSortOrderInput(SortOrder.DESC);
        setMediaTypeInput(mediaTypes.TYPE_ALL);
        setMediaOrderByInput(mediaOrderBy.TOTAL_RATING);
    };

    if (loading) return <CircularProgress />;
    if (error) return <div>{error}</div>;

    return (
        <div style={{ maxHeight: "85vh", width: "30vw", overflowY: "auto" }}>
            <ChipsDisplay
                title={t("filters.genres")}
                items={selectedGenres}
                onRemove={(genre) => handleChipRemove(setSelectedGenres, genre)}
            />
            <ChipsDisplay
                title={t("filters.providers")}
                items={selectedProviders}
                onRemove={(provider) => handleChipRemove(setSelectedProviders, provider)}
            />

            {query && <h4>{t("filters.results_for")}: {query}</h4>}

            <div className="m-1 flex-column" id="filters">
                <form id="filter-form" onSubmit={handleFilterSubmit} className="mb-2 d-flex flex-column">
                    {query && <input type="hidden" name="query" value={query} />}

                    <div className="d-flex flex-row m-1">
                        <select
                            name="type"
                            className="form-select m-1"
                            onChange={(e) => setMediaTypeInput(e.target.value)}
                        >
                            <option selected={mediaTypeInput === mediaTypes.TYPE_ALL} value={mediaTypes.TYPE_ALL}>
                                {t("filters.all")}
                            </option>
                            <option selected={mediaTypeInput === mediaTypes.TYPE_TVSERIE} value={mediaTypes.TYPE_TVSERIE}>
                                {t("filters.series")}
                            </option>
                            <option selected={mediaTypeInput === mediaTypes.TYPE_MOVIE} value={mediaTypes.TYPE_MOVIE}>
                                {t("filters.movies")}
                            </option>
                        </select>

                        <select
                            name="orderBy"
                            className="form-select m-1"
                            onChange={(e) => setMediaOrderByInput(e.target.value)}
                        >
                            <option selected={mediaOrderByInput === mediaOrderBy.NAME} value={mediaOrderBy.NAME}>
                                {t("filters.title")}
                            </option>
                            <option selected={mediaOrderByInput === mediaOrderBy.TOTAL_RATING} value={mediaOrderBy.TOTAL_RATING}>
                                {t("filters.total_rating")}
                            </option>
                            <option selected={mediaOrderByInput === mediaOrderBy.TMDB_RATING} value={mediaOrderBy.TMDB_RATING}>
                                {t("filters.tmdb_rating")}
                            </option>
                            <option selected={mediaOrderByInput === mediaOrderBy.RELEASE_DATE} value={mediaOrderBy.RELEASE_DATE}>
                                {t("filters.release_date")}
                            </option>
                        </select>
                    </div>

                    {searchBar && (
                        <div className="m-1">
                            <input
                                type="search"
                                className="form-control m-1"
                                placeholder={t("filters.search_placeholder")}
                                value={queryInput}
                                onChange={(e) => setQueryInput(e.target.value)}
                            />
                        </div>
                    )}
                    <FilterSection
                        title={t("filters.genres")}
                        isOpen={openGenres}
                        toggleOpen={() => setOpenGenres(!openGenres)}
                    >
                        <FilterList
                            searchValue={searchGenre}
                            onSearchChange={setSearchGenre}
                            items={genresList}
                            selectedItems={selectedGenres}
                            onToggleItem={(genre) =>
                            {
                                setSelectedGenres((prev) =>
                                    prev.some(g => g.id === genre.id) ? prev.filter(g => g.id !== genre.id) : [...prev, genre]
                                )
                            }
                            }
                        />
                    </FilterSection>

                    <FilterSection
                        title={t("filters.providers")}
                        isOpen={openProviders}
                        toggleOpen={() => setOpenProviders(!openProviders)}
                    >
                        <FilterList
                            searchValue={searchProvider}
                            onSearchChange={setSearchProvider}
                            items={providersList}
                            selectedItems={selectedProviders}
                            onToggleItem={(provider) =>
                                setSelectedProviders((prev) =>
                                    prev.some(p => p.id === provider.id) ? prev.filter(p => p.id !== provider.id) : [...prev, provider]
                                )
                            }
                        />
                    </FilterSection>


                    <FormButtons onApply={handleFilterSubmit} onReset={handleReset} />

                </form>
            </div>
        </div>
    );
};

export default FiltersGroup;
