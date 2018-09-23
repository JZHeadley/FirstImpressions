/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FirstImpressionsTestModule } from '../../../test.module';
import { InterviewResourcesDetailComponent } from 'app/entities/interview-resources/interview-resources-detail.component';
import { InterviewResources } from 'app/shared/model/interview-resources.model';

describe('Component Tests', () => {
    describe('InterviewResources Management Detail Component', () => {
        let comp: InterviewResourcesDetailComponent;
        let fixture: ComponentFixture<InterviewResourcesDetailComponent>;
        const route = ({ data: of({ interviewResources: new InterviewResources(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FirstImpressionsTestModule],
                declarations: [InterviewResourcesDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(InterviewResourcesDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(InterviewResourcesDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.interviewResources).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
