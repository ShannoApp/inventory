import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInvoice, NewInvoice } from '../invoice.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoice for edit and NewInvoiceFormGroupInput for create.
 */
type InvoiceFormGroupInput = IInvoice | PartialWithRequiredKeyOf<NewInvoice>;

type InvoiceFormDefaults = Pick<NewInvoice, 'id'>;

type InvoiceFormGroupContent = {
  id: FormControl<IInvoice['id'] | NewInvoice['id']>;
  invoiceNumber: FormControl<IInvoice['invoiceNumber']>;
  issueDate: FormControl<IInvoice['issueDate']>;
  dueDate: FormControl<IInvoice['dueDate']>;
  totalAmount: FormControl<IInvoice['totalAmount']>;
  subTotal: FormControl<IInvoice['subTotal']>;
  tax: FormControl<IInvoice['tax']>;
  discount: FormControl<IInvoice['discount']>;
  shippingCharges: FormControl<IInvoice['shippingCharges']>;
  customer: FormControl<IInvoice['customer']>;
  business: FormControl<IInvoice['business']>;
};

export type InvoiceFormGroup = FormGroup<InvoiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceFormService {
  createInvoiceFormGroup(invoice: InvoiceFormGroupInput = { id: null }): InvoiceFormGroup {
    const invoiceRawValue = {
      ...this.getFormDefaults(),
      ...invoice,
    };
    return new FormGroup<InvoiceFormGroupContent>({
      id: new FormControl(
        { value: invoiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      invoiceNumber: new FormControl(invoiceRawValue.invoiceNumber, {}),
      issueDate: new FormControl(invoiceRawValue.issueDate, {
        validators: [Validators.required],
      }),
      dueDate: new FormControl(invoiceRawValue.dueDate, {
        validators: [Validators.required],
      }),
      totalAmount: new FormControl(invoiceRawValue.totalAmount, {
        validators: [Validators.required, Validators.min(0)],
      }),
      subTotal: new FormControl(invoiceRawValue.subTotal, {
        validators: [Validators.required],
      }),
      tax: new FormControl(invoiceRawValue.tax, {
        validators: [Validators.required],
      }),
      discount: new FormControl(invoiceRawValue.discount, {
        validators: [Validators.required],
      }),
      shippingCharges: new FormControl(invoiceRawValue.shippingCharges, {
        validators: [Validators.required],
      }),
      customer: new FormControl(invoiceRawValue.customer),
      business: new FormControl(invoiceRawValue.business),
    });
  }

  getInvoice(form: InvoiceFormGroup): IInvoice | NewInvoice {
    return form.getRawValue() as IInvoice | NewInvoice;
  }

  resetForm(form: InvoiceFormGroup, invoice: InvoiceFormGroupInput): void {
    const invoiceRawValue = { ...this.getFormDefaults(), ...invoice };
    form.reset(
      {
        ...invoiceRawValue,
        id: { value: invoiceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InvoiceFormDefaults {
    return {
      id: null,
    };
  }
}
