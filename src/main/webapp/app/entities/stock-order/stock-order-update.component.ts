import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IStockOrder } from 'app/shared/model/stock-order.model';
import { StockOrderService } from './stock-order.service';

@Component({
    selector: 'jhi-stock-order-update',
    templateUrl: './stock-order-update.component.html'
})
export class StockOrderUpdateComponent implements OnInit {
    stockOrder: IStockOrder;
    isSaving: boolean;

    constructor(protected stockOrderService: StockOrderService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ stockOrder }) => {
            this.stockOrder = stockOrder;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.stockOrder.id !== undefined) {
            this.subscribeToSaveResponse(this.stockOrderService.update(this.stockOrder));
        } else {
            this.subscribeToSaveResponse(this.stockOrderService.create(this.stockOrder));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IStockOrder>>) {
        result.subscribe((res: HttpResponse<IStockOrder>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
