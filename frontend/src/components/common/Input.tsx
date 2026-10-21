import React, { type InputHTMLAttributes } from "react";
import styles from './Input.module.css'

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
    label: string;
    error?: string;
}

export const Input: React.FC<InputProps> = ({ label, error, id, className, ...props}) => {
    const inputId = id || props.name;

    const inputClasses = [
        styles.inputField,
        error ? styles.inputError : '',
        className || '',
    ].filter(Boolean).join(' ');

    return (
        <div className={styles.container}>
            <label htmlFor={inputId} className={styles.label}>
                {label}
            </label>
            <input id={inputId} className={inputClasses} {...props} />
            {error && <span className={styles.errorMessage}>{error}</span>}
        </div>
    );
};
