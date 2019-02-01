import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStockItem } from 'app/shared/model/stock-item.model';
import { StockItemService } from './stock-item.service';

@Component({
    selector: 'jhi-stock-item-delete-dialog',
    templateUrl: './stock-item-delete-dialog.component.html'
})
export class StockItemDeleteDialogComponent {
    stockItem: IStockItem;

    constructor(
        protected stockItemService: StockItemService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.stockItemService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'stockItemListModification',
                content: 'Deleted an stockItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-stock-item-delete-popup',
    template: ''
})
export class StockItemDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ stockItem }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(StockItemDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.stockItem = stockItem;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/stock-item', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/stock-item', { outlets: { popup: null } }]);
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
