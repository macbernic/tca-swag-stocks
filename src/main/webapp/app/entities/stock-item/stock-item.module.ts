import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TcaSwagStocksSharedModule } from 'app/shared';
import {
    StockItemComponent,
    StockItemDetailComponent,
    StockItemUpdateComponent,
    StockItemDeletePopupComponent,
    StockItemDeleteDialogComponent,
    stockItemRoute,
    stockItemPopupRoute
} from './';

const ENTITY_STATES = [...stockItemRoute, ...stockItemPopupRoute];

@NgModule({
    imports: [TcaSwagStocksSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        StockItemComponent,
        StockItemDetailComponent,
        StockItemUpdateComponent,
        StockItemDeleteDialogComponent,
        StockItemDeletePopupComponent
    ],
    entryComponents: [StockItemComponent, StockItemUpdateComponent, StockItemDeleteDialogComponent, StockItemDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcaSwagStocksStockItemModule {}
