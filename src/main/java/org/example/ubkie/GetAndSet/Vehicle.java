package org.example.ubkie.GetAndSet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 表示車輛信息的類別，包含狀態、類型、車輛唯一識別碼（BikeUID）、管理單位ID、停放柱ID、車站ID和維護信息。
 */
public class Vehicle {
    @JsonProperty("Status")
    private String Status;

    @JsonProperty("Type")
    private String Type;

    @JsonProperty("BikeUID")
    private String BikeUID;

    @JsonProperty("AuthorityID")
    private String AuthorityID;

    @JsonProperty("DockUID")
    private String DockUID;

    @JsonProperty("StationUID")
    private String StationUID;

    @JsonProperty("MaintenanceInfo")
    private String MaintenanceInfo;

    // Getters and setters

    /**
     * 獲取車輛狀態。
     * @return 車輛狀態
     */
    public String getStatus() {
        return Status;
    }

    /**
     * 設定車輛狀態。
     * @param status 車輛狀態
     */
    public void setStatus(String status) {
        Status = status;
    }

    /**
     * 獲取車輛類型。
     * @return 車輛類型
     */
    public String getType() {
        return Type;
    }

    /**
     * 設定車輛類型。
     * @param type 車輛類型
     */
    public void setType(String type) {
        Type = type;
    }

    /**
     * 獲取車輛唯一識別碼（BikeUID）。
     * @return 車輛唯一識別碼（BikeUID）
     */
    public String getBikeUID() {
        return BikeUID;
    }

    /**
     * 設定車輛唯一識別碼（BikeUID）。
     * @param bikeUID 車輛唯一識別碼（BikeUID）
     */
    public void setBikeUID(String bikeUID) {
        BikeUID = bikeUID;
    }

    /**
     * 獲取管理單位ID。
     * @return 管理單位ID
     */
    public String getAuthorityID() {
        return AuthorityID;
    }

    /**
     * 設定管理單位ID。
     * @param authorityID 管理單位ID
     */
    public void setAuthorityID(String authorityID) {
        AuthorityID = authorityID;
    }

    /**
     * 獲取停放柱ID。
     * @return 停放柱ID
     */
    public String getDockUID() {
        return DockUID;
    }

    /**
     * 設定停放柱ID。
     * @param dockUID 停放柱ID
     */
    public void setDockUID(String dockUID) {
        DockUID = dockUID;
    }

    /**
     * 獲取車站ID。
     * @return 車站ID
     */
    public String getStationUID() {
        return StationUID;
    }

    /**
     * 設定車站ID。
     * @param stationUID 車站ID
     */
    public void setStationUID(String stationUID) {
        StationUID = stationUID;
    }

    /**
     * 獲取維護信息。
     * @return 維護信息
     */
    public String getMaintenanceInfo() {
        return MaintenanceInfo;
    }

    /**
     * 設定維護信息。
     * @param maintenanceInfo 維護信息
     */
    public void setMaintenanceInfo(String maintenanceInfo) {
        MaintenanceInfo = maintenanceInfo;
    }
}
