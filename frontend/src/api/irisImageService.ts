import { apiClient } from "./client";
import type { IrisImageCreateDto, IrisImageDetailDto, IrisImageListDto } from "../types";

export const irisImageService = {
    getById: async (id: number): Promise<IrisImageDetailDto> => {
        const { data } = await apiClient.get<IrisImageDetailDto>(`image/${id}`);
        return data;
    },
    getByPatientId: async (patientId: number): Promise<IrisImageListDto[]> => {
        const { data } = await apiClient.get<IrisImageListDto[]>(`image/patient/${patientId}`);
        return data;
    },
    upload: async (file: File, dto: IrisImageCreateDto): Promise<IrisImageDetailDto> => {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('imageData', new Blob([JSON.stringify(dto)], { type: 'application/json' }));
        const { data } = await apiClient.post<IrisImageDetailDto>('image/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return data;
    },
    delete: async (id: number): Promise<void> => {
        await apiClient.delete(`image/delete/${id}`);
    }
};
