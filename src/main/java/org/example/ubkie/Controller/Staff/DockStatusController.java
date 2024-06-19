package org.example.ubkie.Controller.Staff;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.ubkie.GetAndSet.Dock;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 控制器類別用於顯示和搜索車柱的狀態資訊。
 * 提供了從JSON檔案加載車柱數據，並允許用戶根據地區和車柱ID進行搜索。
 */
public class DockStatusController {

    @FXML
    private ComboBox<String> regionComboBox;
    @FXML
    private TextField stationIdField;
    @FXML
    private TextField dockIdField;
    @FXML
    private Label statusLabel;
    @FXML
    private Label maintenanceInfoLabel;

    private List<Dock> docks;

    /**
     * 初始化界面元件和車柱數據。
     */
    @FXML
    private void initialize() {
        regionComboBox.getItems().addAll("臺北市", "新北市");
        loadDockData();
    }

    /**
     * 從JSON檔案加載車柱數據到列表中。
     */
    private void loadDockData() {
        try {
            Gson gson = new Gson();
            Type dockListType = new TypeToken<List<Dock>>() {}.getType();
            docks = gson.fromJson(new InputStreamReader(getClass().getResourceAsStream("/dock_data.json")), dockListType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 處理搜索按鈕事件，根據選擇的地區和輸入的站點ID和車柱ID後綴來搜尋並顯示車柱狀態。
     * @param event 觸發此操作的事件對象。
     */
    @FXML
    private void handleSearchButtonAction(ActionEvent event) {
        String region = regionComboBox.getValue();
        String stationId = stationIdField.getText().trim();
        String dockIdSuffix = dockIdField.getText().trim();
        String prefix = region.equals("新北市") ? "NWT" : "TPE";

        Dock dock = docks.stream()
                .filter(d -> d.getStationUID().equals(stationId) && d.getDockUID().startsWith(prefix) && d.getDockUID().endsWith(dockIdSuffix))
                .findFirst()
                .orElse(null);

        if (dock != null) {
            statusLabel.setText(mapStatus(dock.getStatus()));
            maintenanceInfoLabel.setText(dock.getMaintenanceInfo().isEmpty() ? "無" : dock.getMaintenanceInfo());
        } else {
            statusLabel.setText("");
            maintenanceInfoLabel.setText("");
        }
    }

    /**
     * 將車柱的狀態代碼映射為人類可讀的狀態描述。
     * @param status 車柱的狀態代碼。
     * @return 狀態的文字描述。
     */
    private String mapStatus(String status) {
        switch (status) {
            case "Parked":
                return "滿位";
            case "Not_parked":
                return "空位";
            case "Malfunctioned":
                return "故障中";
            default:
                return "";
        }
    }

    /**
     * 處理返回按鈕事件，返回管理界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML失敗時拋出。
     */
    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        Parent adminView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/admin-view.fxml"));
        Scene adminScene = new Scene(adminView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(adminScene);
        stage.show();
    }
}
