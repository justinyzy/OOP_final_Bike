package org.example.ubkie.GetAndSet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 抽象類別表示車輛的通用屬性和功能。這個類提供了車輛相關的抽象方法，供具體的車輛類別繼承並實現，以符合不同的業務邏輯和數據結構需求。
 */
public abstract class AbstractVehicle {
    @JsonProperty("Status")
    protected String Status;

    @JsonProperty("Type")
    protected String Type;

    @JsonProperty("BikeUID")
    protected String BikeUID;

    @JsonProperty("AuthorityID")
    protected String AuthorityID;

    @JsonProperty("DockUID")
    protected String DockUID;

    @JsonProperty("StationUID")
    protected String StationUID;

    @JsonProperty("MaintenanceInfo")
    protected String MaintenanceInfo;

    /**
     * 獲取車輛的當前狀態。
     * @return 車輛的狀態。
     */
    public abstract String getStatus();

    /**
     * 設置車輛的狀態。
     * @param status 車輛的新狀態。
     */
    public abstract void setStatus(String status);

    /**
     * 獲取車輛的類型。
     * @return 車輛的類型。
     */
    public abstract String getType();

    /**
     * 設置車輛的類型。
     * @param type 車輛的新類型。
     */
    public abstract void setType(String type);

    /**
     * 獲取車輛的唯一識別碼 (BikeUID)。
     * @return 車輛的唯一識別碼。
     */
    public abstract String getBikeUID();

    /**
     * 設置車輛的唯一識別碼 (BikeUID)。
     * @param bikeUID 車輛的新識別碼。
     */
    public abstract void setBikeUID(String bikeUID);

    /**
     * 獲取管理機構的ID。
     * @return 管理機構的ID。
     */
    public abstract String getAuthorityID();

    /**
     * 設置管理機構的ID。
     * @param authorityID 管理機構的新ID。
     */
    public abstract void setAuthorityID(String authorityID);

    /**
     * 獲取停放柱的ID。
     * @return 停放柱的ID。
     */
    public abstract String getDockUID();

    /**
     * 設置停放柱的ID。
     * @param dockUID 停放柱的新ID。
     */
    public abstract void setDockUID(String dockUID);

    /**
     * 獲取車站的ID。
     * @return 車站的ID。
     */
    public abstract String getStationUID();

    /**
     * 設置車站的ID。
     * @param stationUID 車站的新ID。
     */
    public abstract void setStationUID(String stationUID);

    /**
     * 獲取車輛的維護信息。
     * @return 車輛的維護信息。
     */
    public abstract String getMaintenanceInfo();

    /**
     * 設置車輛的維護信息。
     * @param maintenanceInfo 車輛的新維護信息。
     */
    public abstract void setMaintenanceInfo(String maintenanceInfo);
}
