import { useState } from 'react';
import type { FormEvent, ReactNode } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { Plus } from 'lucide-react';
import { toast } from 'sonner';
import { genresApi, platformsApi, publishersApi } from '@/api/catalog';
import { getApiErrorMessage } from '@/api/client';
import { Card, CardBody } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { Skeleton } from '@/components/ui/Skeleton';
import { EmptyState } from '@/components/ui/EmptyState';
import { ConfirmButton } from './ConfirmButton';
import { SectionHeader } from './SectionHeader';

export function GenresSection() {
  const queryClient = useQueryClient();
  const list = useQuery({ queryKey: ['genres'], queryFn: genresApi.list });
  const [name, setName] = useState('');

  const create = useMutation({
    mutationFn: () => genresApi.create(name.trim()),
    onSuccess: () => {
      toast.success('Genre created');
      setName('');
      queryClient.invalidateQueries({ queryKey: ['genres'] });
    },
    onError: (err) => toast.error(getApiErrorMessage(err)),
  });

  const remove = useMutation({
    mutationFn: (id: number) => genresApi.remove(id),
    onSuccess: () => {
      toast.success('Genre deleted');
      queryClient.invalidateQueries({ queryKey: ['genres'] });
    },
    onError: (err) => toast.error(getApiErrorMessage(err)),
  });

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!name.trim()) return;
    create.mutate();
  };

  return (
    <SimpleCatalog
      title="Genres"
      description="Tag games by genre. Used in search filters."
      form={
        <form onSubmit={handleSubmit} className="flex items-end gap-2">
          <Input
            label="New genre"
            placeholder="e.g. Roguelike"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="min-w-64"
          />
          <Button
            type="submit"
            leftIcon={<Plus className="size-4" />}
            loading={create.isPending}
            disabled={!name.trim()}
          >
            Add
          </Button>
        </form>
      }
      isLoading={list.isLoading}
      error={list.error}
      items={list.data?.map((g) => ({
        key: g.id,
        primary: g.name,
        onDelete: () => remove.mutate(g.id),
        deleting: remove.isPending && remove.variables === g.id,
      }))}
    />
  );
}

export function PublishersSection() {
  const queryClient = useQueryClient();
  const list = useQuery({ queryKey: ['publishers'], queryFn: publishersApi.list });
  const [name, setName] = useState('');

  const create = useMutation({
    mutationFn: () => publishersApi.create(name.trim()),
    onSuccess: () => {
      toast.success('Publisher created');
      setName('');
      queryClient.invalidateQueries({ queryKey: ['publishers'] });
    },
    onError: (err) => toast.error(getApiErrorMessage(err)),
  });

  const remove = useMutation({
    mutationFn: (id: number) => publishersApi.remove(id),
    onSuccess: () => {
      toast.success('Publisher deleted');
      queryClient.invalidateQueries({ queryKey: ['publishers'] });
    },
    onError: (err) => toast.error(getApiErrorMessage(err)),
  });

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!name.trim()) return;
    create.mutate();
  };

  return (
    <SimpleCatalog
      title="Publishers"
      description="Studios or labels that release games."
      form={
        <form onSubmit={handleSubmit} className="flex items-end gap-2">
          <Input
            label="New publisher"
            placeholder="e.g. Annapurna Interactive"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="min-w-64"
          />
          <Button
            type="submit"
            leftIcon={<Plus className="size-4" />}
            loading={create.isPending}
            disabled={!name.trim()}
          >
            Add
          </Button>
        </form>
      }
      isLoading={list.isLoading}
      error={list.error}
      items={list.data?.map((p) => ({
        key: p.id,
        primary: p.name,
        onDelete: () => remove.mutate(p.id),
        deleting: remove.isPending && remove.variables === p.id,
      }))}
    />
  );
}

export function PlatformsSection() {
  const queryClient = useQueryClient();
  const list = useQuery({ queryKey: ['platforms'], queryFn: platformsApi.list });
  const [name, setName] = useState('');
  const [releaseDate, setReleaseDate] = useState('');

  const create = useMutation({
    mutationFn: () => platformsApi.create(name.trim(), releaseDate || undefined),
    onSuccess: () => {
      toast.success('Platform created');
      setName('');
      setReleaseDate('');
      queryClient.invalidateQueries({ queryKey: ['platforms'] });
    },
    onError: (err) => toast.error(getApiErrorMessage(err)),
  });

  const remove = useMutation({
    mutationFn: (id: number) => platformsApi.remove(id),
    onSuccess: () => {
      toast.success('Platform deleted');
      queryClient.invalidateQueries({ queryKey: ['platforms'] });
    },
    onError: (err) => toast.error(getApiErrorMessage(err)),
  });

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!name.trim()) return;
    create.mutate();
  };

  return (
    <SimpleCatalog
      title="Platforms"
      description="Hardware or storefronts where games run."
      form={
        <form onSubmit={handleSubmit} className="flex flex-wrap items-end gap-2">
          <Input
            label="New platform"
            placeholder="e.g. Steam Deck"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="min-w-56"
          />
          <Input
            label="Release date"
            type="date"
            value={releaseDate}
            onChange={(e) => setReleaseDate(e.target.value)}
          />
          <Button
            type="submit"
            leftIcon={<Plus className="size-4" />}
            loading={create.isPending}
            disabled={!name.trim()}
          >
            Add
          </Button>
        </form>
      }
      isLoading={list.isLoading}
      error={list.error}
      items={list.data?.map((p) => ({
        key: p.id,
        primary: p.name,
        secondary: p.releaseDate ? new Date(p.releaseDate).toLocaleDateString() : undefined,
        onDelete: () => remove.mutate(p.id),
        deleting: remove.isPending && remove.variables === p.id,
      }))}
    />
  );
}

interface CatalogItem {
  key: number;
  primary: string;
  secondary?: string;
  onDelete: () => void;
  deleting: boolean;
}

interface SimpleCatalogProps {
  title: string;
  description: string;
  form: ReactNode;
  isLoading: boolean;
  error: unknown;
  items: CatalogItem[] | undefined;
}

function SimpleCatalog({ title, description, form, isLoading, error, items }: SimpleCatalogProps) {
  return (
    <div className="space-y-4">
      <SectionHeader title={title} description={description} />
      <Card>
        <CardBody>{form}</CardBody>
      </Card>

      {isLoading && (
        <div className="space-y-2">
          {Array.from({ length: 4 }).map((_, i) => (
            <Skeleton key={i} className="h-12 w-full" />
          ))}
        </div>
      )}
      {error != null && <EmptyState title="Couldn't load" description={getApiErrorMessage(error)} />}
      {items && items.length === 0 && !isLoading && (
        <EmptyState title={`No ${title.toLowerCase()} yet`} />
      )}
      {items && items.length > 0 && (
        <Card>
          <ul className="divide-y divide-line">
            {items.map((it) => (
              <li
                key={it.key}
                className="flex items-center justify-between gap-3 px-5 py-3"
              >
                <div className="min-w-0">
                  <div className="truncate text-sm font-medium">{it.primary}</div>
                  {it.secondary && (
                    <div className="text-xs text-ink-dim">{it.secondary}</div>
                  )}
                </div>
                <ConfirmButton onConfirm={it.onDelete} loading={it.deleting} />
              </li>
            ))}
          </ul>
        </Card>
      )}
    </div>
  );
}
