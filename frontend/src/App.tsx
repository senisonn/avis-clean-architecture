import { Navigate, Route, Routes } from 'react-router-dom';
import { Header } from '@/components/Header';
import { RequireAuth } from '@/components/RequireAuth';
import { LoginPage } from '@/pages/LoginPage';
import { RegisterPage } from '@/pages/RegisterPage';
import { GamesPage } from '@/pages/GamesPage';
import { GameDetailPage } from '@/pages/GameDetailPage';
import { ModerationPage } from '@/pages/ModerationPage';
import { AdminPage } from '@/pages/AdminPage';
import { ProfilePage } from '@/pages/ProfilePage';

export default function App() {
  return (
    <div className="min-h-screen flex flex-col">
      <Header />
      <main className="flex-1 mx-auto w-full max-w-6xl px-6 py-8">
        <Routes>
          <Route path="/" element={<GamesPage />} />
          <Route path="/games/:id" element={<GameDetailPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route
            path="/moderation"
            element={
              <RequireAuth role="MODERATOR">
                <ModerationPage />
              </RequireAuth>
            }
          />
          <Route
            path="/admin"
            element={
              <RequireAuth role="MODERATOR">
                <AdminPage />
              </RequireAuth>
            }
          />
          <Route
            path="/profile"
            element={
              <RequireAuth>
                <ProfilePage />
              </RequireAuth>
            }
          />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
      <footer className="border-t border-line py-6 text-center text-xs text-ink-dim">
        Avis · M2 Clean Architecture · {new Date().getFullYear()}
      </footer>
    </div>
  );
}
