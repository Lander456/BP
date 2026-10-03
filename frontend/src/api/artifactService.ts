import { apiClient } from './client';
import type { ArtifactCreateDto, ArtifactDetailDto, ArtifactListDto, ArtifactUpdateDto } from "../types";

export const artifactService = {
    getAll: async (): Promise<ArtifactListDto[]> => {
        const { data } = await apiClient.get<ArtifactListDto[]>('api/artifact/all');
        return data;
    },
    getById: async (id: number): Promise<ArtifactDetailDto> => {
        const { data } = await apiClient.get<ArtifactDetailDto>(`api/artifact/id/${id}`);
        return data;
    },
    getByLabelCode: async (label: string): Promise<ArtifactDetailDto> => {
        const { data } = await apiClient.get<ArtifactDetailDto>(`api/artifact/label/${label}`);
        return data;
    },
    create: async (dto: ArtifactCreateDto): Promise<ArtifactDetailDto> => {
        const { data } = await apiClient.post<ArtifactDetailDto>('api/artifact/create', dto);
        return data;
    },
    update: async (dto: ArtifactUpdateDto): Promise<ArtifactDetailDto> => {
        const { data } = await apiClient.patch<ArtifactDetailDto>('api/artifact/update', dto);
        return data;
    },
    delete: async (id: number): Promise<void> => {
        await apiClient.delete(`api/artifact/delete/${id}`);
    }
};
