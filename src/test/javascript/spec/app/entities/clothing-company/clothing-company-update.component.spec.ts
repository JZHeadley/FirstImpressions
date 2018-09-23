/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FirstImpressionsTestModule } from '../../../test.module';
import { ClothingCompanyUpdateComponent } from 'app/entities/clothing-company/clothing-company-update.component';
import { ClothingCompanyService } from 'app/entities/clothing-company/clothing-company.service';
import { ClothingCompany } from 'app/shared/model/clothing-company.model';

describe('Component Tests', () => {
    describe('ClothingCompany Management Update Component', () => {
        let comp: ClothingCompanyUpdateComponent;
        let fixture: ComponentFixture<ClothingCompanyUpdateComponent>;
        let service: ClothingCompanyService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FirstImpressionsTestModule],
                declarations: [ClothingCompanyUpdateComponent]
            })
                .overrideTemplate(ClothingCompanyUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ClothingCompanyUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ClothingCompanyService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ClothingCompany(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.clothingCompany = entity;
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
                    const entity = new ClothingCompany();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.clothingCompany = entity;
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
