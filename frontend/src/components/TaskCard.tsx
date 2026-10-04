import type { Task } from '../types/task';
import { useAuth } from '../context/AuthContext';

interface TaskCardProps {
  task: Task;
  onStatusChange: (id: string, status: Task['status']) => void;
  onEdit: (task: Task) => void;
  onDelete: (id: string) => void;
}

const statusClass: Record<Task['status'], string> = {
  TODO: '',
  IN_PROGRESS: 'task-card--progress',
  DONE: 'task-card--done',
};

export default function TaskCard({ task, onStatusChange, onEdit, onDelete }: TaskCardProps) {
  const { user } = useAuth();
  const isAdmin = user?.role === 'ADMIN';
  const isOwner = user?.id === task.ownerId;

  return (
    <div className={`task-card ${statusClass[task.status]}`}>
      <div className="task-card__title">{task.title}</div>
      {task.description && <div className="task-card__desc">{task.description}</div>}
      <div className="task-card__footer">
        {(isAdmin || isOwner) ? (
          <select
            className="status-select"
            value={task.status}
            onChange={e => onStatusChange(task.id, e.target.value as Task['status'])}
          >
            <option value="TODO">To do</option>
            <option value="IN_PROGRESS">In progress</option>
            <option value="DONE">Done</option>
          </select>
        ) : (
          <span className="task-card__status-text">{task.status.replace('_', ' ')}</span>
        )}
        {isAdmin && (
          <div className="task-card__actions">
            <button className="btn--text" onClick={() => onEdit(task)}>Edit</button>
            <button className="btn--text" style={{ color: 'var(--danger)' }} onClick={() => onDelete(task.id)}>Delete</button>
          </div>
        )}
      </div>
    </div>
  );
}