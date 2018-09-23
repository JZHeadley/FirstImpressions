/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FirstImpressionsTestModule } from '../../../test.module';
import { ClothingCompanyDetailComponent } from 'app/entities/clothing-company/clothing-company-detail.component';
import { ClothingCompany } from 'app/shared/model/clothing-company.model';

describe('Component Tests', () => {
    describe('ClothingCompany Management Detail Component', () => {
        let comp: ClothingCompanyDetailComponent;
        let fixture: ComponentFixture<ClothingCompanyDetailComponent>;
        const route = ({ data: of({ clothingCompany: new ClothingCompany(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FirstImpressionsTestModule],
                declarations: [ClothingCompanyDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ClothingCompanyDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ClothingCompanyDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.clothingCompany).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
