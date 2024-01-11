import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, combineLatest, switchMap, throwError } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProduct, NewProduct } from '../product.model';
import { BusinessService } from '../../business/service/business.service';

export type PartialUpdateProduct = Partial<IProduct> & Pick<IProduct, 'id'>;

export type EntityResponseType = HttpResponse<IProduct>;
export type EntityArrayResponseType = HttpResponse<IProduct[]>;

@Injectable({ providedIn: 'root' })
export class ProductService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/products');
  protected businessId: string | undefined;

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
    protected businessService: BusinessService,
  ) {
    this.businessService.businessData$.subscribe(business => {
      this.businessId = business?.id;
    });
  }

  create(product: NewProduct): Observable<EntityResponseType> {
    return this.http.post<IProduct>(this.resourceUrl, product, { observe: 'response' });
  }

  update(product: IProduct): Observable<EntityResponseType> {
    return this.http.put<IProduct>(`${this.resourceUrl}/${this.getProductIdentifier(product)}`, product, { observe: 'response' });
  }

  partialUpdate(product: PartialUpdateProduct): Observable<EntityResponseType> {
    return this.http.patch<IProduct>(`${this.resourceUrl}/${this.getProductIdentifier(product)}`, product, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  // query(req?: any): Observable<EntityArrayResponseType> {
  //   const options = createRequestOption(req);

  //   return this.businessService.businessData$.pipe(
  //     switchMap(business => {
  //       if (business) {
  //         const resourceUrlForQuery = this.applicationConfigService.getEndpointFor('api/' + business.id + '/products');
  //         return this.http.get<IProduct[]>(resourceUrlForQuery, { params: options, observe: 'response' });
  //       } else {
  //         return this.businessService.query().pipe(
  //           switchMap(res => {
  //             const resourceUrlForQuery = this.applicationConfigService.getEndpointFor('api/' + res.body![0].id + '/products');
  //             return this.http.get<IProduct[]>(resourceUrlForQuery, { params: options, observe: 'response' });
  //           }),
  //         );
  //       }
  //     }),
  //   );
  // }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);

    const businessId = this.businessId;

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

  private getInvoicesByBusinessId(businessId: string, options: any): Observable<EntityArrayResponseType> {
    const resourceUrlForQuery = this.applicationConfigService.getEndpointFor(`api/${businessId}/products`);
    return this.http.get<IProduct[]>(resourceUrlForQuery, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductIdentifier(product: Pick<IProduct, 'id'>): string {
    return product.id;
  }

  compareProduct(o1: Pick<IProduct, 'id'> | null, o2: Pick<IProduct, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductIdentifier(o1) === this.getProductIdentifier(o2) : o1 === o2;
  }

  addProductToCollectionIfMissing<Type extends Pick<IProduct, 'id'>>(
    productCollection: Type[],
    ...productsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const products: Type[] = productsToCheck.filter(isPresent);
    if (products.length > 0) {
      const productCollectionIdentifiers = productCollection.map(productItem => this.getProductIdentifier(productItem)!);
      const productsToAdd = products.filter(productItem => {
        const productIdentifier = this.getProductIdentifier(productItem);
        if (productCollectionIdentifiers.includes(productIdentifier)) {
          return false;
        }
        productCollectionIdentifiers.push(productIdentifier);
        return true;
      });
      return [...productsToAdd, ...productCollection];
    }
    return productCollection;
  }
}
