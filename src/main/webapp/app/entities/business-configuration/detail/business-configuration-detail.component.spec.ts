import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BusinessConfigurationDetailComponent } from './business-configuration-detail.component';

describe('BusinessConfiguration Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BusinessConfigurationDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: BusinessConfigurationDetailComponent,
              resolve: { businessConfiguration: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BusinessConfigurationDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load businessConfiguration on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BusinessConfigurationDetailComponent);

      // THEN
      expect(instance.businessConfiguration).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
