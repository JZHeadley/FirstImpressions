/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FirstImpressionsTestModule } from '../../../test.module';
import { InterviewResourcesComponent } from 'app/entities/interview-resources/interview-resources.component';
import { InterviewResourcesService } from 'app/entities/interview-resources/interview-resources.service';
import { InterviewResources } from 'app/shared/model/interview-resources.model';

describe('Component Tests', () => {
    describe('InterviewResources Management Component', () => {
        let comp: InterviewResourcesComponent;
        let fixture: ComponentFixture<InterviewResourcesComponent>;
        let service: InterviewResourcesService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FirstImpressionsTestModule],
                declarations: [InterviewResourcesComponent],
                providers: []
            })
                .overrideTemplate(InterviewResourcesComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(InterviewResourcesComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InterviewResourcesService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new InterviewResources(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.interviewResources[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
