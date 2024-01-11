import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, switchMap, throwError } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICustomer, NewCustomer } from '../customer.model';
import { BusinessService } from '../../business/service/business.service';
import { IBusiness } from 'app/entities/business/business.model';

export type PartialUpdateCustomer = Partial<ICustomer> & Pick<ICustomer, 'id'>;

export type EntityResponseType = HttpResponse<ICustomer>;
export type EntityArrayResponseType = HttpResponse<ICustomer[]>;

@Injectable({ providedIn: 'root' })
export class CustomerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/customers');
  business: IBusiness | null = null;

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
    protected businessService: BusinessService,
  ) {
    this.resourceUrl = this.applicationConfigService.getEndpointFor('api/customers');
    this.businessService.businessData$.subscribe(b => {
      this.business = b;
    });
  }

  create(customer: NewCustomer): Observable<EntityResponseType> {
    return this.http.post<ICustomer>(this.resourceUrl, customer, { observe: 'response' });
  }

  update(customer: ICustomer): Observable<EntityResponseType> {
    return this.http.put<ICustomer>(`${this.resourceUrl}/${this.getCustomerIdentifier(customer)}`, customer, { observe: 'response' });
  }

  partialUpdate(customer: PartialUpdateCustomer): Observable<EntityResponseType> {
    return this.http.patch<ICustomer>(`${this.resourceUrl}/${this.getCustomerIdentifier(customer)}`, customer, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICustomer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);

    const businessId = this.business?.id;

    if (businessId) {
      return this.getCustomersByBusinessId(businessId, options);
    } else {
      return this.businessService.query().pipe(
        switchMap(res => {
          const defaultBusiness = res.body?.find(business => business.deflaut);
          const businessIdFromQuery = defaultBusiness?.id;

          if (businessIdFromQuery) {
            return this.getCustomersByBusinessId(businessIdFromQuery, options);
          } else {
            // Handle the case where there are no businesses or the default business doesn't have an ID
            return throwError('No business ID found');
          }
        }),
      );
    }
  }

  private getCustomersByBusinessId(businessId: string, options: any): Observable<EntityArrayResponseType> {
    const resourceUrlForQuery = this.applicationConfigService.getEndpointFor(`api/${businessId}/customers`);
    return this.http.get<ICustomer[]>(resourceUrlForQuery, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCustomerIdentifier(customer: Pick<ICustomer, 'id'>): string {
    return customer.id;
  }

  compareCustomer(o1: Pick<ICustomer, 'id'> | null, o2: Pick<ICustomer, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustomerIdentifier(o1) === this.getCustomerIdentifier(o2) : o1 === o2;
  }

  addCustomerToCollectionIfMissing<Type extends Pick<ICustomer, 'id'>>(
    customerCollection: Type[],
    ...customersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const customers: Type[] = customersToCheck.filter(isPresent);
    if (customers.length > 0) {
      const customerCollectionIdentifiers = customerCollection.map(customerItem => this.getCustomerIdentifier(customerItem)!);
      const customersToAdd = customers.filter(customerItem => {
        const customerIdentifier = this.getCustomerIdentifier(customerItem);
        if (customerCollectionIdentifiers.includes(customerIdentifier)) {
          return false;
        }
        customerCollectionIdentifiers.push(customerIdentifier);
        return true;
      });
      return [...customersToAdd, ...customerCollection];
    }
    return customerCollection;
  }
}
