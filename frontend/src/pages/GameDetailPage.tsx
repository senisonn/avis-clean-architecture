import { useMemo, useState } from 'react';
import type { FormEvent } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { ArrowLeft, CalendarDays, Gamepad2, MessageSquareText, Trash2 } from 'lucide-react';
import { toast } from 'sonner';
import { gamesApi } from '@/api/games';
import { reviewsApi } from '@/api/reviews';
import { useAuth } from '@/context/AuthContext';
import { Card, CardBody, CardHeader } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { Button } from '@/components/ui/Button';
import { Textarea } from '@/components/ui/Input';
import { RatingStars } from '@/components/ui/RatingStars';
import { Skeleton } from '@/components/ui/Skeleton';
import { EmptyState } from '@/components/ui/EmptyState';
import { getApiErrorMessage } from '@/api/client';
import type { Review, ReviewStatus } from '@/types';

export function GameDetailPage() {
  const params = useParams<{ id: string }>();
  const gameId = Number(params.id);

  const game = useQuery({
    queryKey: ['game', gameId],
    queryFn: () => gamesApi.get(gameId),
    enabled: Number.isFinite(gameId),
  });

  const reviews = useQuery({
    queryKey: ['reviews', 'game', gameId],
    queryFn: () => reviewsApi.byGame(gameId),
    enabled: Number.isFinite(gameId),
  });

  const summary = useMemo(() => {
    const items = (reviews.data ?? []).filter((r) => r.status === 'APPROVED' || r.status === 'PUBLISHED');
    if (items.length === 0) return { avg: 0, count: 0 };
    const total = items.reduce((acc, r) => acc + r.rating, 0);
    return { avg: total / items.length, count: items.length };
  }, [reviews.data]);

  if (!Number.isFinite(gameId)) {
    return <EmptyState title="Game not found" />;
  }

  return (
    <div className="space-y-8">
      <Link to="/" className="inline-flex items-center gap-1.5 text-sm text-ink-muted hover:text-ink">
        <ArrowLeft className="size-4" />
        Back to games
      </Link>

      {game.isLoading && <GameHeroSkeleton />}
      {game.isError && (
        <EmptyState title="Couldn't load game" description={getApiErrorMessage(game.error)} />
      )}
      {game.data && <GameHero game={game.data} avg={summary.avg} count={summary.count} />}

      <section className="space-y-4">
        <header className="flex items-end justify-between">
          <h2 className="text-xl font-semibold tracking-tight">Reviews</h2>
          <span className="text-xs text-ink-dim">
            {summary.count} approved · {(reviews.data?.length ?? 0)} total
          </span>
        </header>

        <ReviewForm gameId={gameId} />

        <div className="space-y-3">
          {reviews.isLoading &&
            Array.from({ length: 2 }).map((_, i) => (
              <Card key={i}>
                <CardBody className="space-y-3">
                  <Skeleton className="h-4 w-32" />
                  <Skeleton className="h-4 w-full" />
                  <Skeleton className="h-4 w-2/3" />
                </CardBody>
              </Card>
            ))}

          {reviews.data && reviews.data.length === 0 && (
            <EmptyState
              icon={<MessageSquareText className="size-8" />}
              title="No reviews yet"
              description="Be the first to share your verdict."
            />
          )}

          {reviews.data?.map((r) => (
            <ReviewCard key={r.id} review={r} />
          ))}
        </div>
      </section>
    </div>
  );
}

function GameHero({
  game,
  avg,
  count,
}: {
  game: import('@/types').Game;
  avg: number;
  count: number;
}) {
  return (
    <Card className="overflow-hidden">
      <div className="grid gap-0 md:grid-cols-[2fr_3fr]">
        <div className="relative aspect-[16/10] md:aspect-auto bg-bg-elevated">
          {game.imageUrl ? (
            <img src={game.imageUrl} alt={game.name} className="h-full w-full object-cover" />
          ) : (
            <div className="flex h-full items-center justify-center text-ink-dim">
              <Gamepad2 className="size-12" />
            </div>
          )}
        </div>
        <div className="space-y-5 p-6 sm:p-8">
          <div className="flex flex-wrap items-center gap-1.5">
            {game.genre && <Badge tone="brand">{game.genre}</Badge>}
            {game.ageRating && <Badge tone="warning">{game.ageRating}</Badge>}
            {game.publisher && <Badge>{game.publisher}</Badge>}
          </div>
          <div>
            <h1 className="text-3xl font-semibold tracking-tight">{game.name}</h1>
            {game.description && (
              <p className="mt-2 text-sm leading-relaxed text-ink-muted">{game.description}</p>
            )}
          </div>
          <div className="flex flex-wrap items-center gap-x-6 gap-y-3 text-sm">
            <div className="flex items-center gap-2">
              <RatingStars value={avg} />
              <span className="font-medium text-ink">{avg.toFixed(1)}</span>
              <span className="text-ink-dim">· {count} reviews</span>
            </div>
            {game.releaseDate && (
              <div className="flex items-center gap-1.5 text-ink-muted">
                <CalendarDays className="size-4" />
                {new Date(game.releaseDate).toLocaleDateString()}
              </div>
            )}
            {typeof game.price === 'number' && (
              <span className="rounded-full bg-bg-elevated px-3 py-1 text-xs font-medium">
                {game.price === 0 ? 'Free' : `${game.price.toFixed(2)} €`}
              </span>
            )}
          </div>
        </div>
      </div>
    </Card>
  );
}

