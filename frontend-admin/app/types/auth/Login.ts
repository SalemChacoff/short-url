import type { ApiResponse } from '../base';

export interface LoginRequestDto {
  email: string;
  password: string;
}

export interface LoginResponseDto {
  token: string;
  refreshToken: string
}

export type LoginApiResponse = ApiResponse<LoginResponseDto>;