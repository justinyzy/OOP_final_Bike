package org.example.ubkie.GetAndSet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 抽象類別表示車柱的通用屬性和功能。此類別提供車柱相關屬性的抽象方法，需由具體車柱類別繼承並實現這些方法。
 */
public abstract class AbstractDock {

    @JsonProperty("Status")
    private String Status;

    @JsonProperty("BikeUID")
    private String BikeUID;

    @JsonProperty("DockUID")
    private String DockUID;

    @JsonProperty("StationUID")
    private String StationUID;

    @JsonProperty("MaintenanceInfo")
    private String MaintenanceInfo;

    /**
     * 獲取車柱的當前狀態。
     *
     * @return 車柱的狀態作為字符串返回。
     */
    public abstract String getStatus();

    /**
     * 設置車柱的狀態。
     *
     * @param status 要設置的車柱狀態。
     */
    public abstract void setStatus(String status);

    /**
     * 獲取車輛的唯一識別碼 (UID)。
     *
     * @return 車輛的UID作為字符串返回。
     */
    public abstract String getBikeUID();

    /**
     * 設置車輛的唯一識別碼 (UID)。
     *
     * @param bikeUID 要設置的車輛UID。
     */
    public abstract void setBikeUID(String bikeUID);

    /**
     * 獲取車柱的唯一識別碼 (UID)。
     *
     * @return 車柱的UID作為字符串返回。
     */
    public abstract String getDockUID();

    /**
     * 設置車柱的唯一識別碼 (UID)。
     *
     * @param dockUID 要設置的車柱UID。
     */
    public abstract void setDockUID(String dockUID);

    /**
     * 獲取站點的唯一識別碼 (UID)。
     *
     * @return 站點的UID作為字符串返回。
     */
    public abstract String getStationUID();

    /**
     * 設置站點的唯一識別碼 (UID)。
     *
     * @param stationUID 要設置的站點UID。
     */
    public abstract void setStationUID(String stationUID);

    /**
     * 獲取車柱的維修信息。
     *
     * @return 維修信息作為字符串返回。
     */
    public abstract String getMaintenanceInfo();

    /**
     * 設置車柱的維修信息。
     *
     * @param maintenanceInfo 要設置的維修信息。
     */
    public abstract void setMaintenanceInfo(String maintenanceInfo);
}
