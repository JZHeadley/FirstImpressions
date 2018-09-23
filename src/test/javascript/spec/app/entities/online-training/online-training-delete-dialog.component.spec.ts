/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FirstImpressionsTestModule } from '../../../test.module';
import { OnlineTrainingDeleteDialogComponent } from 'app/entities/online-training/online-training-delete-dialog.component';
import { OnlineTrainingService } from 'app/entities/online-training/online-training.service';

describe('Component Tests', () => {
    describe('OnlineTraining Management Delete Component', () => {
        let comp: OnlineTrainingDeleteDialogComponent;
        let fixture: ComponentFixture<OnlineTrainingDeleteDialogComponent>;
        let service: OnlineTrainingService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FirstImpressionsTestModule],
                declarations: [OnlineTrainingDeleteDialogComponent]
            })
                .overrideTemplate(OnlineTrainingDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OnlineTrainingDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OnlineTrainingService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
