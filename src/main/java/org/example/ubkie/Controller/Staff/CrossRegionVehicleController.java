package org.example.ubkie.Controller.Staff;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.ubkie.GetAndSet.Vehicle;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 處理跨區域車輛的控制器，負責加載並顯示跨區域車輛數據。
 */
public class CrossRegionVehicleController {

    /**
     * 用於顯示在表格中的車輛數據模型。
     */
    public static class VehicleDisplay {
        private String bikeUID;
        private String currentLocation;
        private String authority;

        /**
         * 建構函數，初始化車輛顯示數據。
         * @param bikeUID 車輛唯一識別碼。
         * @param currentLocation 當前位置。
         * @param authority 管理機關。
         */
        public VehicleDisplay(String bikeUID, String currentLocation, String authority) {
            this.bikeUID = bikeUID;
            this.currentLocation = currentLocation;
            this.authority = authority;
        }

        public String getBikeUID() {
            return bikeUID;
        }

        public void setBikeUID(String bikeUID) {
            this.bikeUID = bikeUID;
        }

        public String getCurrentLocation() {
            return currentLocation;
        }

        public void setCurrentLocation(String currentLocation) {
            this.currentLocation = currentLocation;
        }

        public String getAuthority() {
            return authority;
        }

        public void setAuthority(String authority) {
            this.authority = authority;
        }
    }

    @FXML
    private TableView<VehicleDisplay> vehicleTable;
    @FXML
    private TableColumn<VehicleDisplay, String> bikeUIDColumn;
    @FXML
    private TableColumn<VehicleDisplay, String> currentLocationColumn;
    @FXML
    private TableColumn<VehicleDisplay, String> authorityColumn;

    private ObservableList<VehicleDisplay> vehicleList;

    /**
     * 初始化界面，設置表格欄位綁定並加載車輛數據。
     */
    @FXML
    private void initialize() {
        bikeUIDColumn.setCellValueFactory(new PropertyValueFactory<>("bikeUID"));
        currentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("currentLocation"));
        authorityColumn.setCellValueFactory(new PropertyValueFactory<>("authority"));

        loadCrossRegionVehicles();
    }

    /**
     * 從JSON檔案中加載跨區域車輛數據。
     */
    private void loadCrossRegionVehicles() {
        String vehicleFileName = "/ubike_data.json";

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(vehicleFileName))) {
            Gson gson = new Gson();
            Type vehicleListType = new TypeToken<List<Vehicle>>() {}.getType();
            List<Vehicle> vehicles = gson.fromJson(reader, vehicleListType);

            vehicleList = FXCollections.observableArrayList(
                    vehicles.stream()
                            .filter(v -> isCrossRegion(v.getAuthorityID(), v.getDockUID()))
                            .map(v -> new VehicleDisplay(
                                    v.getBikeUID(),
                                    getLocationFromDockUID(v.getDockUID()),
                                    getLocationFromAuthorityID(v.getAuthorityID())
                            ))
                            .collect(Collectors.toList())
            );

            vehicleTable.setItems(vehicleList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("錯誤", "無法加載車輛數據。");
        }
    }

    /**
     * 判斷車輛是否跨區域。
     * @param authorityID 管理機關ID。
     * @param dockUID 車柱ID。
     * @return 是否跨區域。
     */
    private boolean isCrossRegion(String authorityID, String dockUID) {
        return authorityID.length() >= 3 && dockUID.length() >= 3 && !authorityID.substring(0, 3).equals(dockUID.substring(0, 3));
    }

    /**
     * 根據車柱ID獲取位置名稱。
     * @param dockUID 車柱ID。
     * @return 位置名稱。
     */
    private String getLocationFromDockUID(String dockUID) {
        if (dockUID.startsWith("TPE")) {
            return "臺北市";
        } else if (dockUID.startsWith("NWT")) {
            return "新北市";
        }
        return "未知";
    }

    /**
     * 根據管理機關ID獲取位置名稱。
     * @param authorityID 管理機關ID。
     * @return 位置名稱。
     */
    private String getLocationFromAuthorityID(String authorityID) {
        if (authorityID.startsWith("TPE")) {
            return "臺北市";
        } else if (authorityID.startsWith("NWT")) {
            return "新北市";
        }
        return "未知";
    }

    /**
     * 處理返回按鈕事件，返回管理界面。
     * @param event 事件對象。
     * @throws IOException 當加載FXML失敗時拋出。
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
     * 顯示彈出警告。
     * @param title 標題。
     * @param message 訊息。
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
