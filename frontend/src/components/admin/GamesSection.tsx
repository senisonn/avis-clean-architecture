import { useEffect, useState } from 'react';
import type { FormEvent } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { Pencil, Plus } from 'lucide-react';
import { toast } from 'sonner';
import { gamesApi } from '@/api/games';
import { genresApi, publishersApi } from '@/api/catalog';
import { getApiErrorMessage } from '@/api/client';
import { Card } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input, Textarea } from '@/components/ui/Input';
import { Badge } from '@/components/ui/Badge';
import { Skeleton } from '@/components/ui/Skeleton';
import { EmptyState } from '@/components/ui/EmptyState';
import { Modal } from './Modal';
import { ConfirmButton } from './ConfirmButton';
import { SectionHeader } from './SectionHeader';
import type { CreateGameCommand, Game } from '@/types';

const PEGI_RATINGS: Array<{ id: number; label: string }> = [
  { id: 1, label: 'PEGI 3' },
  { id: 2, label: 'PEGI 7' },
  { id: 3, label: 'PEGI 12' },
  { id: 4, label: 'PEGI 16' },
  { id: 5, label: 'PEGI 18' },
];

export function GamesSection() {
  const queryClient = useQueryClient();
  const [editing, setEditing] = useState<Game | null>(null);
  const [creating, setCreating] = useState(false);

  const games = useQuery({ queryKey: ['games'], queryFn: gamesApi.list });

  const remove = useMutation({
    mutationFn: (id: number) => gamesApi.remove(id),
    onSuccess: () => {
      toast.success('Game deleted');
      queryClient.invalidateQueries({ queryKey: ['games'] });
    },
    onError: (err) => toast.error(getApiErrorMessage(err)),
  });

  return (
    <div className="space-y-4">
      <SectionHeader
        title="Games"
        description="Catalog of games visible on the public landing page."
        action={
          <Button leftIcon={<Plus className="size-4" />} onClick={() => setCreating(true)}>
            New game
          </Button>
        }
      />

      {games.isLoading && (
        <div className="space-y-2">
          {Array.from({ length: 4 }).map((_, i) => (
            <Skeleton key={i} className="h-16 w-full" />
          ))}
        </div>
      )}
      {games.error && (
        <EmptyState title="Couldn't load games" description={getApiErrorMessage(games.error)} />
      )}
      {games.data && games.data.length === 0 && !games.isLoading && (
        <EmptyState title="No games yet" description="Create the first one." />
      )}

      {games.data && games.data.length > 0 && (
        <Card>
          <ul className="divide-y divide-line">
            {games.data.map((g) => (
              <li key={g.id} className="flex items-center gap-4 px-5 py-3">
                <div className="size-12 shrink-0 overflow-hidden rounded-lg bg-bg-elevated">
                  {g.imageUrl && (
                    <img
                      src={g.imageUrl}
                      alt={g.name}
                      className="h-full w-full object-cover"
                    />
                  )}
                </div>
                <div className="min-w-0 flex-1">
                  <div className="flex items-center gap-2">
                    <span className="truncate font-medium">{g.name}</span>
                    {g.ageRating && <Badge tone="warning">{g.ageRating}</Badge>}
                  </div>
                  <div className="mt-0.5 flex flex-wrap items-center gap-x-3 gap-y-0.5 text-xs text-ink-dim">
                    {g.genre && <span>{g.genre}</span>}
                    {g.publisher && <span>· {g.publisher}</span>}
                    {typeof g.price === 'number' && (
                      <span>· {g.price === 0 ? 'Free' : `${g.price.toFixed(2)} €`}</span>
                    )}
                  </div>
                </div>
                <div className="flex items-center gap-1.5">
                  <Button
                    variant="ghost"
                    size="sm"
                    leftIcon={<Pencil className="size-3.5" />}
                    onClick={() => setEditing(g)}
                  >
                    Edit
                  </Button>
                  <ConfirmButton
                    onConfirm={() => remove.mutate(g.id)}
                    loading={remove.isPending && remove.variables === g.id}
                  />
                </div>
              </li>
            ))}
          </ul>
        </Card>
      )}

      <GameFormModal
        open={creating}
        onClose={() => setCreating(false)}
        mode="create"
      />
      <GameFormModal
        open={editing !== null}
        onClose={() => setEditing(null)}
        mode="edit"
        game={editing ?? undefined}
      />
    </div>
  );
}

interface GameFormState {
  name: string;
  description: string;
  imageUrl: string;
  price: string;
  releaseDate: string;
  genreId: string;
  publisherId: string;
  ageRatingId: string;
}

function emptyForm(): GameFormState {
  return {
    name: '',
    description: '',
    imageUrl: '',
    price: '',
    releaseDate: '',
    genreId: '',
    publisherId: '',
    ageRatingId: '',
  };
}

function gameToForm(game: Game): GameFormState {
  return {
    name: game.name,
    description: game.description ?? '',
    imageUrl: game.imageUrl ?? '',
    price: game.price?.toString() ?? '',
    releaseDate: game.releaseDate ?? '',
    genreId: '',
    publisherId: '',
    ageRatingId:
      PEGI_RATINGS.find((p) => p.label === game.ageRating)?.id.toString() ?? '',
  };
}

