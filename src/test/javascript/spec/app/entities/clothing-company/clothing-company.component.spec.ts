/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FirstImpressionsTestModule } from '../../../test.module';
import { ClothingCompanyComponent } from 'app/entities/clothing-company/clothing-company.component';
import { ClothingCompanyService } from 'app/entities/clothing-company/clothing-company.service';
import { ClothingCompany } from 'app/shared/model/clothing-company.model';

describe('Component Tests', () => {
    describe('ClothingCompany Management Component', () => {
        let comp: ClothingCompanyComponent;
        let fixture: ComponentFixture<ClothingCompanyComponent>;
        let service: ClothingCompanyService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FirstImpressionsTestModule],
                declarations: [ClothingCompanyComponent],
                providers: []
            })
                .overrideTemplate(ClothingCompanyComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ClothingCompanyComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ClothingCompanyService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new ClothingCompany(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.clothingCompanies[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
