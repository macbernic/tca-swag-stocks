import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TcaSwagStocksSharedModule } from 'app/shared';
import {
    ProductAssetComponent,
    ProductAssetDetailComponent,
    ProductAssetUpdateComponent,
    ProductAssetDeletePopupComponent,
    ProductAssetDeleteDialogComponent,
    productAssetRoute,
    productAssetPopupRoute
} from './';

const ENTITY_STATES = [...productAssetRoute, ...productAssetPopupRoute];

@NgModule({
    imports: [TcaSwagStocksSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProductAssetComponent,
        ProductAssetDetailComponent,
        ProductAssetUpdateComponent,
        ProductAssetDeleteDialogComponent,
        ProductAssetDeletePopupComponent
    ],
    entryComponents: [
        ProductAssetComponent,
        ProductAssetUpdateComponent,
        ProductAssetDeleteDialogComponent,
        ProductAssetDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcaSwagStocksProductAssetModule {}
