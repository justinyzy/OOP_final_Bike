package org.example.ubkie.Controller.Staff;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.stream.Collectors;

/**
 * 控制器類別用於管理需要維修的車輛和車柱的列表。
 * 提供了從JSON文件加載數據，並在表格中顯示這些需要維修的車輛和車柱。
 */
public class MaintenanceListController {

    @FXML
    private TableView<Vehicle> vehicleTable;
    @FXML
    private TableView<Dock> dockTable;
    @FXML
    private Button maintenanceCompleteButton;

    @FXML
    private TableColumn<Vehicle, String> bikeUIDColumn;
    @FXML
    private TableColumn<Vehicle, String> regionColumn;
    @FXML
    private TableColumn<Vehicle, String> stationUIDColumn;
    @FXML
    private TableColumn<Vehicle, String> dockNumberColumn;
    @FXML
    private TableColumn<Vehicle, String> maintenanceInfoColumn;

    @FXML
    private TableColumn<Dock, String> dockUIDColumn;
    @FXML
    private TableColumn<Dock, String> dockRegionColumn;
    @FXML
    private TableColumn<Dock, String> dockStationUIDColumn;
    @FXML
    private TableColumn<Dock, String> dockMaintenanceInfoColumn;

    private ObservableList<Vehicle> maintenanceVehicles;
    private ObservableList<Dock> maintenanceDocks;

    private List<Vehicle> allVehicles;
    private List<Dock> allDocks;

    /**
     * 初始化表格並加載數據。
     */
    @FXML
    private void initialize() {
        loadVehicleData();
        loadDockData();
        setupTables();
    }

    /**
     * 設置表格欄位與數據模型之間的綁定。
     */
    private void setupTables() {
        // Setup vehicle table columns
        bikeUIDColumn.setCellValueFactory(new PropertyValueFactory<>("BikeUID"));
        regionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDockUID().startsWith("NWT") ? "新北市" : "臺北市"
        ));
        stationUIDColumn.setCellValueFactory(new PropertyValueFactory<>("StationUID"));
        dockNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDockUID().substring(cellData.getValue().getDockUID().length() - 2)
        ));
        maintenanceInfoColumn.setCellValueFactory(new PropertyValueFactory<>("MaintenanceInfo"));

        vehicleTable.setItems(maintenanceVehicles);

        // Setup dock table columns
        dockUIDColumn.setCellValueFactory(new PropertyValueFactory<>("DockUID"));
        dockRegionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDockUID().startsWith("NWT") ? "新北市" : "臺北市"
        ));
        dockStationUIDColumn.setCellValueFactory(new PropertyValueFactory<>("StationUID"));
        dockMaintenanceInfoColumn.setCellValueFactory(new PropertyValueFactory<>("MaintenanceInfo"));

        dockTable.setItems(maintenanceDocks);
    }

    /**
     * 從JSON文件加載車輛數據。
     */
    private void loadVehicleData() {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/ubike_data.json"))) {
            Gson gson = new Gson();
            Type vehicleListType = new TypeToken<List<Vehicle>>() {}.getType();
            allVehicles = gson.fromJson(reader, vehicleListType);
            maintenanceVehicles = FXCollections.observableArrayList(
                    allVehicles.stream().filter(v -> v.getMaintenanceInfo() != null && !v.getMaintenanceInfo().isEmpty()).collect(Collectors.toList())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 從JSON文件加載車柱數據。
     */
    private void loadDockData() {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/Dock_data.json"))) {
            Gson gson = new Gson();
            Type dockListType = new TypeToken<List<Dock>>() {}.getType();
            allDocks = gson.fromJson(reader, dockListType);
            maintenanceDocks = FXCollections.observableArrayList(
                    allDocks.stream().filter(d -> d.getMaintenanceInfo() != null && !d.getMaintenanceInfo().isEmpty()).collect(Collectors.toList())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 將所有車輛數據保存到JSON文件。
     * 此方法會將修改後的車輛列表序列化並保存回原始的數據文件。
     */
    private void saveVehicleData() {
        try {
            Path path = Paths.get(getClass().getResource("/ubike_data.json").toURI());
            Gson gson = new Gson();
            try (Writer writer = Files.newBufferedWriter(path)) {
                gson.toJson(allVehicles, writer);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 將所有車柱數據保存到JSON文件。
     * 此方法會將修改後的車柱列表序列化並保存回原始的數據文件。
     */
    private void saveDockData() {
        try {
            Path path = Paths.get(getClass().getResource("/Dock_data.json").toURI());
            Gson gson = new Gson();
            try (Writer writer = Files.newBufferedWriter(path)) {
                gson.toJson(allDocks, writer);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 處理完成維修的按鈕事件，更新車輛和車柱的狀態並保存數據。
     * @param event 觸發此操作的事件對象。
     */
    @FXML
    private void handleMaintenanceCompleteButtonAction(ActionEvent event) {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            selectedVehicle.setStatus("In_operation");
            selectedVehicle.setMaintenanceInfo("");
            maintenanceVehicles.remove(selectedVehicle); // Remove from the observable list
            saveVehicleData();
        }

        Dock selectedDock = dockTable.getSelectionModel().getSelectedItem();
        if (selectedDock != null) {
            selectedDock.setStatus("Not_parked");
            selectedDock.setMaintenanceInfo("");
            maintenanceDocks.remove(selectedDock); // Remove from the observable list
            saveDockData();
        }

        showAlert("維修完成", "維修操作已成功完成。");
        //HelloApplication.syncDatabase();
    }

    /**
     * 顯示一個信息提示框。
     * @param title 標題
     * @param message 訊息內容
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * 處理返回按鈕事件，將使用者界面從當前視圖返回到管理員視圖。
     * 此方法加載管理員視圖的FXML並顯示該場景。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當FXML文件加載失敗時拋出此異常。
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
