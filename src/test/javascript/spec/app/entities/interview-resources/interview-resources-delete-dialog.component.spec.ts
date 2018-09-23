/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FirstImpressionsTestModule } from '../../../test.module';
import { InterviewResourcesDeleteDialogComponent } from 'app/entities/interview-resources/interview-resources-delete-dialog.component';
import { InterviewResourcesService } from 'app/entities/interview-resources/interview-resources.service';

describe('Component Tests', () => {
    describe('InterviewResources Management Delete Component', () => {
        let comp: InterviewResourcesDeleteDialogComponent;
        let fixture: ComponentFixture<InterviewResourcesDeleteDialogComponent>;
        let service: InterviewResourcesService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FirstImpressionsTestModule],
                declarations: [InterviewResourcesDeleteDialogComponent]
            })
                .overrideTemplate(InterviewResourcesDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(InterviewResourcesDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InterviewResourcesService);
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
