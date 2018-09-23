import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IClothingCompany } from 'app/shared/model/clothing-company.model';
import { ClothingCompanyService } from './clothing-company.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location';

@Component({
    selector: 'jhi-clothing-company-update',
    templateUrl: './clothing-company-update.component.html'
})
export class ClothingCompanyUpdateComponent implements OnInit {
    private _clothingCompany: IClothingCompany;
    isSaving: boolean;

    companylocations: ILocation[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private clothingCompanyService: ClothingCompanyService,
        private locationService: LocationService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ clothingCompany }) => {
            this.clothingCompany = clothingCompany;
        });
        this.locationService.query({ filter: 'clothingcompany-is-null' }).subscribe(
            (res: HttpResponse<ILocation[]>) => {
                if (!this.clothingCompany.companyLocation || !this.clothingCompany.companyLocation.id) {
                    this.companylocations = res.body;
                } else {
                    this.locationService.find(this.clothingCompany.companyLocation.id).subscribe(
                        (subRes: HttpResponse<ILocation>) => {
                            this.companylocations = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.clothingCompany.id !== undefined) {
            this.subscribeToSaveResponse(this.clothingCompanyService.update(this.clothingCompany));
        } else {
            this.subscribeToSaveResponse(this.clothingCompanyService.create(this.clothingCompany));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IClothingCompany>>) {
        result.subscribe((res: HttpResponse<IClothingCompany>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackLocationById(index: number, item: ILocation) {
        return item.id;
    }
    get clothingCompany() {
        return this._clothingCompany;
    }

    set clothingCompany(clothingCompany: IClothingCompany) {
        this._clothingCompany = clothingCompany;
    }
}
