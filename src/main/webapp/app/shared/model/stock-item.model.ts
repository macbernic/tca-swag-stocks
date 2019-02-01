import { IProduct } from 'app/shared/model/product.model';
import { IProductSku } from 'app/shared/model/product-sku.model';
import { IStockOrder } from 'app/shared/model/stock-order.model';

export interface IStockItem {
    id?: number;
    customText?: string;
    appliedPrice?: number;
    fromStock?: boolean;
    product?: IProduct;
    productSku?: IProductSku;
    memberOrder?: IStockOrder;
}

export class StockItem implements IStockItem {
    constructor(
        public id?: number,
        public customText?: string,
        public appliedPrice?: number,
        public fromStock?: boolean,
        public product?: IProduct,
        public productSku?: IProductSku,
        public memberOrder?: IStockOrder
    ) {
        this.fromStock = this.fromStock || false;
    }
}
