import React from 'react';

const FilterList = ({ searchValue, onSearchChange, items, selectedItems, onToggleItem }) => (
    
    <>
        <input
            type="text"
            value={searchValue}
            onChange={(e) => onSearchChange(e.target.value)}
            className="form-control mb-2"
        />
        {items
            .filter((item) => item.name.toLowerCase().includes(searchValue.toLowerCase()))
            .map((item, index) => (
                <div key={index+item.name} className="form-check">
                    <input
                        type="checkbox"
                        className="form-check-input"
                        id={`item-${index+item.name}`}
                        checked={selectedItems.some(selectedItem => selectedItem.id === item.id)? true : false}
                        onClick={() => onToggleItem(item)}
                    />
                    <label className="form-check-label" htmlFor={`item-${index+item.name}`}>
                        {item.name}
                    </label>
                </div>
            ))}
    </>
);

export default FilterList;
