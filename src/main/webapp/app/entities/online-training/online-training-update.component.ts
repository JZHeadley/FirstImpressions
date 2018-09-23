import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IOnlineTraining } from 'app/shared/model/online-training.model';
import { OnlineTrainingService } from './online-training.service';

@Component({
    selector: 'jhi-online-training-update',
    templateUrl: './online-training-update.component.html'
})
export class OnlineTrainingUpdateComponent implements OnInit {
    private _onlineTraining: IOnlineTraining;
    isSaving: boolean;

    constructor(private onlineTrainingService: OnlineTrainingService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ onlineTraining }) => {
            this.onlineTraining = onlineTraining;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.onlineTraining.id !== undefined) {
            this.subscribeToSaveResponse(this.onlineTrainingService.update(this.onlineTraining));
        } else {
            this.subscribeToSaveResponse(this.onlineTrainingService.create(this.onlineTraining));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IOnlineTraining>>) {
        result.subscribe((res: HttpResponse<IOnlineTraining>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get onlineTraining() {
        return this._onlineTraining;
    }

    set onlineTraining(onlineTraining: IOnlineTraining) {
        this._onlineTraining = onlineTraining;
    }
}
