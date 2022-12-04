import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkoutStep } from '../workout-step.model';
import { WorkoutStepService } from '../service/workout-step.service';

@Injectable({ providedIn: 'root' })
export class WorkoutStepRoutingResolveService implements Resolve<IWorkoutStep | null> {
  constructor(protected service: WorkoutStepService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkoutStep | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((workoutStep: HttpResponse<IWorkoutStep>) => {
          if (workoutStep.body) {
            return of(workoutStep.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
