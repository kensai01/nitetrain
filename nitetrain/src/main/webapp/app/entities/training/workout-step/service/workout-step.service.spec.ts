import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWorkoutStep } from '../workout-step.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../workout-step.test-samples';

import { WorkoutStepService } from './workout-step.service';

const requireRestSample: IWorkoutStep = {
  ...sampleWithRequiredData,
};

describe('WorkoutStep Service', () => {
  let service: WorkoutStepService;
  let httpMock: HttpTestingController;
  let expectedResult: IWorkoutStep | IWorkoutStep[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WorkoutStepService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a WorkoutStep', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const workoutStep = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(workoutStep).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WorkoutStep', () => {
      const workoutStep = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(workoutStep).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WorkoutStep', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WorkoutStep', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WorkoutStep', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWorkoutStepToCollectionIfMissing', () => {
      it('should add a WorkoutStep to an empty array', () => {
        const workoutStep: IWorkoutStep = sampleWithRequiredData;
        expectedResult = service.addWorkoutStepToCollectionIfMissing([], workoutStep);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workoutStep);
      });

      it('should not add a WorkoutStep to an array that contains it', () => {
        const workoutStep: IWorkoutStep = sampleWithRequiredData;
        const workoutStepCollection: IWorkoutStep[] = [
          {
            ...workoutStep,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWorkoutStepToCollectionIfMissing(workoutStepCollection, workoutStep);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorkoutStep to an array that doesn't contain it", () => {
        const workoutStep: IWorkoutStep = sampleWithRequiredData;
        const workoutStepCollection: IWorkoutStep[] = [sampleWithPartialData];
        expectedResult = service.addWorkoutStepToCollectionIfMissing(workoutStepCollection, workoutStep);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workoutStep);
      });

      it('should add only unique WorkoutStep to an array', () => {
        const workoutStepArray: IWorkoutStep[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const workoutStepCollection: IWorkoutStep[] = [sampleWithRequiredData];
        expectedResult = service.addWorkoutStepToCollectionIfMissing(workoutStepCollection, ...workoutStepArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workoutStep: IWorkoutStep = sampleWithRequiredData;
        const workoutStep2: IWorkoutStep = sampleWithPartialData;
        expectedResult = service.addWorkoutStepToCollectionIfMissing([], workoutStep, workoutStep2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workoutStep);
        expect(expectedResult).toContain(workoutStep2);
      });

      it('should accept null and undefined values', () => {
        const workoutStep: IWorkoutStep = sampleWithRequiredData;
        expectedResult = service.addWorkoutStepToCollectionIfMissing([], null, workoutStep, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workoutStep);
      });

      it('should return initial array if no WorkoutStep is added', () => {
        const workoutStepCollection: IWorkoutStep[] = [sampleWithRequiredData];
        expectedResult = service.addWorkoutStepToCollectionIfMissing(workoutStepCollection, undefined, null);
        expect(expectedResult).toEqual(workoutStepCollection);
      });
    });

    describe('compareWorkoutStep', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWorkoutStep(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareWorkoutStep(entity1, entity2);
        const compareResult2 = service.compareWorkoutStep(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareWorkoutStep(entity1, entity2);
        const compareResult2 = service.compareWorkoutStep(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareWorkoutStep(entity1, entity2);
        const compareResult2 = service.compareWorkoutStep(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