function GameFormModal({
  open,
  onClose,
  mode,
  game,
}: {
  open: boolean;
  onClose: () => void;
  mode: 'create' | 'edit';
  game?: Game;
}) {
  const queryClient = useQueryClient();
  const genres = useQuery({ queryKey: ['genres'], queryFn: genresApi.list, enabled: open });
  const publishers = useQuery({
    queryKey: ['publishers'],
    queryFn: publishersApi.list,
    enabled: open,
  });

  const [form, setForm] = useState<GameFormState>(() =>
    mode === 'edit' && game ? gameToForm(game) : emptyForm(),
  );

  // Reset the form when the modal opens with a different target.
  const formKey = mode === 'edit' ? `edit-${game?.id ?? 'none'}` : 'create';
  useEffect(() => {
    if (!open) return;
    setForm(mode === 'edit' && game ? gameToForm(game) : emptyForm());
    // formKey captures (mode, gameId), so this re-runs only when target changes
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [open, formKey]);

  // The Game DTO carries names, not IDs. Once the genre/publisher lists arrive,
  // back-fill the matching IDs so the dropdowns reflect the current selection.
  useEffect(() => {
    if (!open || mode !== 'edit' || !game) return;
    setForm((s) => {
      let next = s;
      if (!next.genreId && game.genre && genres.data) {
        const found = genres.data.find((g) => g.name === game.genre)?.id;
        if (found) next = { ...next, genreId: String(found) };
      }
      if (!next.publisherId && game.publisher && publishers.data) {
        const found = publishers.data.find((p) => p.name === game.publisher)?.id;
        if (found) next = { ...next, publisherId: String(found) };
      }
      return next;
    });
  }, [open, mode, game, genres.data, publishers.data]);

  const submit = useMutation({
    mutationFn: () => {
      const cmd: CreateGameCommand = {
        name: form.name.trim(),
        description: form.description.trim() || undefined,
        imageUrl: form.imageUrl.trim() || undefined,
        price: form.price ? Number(form.price) : undefined,
        releaseDate: form.releaseDate || undefined,
        genreId: form.genreId ? Number(form.genreId) : undefined,
        publisherId: form.publisherId ? Number(form.publisherId) : undefined,
        ageRatingId: form.ageRatingId ? Number(form.ageRatingId) : undefined,
      };
      return mode === 'edit' && game ? gamesApi.update(game.id, cmd) : gamesApi.create(cmd);
    },
    onSuccess: () => {
      toast.success(mode === 'edit' ? 'Game updated' : 'Game created');
      queryClient.invalidateQueries({ queryKey: ['games'] });
      onClose();
    },
    onError: (err) => toast.error(getApiErrorMessage(err)),
  });

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!form.name.trim()) {
      toast.error('Name is required.');
      return;
    }
    submit.mutate();
  };

  const update = <K extends keyof GameFormState>(key: K, value: GameFormState[K]) =>
    setForm((s) => ({ ...s, [key]: value }));

  return (
    <Modal
      open={open}
      onClose={onClose}
      title={mode === 'edit' ? 'Edit game' : 'New game'}
      description={mode === 'edit' ? game?.name : 'Add a new title to the catalog.'}
    >
      <form onSubmit={handleSubmit} className="space-y-4">
        <Input
          label="Name"
          required
          value={form.name}
          onChange={(e) => update('name', e.target.value)}
        />
        <Textarea
          label="Description"
          value={form.description}
          onChange={(e) => update('description', e.target.value)}
        />
        <div className="grid grid-cols-2 gap-3">
          <Input
            label="Image URL"
            value={form.imageUrl}
            onChange={(e) => update('imageUrl', e.target.value)}
            placeholder="https://…"
          />
          <Input
            label="Price (€)"
            type="number"
            min={0}
            step="0.01"
            value={form.price}
            onChange={(e) => update('price', e.target.value)}
          />
        </div>
        <div className="grid grid-cols-2 gap-3">
          <Input
            label="Release date"
            type="date"
            value={form.releaseDate}
            onChange={(e) => update('releaseDate', e.target.value)}
          />
          <Select
            label="Age rating"
            value={form.ageRatingId}
            onChange={(v) => update('ageRatingId', v)}
            options={PEGI_RATINGS.map((p) => ({ value: String(p.id), label: p.label }))}
          />
        </div>
        <div className="grid grid-cols-2 gap-3">
          <Select
            label="Genre"
            value={form.genreId}
            onChange={(v) => update('genreId', v)}
            options={(genres.data ?? []).map((g) => ({ value: String(g.id), label: g.name }))}
            loading={genres.isLoading}
          />
          <Select
            label="Publisher"
            value={form.publisherId}
            onChange={(v) => update('publisherId', v)}
            options={(publishers.data ?? []).map((p) => ({ value: String(p.id), label: p.name }))}
            loading={publishers.isLoading}
          />
        </div>
        <div className="flex justify-end gap-2 pt-2">
          <Button type="button" variant="ghost" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" loading={submit.isPending}>
            {mode === 'edit' ? 'Save changes' : 'Create game'}
          </Button>
        </div>
      </form>
    </Modal>
  );
}

function Select({
  label,
  value,
  onChange,
  options,
  loading,
}: {
  label: string;
  value: string;
  onChange: (v: string) => void;
  options: Array<{ value: string; label: string }>;
  loading?: boolean;
}) {
  return (
    <div className="space-y-1.5">
      <label className="text-sm font-medium text-ink-muted">{label}</label>
      <select
        value={value}
        onChange={(e) => onChange(e.target.value)}
        disabled={loading}
        className="h-10 w-full rounded-xl border border-line bg-bg-elevated px-3 text-sm text-ink ring-focus disabled:opacity-50"
      >
        <option value="">— None —</option>
        {options.map((o) => (
          <option key={o.value} value={o.value}>
            {o.label}
          </option>
        ))}
      </select>
    </div>
  );
}
