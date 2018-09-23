import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FirstImpressionsSharedModule } from 'app/shared';
import {
    InterviewResourcesComponent,
    InterviewResourcesDetailComponent,
    InterviewResourcesUpdateComponent,
    InterviewResourcesDeletePopupComponent,
    InterviewResourcesDeleteDialogComponent,
    interviewResourcesRoute,
    interviewResourcesPopupRoute
} from './';

const ENTITY_STATES = [...interviewResourcesRoute, ...interviewResourcesPopupRoute];

@NgModule({
    imports: [FirstImpressionsSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        InterviewResourcesComponent,
        InterviewResourcesDetailComponent,
        InterviewResourcesUpdateComponent,
        InterviewResourcesDeleteDialogComponent,
        InterviewResourcesDeletePopupComponent
    ],
    entryComponents: [
        InterviewResourcesComponent,
        InterviewResourcesUpdateComponent,
        InterviewResourcesDeleteDialogComponent,
        InterviewResourcesDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FirstImpressionsInterviewResourcesModule {}
