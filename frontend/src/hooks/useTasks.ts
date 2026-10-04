import { useState, useEffect, useCallback } from 'react';
import { taskService } from '../services/taskService';
import type { Task, TaskRequest } from '../types/task';
import { useAuth } from '../context/AuthContext';

export function useTasks() {
  const { user } = useAuth();
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchTasks = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = user?.role === 'ADMIN'
        ? await taskService.getAllTasks()
        : await taskService.getMyTasks();
      setTasks(data);
    } catch {
      setError('Failed to load tasks. Please try again.');
    } finally {
      setLoading(false);
    }
  }, [user]);

  useEffect(() => {
    fetchTasks();
  }, [fetchTasks]);

  const createTask = async (task: TaskRequest) => {
    await taskService.createTask(task);
    await fetchTasks();
  };

  const updateTask = async (id: string, task: TaskRequest) => {
    await taskService.updateTask(id, task);
    await fetchTasks();
  };

  const deleteTask = async (id: string) => {
    await taskService.deleteTask(id);
    await fetchTasks();
  };

  return { tasks, loading, error, createTask, updateTask, deleteTask, refetch: fetchTasks };
}