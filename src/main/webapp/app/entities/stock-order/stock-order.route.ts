import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StockOrder } from 'app/shared/model/stock-order.model';
import { StockOrderService } from './stock-order.service';
import { StockOrderComponent } from './stock-order.component';
import { StockOrderDetailComponent } from './stock-order-detail.component';
import { StockOrderUpdateComponent } from './stock-order-update.component';
import { StockOrderDeletePopupComponent } from './stock-order-delete-dialog.component';
import { IStockOrder } from 'app/shared/model/stock-order.model';

@Injectable({ providedIn: 'root' })
export class StockOrderResolve implements Resolve<IStockOrder> {
    constructor(private service: StockOrderService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IStockOrder> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<StockOrder>) => response.ok),
                map((stockOrder: HttpResponse<StockOrder>) => stockOrder.body)
            );
        }
        return of(new StockOrder());
    }
}

export const stockOrderRoute: Routes = [
    {
        path: '',
        component: StockOrderComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'StockOrders'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: StockOrderDetailComponent,
        resolve: {
            stockOrder: StockOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'StockOrders'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: StockOrderUpdateComponent,
        resolve: {
            stockOrder: StockOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'StockOrders'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: StockOrderUpdateComponent,
        resolve: {
            stockOrder: StockOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'StockOrders'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const stockOrderPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: StockOrderDeletePopupComponent,
        resolve: {
            stockOrder: StockOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'StockOrders'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
