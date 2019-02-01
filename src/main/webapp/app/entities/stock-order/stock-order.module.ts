import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TcaSwagStocksSharedModule } from 'app/shared';
import {
    StockOrderComponent,
    StockOrderDetailComponent,
    StockOrderUpdateComponent,
    StockOrderDeletePopupComponent,
    StockOrderDeleteDialogComponent,
    stockOrderRoute,
    stockOrderPopupRoute
} from './';

const ENTITY_STATES = [...stockOrderRoute, ...stockOrderPopupRoute];

@NgModule({
    imports: [TcaSwagStocksSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        StockOrderComponent,
        StockOrderDetailComponent,
        StockOrderUpdateComponent,
        StockOrderDeleteDialogComponent,
        StockOrderDeletePopupComponent
    ],
    entryComponents: [StockOrderComponent, StockOrderUpdateComponent, StockOrderDeleteDialogComponent, StockOrderDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcaSwagStocksStockOrderModule {}
