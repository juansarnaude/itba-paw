import React, { useState } from "react";
import "../formsStyle.css";
import ReportTypes from "../../../../api/values/ReportTypes";
import {useTranslation} from "react-i18next";

const ReportForm = ({onCancel, onReportSubmit }) => {
    const { t } = useTranslation();
    const [error, setError] = useState(null);
    const [reportReason, setReportReason] = useState("");

    const reportReasons = [
        t('reportForm.hate'),
        t('reportForm.abuseAndHarassment'),
        t('reportForm.privacy'),
        t('reportForm.spam')
    ];

    const handleSubmit = async () => {
        try {
            console.log('About to report');
            const reportType = ReportTypes[reportReason];
            const response = await onReportSubmit?.(reportType);
            console.log('reported', response);
            if (response) {
                if (response.status === 200) {
                    onCancel();
                } else {
                    console.log(response);
                    setError(response.response.data.message);
                }
            }
        } catch (error) {
            setError(error.response?.data?.message || "Error making request");
        }
    };

    return (
        <div className="overlay">
            <div className="box-review">
                {!error ? (
                    <>
                        <h2>{t('reportForm.report')}</h2>
                        <div className="radio-group">
                            {reportReasons.map((reason) => (
                                <div key={reason} className="radio-option">
                                    <input
                                        type="radio"
                                        id={reason}
                                        name="reportReason"
                                        value={reason}
                                        checked={reportReason === reason}
                                        onChange={(e) => setReportReason(e.target.value)}
                                    />
                                    <label htmlFor={reason}>{reason}</label>
                                </div>
                            ))}
                        </div>
                        <div className="buttons">
                            <button className="cancel" onClick={onCancel}>
                                {t('reportForm.cancel')}
                            </button>
                            <button
                                className="submit"
                                onClick={handleSubmit}
                                disabled={!reportReason}
                            >
                                {t('reportForm.submitReport')}
                            </button>
                        </div>
                    </>
                ) : (
                    <>
                        <h2 style={{ color: "red" }}>{error}</h2>
                        <button className="cancel" onClick={() => setError(null)}>
                            {t('reportForm.back')}
                        </button>
                    </>
                )}
            </div>
        </div>
    );
}

export default ReportForm;
