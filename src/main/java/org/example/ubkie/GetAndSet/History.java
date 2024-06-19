package org.example.ubkie.GetAndSet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 表示租借記錄的類別，包含租借時間、歸還時間、租借費用、租借地點、歸還地點、租借的車輛ID和會員ID等屬性。
 */
public class History{
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
     * @return 租借時間
     */
    public String getRent_time() {
        return rent_time;
    }

    /**
     * 設定租借時間。
     * @param rent_time 租借時間
     */
    public void setRent_time(String rent_time) {
        this.rent_time = rent_time;
    }

    /**
     * 獲取歸還時間。
     * @return 歸還時間
     */
    public String getReturn_time() {
        return return_time;
    }

    /**
     * 設定歸還時間。
     * @param return_time 歸還時間
     */
    public void setReturn_time(String return_time) {
        this.return_time = return_time;
    }

    /**
     * 獲取租借費用。
     * @return 租借費用
     */
    public int getRent_fee() {
        return rent_fee;
    }

    /**
     * 設定租借費用。
     * @param rent_fee 租借費用
     */
    public void setRent_fee(int rent_fee) {
        this.rent_fee = rent_fee;
    }

    /**
     * 獲取租借地點。
     * @return 租借地點
     */
    public String getRent_place() {
        return rent_place;
    }

    /**
     * 設定租借地點。
     * @param rent_place 租借地點
     */
    public void setRent_place(String rent_place) {
        this.rent_place = rent_place;
    }

    /**
     * 獲取歸還地點。
     * @return 歸還地點
     */
    public String getReturn_place() {
        return return_place;
    }

    /**
     * 設定歸還地點。
     * @param return_place 歸還地點
     */
    public void setReturn_place(String return_place) {
        this.return_place = return_place;
    }

    /**
     * 獲取租借的車輛ID。
     * @return 租借的車輛ID
     */
    public String getRent_BikeUID() {
        return rent_BikeUID;
    }

    /**
     * 設定租借的車輛ID。
     * @param rent_BikeUID 租借的車輛ID
     */
    public void setRent_BikeUID(String rent_BikeUID) {
        this.rent_BikeUID = rent_BikeUID;
    }

    /**
     * 獲取會員ID。
     * @return 會員ID
     */
    public String getMemberID() {
        return MemberID;
    }

    /**
     * 設定會員ID。
     * @param MemberID 會員ID
     */
    public void setMemberID(String MemberID) {
        this.MemberID = MemberID;
    }
}
