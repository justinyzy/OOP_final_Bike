package org.example.ubkie.GetAndSet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 表示悠遊卡歷史記錄的類別，包含會員ID、交易金額、交易時間和操作類型。
 */
public class YoyoHistory {
    @JsonProperty("MemberID")
    private String MemberID;

    @JsonProperty("transaction")
    private String transaction;

    @JsonProperty("time")
    private String time;

    @JsonProperty("action")
    private String action;

    // Getters and setters

    /**
     * 獲取會員ID。
     * @return 會員ID
     */
    public String getMemberID() {
        return MemberID;
    }

    /**
     * 設定會員ID。
     * @param memberID 會員ID
     */
    public void setMemberID(String memberID) {
        MemberID = memberID;
    }

    /**
     * 獲取交易金額。
     * @return 交易金額
     */
    public String getTransaction() {
        return transaction;
    }

    /**
     * 設定交易金額。
     * @param transaction 交易金額
     */
    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    /**
     * 獲取交易時間。
     * @return 交易時間
     */
    public String getTime() {
        return time;
    }

    /**
     * 設定交易時間。
     * @param time 交易時間
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 獲取操作類型。
     * @return 操作類型
     */
    public String getAction() {
        return action;
    }

    /**
     * 設定操作類型。
     * @param action 操作類型
     */
    public void setAction(String action) {
        this.action = action;
    }
}
