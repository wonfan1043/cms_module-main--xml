package com.inext.manage_system.controller.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.inext.manage_system.dto.BaseRes;
import com.inext.manage_system.model.InvoiceSample;
import com.inext.manage_system.service.InvoiceSampleService;

@CrossOrigin
@Controller
public class InvoiceSampleServiceController {

    @Autowired
    private InvoiceSampleService invoiceSampleService;

    // サンプル作成
    @PostMapping("invoice/create_sample")
    public BaseRes createSample(@RequestBody InvoiceSample sample){
        return invoiceSampleService.createSample(sample);
    }

    // サンプル更新
    @PostMapping("invoice/update_sample")
    public BaseRes updateSample(@RequestBody InvoiceSample sample){
        return invoiceSampleService.updateSample(sample);
    }
    
    // サンプル削除
    @PostMapping("invoice/delete_sample")
    public BaseRes deleteSample(@RequestBody InvoiceSample sample){
        return invoiceSampleService.deleteSample(sample);
    }

}
