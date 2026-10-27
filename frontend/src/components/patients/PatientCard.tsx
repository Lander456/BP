import React, {useState} from "react";
import type {PatientDetailDto, PatientListDto, PatientUpdateDto} from "../../types";
import styles from "./PatientCard.module.css"
import {ChevronDown, ChevronUp} from "lucide-react";
import {patientService} from "../../api";
import {Button} from "../common/Button.tsx";

interface PatientCardProps {
    patient: PatientListDto;
    onPatientUpdated?: () => void;
}

export const PatientCard: React.FC<PatientCardProps> = ({
    patient,
    onPatientUpdated,
}) => {
    const [isExpanded, setIsExpanded] = useState<boolean>(false);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [isSaving, setIsSaving] = useState<boolean>(false);

    const [detail, setDetail] = useState<PatientDetailDto | null>(null);

    const [formValues, setFormValues] = useState<PatientUpdateDto>({
        firstName: '',
        lastName: '',
        birthNum: '',
        sex: null,
    });

    const handleToggle = () => {
        const nextState = !isExpanded;
        setIsExpanded(nextState);

        if (nextState && !detail) {
            setIsLoading(true);
            patientService.getById(patient.id)
                .then((data) => {
                    setDetail(data);
                    setFormValues({
                        firstName: data.firstName,
                        lastName: data.lastName,
                        birthNum: data.birthNum,
                        sex: data.sex,
                    });
                })
                .catch((err) => console.error('Failed to fetch patient detail', err))
                .finally(() => setIsLoading(false));
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormValues((prevState) => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSave = async (e: React.SubmitEvent) => {
        e.preventDefault();
        try {
            setIsSaving(true);
            await patientService.update(patient.id, formValues);

            setDetail((prevState) => {
                if (!prevState) return null;
                return {
                    ...prevState,
                    firstName: formValues.firstName ?? prevState.firstName,
                    lastName: formValues.lastName ?? prevState.lastName,
                    birthNum: formValues.birthNum ?? prevState.birthNum,
                    sex: formValues.sex ?? prevState.sex
                };
            });

            onPatientUpdated?.();
        } catch (err) {
            console.error('Failed to update patient', err);
        } finally {
            setIsSaving(false);
        }
    };

    return (
        <div className={`${styles.card} ${isExpanded ? styles.expanded : ''}`}>
            <div className={styles.header} onClick={handleToggle}>
                <span className={styles.name}>
                    {detail ? `${detail.firstName} ${detail.lastName}` : `${patient.firstName} ${patient.lastName}`}
                </span>
                {isExpanded ? (
                    <ChevronUp className={styles.chevron}/>
                ) : (
                    <ChevronDown className={styles.chevron}/>
                    )
                }
            </div>
            {isExpanded && (
                <div className={styles.content}>
                    {isLoading ? (
                        <div className={styles.loading}>
                            Loading details...
                        </div>
                    ) : (
                        <form onSubmit={handleSave} className={styles.form}>
                            <div className={styles.fieldRow}>
                                <label>
                                    First Name
                                </label>
                                <input
                                    name="firstName"
                                    value={formValues.firstName}
                                    onChange={handleChange}
                                />
                            </div>
                            <div className={styles.fieldRow}>
                                <label>
                                    Last Name
                                </label>
                                <input
                                    name="lastName"
                                    value={formValues.lastName}
                                    onChange={handleChange}
                                />
                            </div>
                            <div className={styles.fieldRow}>
                                <label>
                                    Birth Number
                                </label>
                                <input
                                    name="birthNum"
                                    value={formValues.birthNum}
                                    onChange={handleChange}
                                />
                            </div>
                            <div className={styles.actions}>
                                <Button type="button" onClick={handleToggle} className={styles.cancelBtn}>
                                    Collapse
                                </Button>
                                <Button type="submit" disabled={isSaving} className={styles.saveBtn}>
                                    {isSaving ? 'Saving...' : 'Save'}
                                </Button>
                            </div>
                        </form>
                    )}
                </div>
            )}
        </div>
    );
};