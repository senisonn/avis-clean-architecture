import { Star } from 'lucide-react';
import { cn } from '@/lib/cn';

interface RatingStarsProps {
  value: number;
  max?: number;
  size?: number;
  onChange?: (value: number) => void;
  className?: string;
}

export function RatingStars({ value, max = 5, size = 18, onChange, className }: RatingStarsProps) {
  const interactive = typeof onChange === 'function';
  const stars = Array.from({ length: max }, (_, i) => i + 1);

  return (
    <div className={cn('inline-flex items-center gap-0.5', className)}>
      {stars.map((n) => {
        const filled = n <= Math.round(value);
        const Wrapper = interactive ? 'button' : 'span';
        return (
          <Wrapper
            key={n}
            type={interactive ? 'button' : undefined}
            onClick={interactive ? () => onChange?.(n) : undefined}
            className={cn(
              'transition-transform',
              interactive && 'hover:scale-110 ring-focus rounded',
            )}
            aria-label={interactive ? `Rate ${n} star${n > 1 ? 's' : ''}` : undefined}
          >
            <Star
              width={size}
              height={size}
              className={cn(
                filled ? 'fill-warning text-warning' : 'fill-transparent text-ink-dim',
              )}
            />
          </Wrapper>
        );
      })}
    </div>
  );
}
