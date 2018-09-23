/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FirstImpressionsTestModule } from '../../../test.module';
import { OnlineTrainingUpdateComponent } from 'app/entities/online-training/online-training-update.component';
import { OnlineTrainingService } from 'app/entities/online-training/online-training.service';
import { OnlineTraining } from 'app/shared/model/online-training.model';

describe('Component Tests', () => {
    describe('OnlineTraining Management Update Component', () => {
        let comp: OnlineTrainingUpdateComponent;
        let fixture: ComponentFixture<OnlineTrainingUpdateComponent>;
        let service: OnlineTrainingService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FirstImpressionsTestModule],
                declarations: [OnlineTrainingUpdateComponent]
            })
                .overrideTemplate(OnlineTrainingUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OnlineTrainingUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OnlineTrainingService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new OnlineTraining(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.onlineTraining = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new OnlineTraining();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.onlineTraining = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
