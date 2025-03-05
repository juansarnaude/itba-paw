import React from 'react';
import { useTranslation } from 'react-i18next';

const FormButtons = ({ onApply, onReset }) => {
    const { t } = useTranslation();

    return (
        <div className="d-flex flex-column m-1">
            <button type="submit" className="btn btn-outline-success m-1" onClick={onApply}>
                {t('filters.apply')}
            </button>
            <button type="button" className="btn btn-outline-secondary m-1" onClick={onReset}>
                {t('filters.clear')}
            </button>
        </div>
    );
};

export default FormButtons;
