import { useMemo } from 'react';
import { Link } from 'react-router-dom';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MessageSquareText, Trash2 } from 'lucide-react';
import { toast } from 'sonner';
import { reviewsApi } from '@/api/reviews';
import { useAuth } from '@/context/AuthContext';
import { Card, CardBody } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { Button } from '@/components/ui/Button';
import { RatingStars } from '@/components/ui/RatingStars';
import { Skeleton } from '@/components/ui/Skeleton';
import { EmptyState } from '@/components/ui/EmptyState';
import { getApiErrorMessage } from '@/api/client';
import type { Review, ReviewStatus } from '@/types';

export function ProfilePage() {
  const { session } = useAuth();
  const queryClient = useQueryClient();

  const reviews = useQuery({
    queryKey: ['reviews', 'player', session?.id],
    queryFn: () => reviewsApi.byPlayer(session!.id),
    enabled: session !== null,
  });

  const remove = useMutation({
    mutationFn: (reviewId: number) => reviewsApi.remove(reviewId, session!.id),
    onSuccess: () => {
      toast.success('Review deleted');
      queryClient.invalidateQueries({ queryKey: ['reviews'] });
    },
    onError: (err) => toast.error(getApiErrorMessage(err)),
  });

  const stats = useMemo(() => {
    const data = reviews.data ?? [];
    const total = data.length;
    const approved = data.filter((r) => r.status === 'APPROVED').length;
    const pending = data.filter((r) => r.status === 'PUBLISHED').length;
    const rejected = data.filter((r) => r.status === 'REJECTED').length;
    const avg =
      total === 0 ? 0 : data.reduce((acc, r) => acc + r.rating, 0) / total;
    return { total, approved, pending, rejected, avg };
  }, [reviews.data]);

  if (!session) return null;

  return (
    <div className="space-y-8">
      <ProfileHero username={session.username} role={session.role} stats={stats} />

      <section className="space-y-4">
        <h2 className="text-xl font-semibold tracking-tight">Your reviews</h2>

        {reviews.isLoading && (
          <div className="space-y-3">
            {Array.from({ length: 3 }).map((_, i) => (
              <Card key={i}>
                <CardBody className="space-y-3">
                  <Skeleton className="h-4 w-32" />
                  <Skeleton className="h-4 w-full" />
                  <Skeleton className="h-4 w-2/3" />
                </CardBody>
              </Card>
            ))}
          </div>
        )}

        {reviews.isError && (
          <EmptyState
            title="Couldn't load your reviews"
            description={getApiErrorMessage(reviews.error)}
          />
        )}

        {reviews.data && reviews.data.length === 0 && (
          <EmptyState
            icon={<MessageSquareText className="size-8" />}
            title="No reviews yet"
            description="Start exploring the catalog and share your verdicts."
            action={
              <Link to="/">
                <Button>Browse games</Button>
              </Link>
            }
          />
        )}

        {reviews.data && reviews.data.length > 0 && (
          <div className="space-y-3">
            {reviews.data.map((r) => (
              <ReviewRow
                key={r.id}
                review={r}
                onDelete={() => remove.mutate(r.id)}
                deleting={remove.isPending && remove.variables === r.id}
                canDelete={session.role === 'PLAYER'}
              />
            ))}
          </div>
        )}
      </section>
    </div>
  );
}

function ProfileHero({
  username,
  role,
  stats,
}: {
  username: string;
  role: 'PLAYER' | 'MODERATOR';
  stats: { total: number; approved: number; pending: number; rejected: number; avg: number };
}) {
  return (
    <Card>
      <CardBody className="flex flex-col items-start gap-6 sm:flex-row sm:items-center sm:justify-between">
        <div className="flex items-center gap-4">
          <div className="grid size-14 place-items-center rounded-full bg-gradient-to-br from-brand to-cyan-500 text-xl font-semibold text-white">
            {username.charAt(0).toUpperCase()}
          </div>
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-2xl font-semibold tracking-tight">{username}</h1>
              <Badge tone={role === 'MODERATOR' ? 'brand' : 'neutral'}>{role}</Badge>
            </div>
            <p className="text-sm text-ink-muted">
              {stats.total} review{stats.total === 1 ? '' : 's'} · avg {stats.avg.toFixed(1)}/5
            </p>
          </div>
        </div>
        <div className="flex flex-wrap items-center gap-2">
          <Stat label="Approved" value={stats.approved} tone="success" />
          <Stat label="Pending"  value={stats.pending}  tone="warning" />
          <Stat label="Rejected" value={stats.rejected} tone="danger" />
        </div>
      </CardBody>
    </Card>
  );
}

function Stat({
  label,
  value,
  tone,
}: {
  label: string;
  value: number;
  tone: 'success' | 'warning' | 'danger';
}) {
  return (
    <div className="rounded-xl border border-line bg-bg-elevated px-3 py-2">
      <div className="text-xs text-ink-dim">{label}</div>
      <div className="flex items-center gap-2">
        <span className="text-lg font-semibold">{value}</span>
        <Badge tone={tone}>{label}</Badge>
      </div>
    </div>
  );
}

function ReviewRow({
  review,
  onDelete,
  deleting,
  canDelete,
}: {
  review: Review;
  onDelete: () => void;
  deleting: boolean;
  canDelete: boolean;
}) {
  return (
    <Card>
      <CardBody className="space-y-3">
        <div className="flex flex-wrap items-center justify-between gap-2">
          <div>
            <div className="text-sm font-medium">{review.gameName ?? 'Unknown game'}</div>
            <div className="text-xs text-ink-dim">
              {new Date(review.submittedAt).toLocaleString()}
            </div>
          </div>
          <div className="flex items-center gap-2">
            <RatingStars value={review.rating} size={16} />
            <StatusBadge status={review.status} />
          </div>
        </div>
        <p className="text-sm leading-relaxed text-ink">{review.content}</p>
        {canDelete && (
          <div className="flex justify-end">
            <Button
              variant="danger"
              size="sm"
              leftIcon={<Trash2 className="size-3.5" />}
              loading={deleting}
              onClick={onDelete}
            >
              Delete
            </Button>
          </div>
        )}
      </CardBody>
    </Card>
  );
}

function StatusBadge({ status }: { status: ReviewStatus }) {
  const map: Record<ReviewStatus, { tone: 'neutral' | 'success' | 'warning' | 'danger'; label: string }> = {
    DRAFT:     { tone: 'neutral', label: 'Draft' },
    PUBLISHED: { tone: 'warning', label: 'Pending' },
    APPROVED:  { tone: 'success', label: 'Approved' },
    REJECTED:  { tone: 'danger',  label: 'Rejected' },
  };
  const cfg = map[status];
  return <Badge tone={cfg.tone}>{cfg.label}</Badge>;
}
