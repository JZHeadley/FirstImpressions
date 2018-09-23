import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IInterviewResources } from 'app/shared/model/interview-resources.model';
import { Principal } from 'app/core';
import { InterviewResourcesService } from './interview-resources.service';

@Component({
    selector: 'jhi-interview-resources',
    templateUrl: './interview-resources.component.html',
    styleUrls: ['interview-resources.component.scss']
})
export class InterviewResourcesComponent implements OnInit, OnDestroy {
    interviewResources: IInterviewResources[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private interviewResourcesService: InterviewResourcesService,
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
            this.interviewResourcesService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IInterviewResources[]>) => (this.interviewResources = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.interviewResourcesService.query().subscribe(
            (res: HttpResponse<IInterviewResources[]>) => {
                this.interviewResources = res.body;
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
        this.registerChangeInInterviewResources();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IInterviewResources) {
        return item.id;
    }

    registerChangeInInterviewResources() {
        this.eventSubscriber = this.eventManager.subscribe('interviewResourcesListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
