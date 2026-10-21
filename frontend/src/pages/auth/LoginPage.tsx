import {useLocation, useNavigate} from "react-router-dom";
import {useAuth} from "../../context/AuthContext.tsx";
import React, {useState} from "react";
import type {LoginRequestDto} from "../../types";
import {AlertCircle, LogIn} from "lucide-react";
import {Input} from "../../components/common/Input.tsx";
import {Button} from "../../components/common/Button.tsx";
import styles from "./LoginPage.module.css"

export const LoginPage: React.FC = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { login } = useAuth();

    const [formValues, setFormValues] = useState<LoginRequestDto>({
        username: '',
        password: '',
    });
    const [errorMessage, setErrorMessage] = useState<string | null>(null);
    const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

    const from = (location.state as { from?: { pathname: string } })?.from?.pathname || '/patients';

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormValues((prev) => ({ ...prev, [name]: value }));
        if (errorMessage) {
            setErrorMessage(null);
        }
    };

    const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (!formValues.username.trim() || !formValues.password) {
            setErrorMessage('Please enter both username and password.');
            return;
        }

        try {
            setIsSubmitting(true);
            setErrorMessage(null);

            await login(formValues);

            navigate(from, { replace: true });
        } catch (error: any) {
            const message = error.response?.data?.message || (error.response?.status === 401 ? 'Invalid username or password.' : 'Login failed, please try again.');
            setErrorMessage(message);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.card}>
                <div className={styles.header}>
                    <div className={styles.iconBadge}>
                        <LogIn size={26} color="#6750A4" />
                    </div>
                    <h1 className={styles.title}>
                        Login
                    </h1>
                </div>

                {errorMessage && (
                    <div className={styles.errorBanner}>
                        <AlertCircle size={18} />
                        <span>{errorMessage}</span>
                    </div>
                )}

                <form onSubmit={handleSubmit} noValidate>
                    <Input
                        label="Username"
                        id="username"
                        name="username"
                        type="text"
                        placeholder="yourUsername"
                        autoComplete="username"
                        value={formValues.username}
                        onChange={handleChange}
                        required
                    />

                    <Input
                        label="Password"
                        id="password"
                        name="password"
                        type="password"
                        autoComplete="current-password"
                        value={formValues.password}
                        onChange={handleChange}
                        required
                    />

                    <Button type="submit" isLoading={isSubmitting} style={{ marginTop: '8px' }}>
                        Log in
                    </Button>
                </form>
            </div>
        </div>
    );
};
