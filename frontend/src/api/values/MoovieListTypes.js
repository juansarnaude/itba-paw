const MoovieListTypes = {
    MOOVIE_LIST_TYPE_STANDARD_PUBLIC: { type: 1, description: "Listas que crea un usuario y son públicas" },
    MOOVIE_LIST_TYPE_STANDARD_PRIVATE: { type: 2, description: "Listas que crea un usuario y puso privada" },
    MOOVIE_LIST_TYPE_DEFAULT_PUBLIC: { type: 3, description: "Listas creadas automáticamente por ej: 'Top 50'" },
    MOOVIE_LIST_TYPE_DEFAULT_PRIVATE: { type: 4, description: "Listas creadas automáticamente por ej: 'Watchlist'" },

    getType(typeKey) {
        return this[typeKey]?.type ?? null;
    },
};

export default MoovieListTypes;