import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute, FinanceRoute, legalCounselRoute, navbarRoute, transportRoute } from './layouts';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute, transportRoute, FinanceRoute, legalCounselRoute];

@NgModule({
    imports: [
        RouterModule.forRoot(
            [
                ...LAYOUT_ROUTES,
                {
                    path: 'admin',
                    loadChildren: './admin/admin.module#FirstImpressionsAdminModule'
                }
            ],
            { useHash: true, enableTracing: DEBUG_INFO_ENABLED }
        )
    ],
    exports: [RouterModule]
})
export class FirstImpressionsAppRoutingModule {}
