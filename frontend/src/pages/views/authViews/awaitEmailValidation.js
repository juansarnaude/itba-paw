import React, { useState } from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { useLocation, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import userApi from "../../../api/UserApi";
import {useTranslation} from "react-i18next";

const AwaitEmailValidation = () => {
    const { user } = useSelector((state) => state.auth);
    const navigate = useNavigate();
    const { t } = useTranslation();
    const location = useLocation();
    const from = location.state?.from || '/';

    const searchParams = new URLSearchParams(location.search);
    const error = searchParams.get('error');

    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");

    if (user) navigate(from);

    const handleResendVerificationEmail = async () => {
        setLoading(true);
        setMessage("");

        try {
            const token = searchParams.get('token');
            if (!token) {
                setMessage(t("awaitEmailValidation.noToken"));
                setLoading(false);
                return;
            }

            const response = await userApi.resendVerificationEmail(token);

            if (response.status === 200) {
                setMessage(t("awaitEmailValidation.success"));
            } else {
                setMessage(response.data);
            }
        } catch (e) {
            setMessage(e);
        }

        setLoading(false);
    };

    return (
        <Container fluid className="d-flex justify-content-center align-items-center" style={{ minHeight: '60vh', width: 'max-content' }}>
            <Row className="justify-content-center">
                <Col md={12} lg={10}>
                    <Card className="text-center shadow">
                        <Card.Body>
                            <Card.Title>{t("awaitEmailValidation.title")}</Card.Title>
                            {error === "token_expired" ? (
                                <Card.Text>
                                    {t("awaitEmailValidation.expired")}
                                </Card.Text>
                            ) : (
                                <Card.Text>
                                    {t("awaitEmailValidation.email")}
                                </Card.Text>
                            )}
                            {message && <Card.Text>{message}</Card.Text>}
                            {error === "token_expired" ?
                                <Button
                                variant="success"
                                onClick={handleResendVerificationEmail}
                                disabled={loading}
                                >
                                    {loading ? t("awaitEmailValidation.sending") : t("awaitEmailValidation.resend")}
                                </Button> :
                                <Button variant={"success"} onClick={()=>navigate('/')}>
                                    {t('awaitEmailValidation.goHome')}
                                </Button>
                            }
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}

export default AwaitEmailValidation;
