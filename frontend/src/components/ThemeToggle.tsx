import { Moon, Sun } from 'lucide-react';
import { useTheme } from '@/context/ThemeContext';
import { cn } from '@/lib/cn';

interface ThemeToggleProps {
  className?: string;
}

export function ThemeToggle({ className }: ThemeToggleProps) {
  const { resolved, toggle } = useTheme();
  const isDark = resolved === 'dark';

  return (
    <button
      type="button"
      onClick={toggle}
      aria-label={isDark ? 'Switch to light mode' : 'Switch to dark mode'}
      title={isDark ? 'Light mode' : 'Dark mode'}
      className={cn(
        'inline-flex size-9 items-center justify-center rounded-lg border border-line bg-bg-surface text-ink-muted transition-colors ring-focus hover:text-ink hover:border-line/80',
        className,
      )}
    >
      <Sun className={cn('size-4 transition-all', isDark ? 'scale-0 rotate-90 opacity-0' : 'scale-100 rotate-0 opacity-100')} />
      <Moon className={cn('absolute size-4 transition-all', isDark ? 'scale-100 rotate-0 opacity-100' : 'scale-0 -rotate-90 opacity-0')} />
    </button>
  );
}
