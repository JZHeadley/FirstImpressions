import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IClothingCompany } from 'app/shared/model/clothing-company.model';
import { Principal } from 'app/core';
import { ClothingCompanyService } from './clothing-company.service';

@Component({
    selector: 'jhi-clothing-company',
    templateUrl: './clothing-company.component.html'
})
export class ClothingCompanyComponent implements OnInit, OnDestroy {
    clothingCompanies: IClothingCompany[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private clothingCompanyService: ClothingCompanyService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.clothingCompanyService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IClothingCompany[]>) => (this.clothingCompanies = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.clothingCompanyService.query().subscribe(
            (res: HttpResponse<IClothingCompany[]>) => {
                this.clothingCompanies = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInClothingCompanies();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IClothingCompany) {
        return item.id;
    }

    registerChangeInClothingCompanies() {
        this.eventSubscriber = this.eventManager.subscribe('clothingCompanyListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
