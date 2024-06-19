package org.example.ubkie.GetAndSet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 抽象類別，表示會員的通用屬性和功能。這個類提供了會員信息相關的抽象方法，供具體的會員類別繼承並實現，以符合不同的業務邏輯和數據結構需求。
 */
public abstract class AbstractMember {
    @JsonProperty("MemberID")
    protected String MemberID;

    @JsonProperty("Member_password")
    protected String Member_password;

    @JsonProperty("id_number")
    protected String id_number;

    @JsonProperty("email")
    protected String email;

    @JsonProperty("YOYO_Card")
    protected String YOYO_Card;

    @JsonProperty("YOYO_ID")
    protected String YOYO_ID;

    @JsonProperty("rent_status")
    protected String rent_status;

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
     * 獲取會員密碼。
     * @return 會員密碼。
     */
    public abstract String getMember_password();

    /**
     * 設定會員密碼。
     * @param member_password 要設定的會員密碼。
     */
    public abstract void setMember_password(String member_password);

    /**
     * 獲取身份證號碼。
     * @return 身份證號碼。
     */
    public abstract String getId_number();

    /**
     * 設定身份證號碼。
     * @param id_number 要設定的身份證號碼。
     */
    public abstract void setId_number(String id_number);

    /**
     * 獲取電子郵件地址。
     * @return 電子郵件地址。
     */
    public abstract String getEmail();

    /**
     * 設定電子郵件地址。
     * @param email 要設定的電子郵件地址。
     */
    public abstract void setEmail(String email);

    /**
     * 獲取悠遊卡餘額。
     * @return 悠遊卡餘額。
     */
    public abstract String getYOYO_Card();

    /**
     * 設定悠遊卡餘額。
     * @param YOYO_Card 要設定的悠遊卡餘額。
     */
    public abstract void setYOYO_Card(String YOYO_Card);

    /**
     * 獲取悠遊卡ID。
     * @return 悠遊卡ID。
     */
    public abstract String getYOYO_ID();

    /**
     * 設定悠遊卡ID。
     * @param YOYO_ID 要設定的悠遊卡ID。
     */
    public abstract void setYOYO_ID(String YOYO_ID);

    /**
     * 獲取租借狀態。
     * @return 租借狀態。
     */
    public abstract String getRent_status();

    /**
     * 設定租借狀態。
     * @param rent_status 要設定的租借狀態。
     */
    public abstract void setRent_status(String rent_status);
}
