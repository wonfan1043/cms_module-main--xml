package com.inext.manage_system.dao;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ChargeContentDao {

    public int addContent(String invoiceNo, int itemId, int quantity, int unitPrice, String creater, LocalDateTime createDateTime);

    public int deleteAllContent(String invoiceNo);

    public int deleteContent(String invoiceNo, String updater, LocalDateTime updateDateTimeo);

}
