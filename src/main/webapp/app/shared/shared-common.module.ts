import { NgModule } from '@angular/core';

import { TcaSwagStocksSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [TcaSwagStocksSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [TcaSwagStocksSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class TcaSwagStocksSharedCommonModule {}
