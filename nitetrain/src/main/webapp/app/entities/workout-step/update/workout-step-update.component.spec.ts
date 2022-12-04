import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WorkoutStepFormService } from './workout-step-form.service';
import { WorkoutStepService } from '../service/workout-step.service';
import { IWorkoutStep } from '../workout-step.model';
import { IWorkout } from 'app/entities/workout/workout.model';
import { WorkoutService } from 'app/entities/workout/service/workout.service';
import { IBeginnerWorkout } from 'app/entities/beginner-workout/beginner-workout.model';
import { BeginnerWorkoutService } from 'app/entities/beginner-workout/service/beginner-workout.service';
import { IIntermediateWorkout } from 'app/entities/intermediate-workout/intermediate-workout.model';
import { IntermediateWorkoutService } from 'app/entities/intermediate-workout/service/intermediate-workout.service';

import { WorkoutStepUpdateComponent } from './workout-step-update.component';

describe('WorkoutStep Management Update Component', () => {
  let comp: WorkoutStepUpdateComponent;
  let fixture: ComponentFixture<WorkoutStepUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workoutStepFormService: WorkoutStepFormService;
  let workoutStepService: WorkoutStepService;
  let workoutService: WorkoutService;
  let beginnerWorkoutService: BeginnerWorkoutService;
  let intermediateWorkoutService: IntermediateWorkoutService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WorkoutStepUpdateComponent],
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
      .overrideTemplate(WorkoutStepUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkoutStepUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workoutStepFormService = TestBed.inject(WorkoutStepFormService);
    workoutStepService = TestBed.inject(WorkoutStepService);
    workoutService = TestBed.inject(WorkoutService);
    beginnerWorkoutService = TestBed.inject(BeginnerWorkoutService);
    intermediateWorkoutService = TestBed.inject(IntermediateWorkoutService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Workout query and add missing value', () => {
      const workoutStep: IWorkoutStep = { id: 456 };
      const workout: IWorkout = { id: 42153 };
      workoutStep.workout = workout;

      const workoutCollection: IWorkout[] = [{ id: 53288 }];
      jest.spyOn(workoutService, 'query').mockReturnValue(of(new HttpResponse({ body: workoutCollection })));
      const additionalWorkouts = [workout];
      const expectedCollection: IWorkout[] = [...additionalWorkouts, ...workoutCollection];
      jest.spyOn(workoutService, 'addWorkoutToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workoutStep });
      comp.ngOnInit();

      expect(workoutService.query).toHaveBeenCalled();
      expect(workoutService.addWorkoutToCollectionIfMissing).toHaveBeenCalledWith(
        workoutCollection,
        ...additionalWorkouts.map(expect.objectContaining)
      );
      expect(comp.workoutsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BeginnerWorkout query and add missing value', () => {
      const workoutStep: IWorkoutStep = { id: 456 };
      const beginnerWorkout: IBeginnerWorkout = { id: 72059 };
      workoutStep.beginnerWorkout = beginnerWorkout;

      const beginnerWorkoutCollection: IBeginnerWorkout[] = [{ id: 5323 }];
      jest.spyOn(beginnerWorkoutService, 'query').mockReturnValue(of(new HttpResponse({ body: beginnerWorkoutCollection })));
      const additionalBeginnerWorkouts = [beginnerWorkout];
      const expectedCollection: IBeginnerWorkout[] = [...additionalBeginnerWorkouts, ...beginnerWorkoutCollection];
      jest.spyOn(beginnerWorkoutService, 'addBeginnerWorkoutToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workoutStep });
      comp.ngOnInit();

      expect(beginnerWorkoutService.query).toHaveBeenCalled();
      expect(beginnerWorkoutService.addBeginnerWorkoutToCollectionIfMissing).toHaveBeenCalledWith(
        beginnerWorkoutCollection,
        ...additionalBeginnerWorkouts.map(expect.objectContaining)
      );
      expect(comp.beginnerWorkoutsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call IntermediateWorkout query and add missing value', () => {
      const workoutStep: IWorkoutStep = { id: 456 };
      const intermediateWorkout: IIntermediateWorkout = { id: 70735 };
      workoutStep.intermediateWorkout = intermediateWorkout;

      const intermediateWorkoutCollection: IIntermediateWorkout[] = [{ id: 13439 }];
      jest.spyOn(intermediateWorkoutService, 'query').mockReturnValue(of(new HttpResponse({ body: intermediateWorkoutCollection })));
      const additionalIntermediateWorkouts = [intermediateWorkout];
      const expectedCollection: IIntermediateWorkout[] = [...additionalIntermediateWorkouts, ...intermediateWorkoutCollection];
      jest.spyOn(intermediateWorkoutService, 'addIntermediateWorkoutToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workoutStep });
      comp.ngOnInit();

      expect(intermediateWorkoutService.query).toHaveBeenCalled();
      expect(intermediateWorkoutService.addIntermediateWorkoutToCollectionIfMissing).toHaveBeenCalledWith(
        intermediateWorkoutCollection,
        ...additionalIntermediateWorkouts.map(expect.objectContaining)
      );
      expect(comp.intermediateWorkoutsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const workoutStep: IWorkoutStep = { id: 456 };
      const workout: IWorkout = { id: 90727 };
      workoutStep.workout = workout;
      const beginnerWorkout: IBeginnerWorkout = { id: 42837 };
      workoutStep.beginnerWorkout = beginnerWorkout;
      const intermediateWorkout: IIntermediateWorkout = { id: 18882 };
      workoutStep.intermediateWorkout = intermediateWorkout;

      activatedRoute.data = of({ workoutStep });
      comp.ngOnInit();

      expect(comp.workoutsSharedCollection).toContain(workout);
      expect(comp.beginnerWorkoutsSharedCollection).toContain(beginnerWorkout);
      expect(comp.intermediateWorkoutsSharedCollection).toContain(intermediateWorkout);
      expect(comp.workoutStep).toEqual(workoutStep);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkoutStep>>();
      const workoutStep = { id: 123 };
      jest.spyOn(workoutStepFormService, 'getWorkoutStep').mockReturnValue(workoutStep);
      jest.spyOn(workoutStepService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workoutStep });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workoutStep }));
      saveSubject.complete();

      // THEN
      expect(workoutStepFormService.getWorkoutStep).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(workoutStepService.update).toHaveBeenCalledWith(expect.objectContaining(workoutStep));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkoutStep>>();
      const workoutStep = { id: 123 };
      jest.spyOn(workoutStepFormService, 'getWorkoutStep').mockReturnValue({ id: null });
      jest.spyOn(workoutStepService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workoutStep: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workoutStep }));
      saveSubject.complete();

      // THEN
      expect(workoutStepFormService.getWorkoutStep).toHaveBeenCalled();
      expect(workoutStepService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkoutStep>>();
      const workoutStep = { id: 123 };
      jest.spyOn(workoutStepService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workoutStep });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workoutStepService.update).toHaveBeenCalled();
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

    describe('compareBeginnerWorkout', () => {
      it('Should forward to beginnerWorkoutService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(beginnerWorkoutService, 'compareBeginnerWorkout');
        comp.compareBeginnerWorkout(entity, entity2);
        expect(beginnerWorkoutService.compareBeginnerWorkout).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareIntermediateWorkout', () => {
      it('Should forward to intermediateWorkoutService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(intermediateWorkoutService, 'compareIntermediateWorkout');
        comp.compareIntermediateWorkout(entity, entity2);
        expect(intermediateWorkoutService.compareIntermediateWorkout).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
