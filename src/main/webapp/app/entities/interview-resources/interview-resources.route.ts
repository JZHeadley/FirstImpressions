import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { InterviewResources } from 'app/shared/model/interview-resources.model';
import { InterviewResourcesService } from './interview-resources.service';
import { InterviewResourcesComponent } from './interview-resources.component';
import { InterviewResourcesDetailComponent } from './interview-resources-detail.component';
import { InterviewResourcesUpdateComponent } from './interview-resources-update.component';
import { InterviewResourcesDeletePopupComponent } from './interview-resources-delete-dialog.component';
import { IInterviewResources } from 'app/shared/model/interview-resources.model';

@Injectable({ providedIn: 'root' })
export class InterviewResourcesResolve implements Resolve<IInterviewResources> {
    constructor(private service: InterviewResourcesService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((interviewResources: HttpResponse<InterviewResources>) => interviewResources.body));
        }
        return of(new InterviewResources());
    }
}

export const interviewResourcesRoute: Routes = [
    {
        path: 'interview-resources',
        component: InterviewResourcesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'InterviewResources'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'interview-resources/:id/view',
        component: InterviewResourcesDetailComponent,
        resolve: {
            interviewResources: InterviewResourcesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'InterviewResources'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'interview-resources/new',
        component: InterviewResourcesUpdateComponent,
        resolve: {
            interviewResources: InterviewResourcesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'InterviewResources'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'interview-resources/:id/edit',
        component: InterviewResourcesUpdateComponent,
        resolve: {
            interviewResources: InterviewResourcesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'InterviewResources'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const interviewResourcesPopupRoute: Routes = [
    {
        path: 'interview-resources/:id/delete',
        component: InterviewResourcesDeletePopupComponent,
        resolve: {
            interviewResources: InterviewResourcesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'InterviewResources'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
