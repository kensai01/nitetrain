import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IntermediateWorkoutFormService } from './intermediate-workout-form.service';
import { IntermediateWorkoutService } from '../service/intermediate-workout.service';
import { IIntermediateWorkout } from '../intermediate-workout.model';
import { IWorkout } from 'app/entities/workout/workout.model';
import { WorkoutService } from 'app/entities/workout/service/workout.service';

import { IntermediateWorkoutUpdateComponent } from './intermediate-workout-update.component';

describe('IntermediateWorkout Management Update Component', () => {
  let comp: IntermediateWorkoutUpdateComponent;
  let fixture: ComponentFixture<IntermediateWorkoutUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let intermediateWorkoutFormService: IntermediateWorkoutFormService;
  let intermediateWorkoutService: IntermediateWorkoutService;
  let workoutService: WorkoutService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IntermediateWorkoutUpdateComponent],
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
      .overrideTemplate(IntermediateWorkoutUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IntermediateWorkoutUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    intermediateWorkoutFormService = TestBed.inject(IntermediateWorkoutFormService);
    intermediateWorkoutService = TestBed.inject(IntermediateWorkoutService);
    workoutService = TestBed.inject(WorkoutService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call workout query and add missing value', () => {
      const intermediateWorkout: IIntermediateWorkout = { id: 456 };
      const workout: IWorkout = { id: 88292 };
      intermediateWorkout.workout = workout;

      const workoutCollection: IWorkout[] = [{ id: 86972 }];
      jest.spyOn(workoutService, 'query').mockReturnValue(of(new HttpResponse({ body: workoutCollection })));
      const expectedCollection: IWorkout[] = [workout, ...workoutCollection];
      jest.spyOn(workoutService, 'addWorkoutToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ intermediateWorkout });
      comp.ngOnInit();

      expect(workoutService.query).toHaveBeenCalled();
      expect(workoutService.addWorkoutToCollectionIfMissing).toHaveBeenCalledWith(workoutCollection, workout);
      expect(comp.workoutsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const intermediateWorkout: IIntermediateWorkout = { id: 456 };
      const workout: IWorkout = { id: 4840 };
      intermediateWorkout.workout = workout;

      activatedRoute.data = of({ intermediateWorkout });
      comp.ngOnInit();

      expect(comp.workoutsCollection).toContain(workout);
      expect(comp.intermediateWorkout).toEqual(intermediateWorkout);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIntermediateWorkout>>();
      const intermediateWorkout = { id: 123 };
      jest.spyOn(intermediateWorkoutFormService, 'getIntermediateWorkout').mockReturnValue(intermediateWorkout);
      jest.spyOn(intermediateWorkoutService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ intermediateWorkout });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: intermediateWorkout }));
      saveSubject.complete();

      // THEN
      expect(intermediateWorkoutFormService.getIntermediateWorkout).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(intermediateWorkoutService.update).toHaveBeenCalledWith(expect.objectContaining(intermediateWorkout));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIntermediateWorkout>>();
      const intermediateWorkout = { id: 123 };
      jest.spyOn(intermediateWorkoutFormService, 'getIntermediateWorkout').mockReturnValue({ id: null });
      jest.spyOn(intermediateWorkoutService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ intermediateWorkout: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: intermediateWorkout }));
      saveSubject.complete();

      // THEN
      expect(intermediateWorkoutFormService.getIntermediateWorkout).toHaveBeenCalled();
      expect(intermediateWorkoutService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIntermediateWorkout>>();
      const intermediateWorkout = { id: 123 };
      jest.spyOn(intermediateWorkoutService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ intermediateWorkout });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(intermediateWorkoutService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareWorkout', () => {
      it('Should forward to workoutService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(workoutService, 'compareWorkout');
        comp.compareWorkout(entity, entity2);
        expect(workoutService.compareWorkout).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
