import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInterviewResources } from 'app/shared/model/interview-resources.model';

@Component({
    selector: 'jhi-interview-resources-detail',
    templateUrl: './interview-resources-detail.component.html'
})
export class InterviewResourcesDetailComponent implements OnInit {
    interviewResources: IInterviewResources;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ interviewResources }) => {
            this.interviewResources = interviewResources;
        });
    }

    previousState() {
        window.history.back();
    }
}
