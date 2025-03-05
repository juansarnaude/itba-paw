import { Link } from "react-router-dom";
import {useTranslation} from "react-i18next";
import {Helmet} from "react-helmet-async";
import mainStyle from "../../components/mainStyle.css";

export default function Error404() {

    const {t} = useTranslation();

    return (
        <div className={"moovie-default"}>
            <Helmet>
                <title>{t('error404_title')}</title>
            </Helmet>
            <div className="flex-grow whitespace-pre-line">
                <div className="flex flex-wrap p-3.5 mx-auto my-auto">
                    <img src={require('../../../images/logo.png')} alt="Moovie logo"  height="100px" width="100px"/>
                </div>
                <div className="flex flex-col pl-8">
                    <h1>{t('error404_message')}</h1>
                    <h2>{t('error404_description')}</h2>
                    <Link className="text-2xl font-bold text-green-500 hover:text-green-900" to='/'>
                        {t('error404_call_to_action')}
                    </Link>
                </div>
            </div>
        </div>
    );
}