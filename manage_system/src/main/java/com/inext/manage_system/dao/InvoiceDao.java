package com.inext.manage_system.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.inext.manage_system.dto.InvoiceContentRes;
import com.inext.manage_system.dto.SearchInvoiceRes;

@Mapper
public interface InvoiceDao {

    public int searchInvoiceByInvoiceNo(String invoiceNo);

    public int createInvoice(String invoiceNo, int corpId, String topicId, int bankId, String memo, float tax, String other, LocalDate dueDate, LocalDate chargeDate, String creater, LocalDateTime createDateTime);

    public int updateInvoice(String invoiceNo, int corpId, String topicId, int bankId, String memo, float tax, String other, LocalDate dueDate, LocalDate chargeDate, String updater, LocalDateTime updateDateTime);

    public List<SearchInvoiceRes> searchAllInvoices();

    public List<SearchInvoiceRes> searchInvoiceByCreateDate(int year, int month);

    public List<SearchInvoiceRes> searchInvoiceByDueDate(int year, int month);

    public List<SearchInvoiceRes> searchInvoiceByChargeDate(int year, int month);
    
    public List<SearchInvoiceRes> searchInvoiceByPayDate(int year, int month);

    public InvoiceContentRes checkInvoice(String invoiceNo);
    
    public int deleteInvoice(String invoiceNo, String updater, LocalDateTime updateDateTime);
}

