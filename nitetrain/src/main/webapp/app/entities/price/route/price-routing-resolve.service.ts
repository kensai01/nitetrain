import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrice } from '../price.model';
import { PriceService } from '../service/price.service';

@Injectable({ providedIn: 'root' })
export class PriceRoutingResolveService implements Resolve<IPrice | null> {
  constructor(protected service: PriceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrice | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((price: HttpResponse<IPrice>) => {
          if (price.body) {
            return of(price.body);
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
