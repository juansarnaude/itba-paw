import React from 'react';

const Tabs = ({ selectedTab, onTabChange }) => {
  const tabs = [
    { id: 'comments', label: 'Comments' },
    { id: 'ml', label: 'Moovie Lists' },
    { id: 'mlReviews', label: 'Moovie List Reviews' },
    { id: 'reviews', label: 'Media Reviews' },
    { id: 'banned', label: 'Banned Users' },
  ];

  return (
    <div className="flex justify-center mt-4">
      <div className="inline-flex rounded-md shadow-sm" role="group">
        {tabs.map((tab) => (
          <button
            key={tab.id}
            type="button"
            onClick={() => onTabChange(tab.id)}
            className={`px-4 py-2 text-sm font-medium border border-gray-200 ${
              selectedTab === tab.id
                ? 'bg-green-500 text-white'
                : 'bg-white text-gray-700 hover:bg-gray-100'
            } first:rounded-l-md last:rounded-r-md`}
          >
            {tab.label}
          </button>
        ))}
      </div>
    </div>
  );
};

export default Tabs;
