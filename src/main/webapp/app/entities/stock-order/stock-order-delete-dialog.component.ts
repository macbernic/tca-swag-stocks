import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStockOrder } from 'app/shared/model/stock-order.model';
import { StockOrderService } from './stock-order.service';

@Component({
    selector: 'jhi-stock-order-delete-dialog',
    templateUrl: './stock-order-delete-dialog.component.html'
})
export class StockOrderDeleteDialogComponent {
    stockOrder: IStockOrder;

    constructor(
        protected stockOrderService: StockOrderService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.stockOrderService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'stockOrderListModification',
                content: 'Deleted an stockOrder'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-stock-order-delete-popup',
    template: ''
})
export class StockOrderDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ stockOrder }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(StockOrderDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.stockOrder = stockOrder;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/stock-order', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/stock-order', { outlets: { popup: null } }]);
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
