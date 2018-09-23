import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOnlineTraining } from 'app/shared/model/online-training.model';
import { OnlineTrainingService } from './online-training.service';

@Component({
    selector: 'jhi-online-training-delete-dialog',
    templateUrl: './online-training-delete-dialog.component.html'
})
export class OnlineTrainingDeleteDialogComponent {
    onlineTraining: IOnlineTraining;

    constructor(
        private onlineTrainingService: OnlineTrainingService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.onlineTrainingService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'onlineTrainingListModification',
                content: 'Deleted an onlineTraining'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-online-training-delete-popup',
    template: ''
})
export class OnlineTrainingDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ onlineTraining }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(OnlineTrainingDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.onlineTraining = onlineTraining;
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
