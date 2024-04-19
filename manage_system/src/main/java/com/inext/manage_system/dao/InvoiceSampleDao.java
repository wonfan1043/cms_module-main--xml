package com.inext.manage_system.dao;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface InvoiceSampleDao {

    public int searchInvoiceSampleByCorpId(int corpId);

    public int createSample(int corpId, String invoiceNo, String creater, LocalDateTime createDateTime);

    public int updateSample(int corpId, String invoiceNo, String updater, LocalDateTime updateDateTime);

    public int deleteSample(int corpId, String updater, LocalDateTime updateDateTime);

}
