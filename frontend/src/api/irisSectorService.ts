import { apiClient } from "./client";
import type {
    IrisSectorCreateDto,
    IrisSectorDetailDto,
    IrisSectorListDto,
    IrisSectorUpdateDto
} from "../types";

export const irisSectorService = {
    create: async (dto: IrisSectorCreateDto): Promise<IrisSectorDetailDto> => {
        const { data } = await apiClient.post<IrisSectorDetailDto>('sector/create', null, { params: dto });
        return data;
    },
    update: async (dto: IrisSectorUpdateDto): Promise<IrisSectorDetailDto> => {
        const { data } = await apiClient.patch<IrisSectorDetailDto>('sector/update', null, { params: dto });
        return data;
    },
    getByMapId: async (mapId: number): Promise<IrisSectorListDto[]> => {
        const { data } = await apiClient.get<IrisSectorListDto[]>(`sector/map/${mapId}`);
        return data;
    },
    getByMapIdAndName: async (mapId: number, sectorName: string): Promise<IrisSectorDetailDto> => {
        const { data } = await apiClient.get<IrisSectorDetailDto>(`sector/map/${mapId}/${sectorName}`);
        return data;
    },
    delete: async (id: number): Promise<void> => {
        await apiClient.delete(`sector/irisSector/delete/${id}`);
    }
};
