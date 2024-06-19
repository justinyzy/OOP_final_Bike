package org.example.ubkie.Controller.User;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.ubkie.GetAndSet.Dock;
import org.example.ubkie.GetAndSet.Vehicle;
import org.example.ubkie.HelloApplication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 控制器類別，負責管理維修報告的操作。
 * 用戶可以選擇報告車輛或車柱的維修情況，並提交維修描述。
 */
public class MaintenanceReportController {

    @FXML
    private ComboBox<String> reportTypeComboBox;
    @FXML
    private TextField vehicleNumberField;
    @FXML
    private ComboBox<String> regionComboBox;
    @FXML
    private TextField stationField;
    @FXML
    private TextField stationNumberField;
    @FXML
    private TextArea maintenanceDescriptionArea;
    @FXML
    private Button submitButton;
    @FXML
    private AnchorPane vehicleNumberPane;
    @FXML
    private AnchorPane stationPane;

    private List<Vehicle> vehicles;
    private List<Dock> docks;

    /**
     * 初始化控制器，設置報告類型和區域的選項，以及字段的可見性和驗證。
     */
    @FXML
    private void initialize() {
        reportTypeComboBox.getItems().addAll("車號", "車柱");
        reportTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateFieldsVisibility());

        regionComboBox.getItems().addAll("臺北市", "新北市");

        ChangeListener<String> fieldChangeListener = (observable, oldValue, newValue) -> validateFields();

        vehicleNumberField.textProperty().addListener(fieldChangeListener);
        regionComboBox.valueProperty().addListener(fieldChangeListener);
        stationField.textProperty().addListener(fieldChangeListener);
        stationNumberField.textProperty().addListener(fieldChangeListener);
        maintenanceDescriptionArea.textProperty().addListener(fieldChangeListener);

        vehicleNumberPane.setVisible(false);
        stationPane.setVisible(false);
        submitButton.setDisable(true);

        loadVehicleData();
        loadDockData();
    }

    /**
     * 根據選擇的報告類型更新字段的可見性。
     */
    private void updateFieldsVisibility() {
        String selectedType = reportTypeComboBox.getValue();
        boolean isVehicleSelected = "車號".equals(selectedType);
        vehicleNumberPane.setVisible(isVehicleSelected);
        stationPane.setVisible(!isVehicleSelected);

        validateFields();
    }

    /**
     * 驗證輸入字段是否合法。
     * 根據字段的填寫情況啟用或禁用提交按鈕。
     */
    private void validateFields() {
        boolean isVehicleSelected = "車號".equals(reportTypeComboBox.getValue());
        boolean isVehicleNumberFilled = !vehicleNumberField.getText().trim().isEmpty();
        boolean isRegionFilled = regionComboBox.getValue() != null && !regionComboBox.getValue().trim().isEmpty();
        boolean isStationFilled = !stationField.getText().trim().isEmpty();
        boolean isStationNumberFilled = !stationNumberField.getText().trim().isEmpty();
        boolean isMaintenanceDescriptionFilled = !maintenanceDescriptionArea.getText().trim().isEmpty();
        boolean isReportTypeSelected = reportTypeComboBox.getValue() != null;

        boolean isFormValid = isReportTypeSelected && (
                (isVehicleSelected && isVehicleNumberFilled) ||
                        (!isVehicleSelected && isRegionFilled && isStationFilled && isStationNumberFilled)) &&
                isMaintenanceDescriptionFilled;

        submitButton.setDisable(!isFormValid);
    }

    /**
     * 從JSON文件中載入車輛數據。
     */
    private void loadVehicleData() {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/ubike_data.json"))) {
            Gson gson = new Gson();
            Type vehicleListType = new TypeToken<List<Vehicle>>() {}.getType();
            vehicles = gson.fromJson(reader, vehicleListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 從JSON文件中載入車柱數據。
     */
    private void loadDockData() {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/Dock_data.json"))) {
            Gson gson = new Gson();
            Type dockListType = new TypeToken<List<Dock>>() {}.getType();
            docks = gson.fromJson(reader, dockListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存車輛數據到JSON文件。
     */
    private void saveVehicleData() {
        try {
            Path path = Paths.get(getClass().getResource("/ubike_data.json").toURI());
            Gson gson = new Gson();
            try (Writer writer = Files.newBufferedWriter(path)) {
                gson.toJson(vehicles, writer);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存車柱數據到JSON文件。
     */
    private void saveDockData() {
        try {
            Path path = Paths.get(getClass().getResource("/Dock_data.json").toURI());
            Gson gson = new Gson();
            try (Writer writer = Files.newBufferedWriter(path)) {
                gson.toJson(docks, writer);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 處理提交按鈕事件，保存維修報告並更新車輛或車柱狀態。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleSubmitButtonAction(ActionEvent event) throws IOException {
        String selectedType = reportTypeComboBox.getValue();
        String maintenanceDescription = maintenanceDescriptionArea.getText().trim();

        boolean isVehicleSelected = "車號".equals(selectedType);

        if (isVehicleSelected) {
            String bikeUID = vehicleNumberField.getText().trim();
            Vehicle vehicle = vehicles.stream().filter(v -> v.getBikeUID().equals(bikeUID)).findFirst().orElse(null);

            if (vehicle != null) {
                vehicle.setMaintenanceInfo(maintenanceDescription);
                vehicle.setStatus("Malfunctioned");
                saveVehicleData();

                showAlert("報修成功！", Alert.AlertType.INFORMATION);
                goToMenu();
            } else {
                showAlert("查無此車輛", Alert.AlertType.ERROR);
                goToMenu();
            }
        } else {
            String regionPrefix = regionComboBox.getValue().equals("臺北市") ? "TPE" : "NWT";
            String stationUID = stationField.getText().trim();
            String dockUID = stationNumberField.getText().trim();
            Dock dock = docks.stream()
                    .filter(d -> d.getStationUID().equals(stationUID) && d.getDockUID().endsWith(dockUID) && d.getDockUID().startsWith(regionPrefix))
                    .findFirst().orElse(null);

            if (dock != null) {
                dock.setMaintenanceInfo(maintenanceDescription);
                dock.setStatus("Malfunctioned");
                saveDockData();

                showAlert("報修成功！", Alert.AlertType.INFORMATION);
                goToMenu();
            } else {
                showAlert("查無此車柱", Alert.AlertType.ERROR);
                goToMenu();
            }
        }
        //HelloApplication.syncDatabase();
    }

    /**
     * 顯示提示框。
     * @param message 提示框的內容。
     * @param alertType 提示框的類型。
     */
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * 返回主菜單界面。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    private void goToMenu() throws IOException {
        Parent menuView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/menu-view.fxml"));
        Scene menuScene = new Scene(menuView);
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.setScene(menuScene);
        stage.show();
    }

    /**
     * 處理返回按鈕事件，返回主菜單界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        goToMenu();
    }
}
