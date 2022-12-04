import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBeginnerWorkout } from '../beginner-workout.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../beginner-workout.test-samples';

import { BeginnerWorkoutService } from './beginner-workout.service';

const requireRestSample: IBeginnerWorkout = {
  ...sampleWithRequiredData,
};

describe('BeginnerWorkout Service', () => {
  let service: BeginnerWorkoutService;
  let httpMock: HttpTestingController;
  let expectedResult: IBeginnerWorkout | IBeginnerWorkout[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BeginnerWorkoutService);
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

    it('should create a BeginnerWorkout', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const beginnerWorkout = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(beginnerWorkout).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BeginnerWorkout', () => {
      const beginnerWorkout = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(beginnerWorkout).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BeginnerWorkout', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BeginnerWorkout', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BeginnerWorkout', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBeginnerWorkoutToCollectionIfMissing', () => {
      it('should add a BeginnerWorkout to an empty array', () => {
        const beginnerWorkout: IBeginnerWorkout = sampleWithRequiredData;
        expectedResult = service.addBeginnerWorkoutToCollectionIfMissing([], beginnerWorkout);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(beginnerWorkout);
      });

      it('should not add a BeginnerWorkout to an array that contains it', () => {
        const beginnerWorkout: IBeginnerWorkout = sampleWithRequiredData;
        const beginnerWorkoutCollection: IBeginnerWorkout[] = [
          {
            ...beginnerWorkout,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBeginnerWorkoutToCollectionIfMissing(beginnerWorkoutCollection, beginnerWorkout);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BeginnerWorkout to an array that doesn't contain it", () => {
        const beginnerWorkout: IBeginnerWorkout = sampleWithRequiredData;
        const beginnerWorkoutCollection: IBeginnerWorkout[] = [sampleWithPartialData];
        expectedResult = service.addBeginnerWorkoutToCollectionIfMissing(beginnerWorkoutCollection, beginnerWorkout);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(beginnerWorkout);
      });

      it('should add only unique BeginnerWorkout to an array', () => {
        const beginnerWorkoutArray: IBeginnerWorkout[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const beginnerWorkoutCollection: IBeginnerWorkout[] = [sampleWithRequiredData];
        expectedResult = service.addBeginnerWorkoutToCollectionIfMissing(beginnerWorkoutCollection, ...beginnerWorkoutArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const beginnerWorkout: IBeginnerWorkout = sampleWithRequiredData;
        const beginnerWorkout2: IBeginnerWorkout = sampleWithPartialData;
        expectedResult = service.addBeginnerWorkoutToCollectionIfMissing([], beginnerWorkout, beginnerWorkout2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(beginnerWorkout);
        expect(expectedResult).toContain(beginnerWorkout2);
      });

      it('should accept null and undefined values', () => {
        const beginnerWorkout: IBeginnerWorkout = sampleWithRequiredData;
        expectedResult = service.addBeginnerWorkoutToCollectionIfMissing([], null, beginnerWorkout, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(beginnerWorkout);
      });

      it('should return initial array if no BeginnerWorkout is added', () => {
        const beginnerWorkoutCollection: IBeginnerWorkout[] = [sampleWithRequiredData];
        expectedResult = service.addBeginnerWorkoutToCollectionIfMissing(beginnerWorkoutCollection, undefined, null);
        expect(expectedResult).toEqual(beginnerWorkoutCollection);
      });
    });

    describe('compareBeginnerWorkout', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBeginnerWorkout(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBeginnerWorkout(entity1, entity2);
        const compareResult2 = service.compareBeginnerWorkout(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBeginnerWorkout(entity1, entity2);
        const compareResult2 = service.compareBeginnerWorkout(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBeginnerWorkout(entity1, entity2);
        const compareResult2 = service.compareBeginnerWorkout(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
