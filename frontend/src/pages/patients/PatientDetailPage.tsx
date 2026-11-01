import {Link, useParams} from "react-router-dom";
import type {IrisImageListDto} from "../../types";
import React, {useEffect, useState} from "react";
import {irisImageService} from "../../api";
import styles from "./PatientDetailPage.module.css";
import {Loader2, Trash2, Upload} from "lucide-react";

export const PatientDetailPage = () => {
    const { id } = useParams<{ id: string }>();
    const [images, setImages] = useState<IrisImageListDto[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [isUploading, setIsUploading] = useState<boolean>(false);
    const [deletingId, setDeletingId] = useState<number | null>(null);
    const [error, setError] = useState<string | null>(null);

    const loadImages = () => {
        if (!id) {
            return;
        }
        irisImageService.getByPatientId(Number(id))
            .then(setImages)
            .catch(console.error)
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        loadImages();
    }, [id]);

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file || !id) {
            return;
        }

        try {
            setIsUploading(true);
            setError(null);

            await irisImageService.upload(file, {
                patientId: Number(id),
            });

            loadImages();
        } catch (error) {
            console.error("Upload failed", error);
            setError("Failed to upload image, please try again");
        } finally {
            setIsUploading(false);
            e.target.value = '';
        }
    };

    const handleDelete = async (imageId: number) => {
        if (!window.confirm("Are you sure you want to delete this image?")) {
            return;
        }

        try {
            setDeletingId(imageId);
            await irisImageService.delete(imageId);
            setImages((prevState) => prevState.filter((img) => img.id !== imageId));
        } catch (error) {
            console.error("Failed to delete image", error);
            setError("Failed to delete image, please try again");
        } finally {
            setDeletingId(null);
        }
    };

    return (
        <div>
            <Link to='/patients'>
                Back to Patients
            </Link>

            <h2>Iris Images</h2>

            {error && <div className={styles.error}>{error}</div>}

            {loading ? (
                <p>Loading images...</p>
            ) : (
                <div className={styles.grid}>
                    <label className={`${styles.uploadTile} ${isUploading ? styles.disabled : ''}`}>
                        <input
                            type="file"
                            accept="image/*"
                            onChange={handleFileChange}
                            disabled={isUploading}
                            className={styles.hiddenInput}
                        />
                        {isUploading ? (
                            <>
                                <Loader2 className={styles.spinner} size={28} />
                                <span>Uploading...</span>
                            </>
                        ) : (
                            <>
                                <Upload size={28} />
                                <span>Upload New Image</span>
                            </>
                        )}
                    </label>

                    {images.map((img) => (
                        <div key={img.id} className={styles.imageCard}>
                            <div className={styles.imageWrapper}>
                                <img
                                    src={img.imageUrl}
                                    alt={img.label ?? "Iris image"}
                                    className={styles.image}
                                />
                                <button
                                    type="button"
                                    onClick={() => handleDelete(img.id)}
                                    disabled={deletingId === img.id}
                                    className={styles.deleteBtn}
                                    title="Delete image"
                                >
                                    {deletingId === img.id ? (
                                        <Loader2 size={16} className={styles.spinner} />
                                    ) : (
                                        <Trash2 size={16} />
                                    )}
                                </button>
                            </div>
                            {img.label && <span className={styles.imageLabel}>{img.label}</span>}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};