function GameHeroSkeleton() {
  return (
    <Card className="overflow-hidden">
      <div className="grid gap-0 md:grid-cols-[2fr_3fr]">
        <Skeleton className="aspect-[16/10] rounded-none" />
        <div className="space-y-3 p-6 sm:p-8">
          <Skeleton className="h-4 w-32" />
          <Skeleton className="h-8 w-3/4" />
          <Skeleton className="h-4 w-full" />
          <Skeleton className="h-4 w-2/3" />
        </div>
      </div>
    </Card>
  );
}

function ReviewForm({ gameId }: { gameId: number }) {
  const { session } = useAuth();
  const queryClient = useQueryClient();
  const [content, setContent] = useState('');
  const [rating, setRating] = useState(0);

  const mutation = useMutation({
    mutationFn: () => {
      if (!session) throw new Error('Sign in to submit a review.');
      return reviewsApi.submit({ gameId, playerId: session.id, content, rating });
    },
    onSuccess: () => {
      toast.success('Review submitted — pending moderation.');
      setContent('');
      setRating(0);
      queryClient.invalidateQueries({ queryKey: ['reviews', 'game', gameId] });
    },
    onError: (err) => toast.error(getApiErrorMessage(err)),
  });

  if (!session) {
    return (
      <Card>
        <CardBody className="flex flex-col items-start gap-3 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <p className="font-medium">Want to share your verdict?</p>
            <p className="text-sm text-ink-muted">Sign in as a player to write a review.</p>
          </div>
          <Link to="/login">
            <Button size="sm">Sign in</Button>
          </Link>
        </CardBody>
      </Card>
    );
  }

  if (session.role === 'MODERATOR') {
    return (
      <Card>
        <CardBody className="text-sm text-ink-muted">
          Signed in as moderator — switch to a player account to write reviews.
        </CardBody>
      </Card>
    );
  }

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (rating === 0) {
      toast.error('Please pick a rating.');
      return;
    }
    if (content.trim().length === 0) {
      toast.error('Write something about the game.');
      return;
    }
    mutation.mutate();
  };

  return (
    <Card>
      <CardHeader>
        <h3 className="font-semibold">Write a review</h3>
      </CardHeader>
      <CardBody>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="flex items-center gap-3">
            <span className="text-sm text-ink-muted">Your rating</span>
            <RatingStars value={rating} onChange={setRating} size={24} />
            <span className="text-sm text-ink-dim">{rating ? `${rating}/5` : 'Tap to rate'}</span>
          </div>
          <Textarea
            placeholder="What did you think?"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            required
          />
          <div className="flex items-center justify-between">
            <span className="text-xs text-ink-dim">Reviews are reviewed before being published.</span>
            <Button type="submit" loading={mutation.isPending}>
              Submit review
            </Button>
          </div>
        </form>
      </CardBody>
    </Card>
  );
}

function ReviewCard({ review }: { review: Review }) {
  const { session } = useAuth();
  const queryClient = useQueryClient();

  const isOwner =
    session?.role === 'PLAYER' && session.username === review.playerUsername;

  const remove = useMutation({
    mutationFn: () => {
      if (!session) throw new Error('Not signed in.');
      return reviewsApi.remove(review.id, session.id);
    },
    onSuccess: () => {
      toast.success('Review deleted.');
      queryClient.invalidateQueries({ queryKey: ['reviews'] });
    },
    onError: (err) => toast.error(getApiErrorMessage(err)),
  });

  return (
    <Card>
      <CardBody className="space-y-3">
        <div className="flex flex-wrap items-center justify-between gap-2">
          <div className="flex items-center gap-3">
            <Avatar name={review.playerUsername ?? '?'} />
            <div>
              <div className="text-sm font-medium">{review.playerUsername ?? 'Anonymous'}</div>
              <div className="text-xs text-ink-dim">
                {new Date(review.submittedAt).toLocaleString()}
              </div>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <RatingStars value={review.rating} size={16} />
            <StatusBadge status={review.status} />
          </div>
        </div>
        <p className="text-sm leading-relaxed text-ink">{review.content}</p>
        {isOwner && (
          <div className="flex justify-end">
            <Button
              variant="danger"
              size="sm"
              leftIcon={<Trash2 className="size-3.5" />}
              loading={remove.isPending}
              onClick={() => remove.mutate()}
            >
              Delete
            </Button>
          </div>
        )}
      </CardBody>
    </Card>
  );
}

function Avatar({ name }: { name: string }) {
  const initial = name.charAt(0).toUpperCase();
  return (
    <div className="grid size-9 place-items-center rounded-full bg-gradient-to-br from-brand to-cyan-500 text-sm font-semibold text-white">
      {initial}
    </div>
  );
}

function StatusBadge({ status }: { status: ReviewStatus }) {
  const map: Record<ReviewStatus, { tone: 'neutral' | 'success' | 'warning' | 'danger'; label: string }> = {
    DRAFT: { tone: 'neutral', label: 'Draft' },
    PUBLISHED: { tone: 'warning', label: 'Pending' },
    APPROVED: { tone: 'success', label: 'Approved' },
    REJECTED: { tone: 'danger', label: 'Rejected' },
  };
  const cfg = map[status];
  return <Badge tone={cfg.tone}>{cfg.label}</Badge>;
}
