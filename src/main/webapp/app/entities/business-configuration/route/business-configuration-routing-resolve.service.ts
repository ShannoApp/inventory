import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBusinessConfiguration } from '../business-configuration.model';
import { BusinessConfigurationService } from '../service/business-configuration.service';

export const businessConfigurationResolve = (route: ActivatedRouteSnapshot): Observable<null | IBusinessConfiguration> => {
  const id = route.params['id'];
  if (id) {
    return inject(BusinessConfigurationService)
      .find(id)
      .pipe(
        mergeMap((businessConfiguration: HttpResponse<IBusinessConfiguration>) => {
          if (businessConfiguration.body) {
            return of(businessConfiguration.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default businessConfigurationResolve;
