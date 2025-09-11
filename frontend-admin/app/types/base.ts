export interface ApiError {
  errorCode: number;
  errorMessage: string;
  errorCause: string;
}

export interface ApiResponseDto<TData = any> {
  errors: ApiError[];
  success: boolean;
  data: TData;
}

export type ApiSuccess<TData> = ApiResponseDto<TData> & {
  success: true;
  errors: [];
  data: TData;
};

export type ApiFailure = ApiResponseDto<null> & {
  success: false;
  errors: ApiError[];
  data: null;
};

export type ApiResponse<TData> = ApiSuccess<TData> | ApiFailure;