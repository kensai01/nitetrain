import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBeginnerWorkout, NewBeginnerWorkout } from '../beginner-workout.model';

export type PartialUpdateBeginnerWorkout = Partial<IBeginnerWorkout> & Pick<IBeginnerWorkout, 'id'>;

export type EntityResponseType = HttpResponse<IBeginnerWorkout>;
export type EntityArrayResponseType = HttpResponse<IBeginnerWorkout[]>;

@Injectable({ providedIn: 'root' })
export class BeginnerWorkoutService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/beginner-workouts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(beginnerWorkout: NewBeginnerWorkout): Observable<EntityResponseType> {
    return this.http.post<IBeginnerWorkout>(this.resourceUrl, beginnerWorkout, { observe: 'response' });
  }

  update(beginnerWorkout: IBeginnerWorkout): Observable<EntityResponseType> {
    return this.http.put<IBeginnerWorkout>(`${this.resourceUrl}/${this.getBeginnerWorkoutIdentifier(beginnerWorkout)}`, beginnerWorkout, {
      observe: 'response',
    });
  }

  partialUpdate(beginnerWorkout: PartialUpdateBeginnerWorkout): Observable<EntityResponseType> {
    return this.http.patch<IBeginnerWorkout>(`${this.resourceUrl}/${this.getBeginnerWorkoutIdentifier(beginnerWorkout)}`, beginnerWorkout, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBeginnerWorkout>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBeginnerWorkout[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBeginnerWorkoutIdentifier(beginnerWorkout: Pick<IBeginnerWorkout, 'id'>): number {
    return beginnerWorkout.id;
  }

  compareBeginnerWorkout(o1: Pick<IBeginnerWorkout, 'id'> | null, o2: Pick<IBeginnerWorkout, 'id'> | null): boolean {
    return o1 && o2 ? this.getBeginnerWorkoutIdentifier(o1) === this.getBeginnerWorkoutIdentifier(o2) : o1 === o2;
  }

  addBeginnerWorkoutToCollectionIfMissing<Type extends Pick<IBeginnerWorkout, 'id'>>(
    beginnerWorkoutCollection: Type[],
    ...beginnerWorkoutsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const beginnerWorkouts: Type[] = beginnerWorkoutsToCheck.filter(isPresent);
    if (beginnerWorkouts.length > 0) {
      const beginnerWorkoutCollectionIdentifiers = beginnerWorkoutCollection.map(
        beginnerWorkoutItem => this.getBeginnerWorkoutIdentifier(beginnerWorkoutItem)!
      );
      const beginnerWorkoutsToAdd = beginnerWorkouts.filter(beginnerWorkoutItem => {
        const beginnerWorkoutIdentifier = this.getBeginnerWorkoutIdentifier(beginnerWorkoutItem);
        if (beginnerWorkoutCollectionIdentifiers.includes(beginnerWorkoutIdentifier)) {
          return false;
        }
        beginnerWorkoutCollectionIdentifiers.push(beginnerWorkoutIdentifier);
        return true;
      });
      return [...beginnerWorkoutsToAdd, ...beginnerWorkoutCollection];
    }
    return beginnerWorkoutCollection;
  }
}
