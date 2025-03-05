import { createSlice } from "@reduxjs/toolkit";

const loadState = () => {
    try {
        const savedState = sessionStorage.getItem("listState");
        return savedState
            ? JSON.parse(savedState)
            : { selectedMedia: [], name: "", description: "", isPrivate: false };
    } catch (error) {
        console.error("Error loading state:", error);
        return { selectedMedia: [], name: "", description: "", isPrivate: false };
    }
};

const saveState = (state) => {
    try {
        sessionStorage.setItem("listState", JSON.stringify(state));
    } catch (error) {
        console.error("Error saving state:", error);
    }
};

const initialState = loadState();

const listSlice = createSlice({
    name: "list",
    initialState,
    reducers: {
        setSelectedMedia: (state, action) => {
            state.selectedMedia = action.payload;
            saveState(state);
        },
        toggleMediaSelection: (state, action) => {
            const media = action.payload;
            state.selectedMedia = state.selectedMedia.some((item) => item.id === media.id)
                ? state.selectedMedia.filter((item) => item.id !== media.id)
                : [...state.selectedMedia, media];

            saveState(state);
        },
        addIfNotExists: (state, action) => {
            const media = action.payload;
            state.selectedMedia = state.selectedMedia.some((item) => item.id === media.id)
                ? [...state.selectedMedia] : [...state.selectedMedia, media];
            saveState(state);
        },
        setName: (state, action) => {
            state.name = action.payload;
            saveState(state);
        },
        setDescription: (state, action) => {
            state.description = action.payload;
            saveState(state);
        },
        setIsPrivate: (state, action) => {
            state.isPrivate = action.payload;
            saveState(state);
        },
        resetList: (state) => {
            state.selectedMedia = [];
            state.name = "";
            state.description = "";
            state.isPrivate = false;
            saveState(state);
        },
    },
});

export const {
    setSelectedMedia,
    toggleMediaSelection,
    addIfNotExists,
    setName,
    setDescription,
    setIsPrivate,
    resetList
} = listSlice.actions;

export default listSlice.reducer;
