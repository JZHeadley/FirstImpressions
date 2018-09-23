import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInterviewResources } from 'app/shared/model/interview-resources.model';
import { InterviewResourcesService } from './interview-resources.service';

@Component({
    selector: 'jhi-interview-resources-delete-dialog',
    templateUrl: './interview-resources-delete-dialog.component.html'
})
export class InterviewResourcesDeleteDialogComponent {
    interviewResources: IInterviewResources;

    constructor(
        private interviewResourcesService: InterviewResourcesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.interviewResourcesService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'interviewResourcesListModification',
                content: 'Deleted an interviewResources'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-interview-resources-delete-popup',
    template: ''
})
export class InterviewResourcesDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ interviewResources }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(InterviewResourcesDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.interviewResources = interviewResources;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
