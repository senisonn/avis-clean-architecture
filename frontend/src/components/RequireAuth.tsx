import { Navigate, useLocation } from 'react-router-dom';
import type { ReactNode } from 'react';
import { useAuth } from '@/context/AuthContext';
import type { Role } from '@/types';

export function RequireAuth({ children, role }: { children: ReactNode; role?: Role }) {
  const { session } = useAuth();
  const location = useLocation();

  if (!session) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }
  if (role && session.role !== role) {
    return <Navigate to="/" replace />;
  }
  return <>{children}</>;
}
