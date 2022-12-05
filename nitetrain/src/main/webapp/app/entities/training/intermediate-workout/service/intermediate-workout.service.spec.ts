import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIntermediateWorkout } from '../intermediate-workout.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../intermediate-workout.test-samples';

import { IntermediateWorkoutService } from './intermediate-workout.service';

const requireRestSample: IIntermediateWorkout = {
  ...sampleWithRequiredData,
};

describe('IntermediateWorkout Service', () => {
  let service: IntermediateWorkoutService;
  let httpMock: HttpTestingController;
  let expectedResult: IIntermediateWorkout | IIntermediateWorkout[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IntermediateWorkoutService);
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

    it('should create a IntermediateWorkout', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const intermediateWorkout = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(intermediateWorkout).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IntermediateWorkout', () => {
      const intermediateWorkout = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(intermediateWorkout).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IntermediateWorkout', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IntermediateWorkout', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IntermediateWorkout', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIntermediateWorkoutToCollectionIfMissing', () => {
      it('should add a IntermediateWorkout to an empty array', () => {
        const intermediateWorkout: IIntermediateWorkout = sampleWithRequiredData;
        expectedResult = service.addIntermediateWorkoutToCollectionIfMissing([], intermediateWorkout);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(intermediateWorkout);
      });

      it('should not add a IntermediateWorkout to an array that contains it', () => {
        const intermediateWorkout: IIntermediateWorkout = sampleWithRequiredData;
        const intermediateWorkoutCollection: IIntermediateWorkout[] = [
          {
            ...intermediateWorkout,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIntermediateWorkoutToCollectionIfMissing(intermediateWorkoutCollection, intermediateWorkout);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IntermediateWorkout to an array that doesn't contain it", () => {
        const intermediateWorkout: IIntermediateWorkout = sampleWithRequiredData;
        const intermediateWorkoutCollection: IIntermediateWorkout[] = [sampleWithPartialData];
        expectedResult = service.addIntermediateWorkoutToCollectionIfMissing(intermediateWorkoutCollection, intermediateWorkout);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(intermediateWorkout);
      });

      it('should add only unique IntermediateWorkout to an array', () => {
        const intermediateWorkoutArray: IIntermediateWorkout[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const intermediateWorkoutCollection: IIntermediateWorkout[] = [sampleWithRequiredData];
        expectedResult = service.addIntermediateWorkoutToCollectionIfMissing(intermediateWorkoutCollection, ...intermediateWorkoutArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const intermediateWorkout: IIntermediateWorkout = sampleWithRequiredData;
        const intermediateWorkout2: IIntermediateWorkout = sampleWithPartialData;
        expectedResult = service.addIntermediateWorkoutToCollectionIfMissing([], intermediateWorkout, intermediateWorkout2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(intermediateWorkout);
        expect(expectedResult).toContain(intermediateWorkout2);
      });

      it('should accept null and undefined values', () => {
        const intermediateWorkout: IIntermediateWorkout = sampleWithRequiredData;
        expectedResult = service.addIntermediateWorkoutToCollectionIfMissing([], null, intermediateWorkout, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(intermediateWorkout);
      });

      it('should return initial array if no IntermediateWorkout is added', () => {
        const intermediateWorkoutCollection: IIntermediateWorkout[] = [sampleWithRequiredData];
        expectedResult = service.addIntermediateWorkoutToCollectionIfMissing(intermediateWorkoutCollection, undefined, null);
        expect(expectedResult).toEqual(intermediateWorkoutCollection);
      });
    });

    describe('compareIntermediateWorkout', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIntermediateWorkout(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIntermediateWorkout(entity1, entity2);
        const compareResult2 = service.compareIntermediateWorkout(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIntermediateWorkout(entity1, entity2);
        const compareResult2 = service.compareIntermediateWorkout(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIntermediateWorkout(entity1, entity2);
        const compareResult2 = service.compareIntermediateWorkout(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
