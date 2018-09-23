import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { OnlineTraining } from 'app/shared/model/online-training.model';
import { OnlineTrainingService } from './online-training.service';
import { OnlineTrainingComponent } from './online-training.component';
import { OnlineTrainingDetailComponent } from './online-training-detail.component';
import { OnlineTrainingUpdateComponent } from './online-training-update.component';
import { OnlineTrainingDeletePopupComponent } from './online-training-delete-dialog.component';
import { IOnlineTraining } from 'app/shared/model/online-training.model';

@Injectable({ providedIn: 'root' })
export class OnlineTrainingResolve implements Resolve<IOnlineTraining> {
    constructor(private service: OnlineTrainingService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((onlineTraining: HttpResponse<OnlineTraining>) => onlineTraining.body));
        }
        return of(new OnlineTraining());
    }
}

export const onlineTrainingRoute: Routes = [
    {
        path: 'online-training',
        component: OnlineTrainingComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'OnlineTrainings'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'online-training/:id/view',
        component: OnlineTrainingDetailComponent,
        resolve: {
            onlineTraining: OnlineTrainingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'OnlineTrainings'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'online-training/new',
        component: OnlineTrainingUpdateComponent,
        resolve: {
            onlineTraining: OnlineTrainingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'OnlineTrainings'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'online-training/:id/edit',
        component: OnlineTrainingUpdateComponent,
        resolve: {
            onlineTraining: OnlineTrainingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'OnlineTrainings'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const onlineTrainingPopupRoute: Routes = [
    {
        path: 'online-training/:id/delete',
        component: OnlineTrainingDeletePopupComponent,
        resolve: {
            onlineTraining: OnlineTrainingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'OnlineTrainings'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
