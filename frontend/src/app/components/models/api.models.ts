export interface ApiResponse<T> {
    success: boolean;
    message: string;
    data?: T;
    timestamp: Date;
  }
  
  export interface PaginatedResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
  }