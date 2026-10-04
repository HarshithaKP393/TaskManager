import apiClient from './apiClient';
import type { Task, TaskRequest } from '../types/task';

export const taskService = {
  getAllTasks: () => apiClient.get<Task[]>('/tasks').then(res => res.data),

  getMyTasks: () => apiClient.get<Task[]>('/tasks/my').then(res => res.data),

  getTaskById: (id: string) => apiClient.get<Task>(`/tasks/${id}`).then(res => res.data),

  createTask: (task: TaskRequest) => apiClient.post<Task>('/tasks', task).then(res => res.data),

  updateTask: (id: string, task: TaskRequest) =>
    apiClient.put<Task>(`/tasks/${id}`, task).then(res => res.data),

  deleteTask: (id: string) => apiClient.delete(`/tasks/${id}`),
};