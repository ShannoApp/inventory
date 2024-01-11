import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBusinessConfiguration, NewBusinessConfiguration } from '../business-configuration.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBusinessConfiguration for edit and NewBusinessConfigurationFormGroupInput for create.
 */
type BusinessConfigurationFormGroupInput = IBusinessConfiguration | PartialWithRequiredKeyOf<NewBusinessConfiguration>;

type BusinessConfigurationFormDefaults = Pick<NewBusinessConfiguration, 'id'>;

type BusinessConfigurationFormGroupContent = {
  id: FormControl<IBusinessConfiguration['id'] | NewBusinessConfiguration['id']>;
  businessId: FormControl<IBusinessConfiguration['businessId']>;
  discount: FormControl<IBusinessConfiguration['discount']>;
  shippingCharges: FormControl<IBusinessConfiguration['shippingCharges']>;
  tax: FormControl<IBusinessConfiguration['tax']>;
};

export type BusinessConfigurationFormGroup = FormGroup<BusinessConfigurationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BusinessConfigurationFormService {
  createBusinessConfigurationFormGroup(
    businessConfiguration: BusinessConfigurationFormGroupInput = { id: null },
  ): BusinessConfigurationFormGroup {
    const businessConfigurationRawValue = {
      ...this.getFormDefaults(),
      ...businessConfiguration,
    };
    return new FormGroup<BusinessConfigurationFormGroupContent>({
      id: new FormControl(
        { value: businessConfigurationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      businessId: new FormControl(businessConfigurationRawValue.businessId, {
        validators: [Validators.required],
      }),
      discount: new FormControl(businessConfigurationRawValue.discount),
      shippingCharges: new FormControl(businessConfigurationRawValue.shippingCharges),
      tax: new FormControl(businessConfigurationRawValue.tax),
    });
  }

  getBusinessConfiguration(form: BusinessConfigurationFormGroup): IBusinessConfiguration | NewBusinessConfiguration {
    return form.getRawValue() as IBusinessConfiguration | NewBusinessConfiguration;
  }

  resetForm(form: BusinessConfigurationFormGroup, businessConfiguration: BusinessConfigurationFormGroupInput): void {
    const businessConfigurationRawValue = { ...this.getFormDefaults(), ...businessConfiguration };
    form.reset(
      {
        ...businessConfigurationRawValue,
        id: { value: businessConfigurationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BusinessConfigurationFormDefaults {
    return {
      id: null,
    };
  }
}
