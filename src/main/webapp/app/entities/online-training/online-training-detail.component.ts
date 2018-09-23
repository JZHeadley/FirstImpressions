import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOnlineTraining } from 'app/shared/model/online-training.model';

@Component({
    selector: 'jhi-online-training-detail',
    templateUrl: './online-training-detail.component.html'
})
export class OnlineTrainingDetailComponent implements OnInit {
    onlineTraining: IOnlineTraining;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ onlineTraining }) => {
            this.onlineTraining = onlineTraining;
        });
    }

    previousState() {
        window.history.back();
    }
}
