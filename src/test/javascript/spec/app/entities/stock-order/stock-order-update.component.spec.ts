/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TcaSwagStocksTestModule } from '../../../test.module';
import { StockOrderUpdateComponent } from 'app/entities/stock-order/stock-order-update.component';
import { StockOrderService } from 'app/entities/stock-order/stock-order.service';
import { StockOrder } from 'app/shared/model/stock-order.model';

describe('Component Tests', () => {
    describe('StockOrder Management Update Component', () => {
        let comp: StockOrderUpdateComponent;
        let fixture: ComponentFixture<StockOrderUpdateComponent>;
        let service: StockOrderService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TcaSwagStocksTestModule],
                declarations: [StockOrderUpdateComponent]
            })
                .overrideTemplate(StockOrderUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(StockOrderUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StockOrderService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new StockOrder(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.stockOrder = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new StockOrder();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.stockOrder = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
