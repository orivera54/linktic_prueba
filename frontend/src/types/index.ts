export type ProductStatus = 'ACTIVE' | 'INACTIVE';

export interface Product {
    id: string;
    sku: string;
    name: string;
    price: number;
    status: ProductStatus;
    createdAt: string;
    updatedAt: string;
}

export interface Page<T> {
    content: T[];
    pageable: {
        pageNumber: number;
        pageSize: number;
        sort: {
            empty: boolean;
            sorted: boolean;
            unsorted: boolean;
        };
        offset: number;
        paged: boolean;
        unpaged: boolean;
    };
    last: boolean;
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
    sort: {
        empty: boolean;
        sorted: boolean;
        unsorted: boolean;
    };
    first: boolean;
    numberOfElements: number;
    empty: boolean;
}

export interface PurchaseRequest {
    productId: string;
    quantity: number;
}

