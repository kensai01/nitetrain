export interface IPrice {
  id: number;
  name?: string | null;
  location?: string | null;
  description?: string | null;
  cost?: number | null;
}

export type NewPrice = Omit<IPrice, 'id'> & { id: null };
