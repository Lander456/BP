import React, {useEffect, useState} from "react";
import type {PatientListDto} from "../../types";
import {patientService} from "../../api";
import styles from "./PatientDetailPage.module.css"
import {PatientCard} from "../../components/patients/PatientCard.tsx";

export const PatientListPage: React.FC = () => {
    const [patients, setPatients] = useState<PatientListDto[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    const loadPatients = async () => {
        try {
            setError(null);
            const data = await patientService.getAll();
            setPatients(data);
        } catch (err: any) {
            console.error("Failed to load patients", err);
            setError("Could not load patients. Please try again later.");
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        loadPatients();
    }, []);

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h1 className={styles.title}>
                    Patients
                </h1>
            </div>

            {isLoading && (
                <div className={styles.statusMessage}>
                    Loading patients...
                </div>
            )}

            {error && (
                <div className={styles.errorMessage}>
                    {error}
                </div>
            )}

            {!isLoading && !error && patients.length === 0 && (
                <div className={styles.emptyState}>
                    No patients found.
                </div>
            )}

            {!isLoading && !error && (
                <div className={styles.patientList}>
                    {patients.map((patient) => (
                        <PatientCard
                            key={patient.id}
                            patient={patient}
                            onPatientUpdated={loadPatients}
                        />
                    ))}
                </div>
            )}
        </div>
    );
};
