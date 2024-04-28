package com.inext.manage_system.service;

import com.inext.manage_system.dto.BaseRes;
import com.inext.manage_system.model.InvoiceSample;

public interface InvoiceSampleService {

    // サンプル作成
    public BaseRes createSample(InvoiceSample sample);

    // サンプル更新
    public BaseRes updateSample(InvoiceSample sample);

    // サンプル削除
    public BaseRes deleteSample(InvoiceSample sample);

}
