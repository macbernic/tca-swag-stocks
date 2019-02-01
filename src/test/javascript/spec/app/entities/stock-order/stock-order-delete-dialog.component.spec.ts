/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TcaSwagStocksTestModule } from '../../../test.module';
import { StockOrderDeleteDialogComponent } from 'app/entities/stock-order/stock-order-delete-dialog.component';
import { StockOrderService } from 'app/entities/stock-order/stock-order.service';

describe('Component Tests', () => {
    describe('StockOrder Management Delete Component', () => {
        let comp: StockOrderDeleteDialogComponent;
        let fixture: ComponentFixture<StockOrderDeleteDialogComponent>;
        let service: StockOrderService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TcaSwagStocksTestModule],
                declarations: [StockOrderDeleteDialogComponent]
            })
                .overrideTemplate(StockOrderDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(StockOrderDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StockOrderService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
