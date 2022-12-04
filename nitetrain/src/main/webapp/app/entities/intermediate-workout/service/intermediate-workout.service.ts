import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIntermediateWorkout, NewIntermediateWorkout } from '../intermediate-workout.model';

export type PartialUpdateIntermediateWorkout = Partial<IIntermediateWorkout> & Pick<IIntermediateWorkout, 'id'>;

export type EntityResponseType = HttpResponse<IIntermediateWorkout>;
export type EntityArrayResponseType = HttpResponse<IIntermediateWorkout[]>;

@Injectable({ providedIn: 'root' })
export class IntermediateWorkoutService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/intermediate-workouts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(intermediateWorkout: NewIntermediateWorkout): Observable<EntityResponseType> {
    return this.http.post<IIntermediateWorkout>(this.resourceUrl, intermediateWorkout, { observe: 'response' });
  }

  update(intermediateWorkout: IIntermediateWorkout): Observable<EntityResponseType> {
    return this.http.put<IIntermediateWorkout>(
      `${this.resourceUrl}/${this.getIntermediateWorkoutIdentifier(intermediateWorkout)}`,
      intermediateWorkout,
      { observe: 'response' }
    );
  }

  partialUpdate(intermediateWorkout: PartialUpdateIntermediateWorkout): Observable<EntityResponseType> {
    return this.http.patch<IIntermediateWorkout>(
      `${this.resourceUrl}/${this.getIntermediateWorkoutIdentifier(intermediateWorkout)}`,
      intermediateWorkout,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIntermediateWorkout>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIntermediateWorkout[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIntermediateWorkoutIdentifier(intermediateWorkout: Pick<IIntermediateWorkout, 'id'>): number {
    return intermediateWorkout.id;
  }

  compareIntermediateWorkout(o1: Pick<IIntermediateWorkout, 'id'> | null, o2: Pick<IIntermediateWorkout, 'id'> | null): boolean {
    return o1 && o2 ? this.getIntermediateWorkoutIdentifier(o1) === this.getIntermediateWorkoutIdentifier(o2) : o1 === o2;
  }

  addIntermediateWorkoutToCollectionIfMissing<Type extends Pick<IIntermediateWorkout, 'id'>>(
    intermediateWorkoutCollection: Type[],
    ...intermediateWorkoutsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const intermediateWorkouts: Type[] = intermediateWorkoutsToCheck.filter(isPresent);
    if (intermediateWorkouts.length > 0) {
      const intermediateWorkoutCollectionIdentifiers = intermediateWorkoutCollection.map(
        intermediateWorkoutItem => this.getIntermediateWorkoutIdentifier(intermediateWorkoutItem)!
      );
      const intermediateWorkoutsToAdd = intermediateWorkouts.filter(intermediateWorkoutItem => {
        const intermediateWorkoutIdentifier = this.getIntermediateWorkoutIdentifier(intermediateWorkoutItem);
        if (intermediateWorkoutCollectionIdentifiers.includes(intermediateWorkoutIdentifier)) {
          return false;
        }
        intermediateWorkoutCollectionIdentifiers.push(intermediateWorkoutIdentifier);
        return true;
      });
      return [...intermediateWorkoutsToAdd, ...intermediateWorkoutCollection];
    }
    return intermediateWorkoutCollection;
  }
}
