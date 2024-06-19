package org.example.ubkie.Controller.Staff;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.ubkie.GetAndSet.Station;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 控制器類別，負責處理車輛的調度操作。
 * 包括增加和減少車輛於指定站點。
 */
public class VehicleDispatchController {

    @FXML
    private ComboBox<String> regionComboBox;
    @FXML
    private TextField stationField;

    /**
     * 初始化控制器，設定區域選項。
     */
    @FXML
    private void initialize() {
        regionComboBox.getItems().addAll("臺北市", "新北市");
    }

    /**
     * 處理移除車輛的按鈕事件，從選定的站點中移除車輛。
     * 加載對應的視圖並傳遞站點數據到減少車輛的控制器。
     * @param event 觸發此操作的事件對象。
     */
    @FXML
    private void handleRemoveButtonAction(ActionEvent event) {
        String region = regionComboBox.getValue();
        String stationUID = stationField.getText();

        if (region == null || stationUID == null || stationUID.isEmpty()) {
            showAlert("錯誤", "請選擇區域並輸入站點編號。");
            return;
        }

        String stationFileName = region.equals("新北市") ? "/NewTaipei.json" : "/Taipei.json";

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(stationFileName))) {
            Gson gson = new Gson();
            Type stationListType = new TypeToken<List<Station>>() {}.getType();
            List<Station> stations = gson.fromJson(reader, stationListType);

            Station station = stations.stream().filter(s -> s.getStationUID().equals(stationUID)).findFirst().orElse(null);
            if (station == null) {
                showAlert("錯誤", "查無此站點");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ubkie/reduce-vehicle-view.fxml"));
            Parent root = loader.load();

            ReduceVehicleController controller = loader.getController();
            controller.setStationName(station.getStationName().getZh_tw());
            controller.loadDocks(stationUID);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("錯誤", "無法加載站點數據。");
        }
    }

    /**
     * 處理添加車輛的按鈕事件，向選定的站點添加車輛。
     * 加載對應的視圖並傳遞站點數據到添加車輛的控制器。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載視圖失敗時拋出。
     */
    @FXML
    private void handleAddButtonAction(ActionEvent event) throws IOException {
        String region = regionComboBox.getValue();
        String stationUID = stationField.getText();

        if (region == null || stationUID == null || stationUID.isEmpty()) {
            showAlert("錯誤", "請選擇區域並輸入站點編號。");
            return;
        }

        String stationFileName = region.equals("新北市") ? "/NewTaipei.json" : "/Taipei.json";

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(stationFileName))) {
            Gson gson = new Gson();
            Type stationListType = new TypeToken<List<Station>>() {}.getType();
            List<Station> stations = gson.fromJson(reader, stationListType);

            Station station = stations.stream().filter(s -> s.getStationUID().equals(stationUID)).findFirst().orElse(null);
            if (station == null) {
                showAlert("錯誤", "查無此站點");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ubkie/add-vehicle-view.fxml"));
            Parent root = loader.load();

            AddVehicleController controller = loader.getController();
            controller.setStationName(station.getStationName().getZh_tw());
            controller.loadDocks(stationUID);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("錯誤", "無法加載站點數據。");
        }
    }

    /**
     * 處理返回按鈕事件，返回管理界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載視圖失敗時拋出。
     */
    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        Parent menuView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/admin-view.fxml"));
        Scene menuScene = new Scene(menuView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(menuScene);
        stage.show();
    }

    /**
     * 顯示信息提示框。
     * @param title 提示框的標題。
     * @param message 提示框的內容。
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
