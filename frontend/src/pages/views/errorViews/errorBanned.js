import {useTranslation} from "react-i18next";
import {Helmet} from "react-helmet-async";

export default function ErrorBanned() {
    const {t} = useTranslation();

    return (
        <>
            <Helmet>
                <title>{t('errorBanned_title')}</title>
            </Helmet>
            <div className="flex-grow whitespace-pre-line">
                <div className="flex flex-wrap p-3.5 mx-auto my-auto">
                    <img src={require('../../../images/logo.png')} alt="Moovie logo" height="100px" width="100px"/>
                </div>
                <div className="flex flex-col pl-8">
                    <h1>{t('errorBanned_message')}</h1>
                    <h2>{t('errorBanned_description')}</h2>
                </div>
            </div>
        </>
    );
} 