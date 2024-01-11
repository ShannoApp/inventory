jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { BusinessConfigurationService } from '../service/business-configuration.service';

import { BusinessConfigurationDeleteDialogComponent } from './business-configuration-delete-dialog.component';

describe('BusinessConfiguration Management Delete Component', () => {
  let comp: BusinessConfigurationDeleteDialogComponent;
  let fixture: ComponentFixture<BusinessConfigurationDeleteDialogComponent>;
  let service: BusinessConfigurationService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, BusinessConfigurationDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(BusinessConfigurationDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BusinessConfigurationDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BusinessConfigurationService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete('ABC');
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith('ABC');
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
