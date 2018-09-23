/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FirstImpressionsTestModule } from '../../../test.module';
import { InterviewResourcesUpdateComponent } from 'app/entities/interview-resources/interview-resources-update.component';
import { InterviewResourcesService } from 'app/entities/interview-resources/interview-resources.service';
import { InterviewResources } from 'app/shared/model/interview-resources.model';

describe('Component Tests', () => {
    describe('InterviewResources Management Update Component', () => {
        let comp: InterviewResourcesUpdateComponent;
        let fixture: ComponentFixture<InterviewResourcesUpdateComponent>;
        let service: InterviewResourcesService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FirstImpressionsTestModule],
                declarations: [InterviewResourcesUpdateComponent]
            })
                .overrideTemplate(InterviewResourcesUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(InterviewResourcesUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InterviewResourcesService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new InterviewResources(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.interviewResources = entity;
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
                    const entity = new InterviewResources();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.interviewResources = entity;
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
