import { useState } from 'react';
import type { FormEvent } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import { ShieldCheck, User } from 'lucide-react';
import { Card, CardBody, CardHeader } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { useAuth } from '@/context/AuthContext';
import { getApiErrorMessage } from '@/api/client';
import { cn } from '@/lib/cn';
import type { Role } from '@/types';

interface LocationState {
  from?: { pathname?: string };
}

export function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const { loginPlayer, loginModerator } = useAuth();

  const [role, setRole] = useState<Role>('PLAYER');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const redirectTo = (location.state as LocationState | null)?.from?.pathname ?? '/';

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const session = role === 'MODERATOR'
        ? await loginModerator({ email, password })
        : await loginPlayer({ email, password });
      toast.success(`Welcome back, ${session.username}`);
      navigate(role === 'MODERATOR' ? '/moderation' : redirectTo, { replace: true });
    } catch (err) {
      toast.error(getApiErrorMessage(err));
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="mx-auto flex max-w-md flex-col gap-6 py-8">
      <div className="space-y-2 text-center">
        <h1 className="text-3xl font-semibold tracking-tight">Welcome back</h1>
        <p className="text-sm text-ink-muted">Sign in to write reviews and manage your library.</p>
      </div>

      <Card>
        <CardHeader className="pb-0">
          <RoleToggle value={role} onChange={setRole} />
        </CardHeader>
        <CardBody>
          <form onSubmit={handleSubmit} className="space-y-4">
            <Input
              label="Email"
              type="email"
              name="email"
              autoComplete="email"
              placeholder="you@example.com"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <Input
              label="Password"
              type="password"
              name="password"
              autoComplete="current-password"
              placeholder="••••••••"
              required
              minLength={8}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <Button type="submit" className="w-full" loading={submitting}>
              Sign in as {role === 'MODERATOR' ? 'moderator' : 'player'}
            </Button>
          </form>
        </CardBody>
      </Card>

      <p className="text-center text-sm text-ink-muted">
        New here?{' '}
        <Link to="/register" className="text-brand hover:underline">
          Create an account
        </Link>
      </p>
    </div>
  );
}

function RoleToggle({ value, onChange }: { value: Role; onChange: (v: Role) => void }) {
  return (
    <div className="grid grid-cols-2 gap-1 rounded-xl bg-bg p-1 border border-line">
      <ToggleButton active={value === 'PLAYER'} onClick={() => onChange('PLAYER')} icon={<User className="size-4" />}>
        Player
      </ToggleButton>
      <ToggleButton
        active={value === 'MODERATOR'}
        onClick={() => onChange('MODERATOR')}
        icon={<ShieldCheck className="size-4" />}
      >
        Moderator
      </ToggleButton>
    </div>
  );
}

function ToggleButton({
  active,
  onClick,
  icon,
  children,
}: {
  active: boolean;
  onClick: () => void;
  icon: React.ReactNode;
  children: React.ReactNode;
}) {
  return (
    <button
      type="button"
      onClick={onClick}
      className={cn(
        'inline-flex items-center justify-center gap-1.5 rounded-lg px-3 py-2 text-sm font-medium transition-colors ring-focus',
        active ? 'bg-bg-elevated text-ink shadow-sm' : 'text-ink-muted hover:text-ink',
      )}
    >
      {icon}
      {children}
    </button>
  );
}
