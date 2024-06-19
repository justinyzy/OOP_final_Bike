package org.example.ubkie.Controller.Staff;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.ubkie.GetAndSet.Vehicle;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 控制器類別，負責管理車輛狀態的顯示。
 * 提供查詢功能，顯示特定車輛的詳細資訊。
 */
public class VehicleStatusController {

    @FXML
    private TextField bikeIdField;
    @FXML
    private Label bikeTypeLabel;
    @FXML
    private Label authorityLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label maintenanceInfoLabel;

    private List<Vehicle> vehicles;

    /**
     * 初始化控制器，加載車輛數據。
     */
    @FXML
    private void initialize() {
        loadVehicleData();
    }

    /**
     * 從JSON文件加載車輛數據。
     */
    private void loadVehicleData() {
        try {
            Gson gson = new Gson();
            Type vehicleListType = new TypeToken<List<Vehicle>>() {}.getType();
            vehicles = gson.fromJson(new InputStreamReader(getClass().getResourceAsStream("/ubike_data.json")), vehicleListType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 處理搜索按鈕事件，根據輸入的車輛編號顯示相應的車輛資訊。
     * @param event 觸發此操作的事件對象。
     */
    @FXML
    private void handleSearchButtonAction(ActionEvent event) {
        String bikeId = bikeIdField.getText().trim();
        Vehicle vehicle = vehicles.stream().filter(v -> v.getBikeUID().equals(bikeId)).findFirst().orElse(null);

        if (vehicle != null) {
            bikeTypeLabel.setText(vehicle.getType().equals("2.0") ? "電動輔助車" : "一般腳踏車");
            authorityLabel.setText(vehicle.getAuthorityID().equals("NWT") ? "新北市" : "臺北市");
            locationLabel.setText(vehicle.getDockUID().startsWith("NWT") ? "新北市" : "臺北市");
            statusLabel.setText(mapStatus(vehicle.getStatus()));
            maintenanceInfoLabel.setText(vehicle.getMaintenanceInfo().isEmpty() ? "無" : vehicle.getMaintenanceInfo());
        } else {
            bikeTypeLabel.setText("");
            authorityLabel.setText("");
            locationLabel.setText("");
            statusLabel.setText("");
            maintenanceInfoLabel.setText("");
        }
    }

    /**
     * 將車輛狀態代碼映射為更易理解的文字描述。
     * @param status 車輛狀態代碼。
     * @return 車輛狀態的文字描述。
     */
    private String mapStatus(String status) {
        switch (status) {
            case "In_operation":
                return "營運中";
            case "Malfunctioned":
                return "故障中";
            case "Not_in_operation":
                return "非營運狀態";
            case "Rented":
                return "租用中";
            case "Lost":
                return "遺失";
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
