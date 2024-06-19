package org.example.ubkie.GetAndSet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 表示車站名稱的類別，包含繁體中文和英文的名稱信息。
 */
public class StationName {
    @JsonProperty("Zh_tw")
    private String Zh_tw;

    @JsonProperty("En")
    private String En;

    /**
     * 獲取繁體中文名稱。
     * @return 繁體中文名稱
     */
    public String getZh_tw() {
        return Zh_tw;
    }

    /**
     * 設定繁體中文名稱。
     * @param zh_tw 繁體中文名稱
     */
    public void setZh_tw(String zh_tw) {
        Zh_tw = zh_tw;
    }

    /**
     * 獲取英文名稱。
     * @return 英文名稱
     */
    public String getEn() {
        return En;
    }

    /**
     * 設定英文名稱。
     * @param en 英文名稱
     */
    public void setEn(String en) {
        En = en;
    }
}
