import {useTranslation} from "react-i18next";
import {Helmet} from "react-helmet-async";
import {Link} from "react-router-dom";

export default function Error403() {

    const {t} = useTranslation();

    return (
        <>
            <Helmet>
                <title>{t('error403_title')}</title>
            </Helmet>
            <div className="flex-grow whitespace-pre-line">
                <div className="flex flex-wrap p-3.5 mx-auto my-auto">
                    <img src={require('../../../images/logo.png')} alt="Moovie logo"  height="100px" width="100px"/>
                </div>
                <div className="flex flex-col pl-8">
                    <h1>{t('error403_message')}</h1>
                    <h2>{t('error403_description')}</h2>
                    <Link className="text-2xl font-bold text-green-500 hover:text-green-900" to='/'>
                        <button type="button" className="btn btn-outline-success" id="goBackButton">
                            {t('error403_call_to_action')}
                        </button>
                    </Link>
                </div>
            </div>
        </>
    );
}