package org.example.ubkie.GetAndSet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 表示車站位置的類別，包含經度、緯度和 GeoHash 信息。
 */
public class StationPosition {
    @JsonProperty("PositionLon")
    private double PositionLon;

    @JsonProperty("PositionLat")
    private double PositionLat;

    @JsonProperty("GeoHash")
    private String GeoHash;

    /**
     * 獲取車站經度。
     * @return 車站經度
     */
    public double getPositionLon() {
        return PositionLon;
    }

    /**
     * 設定車站經度。
     * @param positionLon 車站經度
     */
    public void setPositionLon(double positionLon) {
        PositionLon = positionLon;
    }

    /**
     * 獲取車站緯度。
     * @return 車站緯度
     */
    public double getPositionLat() {
        return PositionLat;
    }

    /**
     * 設定車站緯度。
     * @param positionLat 車站緯度
     */
    public void setPositionLat(double positionLat) {
        PositionLat = positionLat;
    }

    /**
     * 獲取車站的 GeoHash。
     * @return 車站的 GeoHash
     */
    public String getGeoHash() {
        return GeoHash;
    }

    /**
     * 設定車站的 GeoHash。
     * @param geoHash 車站的 GeoHash
     */
    public void setGeoHash(String geoHash) {
        GeoHash = geoHash;
    }
}
