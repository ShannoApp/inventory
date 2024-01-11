import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, switchMap, tap } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBusiness, NewBusiness } from '../business.model';
import { AccountService } from '../../../core/auth/account.service';

export type PartialUpdateBusiness = Partial<IBusiness> & Pick<IBusiness, 'id'>;

export type EntityResponseType = HttpResponse<IBusiness>;
export type EntityArrayResponseType = HttpResponse<IBusiness[]>;

@Injectable({ providedIn: 'root' })
export class BusinessService {
  private businessListSubject = new BehaviorSubject<IBusiness[] | null>(null);
  public businessListData$ = this.businessListSubject.asObservable();

  private businessSubject = new BehaviorSubject<IBusiness | null>(null);
  public businessData$ = this.businessSubject.asObservable();

  private businessId: string | null = null;

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/businesses');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
    protected accountService: AccountService,
  ) {}

  create(business: NewBusiness): Observable<EntityResponseType> {
    return this.http.post<IBusiness>(this.resourceUrl, business, { observe: 'response' });
  }

  update(business: IBusiness): Observable<EntityResponseType> {
    return this.http.put<IBusiness>(`${this.resourceUrl}/${this.getBusinessIdentifier(business)}`, business, { observe: 'response' });
  }

  partialUpdate(business: PartialUpdateBusiness): Observable<EntityResponseType> {
    return this.http.patch<IBusiness>(`${this.resourceUrl}/${this.getBusinessIdentifier(business)}`, business, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IBusiness>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);

    return this.http.get<IBusiness[]>(this.resourceUrl, { params: options, observe: 'response' }).pipe(
      tap((res: EntityArrayResponseType) => {
        const businesses = res.body ?? [];

        this.businessListSubject.next(businesses);

        const defaultBusiness = businesses.find(b => b.deflaut);
        this.businessSubject.next(defaultBusiness ?? null);

        // Uncomment the line below if you need to set the businessId
        // this.businessId = businesses.length > 0 ? businesses[0].id : null;
      }),
    );
  }

  findAll(req?: any, userId?: string): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBusiness[]>(this.resourceUrl + '/user/' + userId, { params: options, observe: 'response' }).pipe(
      tap((res: EntityArrayResponseType) => {
        this.businessListSubject.next(res.body);
        this.businessSubject.next(res.body![0]);
        this.businessId = res.body![0].id;
      }),
    );
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBusinessIdentifier(business: Pick<IBusiness, 'id'>): string {
    return business.id;
  }

  compareBusiness(o1: Pick<IBusiness, 'id'> | null, o2: Pick<IBusiness, 'id'> | null): boolean {
    return o1 && o2 ? this.getBusinessIdentifier(o1) === this.getBusinessIdentifier(o2) : o1 === o2;
  }

  addBusinessToCollectionIfMissing<Type extends Pick<IBusiness, 'id'>>(
    businessCollection: Type[],
    ...businessesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const businesses: Type[] = businessesToCheck.filter(isPresent);
    if (businesses.length > 0) {
      const businessCollectionIdentifiers = businessCollection.map(businessItem => this.getBusinessIdentifier(businessItem)!);
      const businessesToAdd = businesses.filter(businessItem => {
        const businessIdentifier = this.getBusinessIdentifier(businessItem);
        if (businessCollectionIdentifiers.includes(businessIdentifier)) {
          return false;
        }
        businessCollectionIdentifiers.push(businessIdentifier);
        return true;
      });
      return [...businessesToAdd, ...businessCollection];
    }
    return businessCollection;
  }

  public getSelectedBusiness(): string | null {
    return this.businessId;
  }

  public changeSelectedBusiness(business: IBusiness) {
    this.businessSubject.next(business);
    return;
  }
}
