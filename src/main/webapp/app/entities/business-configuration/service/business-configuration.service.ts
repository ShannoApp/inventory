import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, switchMap, throwError } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBusinessConfiguration, NewBusinessConfiguration } from '../business-configuration.model';
import { BusinessService } from '../../business/service/business.service';
import { IBusiness } from 'app/entities/business/business.model';

export type PartialUpdateBusinessConfiguration = Partial<IBusinessConfiguration> & Pick<IBusinessConfiguration, 'id'>;

export type EntityResponseType = HttpResponse<IBusinessConfiguration>;
export type EntityArrayResponseType = HttpResponse<IBusinessConfiguration[]>;

@Injectable({ providedIn: 'root' })
export class BusinessConfigurationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/business-configurations');
  business: IBusiness | null = null;

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
    protected businessService: BusinessService,
  ) {
    this.businessService.businessData$.subscribe(b => {
      this.business = b;
    });
  }

  create(businessConfiguration: NewBusinessConfiguration): Observable<EntityResponseType> {
    return this.http.post<IBusinessConfiguration>(this.resourceUrl, businessConfiguration, { observe: 'response' });
  }

  update(businessConfiguration: IBusinessConfiguration): Observable<EntityResponseType> {
    return this.http.put<IBusinessConfiguration>(
      `${this.resourceUrl}/${this.getBusinessConfigurationIdentifier(businessConfiguration)}`,
      businessConfiguration,
      { observe: 'response' },
    );
  }

  partialUpdate(businessConfiguration: PartialUpdateBusinessConfiguration): Observable<EntityResponseType> {
    return this.http.patch<IBusinessConfiguration>(
      `${this.resourceUrl}/${this.getBusinessConfigurationIdentifier(businessConfiguration)}`,
      businessConfiguration,
      { observe: 'response' },
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IBusinessConfiguration>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    if (this.business) {
      const resourceUrlForQuery = this.applicationConfigService.getEndpointFor(`api/${this.business.id}/business-configurations`);
      return this.http.get<IBusinessConfiguration[]>(resourceUrlForQuery, { params: options, observe: 'response' });
    } else {
      return this.businessService.query().pipe(
        switchMap(res => {
          const businessId = res.body?.find(business => business.deflaut)?.id;
          if (businessId) {
            const resourceUrlForQuery = this.applicationConfigService.getEndpointFor(`api/${businessId}/business-configurations`);
            return this.http.get<IBusinessConfiguration[]>(resourceUrlForQuery, { params: options, observe: 'response' });
          } else {
            // Handle the case where there are no businesses or the first business doesn't have an ID
            return throwError('No business configurations found');
          }
        }),
      );
    }
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBusinessConfigurationIdentifier(businessConfiguration: Pick<IBusinessConfiguration, 'id'>): string {
    return businessConfiguration.id;
  }

  compareBusinessConfiguration(o1: Pick<IBusinessConfiguration, 'id'> | null, o2: Pick<IBusinessConfiguration, 'id'> | null): boolean {
    return o1 && o2 ? this.getBusinessConfigurationIdentifier(o1) === this.getBusinessConfigurationIdentifier(o2) : o1 === o2;
  }

  addBusinessConfigurationToCollectionIfMissing<Type extends Pick<IBusinessConfiguration, 'id'>>(
    businessConfigurationCollection: Type[],
    ...businessConfigurationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const businessConfigurations: Type[] = businessConfigurationsToCheck.filter(isPresent);
    if (businessConfigurations.length > 0) {
      const businessConfigurationCollectionIdentifiers = businessConfigurationCollection.map(
        businessConfigurationItem => this.getBusinessConfigurationIdentifier(businessConfigurationItem)!,
      );
      const businessConfigurationsToAdd = businessConfigurations.filter(businessConfigurationItem => {
        const businessConfigurationIdentifier = this.getBusinessConfigurationIdentifier(businessConfigurationItem);
        if (businessConfigurationCollectionIdentifiers.includes(businessConfigurationIdentifier)) {
          return false;
        }
        businessConfigurationCollectionIdentifiers.push(businessConfigurationIdentifier);
        return true;
      });
      return [...businessConfigurationsToAdd, ...businessConfigurationCollection];
    }
    return businessConfigurationCollection;
  }
}
