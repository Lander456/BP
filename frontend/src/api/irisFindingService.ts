import { apiClient } from "./client";
import type {
    IrisFindingCreateDto,
    IrisFindingDetailDto,
    IrisFindingListDto,
    IrisFindingUpdateDto
} from "../types";

export const irisFindingService = {
    getAll: async (): Promise<IrisFindingListDto[]> => {
        const { data } = await apiClient.get<IrisFindingListDto[]>('api/irisFinding');
        return data;
    },
    getById: async (id: number): Promise<IrisFindingDetailDto> => {
        const { data } = await apiClient.get<IrisFindingDetailDto>(`api/irisFinding/id/${id}`);
        return data;
    },
    getByImageId: async (imageId: number): Promise<IrisFindingListDto[]> => {
        const { data } = await apiClient.get<IrisFindingListDto[]>(`api/irisFinding/image/${imageId}`);
        return data;
    },
    getByLabelCode: async (labelCode: string): Promise<IrisFindingListDto[]> => {
        const { data } = await apiClient.get<IrisFindingListDto[]>(`api/irisFinding/label/${labelCode}`);
        return data;
    },
    getByIsValidated: async (isValidated: boolean): Promise<IrisFindingListDto[]> => {
        const { data } = await apiClient.get<IrisFindingListDto[]>(`api/irisFinding/validated/${isValidated}`);
        return data;
    },
    create: async (dto: IrisFindingCreateDto): Promise<IrisFindingDetailDto> => {
        const { data } = await apiClient.post<IrisFindingDetailDto>('api/irisFinding/create', dto);
        return data;
    },
    update: async (dto: IrisFindingUpdateDto): Promise<IrisFindingDetailDto> => {
        const { data } = await apiClient.patch<IrisFindingDetailDto>('api/irisFinding/update', dto);
        return data;
    },
    delete: async (id: number): Promise<void> => {
        await apiClient.delete(`api/irisFinding/delete/${id}`);
    }
};
