import { api } from './client';
import type { AuthRequest, AuthResponse, RegisterCommand } from '@/types';

export const authApi = {
  loginPlayer: (req: AuthRequest) =>
    api.post<AuthResponse>('/auth/login', req).then((r) => r.data),

  loginModerator: (req: AuthRequest) =>
    api.post<AuthResponse>('/auth/moderator/login', req).then((r) => r.data),

  register: (cmd: RegisterCommand) =>
    api.post<AuthResponse>('/auth/register', cmd).then((r) => r.data),

  logoutPlayer: () => api.post<void>('/auth/logout').then(() => undefined),

  logoutModerator: () => api.post<void>('/auth/moderator/logout').then(() => undefined),
};
