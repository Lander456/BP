import React, { type ButtonHTMLAttributes } from "react";

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
    isLoading?: boolean;
    children: React.ReactNode;
}

export const Button: React.FC<ButtonProps> = ({ isLoading, disabled, children, ...props }) => {
    return (
        <button
            {...props}
            disabled={isLoading || disabled}
            style={{
                width: '100%',
                padding: '10px 16px',
                borderRadius: '6px',
                border: 'none',
                backgroundColor: isLoading || disabled ? '#9ca3af' : '#FFFFFF',
                color: '#6750A4',
                fontWeight: 600,
                fontSize: '0.95rem',
                cursor: isLoading || disabled ? 'not-allowed' : 'pointer',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                gap: '8px',
                transition: 'background-color 0.2s ease',
                ...props.style,
            }}
        >
            {children}
        </button>
    );
};
