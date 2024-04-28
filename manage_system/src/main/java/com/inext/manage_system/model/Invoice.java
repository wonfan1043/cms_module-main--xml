package com.inext.manage_system.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    /** 請求書番号 **/
    private String invoiceNo;

    /** 会社ID **/
    private int corpId;

    /** 主旨ID **/
    private String topicId;

    /** 銀行ID **/
    private int bankId;

    /** 税率 **/
    private float tax;

    /** メモ **/
    private String memo;

    /** 決済期限 **/
    private LocalDate dueDate;

    /** 請求日 **/
    private LocalDate chargeDate;

    /** 決済日 **/
    private LocalDate payDate;

    /** 作成者 **/
    private String creater;

    /** 作成時間 **/
    private LocalDateTime createDateTime;

    /** 編集者 **/
    private String updater;

    /** 編集時間 **/
    private LocalDateTime updateDateTime;

    /** 削除フラグ **/
    private boolean delete;
    
}
