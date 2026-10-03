import { apiClient } from "./client";
import type { IridologistDetailDto, IridologistUpdateDto, IridologistListDto, IridologistCreateDto } from "../types";

export const iridologistService = {
    register: async (dto: IridologistCreateDto): Promise<IridologistDetailDto> => {
        const { data } = await apiClient.post<IridologistDetailDto>('api/iridologist/register', dto);
        return data;
    },
    getById: async (id: number): Promise<IridologistDetailDto> => {
        const { data } = await apiClient.get<IridologistDetailDto>(`api/iridologist/id/${id}`);
        return data;
    },
    getAll: async (): Promise<IridologistListDto[]> => {
        const { data } = await apiClient.get<IridologistListDto[]>('api/iridologist');
        return data;
    },
    getByFirstName: async (firstName: string): Promise<IridologistListDto[]> => {
        const { data } = await apiClient.get<IridologistListDto[]>(`api/iridologist/firstName/${firstName}`);
        return data;
    },
    getByLastName: async (lastName: string): Promise<IridologistListDto[]> => {
        const { data } = await apiClient.get<IridologistListDto[]>(`api/iridologist/lastName/${lastName}`);
        return data;
    },
    getByUserName: async (username: string): Promise<IridologistDetailDto> => {
        const { data } = await apiClient.get<IridologistDetailDto>(`api/iridologist/username/${username}`);
        return data;
    },
    update: async (id: number, dto: IridologistUpdateDto): Promise<void> => {
        await apiClient.patch(`api/iridologist/update/${id}`, dto);
    },
    delete: async (id: number): Promise<void> => {
        await apiClient.delete(`api/iridologist/delete/${id}`);
    }
}