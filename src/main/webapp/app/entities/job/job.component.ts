import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IJob } from 'app/shared/model/job.model';
import { Principal } from 'app/core';
import { JobService } from './job.service';

@Component({
    selector: 'jhi-job',
    templateUrl: './job.component.html',
    styleUrls: ['job.component.scss']
})
export class JobComponent implements OnInit, OnDestroy {
    jobs: IJob[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private jobService: JobService,
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
            this.jobService
                .search({
                    query: this.currentSearch
                })
                .subscribe((res: HttpResponse<IJob[]>) => (this.jobs = res.body), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.jobService.query().subscribe(
            (res: HttpResponse<IJob[]>) => {
                this.jobs = res.body;
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
        this.registerChangeInJobs();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IJob) {
        return item.id;
    }

    registerChangeInJobs() {
        this.eventSubscriber = this.eventManager.subscribe('jobListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
