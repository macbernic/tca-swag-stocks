import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'product',
                loadChildren: './product/product.module#TcaSwagStocksProductModule'
            },
            {
                path: 'product-sku',
                loadChildren: './product-sku/product-sku.module#TcaSwagStocksProductSkuModule'
            },
            {
                path: 'product-asset',
                loadChildren: './product-asset/product-asset.module#TcaSwagStocksProductAssetModule'
            },
            {
                path: 'stock-order',
                loadChildren: './stock-order/stock-order.module#TcaSwagStocksStockOrderModule'
            },
            {
                path: 'stock-item',
                loadChildren: './stock-item/stock-item.module#TcaSwagStocksStockItemModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcaSwagStocksEntityModule {}
