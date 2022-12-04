import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PriceComponent } from './list/price.component';
import { PriceDetailComponent } from './detail/price-detail.component';
import { PriceUpdateComponent } from './update/price-update.component';
import { PriceDeleteDialogComponent } from './delete/price-delete-dialog.component';
import { PriceRoutingModule } from './route/price-routing.module';

@NgModule({
  imports: [SharedModule, PriceRoutingModule],
  declarations: [PriceComponent, PriceDetailComponent, PriceUpdateComponent, PriceDeleteDialogComponent],
})
export class PriceModule {}
