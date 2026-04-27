export type Role = 'PLAYER' | 'MODERATOR';

export type ReviewStatus = 'DRAFT' | 'PUBLISHED' | 'APPROVED' | 'REJECTED';

export interface AuthResponse {
  token: string;
  id: number;
  username: string;
  role: Role;
}

export interface AuthRequest {
  email: string;
  password: string;
}

export interface RegisterCommand {
  username: string;
  email: string;
  password: string;
  birthDate?: string;
}

export interface Game {
  id: number;
  name: string;
  description: string | null;
  imageUrl: string | null;
  price: number | null;
  releaseDate: string | null;
  genre: string | null;
  publisher: string | null;
  ageRating: string | null;
}

export interface CreateGameCommand {
  name: string;
  description?: string;
  imageUrl?: string;
  price?: number;
  releaseDate?: string;
  genreId?: number;
  publisherId?: number;
  ageRatingId?: number;
}

export interface Review {
  id: number;
  content: string;
  rating: number;
  submittedAt: string;
  status: ReviewStatus;
  playerUsername: string | null;
  gameName: string | null;
}

export interface CreateReviewCommand {
  gameId: number;
  playerId: number;
  content: string;
  rating: number;
}

export interface Genre {
  id: number;
  name: string;
}

export interface Platform {
  id: number;
  name: string;
  releaseDate: string | null;
}

export interface Publisher {
  id: number;
  name: string;
}

export interface AuthSession {
  token: string;
  id: number;
  username: string;
  role: Role;
}
