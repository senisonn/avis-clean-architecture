import axios, { AxiosError } from 'axios';

const STORAGE_KEY = 'avis.session';

export const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  const raw = localStorage.getItem(STORAGE_KEY);
  if (raw) {
    try {
      const session = JSON.parse(raw) as { token?: string };
      if (session.token) {
        config.headers.Authorization = `Bearer ${session.token}`;
      }
    } catch {
      localStorage.removeItem(STORAGE_KEY);
    }
  }
  return config;
});

export function getApiErrorMessage(err: unknown): string {
  if (err instanceof AxiosError) {
    const data = err.response?.data;
    if (typeof data === 'string') return data;
    if (data && typeof data === 'object') {
      const message =
        (data as { message?: string }).message ??
        (data as { error?: string }).error ??
        (data as { detail?: string }).detail;
      if (message) return message;
    }
    if (err.response?.status === 401) return 'Authentication required.';
    if (err.response?.status === 403) return 'Forbidden — insufficient permissions.';
    return err.message;
  }
  return err instanceof Error ? err.message : 'Unexpected error';
}

export const SESSION_STORAGE_KEY = STORAGE_KEY;
