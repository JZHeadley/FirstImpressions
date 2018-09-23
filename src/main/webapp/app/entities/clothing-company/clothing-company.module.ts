import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FirstImpressionsSharedModule } from 'app/shared';
import {
    ClothingCompanyComponent,
    ClothingCompanyDetailComponent,
    ClothingCompanyUpdateComponent,
    ClothingCompanyDeletePopupComponent,
    ClothingCompanyDeleteDialogComponent,
    clothingCompanyRoute,
    clothingCompanyPopupRoute
} from './';

const ENTITY_STATES = [...clothingCompanyRoute, ...clothingCompanyPopupRoute];

@NgModule({
    imports: [FirstImpressionsSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ClothingCompanyComponent,
        ClothingCompanyDetailComponent,
        ClothingCompanyUpdateComponent,
        ClothingCompanyDeleteDialogComponent,
        ClothingCompanyDeletePopupComponent
    ],
    entryComponents: [
        ClothingCompanyComponent,
        ClothingCompanyUpdateComponent,
        ClothingCompanyDeleteDialogComponent,
        ClothingCompanyDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FirstImpressionsClothingCompanyModule {}
