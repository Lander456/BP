import React, {useState} from "react";
import type {PatientCreateDto} from "../../types";
import {patientService} from "../../api";
import styles from "./PatientCard.module.css"
import {Button} from "../common/Button.tsx";
import {X} from "lucide-react";
import {Input} from "../common/Input.tsx";

interface CreatePatientModalProps {
    isOpen: boolean;
    onClose: () => void;
    onPatientCreated: () => void;
}

export const CreatePatientModal: React.FC<CreatePatientModalProps> = ({
    isOpen,
    onClose,
    onPatientCreated,
}) => {
    const [formData, setFormData] = useState<PatientCreateDto>({
        firstName: "",
        lastName: "",
        birthNum: "",
        age: undefined,
        sex: undefined,
    });
    const [isSaving, setIsSaving] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    if (!isOpen) {
        return null;
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const {name, value} = e.target;
        setFormData((prevState) => ({
            ...prevState,
            [name]: name === "age" ? (value ? Number(value) : undefined) : value,
        }));
    };

    const handleSubmit = async (e: React.ChangeEvent) => {
        e.preventDefault();
        if (!formData.firstName.trim() || !formData.lastName.trim()) {
            setError("First name and last name are required");
            return;
        }

        try {
            setIsSaving(true);
            setError(null);
            await patientService.create(formData);
            onPatientCreated();
            onClose();
        } catch (error: any) {
            console.error("Failed to create patient", error);
            setError(error.response?.data?.message || "Failed to create patient. Please try again.");
        } finally {
            setIsSaving(false);
        }
    };

    return (
        <div className={styles.overlay} onClick={onClose}>
            <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
                <div className={styles.header}>
                    <h2>
                        Create new patient
                    </h2>
                    <Button type="button" className={styles.closeBtn} onClick={onClose}>
                        <X size={20}/>
                    </Button>
                </div>

                {error && <div className={styles.errorMessage}>{error}</div>}

                <form onSubmit={handleSubmit} className={styles.form}>
                    <Input
                        label="First Name *"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleChange}
                        required={true}
                    />

                    <Input
                        label="Last Name *"
                        name="lastName"
                        value={formData.lastName}
                        onChange={handleChange}
                        required={true}
                    />

                    <Input
                        label="Birth Number"
                        name="birthNum"
                        value={formData.birthNum ?? ""}
                        onChange={handleChange}
                    />

                    <Input
                        label="Age"
                        name="age"
                        type="number"
                        min="0"
                        value={formData.age ?? ""}
                        onChange={handleChange}
                    />

                    <div className={styles.selectRow}>
                        <label htmlFor="sex" className={styles.label}>
                            Sex
                        </label>
                        <select
                            id="sex"
                            name="sex"
                            value={formData.sex ?? ""}
                            onChange={handleChange}
                            className={styles.select}
                        >
                            <option value="">Select Sex</option>
                            <option value="MALE">Male</option>
                            <option value="FEMALE">Female</option>
                        </select>
                    </div>

                    <div className={styles.actions}>
                        <Button type="button" onClick={onClose}>
                            Cancel
                        </Button>
                        <Button type="submit" isLoading={isSaving}>
                            {isSaving ? "Creating..." : "Create Patient"}
                        </Button>
                    </div>
                </form>
            </div>
        </div>
    )
}
