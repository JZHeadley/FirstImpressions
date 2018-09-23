import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FirstImpressionsSharedModule } from 'app/shared';
import {
    OnlineTrainingComponent,
    OnlineTrainingDetailComponent,
    OnlineTrainingUpdateComponent,
    OnlineTrainingDeletePopupComponent,
    OnlineTrainingDeleteDialogComponent,
    onlineTrainingRoute,
    onlineTrainingPopupRoute
} from './';

const ENTITY_STATES = [...onlineTrainingRoute, ...onlineTrainingPopupRoute];

@NgModule({
    imports: [FirstImpressionsSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OnlineTrainingComponent,
        OnlineTrainingDetailComponent,
        OnlineTrainingUpdateComponent,
        OnlineTrainingDeleteDialogComponent,
        OnlineTrainingDeletePopupComponent
    ],
    entryComponents: [
        OnlineTrainingComponent,
        OnlineTrainingUpdateComponent,
        OnlineTrainingDeleteDialogComponent,
        OnlineTrainingDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FirstImpressionsOnlineTrainingModule {}
