import React, { useEffect, useState } from "react";
import { Container, Form, Button, Alert, Col } from "react-bootstrap";
import { useTranslation } from "react-i18next";
import { useDispatch, useSelector } from "react-redux";
import {useLocation, useNavigate, useSearchParams} from "react-router-dom";
import { loginUser, attemptReconnect } from "../../../features/authSlice";

const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [rememberMe, setRememberMe] = useState(false);

    const { t } = useTranslation();
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const location = useLocation();
    const [searchParams] = useSearchParams();

    const { isLoggedIn, status, error } = useSelector((state) => state.auth);

    const from = location.state?.from || "/";

    // Attempt reconnect on page load
    useEffect(() => {
        dispatch(attemptReconnect());
    }, [dispatch]);

    useEffect(() => {
        if (isLoggedIn) {
            navigate(from, { replace: true });
        }
    }, [isLoggedIn, navigate, from]);

    const handleSubmit = async (event) => {
        event.preventDefault();
        dispatch(loginUser({ username, password }))
            .unwrap()
            .then(() => {
                if (rememberMe) {
                    const token = sessionStorage.getItem("jwtToken");
                    const user = sessionStorage.getItem("username");
                    localStorage.setItem("jwtToken", token);
                    localStorage.setItem("username", user);
                }
                navigate(from, { replace: true });
            })
            .catch(() => {});
    };

    return (
        <div
            className={"p-5 vh-100"}
            style={{ background: "whitesmoke" }}
        >
            <Container className={"d-flex align-items-center justify-content-center"}>
                <Col xs={12} md={6} className="p-4 bg-light shadow rounded">
                    <h2 className="text-center mb-3">{t("login.title")}</h2>

                    {searchParams.get("error") && <Alert variant="danger">User already verified</Alert>}
                    {status === "failed" && <Alert variant="danger">{error}</Alert>}

                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>{t("login.username")}</Form.Label>
                            <Form.Control
                                type="text"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>{t("login.password")}</Form.Label>
                            <Form.Control
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Check
                                type="checkbox"
                                label={t("login.rememberMe")}
                                checked={rememberMe}
                                onChange={(e) => setRememberMe(e.target.checked)}
                            />
                        </Form.Group>

                        <Button
                            variant="success"
                            type="submit"
                            className="w-100"
                            disabled={status === "loading"}
                        >
                            {status === "loading" ? t("login.submitting") : t("login.login")}
                        </Button>
                    </Form>

                    <div className="text-center mt-3">
                        <p className={"d-flex m-0 justify-content-center align-items-center"}>
                            {t("login.noAccount")}
                            <button type={"button"} className={"btn btn-link ps-1"} onClick={()=>navigate("/register", { state: {from: location.state?.from || location.pathname}})}>{t("login.signUp")}</button>
                        </p>
                        <p className={"d-flex m-0 justify-content-center align-items-center"}>
                            {t("login.continue")}
                            <button type={"button"} className={"btn btn-link ps-1"} onClick={()=>navigate(from)}>{t("login.without")}</button>
                        </p>
                        <p className={"d-flex m-0 justify-content-center align-items-center"}>
                            <button type={"button"} className={"btn btn-link ps-1"} onClick={()=>navigate("/passwordRecovery")}>{t("login.forgot")}</button>
                        </p>
                    </div>
                </Col>
            </Container>
        </div>
    );
};

export default Login;
