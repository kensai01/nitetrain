import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IntermediateWorkoutComponent } from '../list/intermediate-workout.component';
import { IntermediateWorkoutDetailComponent } from '../detail/intermediate-workout-detail.component';
import { IntermediateWorkoutUpdateComponent } from '../update/intermediate-workout-update.component';
import { IntermediateWorkoutRoutingResolveService } from './intermediate-workout-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const intermediateWorkoutRoute: Routes = [
  {
    path: '',
    component: IntermediateWorkoutComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IntermediateWorkoutDetailComponent,
    resolve: {
      intermediateWorkout: IntermediateWorkoutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IntermediateWorkoutUpdateComponent,
    resolve: {
      intermediateWorkout: IntermediateWorkoutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IntermediateWorkoutUpdateComponent,
    resolve: {
      intermediateWorkout: IntermediateWorkoutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(intermediateWorkoutRoute)],
  exports: [RouterModule],
})
export class IntermediateWorkoutRoutingModule {}
