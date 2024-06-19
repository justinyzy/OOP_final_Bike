package org.example.ubkie.GetAndSet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 抽象類別表示車站的通用屬性和功能。此類提供車站相關的抽象方法，供繼承的車站類別實現，以符合特定的業務需求和數據結構。
 */
public abstract class AbstractStation {
    @JsonProperty("StationUID")
    protected String StationUID;

    @JsonProperty("StationID")
    protected String StationID;

    @JsonProperty("AuthorityID")
    protected String AuthorityID;

    @JsonProperty("StationName")
    protected StationName StationName;

    @JsonProperty("StationPosition")
    protected StationPosition StationPosition;

    @JsonProperty("StationAddress")
    protected StationAddress StationAddress;

    @JsonProperty("BikesCapacity")
    protected int BikesCapacity;

    @JsonProperty("bike_available")
    protected int bike_available;

    /**
     * 獲取車站的唯一識別碼。
     * @return 車站的唯一識別碼。
     */
    public abstract String getStationUID();

    /**
     * 設定車站的唯一識別碼。
     * @param stationUID 新的車站唯一識別碼。
     */
    public abstract void setStationUID(String stationUID);

    /**
     * 獲取車站的識別ID。
     * @return 車站的識別ID。
     */
    public abstract String getStationID();

    /**
     * 設定車站的識別ID。
     * @param stationID 新的車站識別ID。
     */
    public abstract void setStationID(String stationID);

    /**
     * 獲取管理機構的ID。
     * @return 管理機構的ID。
     */
    public abstract String getAuthorityID();

    /**
     * 設定管理機構的ID。
     * @param authorityID 新的管理機構ID。
     */
    public abstract void setAuthorityID(String authorityID);

    /**
     * 獲取車站名稱。
     * @return 車站名稱。
     */
    public abstract StationName getStationName();

    /**
     * 設定車站名稱。
     * @param stationName 新的車站名稱。
     */
    public abstract void setStationName(StationName stationName);

    /**
     * 獲取車站位置。
     * @return 車站位置。
     */
    public abstract StationPosition getStationPosition();

    /**
     * 設定車站位置。
     * @param stationPosition 新的車站位置。
     */
    public abstract void setStationPosition(StationPosition stationPosition);

    /**
     * 獲取車站地址。
     * @return 車站地址。
     */
    public abstract StationAddress getStationAddress();

    /**
     * 設定車站地址。
     * @param stationAddress 新的車站地址。
     */
    public abstract void setStationAddress(StationAddress stationAddress);

    /**
     * 獲取自行車容量。
     * @return 自行車的總容量。
     */
    public abstract int getBikesCapacity();

    /**
     * 設定自行車容量。
     * @param bikesCapacity 新的自行車容量。
     */
    public abstract void setBikesCapacity(int bikesCapacity);

    /**
     * 獲取可用自行車數量。
     * @return 可用自行車的數量。
     */
    public abstract int getBike_available();

    /**
     * 設定可用自行車數量。
     * @param bikeAvailable 新的可用自行車數量。
     */
    public abstract void setBike_available(int bike_available);
}
