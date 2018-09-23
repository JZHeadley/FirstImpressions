import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IOnlineTraining } from 'app/shared/model/online-training.model';
import { Principal } from 'app/core';
import { OnlineTrainingService } from './online-training.service';

@Component({
    selector: 'jhi-online-training',
    templateUrl: './online-training.component.html'
})
export class OnlineTrainingComponent implements OnInit, OnDestroy {
    onlineTrainings: IOnlineTraining[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private onlineTrainingService: OnlineTrainingService,
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
            this.onlineTrainingService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IOnlineTraining[]>) => (this.onlineTrainings = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.onlineTrainingService.query().subscribe(
            (res: HttpResponse<IOnlineTraining[]>) => {
                this.onlineTrainings = res.body;
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
        this.registerChangeInOnlineTrainings();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IOnlineTraining) {
        return item.id;
    }

    registerChangeInOnlineTrainings() {
        this.eventSubscriber = this.eventManager.subscribe('onlineTrainingListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
