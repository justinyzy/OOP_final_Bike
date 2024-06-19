package org.example.ubkie.GetAndSet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 抽象類別表示租借記錄的通用屬性和功能，具體的租借記錄類別需繼承此類別並實現其抽象方法。
 * 提供租借記錄相關的抽象方法，允許子類具體實現這些功能以符合特定的業務需求。
 */
public abstract class AbstractHistory {
    @JsonProperty("rent_time")
    private String rent_time;

    @JsonProperty("return_time")
    private String return_time;

    @JsonProperty("rent_fee")
    private int rent_fee;

    @JsonProperty("rent_place")
    private String rent_place;

    @JsonProperty("return_place")
    private String return_place;

    @JsonProperty("rent_BikeUID")
    private String rent_BikeUID;

    @JsonProperty("MemberID")
    private String MemberID;

    /**
     * 獲取租借時間。
     *
     * @return 租借時間的字符串表示。
     */
    public abstract String getRent_time();

    /**
     * 設定租借時間。
     *
     * @param rent_time 要設定的租借時間。
     */
    public abstract void setRent_time(String rent_time);

    /**
     * 獲取歸還時間。
     *
     * @return 歸還時間的字符串表示。
     */
    public abstract String getReturn_time();

    /**
     * 設定歸還時間。
     *
     * @param return_time 要設定的歸還時間。
     */
    public abstract void setReturn_time(String return_time);

    /**
     * 獲取租借費用。
     *
     * @return 租借費用的整數值。
     */
    public abstract int getRent_fee();

    /**
     * 設定租借費用。
     *
     * @param rent_fee 要設定的租借費用。
     */
    public abstract void setRent_fee(int rent_fee);

    /**
     * 獲取租借地點。
     *
     * @return 租借地點的字符串表示。
     */
    public abstract String getRent_place();

    /**
     * 設定租借地點。
     *
     * @param rent_place 要設定的租借地點。
     */
    public abstract void setRent_place(String rent_place);

    /**
     * 獲取歸還地點。
     *
     * @return 歸還地點的字符串表示。
     */
    public abstract String getReturn_place();

    /**
     * 設定歸還地點。
     *
     * @param return_place 要設定的歸還地點。
     */
    public abstract void setReturn_place(String return_place);

    /**
     * 獲取租借的車輛ID。
     *
     * @return 租借的車輛ID的字符串表示。
     */
    public abstract String getRent_BikeUID();

    /**
     * 設定租借的車輛ID。
     *
     * @param rent_BikeUID 要設定的車輛ID。
     */
    public abstract void setRent_BikeUID(String rent_BikeUID);

    /**
     * 獲取會員ID。
     *
     * @return 會員ID的字符串表示。
     */
    public abstract String getMemberID();

    /**
     * 設定會員ID。
     *
     * @param MemberID 要設定的會員ID。
     */
    public abstract void setMemberID(String MemberID);
}
