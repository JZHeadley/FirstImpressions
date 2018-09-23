/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FirstImpressionsTestModule } from '../../../test.module';
import { OnlineTrainingDetailComponent } from 'app/entities/online-training/online-training-detail.component';
import { OnlineTraining } from 'app/shared/model/online-training.model';

describe('Component Tests', () => {
    describe('OnlineTraining Management Detail Component', () => {
        let comp: OnlineTrainingDetailComponent;
        let fixture: ComponentFixture<OnlineTrainingDetailComponent>;
        const route = ({ data: of({ onlineTraining: new OnlineTraining(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FirstImpressionsTestModule],
                declarations: [OnlineTrainingDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(OnlineTrainingDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OnlineTrainingDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.onlineTraining).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
