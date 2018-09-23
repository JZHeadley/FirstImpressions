import { NgModule } from '@angular/core';

import { FirstImpressionsSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [FirstImpressionsSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [FirstImpressionsSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class FirstImpressionsSharedCommonModule {}
