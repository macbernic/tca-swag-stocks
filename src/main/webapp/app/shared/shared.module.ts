import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { TcaSwagStocksSharedLibsModule, TcaSwagStocksSharedCommonModule, HasAnyAuthorityDirective } from './';

@NgModule({
    imports: [TcaSwagStocksSharedLibsModule, TcaSwagStocksSharedCommonModule],
    declarations: [HasAnyAuthorityDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    exports: [TcaSwagStocksSharedCommonModule, HasAnyAuthorityDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcaSwagStocksSharedModule {
    static forRoot() {
        return {
            ngModule: TcaSwagStocksSharedModule
        };
    }
}
