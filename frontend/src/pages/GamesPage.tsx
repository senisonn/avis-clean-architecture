import { useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { Gamepad2, Search, Sparkles } from 'lucide-react';
import { gamesApi } from '@/api/games';
import { Card } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { Input } from '@/components/ui/Input';
import { Skeleton } from '@/components/ui/Skeleton';
import { EmptyState } from '@/components/ui/EmptyState';
import { getApiErrorMessage } from '@/api/client';
import type { Game } from '@/types';

export function GamesPage() {
  const [query, setQuery] = useState('');
  const [genre, setGenre] = useState<string | null>(null);

  const { data, isLoading, isError, error } = useQuery({
    queryKey: ['games'],
    queryFn: gamesApi.list,
  });

  const genres = useMemo(() => {
    if (!data) return [];
    const set = new Set<string>();
    for (const g of data) if (g.genre) set.add(g.genre);
    return [...set].sort();
  }, [data]);

  const filtered = useMemo(() => {
    if (!data) return [];
    const q = query.trim().toLowerCase();
    return data.filter((g) => {
      if (genre && g.genre !== genre) return false;
      if (!q) return true;
      return (
        g.name.toLowerCase().includes(q) ||
        g.description?.toLowerCase().includes(q) ||
        g.publisher?.toLowerCase().includes(q)
      );
    });
  }, [data, query, genre]);

  return (
    <div className="space-y-8">
      <Hero count={data?.length} />

      <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
        <div className="relative md:w-80">
          <Search className="absolute left-3 top-1/2 size-4 -translate-y-1/2 text-ink-dim" />
          <Input
            className="pl-9"
            placeholder="Search games, publishers…"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
        </div>
        {genres.length > 0 && (
          <div className="flex flex-wrap items-center gap-1.5">
            <FilterChip active={genre === null} onClick={() => setGenre(null)}>
              All
            </FilterChip>
            {genres.map((g) => (
              <FilterChip key={g} active={genre === g} onClick={() => setGenre(g)}>
                {g}
              </FilterChip>
            ))}
          </div>
        )}
      </div>

      {isLoading && (
        <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
          {Array.from({ length: 6 }).map((_, i) => (
            <Card key={i} className="overflow-hidden">
              <Skeleton className="aspect-[16/9] rounded-none" />
              <div className="space-y-2 p-5">
                <Skeleton className="h-5 w-3/4" />
                <Skeleton className="h-4 w-full" />
                <Skeleton className="h-4 w-1/2" />
              </div>
            </Card>
          ))}
        </div>
      )}

      {isError && (
        <EmptyState
          icon={<Gamepad2 className="size-10" />}
          title="Couldn't load games"
          description={getApiErrorMessage(error)}
        />
      )}

      {!isLoading && !isError && filtered.length === 0 && (
        <EmptyState
          icon={<Gamepad2 className="size-10" />}
          title={data?.length === 0 ? 'No games yet' : 'No games match your search'}
          description={
            data?.length === 0
              ? 'A moderator can create games from the API. Once added, they will show up here.'
              : 'Try clearing filters or refining your search.'
          }
        />
      )}

      {filtered.length > 0 && (
        <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
          {filtered.map((g) => (
            <GameCard key={g.id} game={g} />
          ))}
        </div>
      )}
    </div>
  );
}

function Hero({ count }: { count: number | undefined }) {
  return (
    <section className="overflow-hidden rounded-3xl border border-line bg-gradient-to-br from-bg-elevated via-bg-surface to-bg p-8 sm:p-10">
      <div className="flex items-center gap-2 text-xs font-medium text-brand-50">
        <Sparkles className="size-3.5" />
        <span>Clean architecture · Hexagonal ports & adapters</span>
      </div>
      <h1 className="mt-3 max-w-2xl text-3xl font-semibold tracking-tight sm:text-4xl">
        Discover games. Share your verdict.
      </h1>
      <p className="mt-2 max-w-xl text-sm text-ink-muted">
        A small showcase that ties a Spring domain core to a typed React UI through clearly defined ports.
      </p>
      {typeof count === 'number' && (
        <div className="mt-4 inline-flex items-center gap-2 rounded-full border border-line bg-bg-surface/80 px-3 py-1 text-xs text-ink-muted">
          <span className="size-1.5 rounded-full bg-success" />
          {count} games available
        </div>
      )}
    </section>
  );
}

function FilterChip({
  active,
  onClick,
  children,
}: {
  active: boolean;
  onClick: () => void;
  children: React.ReactNode;
}) {
  return (
    <button
      type="button"
      onClick={onClick}
      className={
        'rounded-full border px-3 py-1 text-xs font-medium transition-colors ring-focus ' +
        (active
          ? 'border-brand/40 bg-brand/15 text-brand-50'
          : 'border-line bg-bg-surface text-ink-muted hover:text-ink hover:border-line/80')
      }
    >
      {children}
    </button>
  );
}

function GameCard({ game }: { game: Game }) {
  return (
    <Link to={`/games/${game.id}`} className="group">
      <Card className="overflow-hidden transition-all duration-200 group-hover:border-brand/40 group-hover:-translate-y-0.5 group-hover:shadow-glow">
        <div className="relative aspect-[16/9] bg-bg-elevated">
          {game.imageUrl ? (
            <img
              src={game.imageUrl}
              alt={game.name}
              loading="lazy"
              className="h-full w-full object-cover transition-transform duration-500 group-hover:scale-[1.03]"
            />
          ) : (
            <div className="flex h-full items-center justify-center text-ink-dim">
              <Gamepad2 className="size-10" />
            </div>
          )}
          {typeof game.price === 'number' && (
            <div className="absolute right-3 top-3 rounded-full bg-bg/90 px-2.5 py-1 text-xs font-medium text-ink backdrop-blur">
              {game.price === 0 ? 'Free' : `${game.price.toFixed(2)} €`}
            </div>
          )}
        </div>
        <div className="space-y-3 p-5">
          <div className="flex items-start justify-between gap-2">
            <h3 className="line-clamp-1 text-base font-semibold text-ink group-hover:text-brand-50">
              {game.name}
            </h3>
            {game.ageRating && <Badge tone="warning">{game.ageRating}</Badge>}
          </div>
          {game.description && (
            <p className="line-clamp-2 text-sm text-ink-muted">{game.description}</p>
          )}
          <div className="flex flex-wrap items-center gap-1.5">
            {game.genre && <Badge tone="brand">{game.genre}</Badge>}
            {game.publisher && <Badge>{game.publisher}</Badge>}
          </div>
        </div>
      </Card>
    </Link>
  );
}
