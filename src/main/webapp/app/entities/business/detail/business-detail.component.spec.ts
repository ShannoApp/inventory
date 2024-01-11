import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BusinessDetailComponent } from './business-detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('Business Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, BusinessDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: BusinessDetailComponent,
              resolve: { business: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BusinessDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load business on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BusinessDetailComponent);

      // THEN
      expect(instance.business).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
