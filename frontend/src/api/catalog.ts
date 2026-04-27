import { api } from './client';
import type { Genre, Platform, Publisher } from '@/types';

export const genresApi = {
  list: () => api.get<Genre[]>('/genres').then((r) => r.data),
  create: (name: string) =>
    api
      .post<Genre>('/genres', null, { params: { name } })
      .then((r) => r.data),
  remove: (id: number) => api.delete<void>(`/genres/${id}`).then(() => undefined),
};

export const publishersApi = {
  list: () => api.get<Publisher[]>('/publishers').then((r) => r.data),
  create: (name: string) =>
    api
      .post<Publisher>('/publishers', null, { params: { name } })
      .then((r) => r.data),
  remove: (id: number) =>
    api.delete<void>(`/publishers/${id}`).then(() => undefined),
};

export const platformsApi = {
  list: () => api.get<Platform[]>('/platforms').then((r) => r.data),
  create: (name: string, releaseDate?: string) =>
    api
      .post<Platform>('/platforms', null, {
        params: releaseDate ? { name, releaseDate } : { name },
      })
      .then((r) => r.data),
  remove: (id: number) =>
    api.delete<void>(`/platforms/${id}`).then(() => undefined),
};
