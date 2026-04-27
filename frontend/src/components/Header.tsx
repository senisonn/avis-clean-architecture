import { Link, NavLink, useNavigate } from 'react-router-dom';
import { Gamepad2, LogOut, Settings, ShieldCheck, UserCircle2 } from 'lucide-react';
import { useAuth } from '@/context/AuthContext';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { ThemeToggle } from '@/components/ThemeToggle';
import { cn } from '@/lib/cn';

export function Header() {
  const { session, isModerator, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  return (
    <header className="sticky top-0 z-30 border-b border-line bg-bg/70 backdrop-blur-xl">
      <div className="mx-auto flex h-16 max-w-6xl items-center justify-between px-6">
        <Link to="/" className="flex items-center gap-2 font-semibold tracking-tight">
          <span className="grid size-8 place-items-center rounded-xl bg-gradient-to-br from-brand to-cyan-400 text-white shadow-glow">
            <Gamepad2 className="size-4" />
          </span>
          <span>Avis</span>
          <span className="hidden text-xs font-normal text-ink-dim sm:inline">
            · clean architecture
          </span>
        </Link>

        <nav className="flex items-center gap-1">
          <NavItem to="/" label="Games" />
          {session && !isModerator && <NavItem to="/profile" label="Profile" icon={<UserCircle2 className="size-4" />} />}
          {isModerator && (
            <>
              <NavItem to="/moderation" label="Moderation" icon={<ShieldCheck className="size-4" />} />
              <NavItem to="/admin"      label="Admin"      icon={<Settings className="size-4" />} />
            </>
          )}
        </nav>

        <div className="flex items-center gap-3">
          <ThemeToggle />
          {session ? (
            <>
              <div className="hidden items-center gap-2 text-sm sm:flex">
                <UserCircle2 className="size-4 text-ink-muted" />
                <span className="text-ink">{session.username}</span>
                <Badge tone={isModerator ? 'brand' : 'neutral'}>{session.role}</Badge>
              </div>
              <Button variant="ghost" size="sm" leftIcon={<LogOut className="size-4" />} onClick={handleLogout}>
                Sign out
              </Button>
            </>
          ) : (
            <>
              <Button variant="ghost" size="sm" onClick={() => navigate('/login')}>
                Sign in
              </Button>
              <Button size="sm" onClick={() => navigate('/register')}>
                Get started
              </Button>
            </>
          )}
        </div>
      </div>
    </header>
  );
}

function NavItem({ to, label, icon }: { to: string; label: string; icon?: React.ReactNode }) {
  return (
    <NavLink
      to={to}
      end
      className={({ isActive }) =>
        cn(
          'inline-flex items-center gap-1.5 rounded-lg px-3 py-1.5 text-sm transition-colors',
          isActive ? 'bg-bg-elevated text-ink' : 'text-ink-muted hover:text-ink hover:bg-bg-surface',
        )
      }
    >
      {icon}
      {label}
    </NavLink>
  );
}
