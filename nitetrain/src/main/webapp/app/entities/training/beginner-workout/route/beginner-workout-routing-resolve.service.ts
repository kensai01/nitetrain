import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBeginnerWorkout } from '../beginner-workout.model';
import { BeginnerWorkoutService } from '../service/beginner-workout.service';

@Injectable({ providedIn: 'root' })
export class BeginnerWorkoutRoutingResolveService implements Resolve<IBeginnerWorkout | null> {
  constructor(protected service: BeginnerWorkoutService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBeginnerWorkout | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((beginnerWorkout: HttpResponse<IBeginnerWorkout>) => {
          if (beginnerWorkout.body) {
            return of(beginnerWorkout.body);
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
