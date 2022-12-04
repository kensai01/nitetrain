import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PriceComponent } from '../list/price.component';
import { PriceDetailComponent } from '../detail/price-detail.component';
import { PriceUpdateComponent } from '../update/price-update.component';
import { PriceRoutingResolveService } from './price-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const priceRoute: Routes = [
  {
    path: '',
    component: PriceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PriceDetailComponent,
    resolve: {
      price: PriceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PriceUpdateComponent,
    resolve: {
      price: PriceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PriceUpdateComponent,
    resolve: {
      price: PriceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(priceRoute)],
  exports: [RouterModule],
})
export class PriceRoutingModule {}
