import React, { useState } from 'react';
import { Container, Form, Button, Alert, Col } from 'react-bootstrap';
import { useNavigate, useLocation } from 'react-router-dom';
import { useTranslation } from "react-i18next";
import userApi from "../../../api/UserApi";
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../components/mainStyle.css';

const ForgotPassword = () => {
    const [email, setEmail] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState(false);

    const { t } = useTranslation();
    const navigate = useNavigate();
    const location = useLocation();
    const from = location.state?.from || '/';

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setLoading(true);
            const response = await userApi.forgotPassword(email);

            if (response.status === 500) {
                setError(t("forgotPassword.error"));
            } else {
                setSuccess(true);
            }
        } catch (error) {
            setLoading(false);
            setError(error.message);
        }
    };

    return (
        <div className="p-5 vh-100" style={{ background: "whitesmoke" }}>
            <Container className="d-flex align-items-center justify-content-center">
                <Col xs={12} md={6} className="p-4 bg-light shadow rounded">
                    <h2 className="text-center mb-3">{t("forgotPassword.title")}</h2>
                    <p style={{ fontWeight: "bold" }} className="text-center mb-3">
                        {t("forgotPassword.instruction")}
                    </p>

                    {success && <Alert variant={"success"}>{t("forgotPassword.success")}</Alert>}
                    {error && <Alert variant="danger">{error}</Alert>}

                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>{t("forgotPassword.emailLabel")}</Form.Label>
                            <Form.Control
                                type="email"
                                value={email}
                                name="email"
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Button variant="success" type="submit" className="w-100" disabled={loading}>
                            {t("forgotPassword.submit")}
                        </Button>
                        <Button className="w-100" variant="link" type="button" onClick={() => navigate('/login')}>
                            {t("forgotPassword.goToLogin")}
                        </Button>
                        <Button className="w-100" variant="link" type="button" onClick={() => navigate('/')}>
                            {t("forgotPassword.returnHome")}
                        </Button>
                    </Form>
                </Col>
            </Container>
        </div>
    );
};

export default ForgotPassword;
