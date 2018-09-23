import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ClothingCompany } from 'app/shared/model/clothing-company.model';
import { ClothingCompanyService } from './clothing-company.service';
import { ClothingCompanyComponent } from './clothing-company.component';
import { ClothingCompanyDetailComponent } from './clothing-company-detail.component';
import { ClothingCompanyUpdateComponent } from './clothing-company-update.component';
import { ClothingCompanyDeletePopupComponent } from './clothing-company-delete-dialog.component';
import { IClothingCompany } from 'app/shared/model/clothing-company.model';

@Injectable({ providedIn: 'root' })
export class ClothingCompanyResolve implements Resolve<IClothingCompany> {
    constructor(private service: ClothingCompanyService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((clothingCompany: HttpResponse<ClothingCompany>) => clothingCompany.body));
        }
        return of(new ClothingCompany());
    }
}

export const clothingCompanyRoute: Routes = [
    {
        path: 'clothing-company',
        component: ClothingCompanyComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ClothingCompanies'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'clothing-company/:id/view',
        component: ClothingCompanyDetailComponent,
        resolve: {
            clothingCompany: ClothingCompanyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ClothingCompanies'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'clothing-company/new',
        component: ClothingCompanyUpdateComponent,
        resolve: {
            clothingCompany: ClothingCompanyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ClothingCompanies'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'clothing-company/:id/edit',
        component: ClothingCompanyUpdateComponent,
        resolve: {
            clothingCompany: ClothingCompanyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ClothingCompanies'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const clothingCompanyPopupRoute: Routes = [
    {
        path: 'clothing-company/:id/delete',
        component: ClothingCompanyDeletePopupComponent,
        resolve: {
            clothingCompany: ClothingCompanyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ClothingCompanies'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
