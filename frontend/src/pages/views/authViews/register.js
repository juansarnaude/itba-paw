import React, { useState } from 'react';
import { Container, Form, Button, Alert, Col } from 'react-bootstrap';
import { useNavigate, useLocation } from 'react-router-dom';
import { useTranslation } from "react-i18next";
import userApi from "../../../api/UserApi";
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../components/mainStyle.css';

const RegisterForm = () => {
    const [form, setForm] = useState({
        username: '',
        email: '',
        password: '',
        repeatPassword: '',
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const { t } = useTranslation();
    const navigate = useNavigate();
    const location = useLocation();
    const from = location.state?.from || '/';

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setForm({ ...form, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (form.password !== form.repeatPassword) {
            setError("register.passwordMismatch");
            return;
        }

        try {
            setLoading(true);
            await userApi.register({
                username: form.username,
                email: form.email,
                password: form.password,
            });
            navigate('/register/verify');
            setError('');
        } catch (error) {
            setLoading(false);
            setError(error.message);
        }
    };

    return (
        <div className="p-5 vh-100" style={{ background: "whitesmoke" }}>
            <Container className="d-flex align-items-center justify-content-center">
                <Col xs={12} md={6} className="p-4 bg-light shadow rounded">
                    <h2 className="text-center mb-3">{t("register.register")}</h2>

                    {error && <Alert variant="danger">{error}</Alert>}

                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>{t("register.username")}</Form.Label>
                            <Form.Control
                                type="text"
                                value={form.username}
                                name="username"
                                onChange={handleInputChange}
                                required
                                minLength={4}
                                maxLength={100}
                                pattern="^[a-zA-Z0-9]+$"
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>{t("register.email")}</Form.Label>
                            <Form.Control
                                type="email"
                                value={form.email}
                                name="email"
                                onChange={handleInputChange}
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>{t("login.password")}</Form.Label>
                            <Form.Control
                                type="password"
                                value={form.password}
                                name="password"
                                onChange={handleInputChange}
                                required
                                minLength={8}
                                maxLength={100}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>{t("register.repeatPassword")}</Form.Label>
                            <Form.Control
                                type="password"
                                value={form.repeatPassword}
                                name="repeatPassword"
                                onChange={handleInputChange}
                                required
                            />
                        </Form.Group>

                        <Button variant="success" type="submit" className="w-100" disabled={loading}>
                            {t("register.signUp")}
                        </Button>
                    </Form>

                    <div className="text-center mt-3">
                        <p className={"mb-0 d-flex align-items-center justify-content-center"}>
                            {t("register.alreadyHaveAnAccount")}
                            <button type="button" className="btn btn-link p-0" onClick={() => navigate("/login")}>
                                {t("register.login")}
                            </button>
                        </p>
                        <p className={"mb-0 d-flex align-items-center justify-content-center"}>
                            <button type="button" className="btn btn-link p-0" onClick={() => navigate(from)}>
                                {t("register.continueWithoutRegistering")}
                            </button>
                        </p>
                    </div>
                </Col>
            </Container>
        </div>
    );
};

export default RegisterForm;
