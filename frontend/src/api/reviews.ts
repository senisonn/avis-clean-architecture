import { api } from './client';
import type { CreateReviewCommand, Review } from '@/types';

export const reviewsApi = {
  byGame: (gameId: number) =>
    api.get<Review[]>(`/reviews/game/${gameId}`).then((r) => r.data),

  byPlayer: (playerId: number) =>
    api.get<Review[]>(`/reviews/player/${playerId}`).then((r) => r.data),

  submit: (cmd: CreateReviewCommand) =>
    api.post<Review>('/reviews', cmd).then((r) => r.data),

  remove: (reviewId: number, playerId: number) =>
    api.delete<void>(`/reviews/${reviewId}/player/${playerId}`).then(() => undefined),
};

export const moderationApi = {
  pending: () => api.get<Review[]>('/moderation/pending').then((r) => r.data),

  approve: (reviewId: number, moderatorId: number) =>
    api
      .patch<Review>(`/moderation/${reviewId}/approve/moderator/${moderatorId}`)
      .then((r) => r.data),

  reject: (reviewId: number, moderatorId: number) =>
    api
      .patch<Review>(`/moderation/${reviewId}/reject/moderator/${moderatorId}`)
      .then((r) => r.data),
};
