import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClothingCompany } from 'app/shared/model/clothing-company.model';
import { ClothingCompanyService } from './clothing-company.service';

@Component({
    selector: 'jhi-clothing-company-delete-dialog',
    templateUrl: './clothing-company-delete-dialog.component.html'
})
export class ClothingCompanyDeleteDialogComponent {
    clothingCompany: IClothingCompany;

    constructor(
        private clothingCompanyService: ClothingCompanyService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.clothingCompanyService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'clothingCompanyListModification',
                content: 'Deleted an clothingCompany'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-clothing-company-delete-popup',
    template: ''
})
export class ClothingCompanyDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ clothingCompany }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ClothingCompanyDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.clothingCompany = clothingCompany;
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
