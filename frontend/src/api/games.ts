import { api } from './client';
import type { CreateGameCommand, Game } from '@/types';

export const gamesApi = {
  list: () => api.get<Game[]>('/games').then((r) => r.data),
  get: (id: number) => api.get<Game>(`/games/${id}`).then((r) => r.data),
  create: (cmd: CreateGameCommand) =>
    api.post<Game>('/games', cmd).then((r) => r.data),
  update: (id: number, cmd: CreateGameCommand) =>
    api.put<Game>(`/games/${id}`, cmd).then((r) => r.data),
  remove: (id: number) => api.delete<void>(`/games/${id}`).then(() => undefined),
};
