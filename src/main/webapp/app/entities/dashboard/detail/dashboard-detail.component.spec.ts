import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DashboardDetailComponent } from './dashboard-detail.component';

describe('Dashboard Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DashboardDetailComponent,
              resolve: { dashboard: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DashboardDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load dashboard on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DashboardDetailComponent);

      // THEN
      expect(instance.dashboard).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
