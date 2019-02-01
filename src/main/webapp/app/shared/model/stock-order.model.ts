import { IStockItem } from 'app/shared/model/stock-item.model';

export const enum OrderStatus {
    DRAFT = 'DRAFT',
    SUBMITTED = 'SUBMITTED',
    PAYED = 'PAYED',
    DELIVERED = 'DELIVERED',
    ARCHIVED = 'ARCHIVED'
}

export interface IStockOrder {
    id?: number;
    orderId?: string;
    first?: boolean;
    amount?: number;
    status?: OrderStatus;
    items?: IStockItem[];
}

export class StockOrder implements IStockOrder {
    constructor(
        public id?: number,
        public orderId?: string,
        public first?: boolean,
        public amount?: number,
        public status?: OrderStatus,
        public items?: IStockItem[]
    ) {
        this.first = this.first || false;
    }
}
