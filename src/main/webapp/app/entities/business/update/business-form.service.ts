import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBusiness, NewBusiness } from '../business.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBusiness for edit and NewBusinessFormGroupInput for create.
 */
type BusinessFormGroupInput = IBusiness | PartialWithRequiredKeyOf<NewBusiness>;

type BusinessFormDefaults = Pick<NewBusiness, 'id'>;

type BusinessFormGroupContent = {
  id: FormControl<IBusiness['id'] | NewBusiness['id']>;
  name: FormControl<IBusiness['name']>;
  website: FormControl<IBusiness['website']>;
  email: FormControl<IBusiness['email']>;
  phone: FormControl<IBusiness['phone']>;
  deflaut: FormControl<IBusiness['deflaut']>;
};

export type BusinessFormGroup = FormGroup<BusinessFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BusinessFormService {
  createBusinessFormGroup(business: BusinessFormGroupInput = { id: null }): BusinessFormGroup {
    const businessRawValue = {
      ...this.getFormDefaults(),
      ...business,
    };
    return new FormGroup<BusinessFormGroupContent>({
      id: new FormControl(
        { value: businessRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(businessRawValue.name, {
        validators: [Validators.required, Validators.minLength(2)],
      }),
      website: new FormControl(businessRawValue.website),
      email: new FormControl(businessRawValue.email),
      phone: new FormControl(businessRawValue.phone),
      deflaut: new FormControl(businessRawValue.deflaut),
    });
  }

  getBusiness(form: BusinessFormGroup): IBusiness | NewBusiness {
    return form.getRawValue() as IBusiness | NewBusiness;
  }

  resetForm(form: BusinessFormGroup, business: BusinessFormGroupInput): void {
    const businessRawValue = { ...this.getFormDefaults(), ...business };
    form.reset(
      {
        ...businessRawValue,
        id: { value: businessRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BusinessFormDefaults {
    return {
      id: null,
    };
  }
}
