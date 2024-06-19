package org.example.ubkie.GetAndSet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**

 表示車站信息的類別，包含車站UID、車站ID、管理機構ID、車站名稱、車站位置、車站地址、自行車容量及可用自行車數量等屬性。
 */
public class Station extends AbstractStation {
    @JsonProperty("StationUID")
    private String StationUID;

    @JsonProperty("StationID")
    private String StationID;

    @JsonProperty("AuthorityID")
    private String AuthorityID;

    @JsonProperty("StationName")
    private StationName StationName;

    @JsonProperty("StationPosition")
    private StationPosition StationPosition;

    @JsonProperty("StationAddress")
    private StationAddress StationAddress;

    @JsonProperty("BikesCapacity")
    private int BikesCapacity;

    @JsonProperty("bike_available")
    private int bike_available;

    /**

     獲取車站UID。
     @return 車站UID
     */
    public String getStationUID() {
        return StationUID;
    }
    /**

     設定車站UID。
     @param stationUID 車站UID
     */
    public void setStationUID(String stationUID) {
        StationUID = stationUID;
    }
    /**

     獲取車站ID。
     @return 車站ID
     */
    public String getStationID() {
        return StationID;
    }
    /**

     設定車站ID。
     @param stationID 車站ID
     */
    public void setStationID(String stationID) {
        StationID = stationID;
    }
    /**

     獲取管理機構ID。
     @return 管理機構ID
     */
    public String getAuthorityID() {
        return AuthorityID;
    }
    /**

     設定管理機構ID。
     @param authorityID 管理機構ID
     */
    public void setAuthorityID(String authorityID) {
        AuthorityID = authorityID;
    }
    /**

     獲取車站名稱。
     @return 車站名稱
     */
    public StationName getStationName() {
        return StationName;
    }
    /**

     設定車站名稱。
     @param stationName 車站名稱
     */
    public void setStationName(StationName stationName) {
        StationName = stationName;
    }
    /**

     獲取車站位置。
     @return 車站位置
     */
    public StationPosition getStationPosition() {
        return StationPosition;
    }
    /**

     設定車站位置。
     @param stationPosition 車站位置
     */
    public void setStationPosition(StationPosition stationPosition) {
        StationPosition = stationPosition;
    }
    /**

     獲取車站地址。
     @return 車站地址
     */
    public StationAddress getStationAddress() {
        return StationAddress;
    }
    /**

     設定車站地址。
     @param stationAddress 車站地址
     */
    public void setStationAddress(StationAddress stationAddress) {
        StationAddress = stationAddress;
    }
    /**

     獲取自行車容量。
     @return 自行車容量
     */
    public int getBikesCapacity() {
        return BikesCapacity;
    }
    /**

     設定自行車容量。
     @param bikesCapacity 自行車容量
     */
    public void setBikesCapacity(int bikesCapacity) {
        BikesCapacity = bikesCapacity;
    }
    /**

     獲取可用自行車數量。
     @return 可用自行車數量
     */
    public int getBike_available() {
        return bike_available;
    }
    /**

     設定可用自行車數量。
     @param bike_available 可用自行車數量
     */
    public void setBike_available(int bike_available) {
        this.bike_available = bike_available;
    }
}