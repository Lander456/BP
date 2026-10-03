import { apiClient } from "./client";
import type { AuthResponseDto, LoginRequestDto } from "../types";

export const authService = {
    login: async (dto: LoginRequestDto): Promise<AuthResponseDto> => {
        const { data } = await apiClient.post<AuthResponseDto>('api/auth/login', dto);
        if (data.token) {
            localStorage.setItem('token', data.token);
        }
        return data;
    },
    logout: (): void => {
        localStorage.removeItem('token');
    }
};
