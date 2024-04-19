package com.inext.manage_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inext.manage_system.dao.InvoiceNoDao;

@Service
public class InvoiceNoServiceImpl implements InvoiceNoService {

    @Autowired
    private InvoiceNoDao invoiceNoDao;

    @Override
    public int createNo() {
        return (invoiceNoDao.findLastNumber() + 1);
    }

    @Override
    public int addNumber(String type) {
        return invoiceNoDao.addNumber(type);
    }

}