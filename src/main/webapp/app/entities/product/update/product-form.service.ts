import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProduct, NewProduct } from '../product.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProduct for edit and NewProductFormGroupInput for create.
 */
type ProductFormGroupInput = IProduct | PartialWithRequiredKeyOf<NewProduct>;

type ProductFormDefaults = Pick<NewProduct, 'id'>;

type ProductFormGroupContent = {
  id: FormControl<IProduct['id'] | NewProduct['id']>;
  name: FormControl<IProduct['name']>;
  description: FormControl<IProduct['description']>;
  unit: FormControl<IProduct['unit']>;
  sellingPrice: FormControl<IProduct['sellingPrice']>;
  openingQuantity: FormControl<IProduct['openingQuantity']>;
  asOfDate: FormControl<IProduct['asOfDate']>;
  purchasePrice: FormControl<IProduct['purchasePrice']>;
  minStockToMaintain: FormControl<IProduct['minStockToMaintain']>;
  location: FormControl<IProduct['location']>;
  business: FormControl<IProduct['business']>;
  category: FormControl<IProduct['category']>;
};

export type ProductFormGroup = FormGroup<ProductFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductFormService {
  createProductFormGroup(product: ProductFormGroupInput = { id: null }): ProductFormGroup {
    const productRawValue = {
      ...this.getFormDefaults(),
      ...product,
    };
    return new FormGroup<ProductFormGroupContent>({
      id: new FormControl(
        { value: productRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productRawValue.name, {
        validators: [Validators.required, Validators.minLength(2)],
      }),
      description: new FormControl(productRawValue.description),
      unit: new FormControl(productRawValue.unit),
      sellingPrice: new FormControl(productRawValue.sellingPrice, {
        validators: [Validators.required, Validators.min(0)],
      }),
      openingQuantity: new FormControl(productRawValue.openingQuantity, {
        validators: [Validators.required, Validators.min(0)],
      }),
      asOfDate: new FormControl(productRawValue.asOfDate, {
        validators: [Validators.required],
      }),
      purchasePrice: new FormControl(productRawValue.purchasePrice, {
        validators: [Validators.required, Validators.min(0)],
      }),
      minStockToMaintain: new FormControl(productRawValue.minStockToMaintain, {
        validators: [Validators.required, Validators.min(0)],
      }),
      location: new FormControl(productRawValue.location, {}),
      business: new FormControl(productRawValue.business),
      category: new FormControl(productRawValue.category),
    });
  }

  getProduct(form: ProductFormGroup): IProduct | NewProduct {
    return form.getRawValue() as IProduct | NewProduct;
  }

  resetForm(form: ProductFormGroup, product: ProductFormGroupInput): void {
    const productRawValue = { ...this.getFormDefaults(), ...product };
    form.reset(
      {
        ...productRawValue,
        id: { value: productRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductFormDefaults {
    return {
      id: null,
    };
  }
}
