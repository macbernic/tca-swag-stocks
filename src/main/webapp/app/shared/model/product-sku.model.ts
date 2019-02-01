export const enum GenderType {
    MALE = 'MALE',
    FEMALE = 'FEMALE',
    UNISEX = 'UNISEX'
}

export const enum ProductSize {
    UNIQUE = 'UNIQUE',
    XXSMALL = 'XXSMALL',
    XSMALL = 'XSMALL',
    SMALL = 'SMALL',
    MEDIUM = 'MEDIUM',
    LARGE = 'LARGE',
    XLARGE = 'XLARGE',
    XXLARGE = 'XXLARGE',
    SPECIAL = 'SPECIAL'
}

export interface IProductSku {
    id?: number;
    gender?: GenderType;
    size?: ProductSize;
}

export class ProductSku implements IProductSku {
    constructor(public id?: number, public gender?: GenderType, public size?: ProductSize) {}
}
