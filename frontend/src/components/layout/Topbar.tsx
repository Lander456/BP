import React from "react";
import {useAuth} from "../../context/AuthContext.tsx";
import {NavLink, useNavigate} from "react-router-dom";
import styles from './Topbar.module.css'
import {Eye, LogOut, Users} from "lucide-react";


export const Topbar: React.FC = () => {
    const { logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login', { replace: true });
    };

    const getNavClass = ({ isActive }: { isActive: boolean }) =>
        `${styles.navItem} ${isActive ? styles.activeNavItem : ''}`;

    return (
        <header className={styles.topbar}>
            <nav className={styles.navLinks}>
                <NavLink to="/patients" className={getNavClass}>
                    <Users size={18} />
                    <span>Patients</span>
                </NavLink>
                <NavLink to="/irisCharts" className={getNavClass}>
                    <Eye size={18} />
                    <span>My Irischarts</span>
                </NavLink>
            </nav>

            <button
                type="button"
                onClick={handleLogout}
                className={styles.logoutButton}
                title="Logout"
            >
                <LogOut size={16} />
                <span>Logout</span>
            </button>
        </header>
    );
};
