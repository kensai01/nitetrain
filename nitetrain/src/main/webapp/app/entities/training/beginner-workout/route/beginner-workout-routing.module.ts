import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BeginnerWorkoutComponent } from '../list/beginner-workout.component';
import { BeginnerWorkoutDetailComponent } from '../detail/beginner-workout-detail.component';
import { BeginnerWorkoutUpdateComponent } from '../update/beginner-workout-update.component';
import { BeginnerWorkoutRoutingResolveService } from './beginner-workout-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const beginnerWorkoutRoute: Routes = [
  {
    path: '',
    component: BeginnerWorkoutComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BeginnerWorkoutDetailComponent,
    resolve: {
      beginnerWorkout: BeginnerWorkoutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BeginnerWorkoutUpdateComponent,
    resolve: {
      beginnerWorkout: BeginnerWorkoutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BeginnerWorkoutUpdateComponent,
    resolve: {
      beginnerWorkout: BeginnerWorkoutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(beginnerWorkoutRoute)],
  exports: [RouterModule],
})
export class BeginnerWorkoutRoutingModule {}
