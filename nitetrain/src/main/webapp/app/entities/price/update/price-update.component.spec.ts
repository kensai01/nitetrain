import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PriceFormService } from './price-form.service';
import { PriceService } from '../service/price.service';
import { IPrice } from '../price.model';

import { PriceUpdateComponent } from './price-update.component';

describe('Price Management Update Component', () => {
  let comp: PriceUpdateComponent;
  let fixture: ComponentFixture<PriceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let priceFormService: PriceFormService;
  let priceService: PriceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PriceUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PriceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PriceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    priceFormService = TestBed.inject(PriceFormService);
    priceService = TestBed.inject(PriceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const price: IPrice = { id: 456 };

      activatedRoute.data = of({ price });
      comp.ngOnInit();

      expect(comp.price).toEqual(price);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrice>>();
      const price = { id: 123 };
      jest.spyOn(priceFormService, 'getPrice').mockReturnValue(price);
      jest.spyOn(priceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ price });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: price }));
      saveSubject.complete();

      // THEN
      expect(priceFormService.getPrice).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(priceService.update).toHaveBeenCalledWith(expect.objectContaining(price));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrice>>();
      const price = { id: 123 };
      jest.spyOn(priceFormService, 'getPrice').mockReturnValue({ id: null });
      jest.spyOn(priceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ price: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: price }));
      saveSubject.complete();

      // THEN
      expect(priceFormService.getPrice).toHaveBeenCalled();
      expect(priceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrice>>();
      const price = { id: 123 };
      jest.spyOn(priceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ price });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(priceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
