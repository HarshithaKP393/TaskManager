import { useState, type FormEvent } from 'react';
import type { Task, TaskRequest } from '../types/task';

interface TaskFormProps {
  initialTask?: Task;
  onSubmit: (task: TaskRequest) => Promise<void>;
  onCancel: () => void;
}

export default function TaskForm({ initialTask, onSubmit, onCancel }: TaskFormProps) {
  const [title, setTitle] = useState(initialTask?.title ?? '');
  const [description, setDescription] = useState(initialTask?.description ?? '');
  const [ownerId, setOwnerId] = useState(initialTask?.ownerId ?? '');
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      await onSubmit({ title, description, ownerId: ownerId || undefined });
      onCancel();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to save task');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="task-panel">
      <form onSubmit={handleSubmit}>
        <div className="field">
          <label>Title</label>
          <input value={title} onChange={e => setTitle(e.target.value)} required />
        </div>
        <div className="field">
          <label>Description</label>
          <textarea rows={2} value={description} onChange={e => setDescription(e.target.value)} />
        </div>
        <div className="field">
          <label>Assign to (user UUID)</label>
          <input value={ownerId} onChange={e => setOwnerId(e.target.value)} placeholder="Optional — defaults to you" />
        </div>
        {error && <p className="form-error">{error}</p>}
        <div className="form-actions">
          <button className="btn btn--primary" type="submit" disabled={submitting}>
            {submitting ? 'Saving...' : 'Save'}
          </button>
          <button className="btn" type="button" onClick={onCancel}>Cancel</button>
        </div>
      </form>
    </div>
  );
}