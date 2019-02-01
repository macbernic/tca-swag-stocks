import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IStockItem } from 'app/shared/model/stock-item.model';
import { StockItemService } from './stock-item.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';
import { IProductSku } from 'app/shared/model/product-sku.model';
import { ProductSkuService } from 'app/entities/product-sku';
import { IStockOrder } from 'app/shared/model/stock-order.model';
import { StockOrderService } from 'app/entities/stock-order';

@Component({
    selector: 'jhi-stock-item-update',
    templateUrl: './stock-item-update.component.html'
})
export class StockItemUpdateComponent implements OnInit {
    stockItem: IStockItem;
    isSaving: boolean;

    products: IProduct[];

    productskus: IProductSku[];

    stockorders: IStockOrder[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected stockItemService: StockItemService,
        protected productService: ProductService,
        protected productSkuService: ProductSkuService,
        protected stockOrderService: StockOrderService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ stockItem }) => {
            this.stockItem = stockItem;
        });
        this.productService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IProduct[]>) => mayBeOk.ok),
                map((response: HttpResponse<IProduct[]>) => response.body)
            )
            .subscribe((res: IProduct[]) => (this.products = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.productSkuService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IProductSku[]>) => mayBeOk.ok),
                map((response: HttpResponse<IProductSku[]>) => response.body)
            )
            .subscribe((res: IProductSku[]) => (this.productskus = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.stockOrderService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IStockOrder[]>) => mayBeOk.ok),
                map((response: HttpResponse<IStockOrder[]>) => response.body)
            )
            .subscribe((res: IStockOrder[]) => (this.stockorders = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.stockItem.id !== undefined) {
            this.subscribeToSaveResponse(this.stockItemService.update(this.stockItem));
        } else {
            this.subscribeToSaveResponse(this.stockItemService.create(this.stockItem));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IStockItem>>) {
        result.subscribe((res: HttpResponse<IStockItem>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackProductById(index: number, item: IProduct) {
        return item.id;
    }

    trackProductSkuById(index: number, item: IProductSku) {
        return item.id;
    }

    trackStockOrderById(index: number, item: IStockOrder) {
        return item.id;
    }
}
