package org.example.ubkie.GetAndSet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 抽象類別表示悠遊卡歷史記錄的通用屬性和功能。這個類提供了悠遊卡歷史記錄相關的抽象方法，供具體的悠遊卡歷史記錄類別繼承並實現，以符合不同的業務邏輯和數據結構需求。
 */
public abstract class AbstractYoyoHistory {
    @JsonProperty("MemberID")
    protected String MemberID;

    @JsonProperty("transaction")
    protected String transaction;

    @JsonProperty("time")
    protected String time;

    @JsonProperty("action")
    protected String action;

    /**
     * 獲取會員ID。
     * @return 會員ID。
     */
    public abstract String getMemberID();

    /**
     * 設定會員ID。
     * @param memberID 要設定的會員ID。
     */
    public abstract void setMemberID(String memberID);

    /**
     * 獲取交易金額。
     * @return 交易金額。
     */
    public abstract String getTransaction();

    /**
     * 設定交易金額。
     * @param transaction 要設定的交易金額。
     */
    public abstract void setTransaction(String transaction);

    /**
     * 獲取交易時間。
     * @return 交易時間。
     */
    public abstract String getTime();

    /**
     * 設定交易時間。
     * @param time 要設定的交易時間。
     */
    public abstract void setTime(String time);

    /**
     * 獲取操作類型。
     * @return 操作類型。
     */
    public abstract String getAction();

    /**
     * 設定操作類型。
     * @param action 要設定的操作類型。
     */
    public abstract void setAction(String action);
}
