// ============================================
// RESPUESTAS GENÉRICAS
// ============================================

// api.models.ts
// src/app/components/models/api.models.ts
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