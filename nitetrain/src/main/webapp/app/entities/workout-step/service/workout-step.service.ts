import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorkoutStep, NewWorkoutStep } from '../workout-step.model';

export type PartialUpdateWorkoutStep = Partial<IWorkoutStep> & Pick<IWorkoutStep, 'id'>;

export type EntityResponseType = HttpResponse<IWorkoutStep>;
export type EntityArrayResponseType = HttpResponse<IWorkoutStep[]>;

@Injectable({ providedIn: 'root' })
export class WorkoutStepService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/workout-steps');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(workoutStep: NewWorkoutStep): Observable<EntityResponseType> {
    return this.http.post<IWorkoutStep>(this.resourceUrl, workoutStep, { observe: 'response' });
  }

  update(workoutStep: IWorkoutStep): Observable<EntityResponseType> {
    return this.http.put<IWorkoutStep>(`${this.resourceUrl}/${this.getWorkoutStepIdentifier(workoutStep)}`, workoutStep, {
      observe: 'response',
    });
  }

  partialUpdate(workoutStep: PartialUpdateWorkoutStep): Observable<EntityResponseType> {
    return this.http.patch<IWorkoutStep>(`${this.resourceUrl}/${this.getWorkoutStepIdentifier(workoutStep)}`, workoutStep, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorkoutStep>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkoutStep[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWorkoutStepIdentifier(workoutStep: Pick<IWorkoutStep, 'id'>): number {
    return workoutStep.id;
  }

  compareWorkoutStep(o1: Pick<IWorkoutStep, 'id'> | null, o2: Pick<IWorkoutStep, 'id'> | null): boolean {
    return o1 && o2 ? this.getWorkoutStepIdentifier(o1) === this.getWorkoutStepIdentifier(o2) : o1 === o2;
  }

  addWorkoutStepToCollectionIfMissing<Type extends Pick<IWorkoutStep, 'id'>>(
    workoutStepCollection: Type[],
    ...workoutStepsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const workoutSteps: Type[] = workoutStepsToCheck.filter(isPresent);
    if (workoutSteps.length > 0) {
      const workoutStepCollectionIdentifiers = workoutStepCollection.map(
        workoutStepItem => this.getWorkoutStepIdentifier(workoutStepItem)!
      );
      const workoutStepsToAdd = workoutSteps.filter(workoutStepItem => {
        const workoutStepIdentifier = this.getWorkoutStepIdentifier(workoutStepItem);
        if (workoutStepCollectionIdentifiers.includes(workoutStepIdentifier)) {
          return false;
        }
        workoutStepCollectionIdentifiers.push(workoutStepIdentifier);
        return true;
      });
      return [...workoutStepsToAdd, ...workoutStepCollection];
    }
    return workoutStepCollection;
  }
}
