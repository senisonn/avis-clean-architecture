import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { Check, ShieldCheck, X } from 'lucide-react';
import { toast } from 'sonner';
import { moderationApi } from '@/api/reviews';
import { useAuth } from '@/context/AuthContext';
import { Card, CardBody } from '@/components/ui/Card';
import { Badge } from '@/components/ui/Badge';
import { Button } from '@/components/ui/Button';
import { RatingStars } from '@/components/ui/RatingStars';
import { Skeleton } from '@/components/ui/Skeleton';
import { EmptyState } from '@/components/ui/EmptyState';
import { getApiErrorMessage } from '@/api/client';
import type { Review } from '@/types';

export function ModerationPage() {
  const { session } = useAuth();
  const queryClient = useQueryClient();

  const pending = useQuery({
    queryKey: ['moderation', 'pending'],
    queryFn: moderationApi.pending,
  });

  const decide = useMutation({
    mutationFn: ({ review, decision }: { review: Review; decision: 'approve' | 'reject' }) => {
      if (!session) throw new Error('Not signed in.');
      return decision === 'approve'
        ? moderationApi.approve(review.id, session.id)
        : moderationApi.reject(review.id, session.id);
    },
    onSuccess: (_data, vars) => {
      toast.success(vars.decision === 'approve' ? 'Review approved.' : 'Review rejected.');
      queryClient.invalidateQueries({ queryKey: ['moderation', 'pending'] });
      queryClient.invalidateQueries({ queryKey: ['reviews'] });
    },
    onError: (err) => toast.error(getApiErrorMessage(err)),
  });

  return (
    <div className="space-y-6">
      <header className="flex items-end justify-between">
        <div>
          <div className="flex items-center gap-2 text-xs font-medium text-brand-50">
            <ShieldCheck className="size-3.5" />
            Moderation queue
          </div>
          <h1 className="mt-1 text-2xl font-semibold tracking-tight">Pending reviews</h1>
          <p className="text-sm text-ink-muted">
            Approve or reject submissions before they appear publicly.
          </p>
        </div>
        {pending.data && (
          <Badge tone="warning">
            {pending.data.length} pending
          </Badge>
        )}
      </header>

      {pending.isLoading && (
        <div className="space-y-3">
          {Array.from({ length: 3 }).map((_, i) => (
            <Card key={i}>
              <CardBody>
                <Skeleton className="h-5 w-1/3" />
                <Skeleton className="mt-3 h-4 w-full" />
                <Skeleton className="mt-2 h-4 w-2/3" />
              </CardBody>
            </Card>
          ))}
        </div>
      )}

      {pending.isError && (
        <EmptyState title="Couldn't load queue" description={getApiErrorMessage(pending.error)} />
      )}

      {pending.data && pending.data.length === 0 && (
        <EmptyState
          icon={<ShieldCheck className="size-10" />}
          title="All caught up"
          description="No pending reviews. Take a break."
        />
      )}

      {pending.data && pending.data.length > 0 && (
        <div className="space-y-3">
          {pending.data.map((r) => (
            <Card key={r.id}>
              <CardBody className="space-y-4">
                <div className="flex flex-wrap items-start justify-between gap-3">
                  <div className="space-y-1">
                    <div className="flex items-center gap-2 text-sm">
                      <span className="font-semibold">{r.playerUsername ?? 'Anonymous'}</span>
                      <span className="text-ink-dim">on</span>
                      <span className="text-ink">{r.gameName ?? '—'}</span>
                    </div>
                    <div className="flex items-center gap-2 text-xs text-ink-dim">
                      <RatingStars value={r.rating} size={14} />
                      <span>{r.rating.toFixed(1)} · {new Date(r.submittedAt).toLocaleString()}</span>
                    </div>
                  </div>
                  <Badge tone="warning">Pending</Badge>
                </div>

                <p className="rounded-xl border border-line bg-bg p-4 text-sm leading-relaxed">
                  {r.content}
                </p>

                <div className="flex justify-end gap-2">
                  <Button
                    variant="danger"
                    size="sm"
                    leftIcon={<X className="size-4" />}
                    loading={decide.isPending && decide.variables?.review.id === r.id && decide.variables.decision === 'reject'}
                    onClick={() => decide.mutate({ review: r, decision: 'reject' })}
                  >
                    Reject
                  </Button>
                  <Button
                    size="sm"
                    leftIcon={<Check className="size-4" />}
                    loading={decide.isPending && decide.variables?.review.id === r.id && decide.variables.decision === 'approve'}
                    onClick={() => decide.mutate({ review: r, decision: 'approve' })}
                  >
                    Approve
                  </Button>
                </div>
              </CardBody>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
}
