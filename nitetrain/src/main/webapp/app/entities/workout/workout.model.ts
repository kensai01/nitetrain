export interface IWorkout {
  id: number;
  title?: string | null;
  description?: string | null;
  time?: number | null;
  videoId?: string | null;
  scaling?: string | null;
}

export type NewWorkout = Omit<IWorkout, 'id'> & { id: null };
