/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TcaSwagStocksTestModule } from '../../../test.module';
import { StockOrderDetailComponent } from 'app/entities/stock-order/stock-order-detail.component';
import { StockOrder } from 'app/shared/model/stock-order.model';

describe('Component Tests', () => {
    describe('StockOrder Management Detail Component', () => {
        let comp: StockOrderDetailComponent;
        let fixture: ComponentFixture<StockOrderDetailComponent>;
        const route = ({ data: of({ stockOrder: new StockOrder(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TcaSwagStocksTestModule],
                declarations: [StockOrderDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(StockOrderDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(StockOrderDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.stockOrder).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
