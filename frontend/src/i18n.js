import i18n from 'i18next';
import LanguageDetector from 'i18next-browser-languagedetector';
import { initReactI18next } from 'react-i18next';
import localeEN from './locale/en/translation.json';
import localeES from './locale/es/translation.json';

const resources = {
    en: {
        translation: localeEN
    },
    es: {
        translation: localeES
    }
};

i18n
    .use(LanguageDetector)
    .use(initReactI18next)
    .init({
        resources,
        fallbackLng: 'en',

        keySeparator: false,
        debug: true,

        interpolation: {
            escapeValue: false,
        },
    });

export default i18n;