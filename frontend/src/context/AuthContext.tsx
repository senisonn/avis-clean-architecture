import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import type { ReactNode } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { authApi } from '@/api/auth';
import { SESSION_STORAGE_KEY } from '@/api/client';
import type { AuthRequest, AuthSession, RegisterCommand, Role } from '@/types';

interface AuthContextValue {
  session: AuthSession | null;
  isAuthenticated: boolean;
  isModerator: boolean;
  loginPlayer: (req: AuthRequest) => Promise<AuthSession>;
  loginModerator: (req: AuthRequest) => Promise<AuthSession>;
  register: (cmd: RegisterCommand) => Promise<AuthSession>;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextValue | null>(null);

function readSession(): AuthSession | null {
  const raw = localStorage.getItem(SESSION_STORAGE_KEY);
  if (!raw) return null;
  try {
    return JSON.parse(raw) as AuthSession;
  } catch {
    localStorage.removeItem(SESSION_STORAGE_KEY);
    return null;
  }
}

function persistSession(session: AuthSession | null) {
  if (session) {
    localStorage.setItem(SESSION_STORAGE_KEY, JSON.stringify(session));
  } else {
    localStorage.removeItem(SESSION_STORAGE_KEY);
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const queryClient = useQueryClient();
  const [session, setSession] = useState<AuthSession | null>(() => readSession());

  useEffect(() => {
    const onStorage = (e: StorageEvent) => {
      if (e.key === SESSION_STORAGE_KEY) setSession(readSession());
    };
    window.addEventListener('storage', onStorage);
    return () => window.removeEventListener('storage', onStorage);
  }, []);

  const apply = useCallback(
    (next: AuthSession | null) => {
      persistSession(next);
      setSession(next);
      queryClient.clear();
    },
    [queryClient],
  );

  const loginAs = useCallback(
    async (role: Role, req: AuthRequest): Promise<AuthSession> => {
      const res =
        role === 'MODERATOR' ? await authApi.loginModerator(req) : await authApi.loginPlayer(req);
      const next: AuthSession = {
        token: res.token,
        id: res.id,
        username: res.username,
        role: res.role,
      };
      apply(next);
      return next;
    },
    [apply],
  );

  const register = useCallback(
    async (cmd: RegisterCommand): Promise<AuthSession> => {
      const res = await authApi.register(cmd);
      const next: AuthSession = {
        token: res.token,
        id: res.id,
        username: res.username,
        role: res.role,
      };
      apply(next);
      return next;
    },
    [apply],
  );

  const logout = useCallback(async () => {
    if (!session) return;
    try {
      if (session.role === 'MODERATOR') await authApi.logoutModerator();
      else await authApi.logoutPlayer();
    } catch {
      // best-effort — clear locally anyway
    }
    apply(null);
  }, [apply, session]);

  const value = useMemo<AuthContextValue>(
    () => ({
      session,
      isAuthenticated: session !== null,
      isModerator: session?.role === 'MODERATOR',
      loginPlayer: (req) => loginAs('PLAYER', req),
      loginModerator: (req) => loginAs('MODERATOR', req),
      register,
      logout,
    }),
    [session, loginAs, register, logout],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
