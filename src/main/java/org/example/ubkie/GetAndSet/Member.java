package org.example.ubkie.GetAndSet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 表示會員信息的類別，包含會員ID、密碼、身份證號碼、電子郵件、悠遊卡號、悠遊卡ID及租借狀態等屬性。
 */
public class Member {
    @JsonProperty("MemberID")
    private String MemberID;

    @JsonProperty("Member_password")
    private String Member_password;

    @JsonProperty("id_number")
    private String id_number;

    @JsonProperty("email")
    private String email;

    @JsonProperty("YOYO_Card")
    private String YOYO_Card;

    @JsonProperty("YOYO_ID")
    private String YOYO_ID;

    @JsonProperty("rent_status")
    private String rent_status;

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
        this.MemberID = memberID;
    }

    /**
     * 獲取會員密碼。
     * @return 會員密碼
     */
    public String getMember_password() {
        return Member_password;
    }

    /**
     * 設定會員密碼。
     * @param member_password 會員密碼
     */
    public void setMember_password(String member_password) {
        this.Member_password = member_password;
    }

    /**
     * 獲取身份證號碼。
     * @return 身份證號碼
     */
    public String getId_number() {
        return id_number;
    }

    /**
     * 設定身份證號碼。
     * @param id_number 身份證號碼
     */
    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    /**
     * 獲取電子郵件。
     * @return 電子郵件
     */
    public String getEmail() {
        return email;
    }

    /**
     * 設定電子郵件。
     * @param email 電子郵件
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 獲取悠遊卡餘額。
     * @return 悠遊卡餘額
     */
    public String getYOYO_Card() {
        return YOYO_Card;
    }

    /**
     * 設定悠遊卡餘額。
     * @param YOYO_Card 悠遊卡餘額
     */
    public void setYOYO_Card(String YOYO_Card) {
        this.YOYO_Card = YOYO_Card;
    }

    /**
     * 獲取悠遊卡ID。
     * @return 悠遊卡ID
     */
    public String getYOYO_ID() {
        return YOYO_ID;
    }

    /**
     * 設定悠遊卡ID。
     * @param YOYO_ID 悠遊卡ID
     */
    public void setYOYO_ID(String YOYO_ID) {
        this.YOYO_ID = YOYO_ID;
    }

    /**
     * 獲取租借狀態。
     * @return 租借狀態
     */
    public String getRent_status() {
        return rent_status;
    }

    /**
     * 設定租借狀態。
     * @param rent_status 租借狀態
     */
    public void setRent_status(String rent_status) {
        this.rent_status = rent_status;
    }
}
