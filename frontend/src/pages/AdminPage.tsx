import { useState } from 'react';
import { Gamepad2, Layers, ShieldCheck, Tag, Tv } from 'lucide-react';
import {
  GenresSection,
  PlatformsSection,
  PublishersSection,
} from '@/components/admin/CatalogSections';
import { GamesSection } from '@/components/admin/GamesSection';
import { cn } from '@/lib/cn';

type Tab = 'games' | 'genres' | 'publishers' | 'platforms';

const TABS: Array<{ id: Tab; label: string; icon: React.ReactNode }> = [
  { id: 'games',      label: 'Games',      icon: <Gamepad2 className="size-4" /> },
  { id: 'genres',     label: 'Genres',     icon: <Tag className="size-4" /> },
  { id: 'publishers', label: 'Publishers', icon: <Layers className="size-4" /> },
  { id: 'platforms',  label: 'Platforms',  icon: <Tv className="size-4" /> },
];

export function AdminPage() {
  const [tab, setTab] = useState<Tab>('games');

  return (
    <div className="space-y-6">
      <header>
        <div className="flex items-center gap-2 text-xs font-medium text-brand-50">
          <ShieldCheck className="size-3.5" />
          Moderator console
        </div>
        <h1 className="mt-1 text-2xl font-semibold tracking-tight">Catalog administration</h1>
        <p className="text-sm text-ink-muted">
          Create, edit and remove games and the taxonomies that classify them.
        </p>
      </header>

      <div role="tablist" className="flex flex-wrap gap-1 rounded-xl border border-line bg-bg-surface/60 p-1">
        {TABS.map((t) => (
          <button
            key={t.id}
            role="tab"
            aria-selected={tab === t.id}
            onClick={() => setTab(t.id)}
            className={cn(
              'inline-flex items-center gap-1.5 rounded-lg px-3 py-1.5 text-sm font-medium transition-colors ring-focus',
              tab === t.id
                ? 'bg-bg-elevated text-ink shadow-sm'
                : 'text-ink-muted hover:text-ink',
            )}
          >
            {t.icon}
            {t.label}
          </button>
        ))}
      </div>

      <div role="tabpanel">
        {tab === 'games'      && <GamesSection />}
        {tab === 'genres'     && <GenresSection />}
        {tab === 'publishers' && <PublishersSection />}
        {tab === 'platforms'  && <PlatformsSection />}
      </div>
    </div>
  );
}
