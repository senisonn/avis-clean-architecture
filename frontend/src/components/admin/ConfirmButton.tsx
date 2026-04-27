import { useState } from 'react';
import { Trash2 } from 'lucide-react';
import { Button } from '@/components/ui/Button';

interface ConfirmButtonProps {
  onConfirm: () => void;
  loading?: boolean;
  label?: string;
}

export function ConfirmButton({ onConfirm, loading, label = 'Delete' }: ConfirmButtonProps) {
  const [armed, setArmed] = useState(false);

  if (!armed) {
    return (
      <Button
        variant="ghost"
        size="sm"
        leftIcon={<Trash2 className="size-3.5" />}
        onClick={() => setArmed(true)}
      >
        {label}
      </Button>
    );
  }
  return (
    <div className="inline-flex items-center gap-1.5">
      <Button
        variant="danger"
        size="sm"
        loading={loading}
        onClick={() => {
          onConfirm();
          setArmed(false);
        }}
      >
        Confirm
      </Button>
      <Button variant="ghost" size="sm" onClick={() => setArmed(false)}>
        Cancel
      </Button>
    </div>
  );
}
