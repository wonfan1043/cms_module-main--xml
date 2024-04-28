package com.inext.manage_system.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.inext.manage_system.model.InvoiceSample;

@Mapper
public interface InvoiceSampleDao {

    // サンプル請求書番号取得 by corpId
    public InvoiceSample selectInvoiceSampleByCorpId(@Param("corpId") int corpId);

    // サンプル追加
    public void insertInvoiceSample(@Param("invoiceSample") InvoiceSample invoiceSample);

    // サンプル編集
    public void updateInvoiceSampleByCorpId(@Param("invoiceSample") InvoiceSample invoiceSample);

    // サンプル状態編集
    public void updateInvoiceSampleStatusByCorpId(@Param("invoiceSample") InvoiceSample invoiceSample);

}
