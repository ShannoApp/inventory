import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

import { map, switchMap } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvoice, NewInvoice } from '../invoice.model';
import { BusinessService } from '../../business/service/business.service';

export type PartialUpdateInvoice = Partial<IInvoice> & Pick<IInvoice, 'id'>;

type RestOf<T extends IInvoice | NewInvoice> = Omit<T, 'issueDate' | 'dueDate'> & {
  issueDate?: string | null;
  dueDate?: string | null;
};

export type RestInvoice = RestOf<IInvoice>;

export type NewRestInvoice = RestOf<NewInvoice>;

export type PartialUpdateRestInvoice = RestOf<PartialUpdateInvoice>;

export type EntityResponseType = HttpResponse<IInvoice>;
export type EntityArrayResponseType = HttpResponse<IInvoice[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/invoices');
  business: any;

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
    protected businessService: BusinessService,
  ) {
    this.businessService.businessData$.subscribe(business => {
      this.business = business;
    });
  }

  create(invoice: NewInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoice);
    return this.http
      .post<RestInvoice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(invoice: IInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoice);
    return this.http
      .put<RestInvoice>(`${this.resourceUrl}/${this.getInvoiceIdentifier(invoice)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(invoice: PartialUpdateInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoice);
    return this.http
      .patch<RestInvoice>(`${this.resourceUrl}/${this.getInvoiceIdentifier(invoice)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  // query(req?: any): Observable<EntityArrayResponseType> {
  //   const options = createRequestOption(req);
  //   return this.businessService.businessData$.pipe(
  //     switchMap(business => {
  //       if (business) {
  //         const resourceUrlForQuery = this.applicationConfigService.getEndpointFor('api/' + business.id + '/invoices');
  //         return this.http
  //           .get<RestInvoice[]>(resourceUrlForQuery, { params: options, observe: 'response' })
  //           .pipe(map(res => this.convertResponseArrayFromServer(res)));
  //       } else {
  //         return this.businessService.query().pipe(
  //           switchMap(res => {
  //             const resourceUrlForQuery = this.applicationConfigService.getEndpointFor('api/' + res.body![0].id + '/invoices');
  //             return this.http
  //               .get<RestInvoice[]>(resourceUrlForQuery, { params: options, observe: 'response' })
  //               .pipe(map(resMap => this.convertResponseArrayFromServer(resMap)));
  //           }),
  //         );
  //       }
  //     }),
  //   );
  // }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);

    const businessId = this.business?.id;

    if (businessId) {
      return this.getInvoicesByBusinessId(businessId, options);
    } else {
      return this.businessService.query().pipe(
        switchMap(res => {
          const defaultBusiness = res.body?.find(business => business.deflaut);
          const businessIdFromQuery = defaultBusiness?.id;

          if (businessIdFromQuery) {
            return this.getInvoicesByBusinessId(businessIdFromQuery, options);
          } else {
            // Handle the case where there are no businesses or the default business doesn't have an ID
            return throwError('No business ID found');
          }
        }),
      );
    }
  }

  findInvoiceByCustomer(customerId: string): Observable<EntityArrayResponseType> {
    const businessId = this.business?.id;

    if (businessId) {
      const resourceUrlForQuery = this.applicationConfigService.getEndpointFor(`api/${businessId}/invoices/${customerId}`);
      return this.http.get<IInvoice[]>(resourceUrlForQuery, { observe: 'response' });
    } else {
      return this.businessService.query().pipe(
        switchMap(res => {
          const defaultBusiness = res.body?.find(business => business.deflaut);
          const businessIdFromQuery = defaultBusiness?.id;

          if (businessIdFromQuery) {
            const resourceUrlForQuery = this.applicationConfigService.getEndpointFor(`api/${businessIdFromQuery}/invoices/${customerId}`);
            return this.http.get<IInvoice[]>(resourceUrlForQuery, { observe: 'response' });
          } else {
            // Handle the case where there are no businesses or the default business doesn't have an ID
            return throwError('No business ID found');
          }
        }),
      );
    }
  }

  private getInvoicesByBusinessId(businessId: string, options: any): Observable<EntityArrayResponseType> {
    const resourceUrlForQuery = this.applicationConfigService.getEndpointFor(`api/${businessId}/invoices`);
    return this.http.get<IInvoice[]>(resourceUrlForQuery, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInvoiceIdentifier(invoice: Pick<IInvoice, 'id'>): string {
    return invoice.id;
  }

  compareInvoice(o1: Pick<IInvoice, 'id'> | null, o2: Pick<IInvoice, 'id'> | null): boolean {
    return o1 && o2 ? this.getInvoiceIdentifier(o1) === this.getInvoiceIdentifier(o2) : o1 === o2;
  }

  addInvoiceToCollectionIfMissing<Type extends Pick<IInvoice, 'id'>>(
    invoiceCollection: Type[],
    ...invoicesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const invoices: Type[] = invoicesToCheck.filter(isPresent);
    if (invoices.length > 0) {
      const invoiceCollectionIdentifiers = invoiceCollection.map(invoiceItem => this.getInvoiceIdentifier(invoiceItem)!);
      const invoicesToAdd = invoices.filter(invoiceItem => {
        const invoiceIdentifier = this.getInvoiceIdentifier(invoiceItem);
        if (invoiceCollectionIdentifiers.includes(invoiceIdentifier)) {
          return false;
        }
        invoiceCollectionIdentifiers.push(invoiceIdentifier);
        return true;
      });
      return [...invoicesToAdd, ...invoiceCollection];
    }
    return invoiceCollection;
  }

  protected convertDateFromClient<T extends IInvoice | NewInvoice | PartialUpdateInvoice>(invoice: T): RestOf<T> {
    return {
      ...invoice,
      issueDate: invoice.issueDate?.format(DATE_FORMAT) ?? null,
      dueDate: invoice.dueDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restInvoice: RestInvoice): IInvoice {
    return {
      ...restInvoice,
      issueDate: restInvoice.issueDate ? dayjs(restInvoice.issueDate) : undefined,
      dueDate: restInvoice.dueDate ? dayjs(restInvoice.dueDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestInvoice>): HttpResponse<IInvoice> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestInvoice[]>): HttpResponse<IInvoice[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  calculatePendingAndPaidAmount(invoices: IInvoice[]): InvoiceSummary {
    return invoices.reduce(
      (summary, invoice) => {
        const { paid, totalAmount } = invoice;
        const amount = totalAmount ?? 0;

        if (paid) {
          summary.paidAmount += amount;
          summary.paidInvoices++;
        } else {
          summary.pendingAmount += amount;
          summary.pendingInvoices++;
        }

        return summary;
      },
      {
        pendingAmount: 0,
        paidAmount: 0,
        pendingInvoices: 0,
        paidInvoices: 0,
      },
    );
  }
}

interface InvoiceSummary {
  pendingAmount: number;
  paidAmount: number;
  pendingInvoices: number;
  paidInvoices: number;
}
