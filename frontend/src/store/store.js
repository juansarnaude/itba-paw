// src/store.js
import {configureStore} from '@reduxjs/toolkit';
import authReducer from '../features/authSlice';
import listReducer from '../features/createListSlice'

export const store = configureStore({
    reducer: {
        auth: authReducer,
        list: listReducer
    },
});

export default store;
