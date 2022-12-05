import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIntermediateWorkout } from '../intermediate-workout.model';
import { IntermediateWorkoutService } from '../service/intermediate-workout.service';

@Injectable({ providedIn: 'root' })
export class IntermediateWorkoutRoutingResolveService implements Resolve<IIntermediateWorkout | null> {
  constructor(protected service: IntermediateWorkoutService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIntermediateWorkout | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((intermediateWorkout: HttpResponse<IIntermediateWorkout>) => {
          if (intermediateWorkout.body) {
            return of(intermediateWorkout.body);
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
