import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClothingCompany } from 'app/shared/model/clothing-company.model';

@Component({
    selector: 'jhi-clothing-company-detail',
    templateUrl: './clothing-company-detail.component.html'
})
export class ClothingCompanyDetailComponent implements OnInit {
    clothingCompany: IClothingCompany;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ clothingCompany }) => {
            this.clothingCompany = clothingCompany;
        });
    }

    previousState() {
        window.history.back();
    }
}
