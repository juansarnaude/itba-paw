// src/components/listContentPaginated/ListContentPaginated.js

import React from "react";
import PaginationButton from "../paginationButton/PaginationButton";
import DropdownMenu from "../dropdownMenu/DropdownMenu";
import MediaOrderBy from "../../../api/values/MediaOrderBy";
import ListCard from "../listCard/ListCard";
import "../../views/browseLists.css"

const ListCardsPaginated = ({
                                  mlcList,
                                  page,
                                  lastPage,
                                  handlePageChange,
                                  currentOrderBy,
                                  setOrderBy,
                                  currentSortOrder,
                                  setSortOrder
                              }) => {
    return (
        <div>
            <DropdownMenu
                setOrderBy={setOrderBy}
                setSortOrder={setSortOrder}
                currentOrderDefault={currentSortOrder}
                values={Object.values(MediaOrderBy)}
            />

            <div className="list-card-container">
                {Array.isArray(mlcList?.data) && mlcList.data.length > 0 ? (
                    mlcList.data.map(list => (
                        <ListCard key={list.id} listCard={list}/>
                    ))
                ) : null}
            </div>

            <div className="flex justify-center pt-4">
                {mlcList?.data?.length > 0 && mlcList.links?.last?.page > 1 && (
                    <PaginationButton
                        page={page}
                        lastPage={lastPage}
                        setPage={handlePageChange}
                    />
                )}
            </div>
        </div>
    );
};

export default ListCardsPaginated;
