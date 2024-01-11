import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../business-configuration.test-samples';

import { BusinessConfigurationFormService } from './business-configuration-form.service';

describe('BusinessConfiguration Form Service', () => {
  let service: BusinessConfigurationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BusinessConfigurationFormService);
  });

  describe('Service methods', () => {
    describe('createBusinessConfigurationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBusinessConfigurationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            businessId: expect.any(Object),
            discount: expect.any(Object),
            shippingCharges: expect.any(Object),
            tax: expect.any(Object),
          }),
        );
      });

      it('passing IBusinessConfiguration should create a new form with FormGroup', () => {
        const formGroup = service.createBusinessConfigurationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            businessId: expect.any(Object),
            discount: expect.any(Object),
            shippingCharges: expect.any(Object),
            tax: expect.any(Object),
          }),
        );
      });
    });

    describe('getBusinessConfiguration', () => {
      it('should return NewBusinessConfiguration for default BusinessConfiguration initial value', () => {
        const formGroup = service.createBusinessConfigurationFormGroup(sampleWithNewData);

        const businessConfiguration = service.getBusinessConfiguration(formGroup) as any;

        expect(businessConfiguration).toMatchObject(sampleWithNewData);
      });

      it('should return NewBusinessConfiguration for empty BusinessConfiguration initial value', () => {
        const formGroup = service.createBusinessConfigurationFormGroup();

        const businessConfiguration = service.getBusinessConfiguration(formGroup) as any;

        expect(businessConfiguration).toMatchObject({});
      });

      it('should return IBusinessConfiguration', () => {
        const formGroup = service.createBusinessConfigurationFormGroup(sampleWithRequiredData);

        const businessConfiguration = service.getBusinessConfiguration(formGroup) as any;

        expect(businessConfiguration).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBusinessConfiguration should not enable id FormControl', () => {
        const formGroup = service.createBusinessConfigurationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBusinessConfiguration should disable id FormControl', () => {
        const formGroup = service.createBusinessConfigurationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
