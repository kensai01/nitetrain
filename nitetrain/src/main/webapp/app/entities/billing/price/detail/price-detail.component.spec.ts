import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PriceDetailComponent } from './price-detail.component';

describe('Price Management Detail Component', () => {
  let comp: PriceDetailComponent;
  let fixture: ComponentFixture<PriceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PriceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ price: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PriceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PriceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load price on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.price).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
