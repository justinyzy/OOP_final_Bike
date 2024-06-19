package org.example.ubkie.GetAndSet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 表示車柱的類別，包含車柱的狀態、車輛ID、車柱ID、站點ID和維修資訊等屬性。
 */
public class Dock{
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
     * 獲取車柱的狀態。
     * @return 車柱的狀態
     */
    public String getStatus() {
        return Status;
    }

    /**
     * 設定車柱的狀態。
     * @param status 車柱的狀態
     */
    public void setStatus(String status) {
        Status = status;
    }

    /**
     * 獲取車輛的ID。
     * @return 車輛的ID
     */
    public String getBikeUID() {
        return BikeUID;
    }

    /**
     * 設定車輛的ID。
     * @param bikeUID 車輛的ID
     */
    public void setBikeUID(String bikeUID) {
        BikeUID = bikeUID;
    }

    /**
     * 獲取車柱的ID。
     * @return 車柱的ID
     */
    public String getDockUID() {
        return DockUID;
    }

    /**
     * 設定車柱的ID。
     * @param dockUID 車柱的ID
     */
    public void setDockUID(String dockUID) {
        DockUID = dockUID;
    }

    /**
     * 獲取站點的ID。
     * @return 站點的ID
     */
    public String getStationUID() {
        return StationUID;
    }

    /**
     * 設定站點的ID。
     * @param stationUID 站點的ID
     */
    public void setStationUID(String stationUID) {
        StationUID = stationUID;
    }

    /**
     * 獲取維修資訊。
     * @return 維修資訊
     */
    public String getMaintenanceInfo() {
        return MaintenanceInfo;
    }

    /**
     * 設定維修資訊。
     * @param maintenanceInfo 維修資訊
     */
    public void setMaintenanceInfo(String maintenanceInfo) {
        MaintenanceInfo = maintenanceInfo;
    }
}
