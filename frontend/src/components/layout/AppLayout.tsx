import React from 'react';
import styles from './AppLayout.module.css';
import {Topbar} from "./Topbar.tsx";
import {Outlet} from "react-router-dom";

export const AppLayout: React.FC = () => {
    return (
        <div className={styles.layoutWrapper}>
            <Topbar />

            <main className={styles.contentArea}>
                <Outlet />
            </main>
        </div>
    )
}