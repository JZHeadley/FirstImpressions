/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FirstImpressionsTestModule } from '../../../test.module';
import { OnlineTrainingComponent } from 'app/entities/online-training/online-training.component';
import { OnlineTrainingService } from 'app/entities/online-training/online-training.service';
import { OnlineTraining } from 'app/shared/model/online-training.model';

describe('Component Tests', () => {
    describe('OnlineTraining Management Component', () => {
        let comp: OnlineTrainingComponent;
        let fixture: ComponentFixture<OnlineTrainingComponent>;
        let service: OnlineTrainingService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FirstImpressionsTestModule],
                declarations: [OnlineTrainingComponent],
                providers: []
            })
                .overrideTemplate(OnlineTrainingComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OnlineTrainingComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OnlineTrainingService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new OnlineTraining(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.onlineTrainings[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
