import { Route } from '@angular/router';

import { NavbarComponent } from './navbar.component';
import { TransportComponent } from 'app/layouts/transport/transport.component';
import { LegalCounselComponent } from 'app/layouts/legal-counsel/legal-counsel.component';
import { FinanceComponent } from 'app/layouts/finance/finance.component';

export const navbarRoute: Route = {
    path: '',
    component: NavbarComponent,
    outlet: 'navbar'
};

export const transportRoute: Route = {
    path: 'transport',
    component: TransportComponent
};

export const legalCounselRoute: Route = {
    path: 'legal-counsel',
    component: LegalCounselComponent
};

export const FinanceRoute: Route = {
    path: 'finance',
    component: FinanceComponent
};
