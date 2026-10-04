import type { Task } from '../types/task';
import TaskCard from './TaskCard';

interface TaskListProps {
  tasks: Task[];
  onStatusChange: (id: string, status: Task['status']) => void;
  onEdit: (task: Task) => void;
  onDelete: (id: string) => void;
}

const columns: { key: Task['status']; label: string }[] = [
  { key: 'TODO', label: 'To do' },
  { key: 'IN_PROGRESS', label: 'In progress' },
  { key: 'DONE', label: 'Done' },
];

export default function TaskList({ tasks, onStatusChange, onEdit, onDelete }: TaskListProps) {
  return (
    <div className="board">
      {columns.map(col => {
        const colTasks = tasks.filter(t => t.status === col.key);
        return (
          <div key={col.key}>
            <div className="board__column-header">
              <span className="board__column-title">{col.label}</span>
              <span className="board__column-count">{colTasks.length}</span>
            </div>
            {colTasks.length === 0 && <p className="empty-column">Nothing here.</p>}
            {colTasks.map(task => (
              <TaskCard key={task.id} task={task} onStatusChange={onStatusChange} onEdit={onEdit} onDelete={onDelete} />
            ))}
          </div>
        );
      })}
    </div>
  );
}