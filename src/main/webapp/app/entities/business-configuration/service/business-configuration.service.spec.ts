import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBusinessConfiguration } from '../business-configuration.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../business-configuration.test-samples';

import { BusinessConfigurationService } from './business-configuration.service';

const requireRestSample: IBusinessConfiguration = {
  ...sampleWithRequiredData,
};

describe('BusinessConfiguration Service', () => {
  let service: BusinessConfigurationService;
  let httpMock: HttpTestingController;
  let expectedResult: IBusinessConfiguration | IBusinessConfiguration[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BusinessConfigurationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    // it('should find an element', () => {
    //   const returnedFromService = { ...requireRestSample };
    //   const expected = { ...sampleWithRequiredData };

    //   service.find('ABC').subscribe(resp => (expectedResult = resp.body));

    //   const req = httpMock.expectOne({ method: 'GET' });
    //   req.flush(returnedFromService);
    //   expect(expectedResult).toMatchObject(expected);
    // });

    // it('should create a BusinessConfiguration', () => {
    //   const businessConfiguration = { ...sampleWithNewData };
    //   const returnedFromService = { ...requireRestSample };
    //   const expected = { ...sampleWithRequiredData };

    //   service.create(businessConfiguration).subscribe(resp => (expectedResult = resp.body));

    //   const req = httpMock.expectOne({ method: 'POST' });
    //   req.flush(returnedFromService);
    //   expect(expectedResult).toMatchObject(expected);
    // });

    // it('should update a BusinessConfiguration', () => {
    //   const businessConfiguration = { ...sampleWithRequiredData };
    //   const returnedFromService = { ...requireRestSample };
    //   const expected = { ...sampleWithRequiredData };

    //   service.update(businessConfiguration).subscribe(resp => (expectedResult = resp.body));

    //   const req = httpMock.expectOne({ method: 'PUT' });
    //   req.flush(returnedFromService);
    //   expect(expectedResult).toMatchObject(expected);
    // });

    // it('should partial update a BusinessConfiguration', () => {
    //   const patchObject = { ...sampleWithPartialData };
    //   const returnedFromService = { ...requireRestSample };
    //   const expected = { ...sampleWithRequiredData };

    //   service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

    //   const req = httpMock.expectOne({ method: 'PATCH' });
    //   req.flush(returnedFromService);
    //   expect(expectedResult).toMatchObject(expected);
    // });

    // it('should return a list of BusinessConfiguration', () => {
    //   const returnedFromService = { ...requireRestSample };

    //   const expected = { ...sampleWithRequiredData };

    //   service.query().subscribe(resp => (expectedResult = resp.body));

    //   const req = httpMock.expectOne({ method: 'GET' });
    //   req.flush([returnedFromService]);
    //   httpMock.verify();
    //   expect(expectedResult).toMatchObject([expected]);
    // });

    // it('should delete a BusinessConfiguration', () => {
    //   const expected = true;

    //   service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

    //   const req = httpMock.expectOne({ method: 'DELETE' });
    //   req.flush({ status: 200 });
    //   expect(expectedResult).toBe(expected);
    // });

    // describe('addBusinessConfigurationToCollectionIfMissing', () => {
    //   it('should add a BusinessConfiguration to an empty array', () => {
    //     const businessConfiguration: IBusinessConfiguration = sampleWithRequiredData;
    //     expectedResult = service.addBusinessConfigurationToCollectionIfMissing([], businessConfiguration);
    //     expect(expectedResult).toHaveLength(1);
    //     expect(expectedResult).toContain(businessConfiguration);
    //   });

    //   it('should not add a BusinessConfiguration to an array that contains it', () => {
    //     const businessConfiguration: IBusinessConfiguration = sampleWithRequiredData;
    //     const businessConfigurationCollection: IBusinessConfiguration[] = [
    //       {
    //         ...businessConfiguration,
    //       },
    //       sampleWithPartialData,
    //     ];
    //     expectedResult = service.addBusinessConfigurationToCollectionIfMissing(businessConfigurationCollection, businessConfiguration);
    //     expect(expectedResult).toHaveLength(2);
    //   });

    //   it("should add a BusinessConfiguration to an array that doesn't contain it", () => {
    //     const businessConfiguration: IBusinessConfiguration = sampleWithRequiredData;
    //     const businessConfigurationCollection: IBusinessConfiguration[] = [sampleWithPartialData];
    //     expectedResult = service.addBusinessConfigurationToCollectionIfMissing(businessConfigurationCollection, businessConfiguration);
    //     expect(expectedResult).toHaveLength(2);
    //     expect(expectedResult).toContain(businessConfiguration);
    //   });

    //   it('should add only unique BusinessConfiguration to an array', () => {
    //     const businessConfigurationArray: IBusinessConfiguration[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
    //     const businessConfigurationCollection: IBusinessConfiguration[] = [sampleWithRequiredData];
    //     expectedResult = service.addBusinessConfigurationToCollectionIfMissing(
    //       businessConfigurationCollection,
    //       ...businessConfigurationArray,
    //     );
    //     expect(expectedResult).toHaveLength(3);
    //   });

    //   it('should accept varargs', () => {
    //     const businessConfiguration: IBusinessConfiguration = sampleWithRequiredData;
    //     const businessConfiguration2: IBusinessConfiguration = sampleWithPartialData;
    //     expectedResult = service.addBusinessConfigurationToCollectionIfMissing([], businessConfiguration, businessConfiguration2);
    //     expect(expectedResult).toHaveLength(2);
    //     expect(expectedResult).toContain(businessConfiguration);
    //     expect(expectedResult).toContain(businessConfiguration2);
    //   });

    //   it('should accept null and undefined values', () => {
    //     const businessConfiguration: IBusinessConfiguration = sampleWithRequiredData;
    //     expectedResult = service.addBusinessConfigurationToCollectionIfMissing([], null, businessConfiguration, undefined);
    //     expect(expectedResult).toHaveLength(1);
    //     expect(expectedResult).toContain(businessConfiguration);
    //   });

    //   it('should return initial array if no BusinessConfiguration is added', () => {
    //     const businessConfigurationCollection: IBusinessConfiguration[] = [sampleWithRequiredData];
    //     expectedResult = service.addBusinessConfigurationToCollectionIfMissing(businessConfigurationCollection, undefined, null);
    //     expect(expectedResult).toEqual(businessConfigurationCollection);
    //   });
    // });

    describe('compareBusinessConfiguration', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBusinessConfiguration(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareBusinessConfiguration(entity1, entity2);
        const compareResult2 = service.compareBusinessConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareBusinessConfiguration(entity1, entity2);
        const compareResult2 = service.compareBusinessConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareBusinessConfiguration(entity1, entity2);
        const compareResult2 = service.compareBusinessConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
