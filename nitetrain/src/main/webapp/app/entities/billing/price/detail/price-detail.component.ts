import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrice } from '../price.model';

@Component({
  selector: 'jhi-price-detail',
  templateUrl: './price-detail.component.html',
})
export class PriceDetailComponent implements OnInit {
  price: IPrice | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ price }) => {
      this.price = price;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
