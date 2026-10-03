import { apiClient } from "./client";
import type { PatientCreateDto, PatientDetailDto, PatientListDto, PatientUpdateDto } from "../types";

export const patientService = {
    getAll: async (): Promise<PatientListDto[]> => {
        const { data } = await apiClient.get<PatientListDto[]>('patient/all');
        return data;
    },
    getById: async (id: number): Promise<PatientDetailDto> => {
        const { data } = await apiClient.get<PatientDetailDto>(`patient/${id}`);
        return data;
    },
    getByFirstName: async (firstName: string): Promise<PatientListDto[]> => {
        const { data } = await apiClient.get<PatientListDto[]>(`patient/filterFirstName/${firstName}`);
        return data;
    },
    getByLastName: async (lastName: string): Promise<PatientListDto[]> => {
        const { data } = await apiClient.get<PatientListDto[]>(`patient/filterLastName/${lastName}`);
        return data;
    },
    create: async (dto: PatientCreateDto): Promise<PatientDetailDto> => {
        const { data } = await apiClient.post<PatientDetailDto>('patient/create', null, { params: dto });
        return data;
    },
    update: async (id: number, dto: PatientUpdateDto): Promise<void> => {
        await apiClient.patch(`patient/update/${id}`, null, { params: dto });
    },
    delete: async (id: number): Promise<void> => {
        await apiClient.delete(`patient/delete/${id}`);
    }
};
