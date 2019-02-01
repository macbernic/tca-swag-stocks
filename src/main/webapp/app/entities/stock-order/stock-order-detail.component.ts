import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStockOrder } from 'app/shared/model/stock-order.model';

@Component({
    selector: 'jhi-stock-order-detail',
    templateUrl: './stock-order-detail.component.html'
})
export class StockOrderDetailComponent implements OnInit {
    stockOrder: IStockOrder;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ stockOrder }) => {
            this.stockOrder = stockOrder;
        });
    }

    previousState() {
        window.history.back();
    }
}
