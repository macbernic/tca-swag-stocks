import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IProductSku } from 'app/shared/model/product-sku.model';
import { ProductSkuService } from './product-sku.service';

@Component({
    selector: 'jhi-product-sku-update',
    templateUrl: './product-sku-update.component.html'
})
export class ProductSkuUpdateComponent implements OnInit {
    productSku: IProductSku;
    isSaving: boolean;

    constructor(protected productSkuService: ProductSkuService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ productSku }) => {
            this.productSku = productSku;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.productSku.id !== undefined) {
            this.subscribeToSaveResponse(this.productSkuService.update(this.productSku));
        } else {
            this.subscribeToSaveResponse(this.productSkuService.create(this.productSku));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductSku>>) {
        result.subscribe((res: HttpResponse<IProductSku>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
