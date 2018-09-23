import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IInterviewResources } from 'app/shared/model/interview-resources.model';
import { InterviewResourcesService } from './interview-resources.service';

@Component({
    selector: 'jhi-interview-resources-update',
    templateUrl: './interview-resources-update.component.html'
})
export class InterviewResourcesUpdateComponent implements OnInit {
    private _interviewResources: IInterviewResources;
    isSaving: boolean;

    constructor(private interviewResourcesService: InterviewResourcesService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ interviewResources }) => {
            this.interviewResources = interviewResources;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.interviewResources.id !== undefined) {
            this.subscribeToSaveResponse(this.interviewResourcesService.update(this.interviewResources));
        } else {
            this.subscribeToSaveResponse(this.interviewResourcesService.create(this.interviewResources));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IInterviewResources>>) {
        result.subscribe((res: HttpResponse<IInterviewResources>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get interviewResources() {
        return this._interviewResources;
    }

    set interviewResources(interviewResources: IInterviewResources) {
        this._interviewResources = interviewResources;
    }
}
