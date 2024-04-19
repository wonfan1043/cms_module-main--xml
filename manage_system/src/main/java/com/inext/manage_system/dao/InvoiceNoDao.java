package com.inext.manage_system.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InvoiceNoDao {

    public int findLastNumber();

    public int addNumber(@Param("type") String type);

}