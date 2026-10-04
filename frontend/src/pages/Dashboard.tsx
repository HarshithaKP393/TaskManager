import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useTasks } from '../hooks/useTasks';
import TaskList from '../components/TaskList';
import TaskForm from '../components/TaskForm';
import LogoutButton from '../components/LogoutButton';
import type { Task, TaskRequest } from '../types/task';

export default function Dashboard() {
  const { user } = useAuth();
  const { tasks, loading, error, createTask, updateTask, deleteTask } = useTasks();
  const [showForm, setShowForm] = useState(false);
  const [editingTask, setEditingTask] = useState<Task | null>(null);

  const handleStatusChange = (id: string, status: Task['status']) => updateTask(id, { status });
  const handleEdit = (task: Task) => { setEditingTask(task); setShowForm(true); };
  const handleDelete = async (id: string) => { if (confirm('Delete this task?')) await deleteTask(id); };
  const handleFormSubmit = async (taskData: TaskRequest) => {
    if (editingTask) await updateTask(editingTask.id, taskData);
    else await createTask(taskData);
  };

  return (
    <div>
      <div className="topbar">
        <span className="topbar__brand">Task Manager</span>
        <div className="topbar__user">
          <span>{user?.email} · {user?.role}</span>
          <LogoutButton />
        </div>
      </div>

      <div className="page">
        <div className="page__header">
          <h1 className="page__title">Tasks</h1>
          {user?.role === 'ADMIN' && !showForm && (
            <button className="btn btn--primary" onClick={() => { setEditingTask(null); setShowForm(true); }}>
              New task
            </button>
          )}
        </div>

        {showForm && (
          <TaskForm
            initialTask={editingTask ?? undefined}
            onSubmit={handleFormSubmit}
            onCancel={() => { setShowForm(false); setEditingTask(null); }}
          />
        )}

        {loading && <p>Loading tasks...</p>}
        {error && <p className="form-error">{error}</p>}
        {!loading && !error && (
          <TaskList tasks={tasks} onStatusChange={handleStatusChange} onEdit={handleEdit} onDelete={handleDelete} />
        )}
      </div>
    </div>
  );
}