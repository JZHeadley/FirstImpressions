import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { FirstImpressionsJobModule } from './job/job.module';
import { FirstImpressionsProgramModule } from './program/program.module';
import { FirstImpressionsLocationModule } from './location/location.module';
import { FirstImpressionsClothingCompanyModule } from './clothing-company/clothing-company.module';
import { FirstImpressionsOnlineTrainingModule } from './online-training/online-training.module';
import { FirstImpressionsInterviewResourcesModule } from './interview-resources/interview-resources.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        FirstImpressionsJobModule,
        FirstImpressionsProgramModule,
        FirstImpressionsLocationModule,
        FirstImpressionsClothingCompanyModule,
        FirstImpressionsOnlineTrainingModule,
        FirstImpressionsInterviewResourcesModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FirstImpressionsEntityModule {}
