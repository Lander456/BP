import { apiClient } from "./client";
import type {
    IrisMapCreateDto,
    IrisMapDetailDto,
    IrisMapListDto,
    IrisMapUpdateDto
} from "../types";

export const irisMapService = {
    getAll: async (): Promise<IrisMapListDto[]> => {
        const { data } = await apiClient.get<IrisMapListDto[]>('map');
        return data;
    },
    getById: async (id: number): Promise<IrisMapDetailDto> => {
        const { data } = await apiClient.get<IrisMapDetailDto>(`map/${id}`);
        return data;
    },
    getByIridologistId: async (iridologistId: number): Promise<IrisMapListDto[]> => {
        const { data } = await apiClient.get<IrisMapListDto[]>(`map/iridologist/${iridologistId}`);
        return data;
    },
    upload: async (file: File, dto: IrisMapCreateDto): Promise<IrisMapDetailDto> => {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('imageData', new Blob([JSON.stringify(dto)], { type: 'application/json' }));
        const { data } = await apiClient.post<IrisMapDetailDto>('map/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return data;
    },
    update: async (dto: IrisMapUpdateDto): Promise<IrisMapDetailDto> => {
        const { data } = await apiClient.patch<IrisMapDetailDto>('map/update', dto);
        return data;
    },
    delete: async (id: number): Promise<void> => {
        await apiClient.delete(`map/delete/${id}`);
    }
};
