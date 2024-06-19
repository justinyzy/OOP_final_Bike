package org.example.ubkie.GetAndSet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 表示車站地址的類別，包含繁體中文和英文的地址信息。
 */
public class StationAddress {
    @JsonProperty("Zh_tw")
    private String Zh_tw;

    @JsonProperty("En")
    private String En;

    /**
     * 獲取繁體中文地址。
     * @return 繁體中文地址
     */
    public String getZh_tw() {
        return Zh_tw;
    }

    /**
     * 設定繁體中文地址。
     * @param zh_tw 繁體中文地址
     */
    public void setZh_tw(String zh_tw) {
        Zh_tw = zh_tw;
    }

    /**
     * 獲取英文地址。
     * @return 英文地址
     */
    public String getEn() {
        return En;
    }

    /**
     * 設定英文地址。
     * @param en 英文地址
     */
    public void setEn(String en) {
        En = en;
    }
}
