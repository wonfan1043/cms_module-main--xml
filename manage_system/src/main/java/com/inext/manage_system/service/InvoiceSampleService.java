package com.inext.manage_system.service;

import com.inext.manage_system.dto.BaseRes;
import com.inext.manage_system.dto.CreateOrUpdateSampleReq;

public interface InvoiceSampleService {

    // サンプル作成
    public BaseRes createSample(CreateOrUpdateSampleReq req);

    // サンプル更新
    public BaseRes updateSample(CreateOrUpdateSampleReq req);

    // サンプル削除
    public BaseRes deleteSample(CreateOrUpdateSampleReq req);

}
