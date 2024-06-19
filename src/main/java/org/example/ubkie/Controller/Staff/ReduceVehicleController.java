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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.ubkie.GetAndSet.Dock;
import org.example.ubkie.GetAndSet.Station;
import org.example.ubkie.GetAndSet.Vehicle;
import org.example.ubkie.HelloApplication;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 控制器類別，負責管理車柱的車輛減少操作。
 * 允許用戶從指定車柱中移除車輛並更新相關數據。
 */
public class ReduceVehicleController {

    @FXML
    private Label stationNameLabel;
    @FXML
    private TableView<Dock> dockTable;
    @FXML
    private TableColumn<Dock, String> dockUIDColumn;
    @FXML
    private TableColumn<Dock, String> bikeUIDColumn;

    private ObservableList<Dock> dockList;
    private List<Dock> docks;
    private List<Station> stations;

    /**
     * 設定顯示的車站名稱。
     * @param stationName 完整的車站名稱。
     */
    public void setStationName(String stationName) {
        String displayName = stationName.split("YouBike2.0_")[1];
        stationNameLabel.setText(displayName);
    }

    /**
     * 根據車站UID加載對應的車柱數據。
     * @param stationUID 車站的唯一識別碼。
     */
    public void loadDocks(String stationUID) {
        String dockFileName = "/Dock_data.json";

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(dockFileName))) {
            Gson gson = new Gson();
            Type dockListType = new TypeToken<List<Dock>>() {}.getType();
            docks = gson.fromJson(reader, dockListType);

            dockList = FXCollections.observableArrayList(
                    docks.stream()
                            .filter(d -> d.getStationUID().equals(stationUID) && "Parked".equals(d.getStatus()))
                            .collect(Collectors.toList())
            );

            dockUIDColumn.setCellValueFactory(new PropertyValueFactory<>("dockUID"));
            bikeUIDColumn.setCellValueFactory(new PropertyValueFactory<>("bikeUID"));

            dockTable.setItems(dockList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("錯誤", "無法加載車柱數據。");
        }
    }

    /**
     * 初始化界面，設定表格顯示的列。
     */
    @FXML
    private void initialize() {
        dockUIDColumn.setCellFactory(column -> new TableCell<Dock, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.substring(item.length() - 2));
                }
            }
        });

        bikeUIDColumn.setCellFactory(column -> new TableCell<Dock, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });

        dockUIDColumn.setStyle("-fx-font-size: 16px;");
        bikeUIDColumn.setStyle("-fx-font-size: 16px;");
    }

    /**
     * 處理減少車輛的按鈕事件，從選定的車柱中移除車輛。
     * @param event 觸發此操作的事件對象。
     */
    @FXML
    private void handleReduceButtonAction(ActionEvent event) {
        Dock selectedDock = dockTable.getSelectionModel().getSelectedItem();
        if (selectedDock == null) {
            showAlert("錯誤", "請選擇一個車柱。");
            return;
        }

        String bikeUID = selectedDock.getBikeUID();
        selectedDock.setStatus("Not_parked");
        selectedDock.setBikeUID("");

        try {
            saveDockData();
            updateStationData(selectedDock.getStationUID(), -1);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            showAlert("錯誤", "無法保存數據。");
            return;
        }

        updateBikeStatus(bikeUID);

        dockList.remove(selectedDock);
        dockTable.refresh();

        showAlert("成功", "減少車輛成功。");
        //HelloApplication.syncDatabase();
    }

    /**
     * 儲存車柱數據到JSON文件。
     * 如果文件路徑不正確或文件不存在，將拋出FileNotFoundException。
     * @throws IOException 當文件寫入失敗時拋出此異常。
     * @throws URISyntaxException 當路徑解析失敗時拋出此異常。
     */
    private void saveDockData() throws IOException, URISyntaxException {
        URL dockFileURL = getClass().getResource("/Dock_data.json");
        if (dockFileURL == null) {
            throw new FileNotFoundException("Dock_data.json 文件未找到");
        }

        File dockFile = Paths.get(dockFileURL.toURI()).toFile();
        try (Writer writer = new FileWriter(dockFile)) {
            Gson gson = new Gson();
            gson.toJson(docks, writer);
        }
    }

    /**
     * 更新指定車輛的狀態為「非營運」。
     * 如果找不到指定的車輛或文件路徑問題，將處理相應的異常。
     * @param bikeUID 需要更新的車輛的唯一識別碼。
     */
    private void updateBikeStatus(String bikeUID) {
        String bikeFileName = "/ubike_data.json";

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(bikeFileName))) {
            Gson gson = new Gson();
            Type bikeListType = new TypeToken<List<Vehicle>>() {}.getType();
            List<Vehicle> bikes = gson.fromJson(reader, bikeListType);

            Vehicle bike = bikes.stream().filter(b -> b.getBikeUID().equals(bikeUID)).findFirst().orElse(null);
            if (bike != null) {
                bike.setStatus("Not_in_operation");
                bike.setDockUID("");
                bike.setStationUID("");

                URL bikeFileURL = getClass().getResource(bikeFileName);
                if (bikeFileURL == null) {
                    throw new FileNotFoundException("ubike_data.json 文件未找到");
                }

                File bikeFile = Paths.get(bikeFileURL.toURI()).toFile();
                try (Writer writer = new FileWriter(bikeFile)) {
                    gson.toJson(bikes, writer);
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("錯誤", "無法保存車輛數據。");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("錯誤", "無法加載車輛數據。");
        }
    }

    /**
     * 根據指定的減量更新站點可用車輛數。
     * @param stationUID 站點的唯一識別碼。
     * @param decrement 要減少的車輛數量。
     */
    private void updateStationData(String stationUID, int decrement) {
        String stationFileName = stationUID.startsWith("NWT") ? "/NewTaipei.json" : "/Taipei.json";

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(stationFileName))) {
            Gson gson = new Gson();
            Type stationListType = new TypeToken<List<Station>>() {}.getType();
            stations = gson.fromJson(reader, stationListType);

            for (Station station : stations) {
                if (station.getStationUID().equals(stationUID)) {
                    station.setBike_available(station.getBike_available() + decrement);
                    break;
                }
            }

            URL stationFileURL = getClass().getResource(stationFileName);
            if (stationFileURL == null) {
                throw new FileNotFoundException(stationFileName + " 文件未找到");
            }

            File stationFile = Paths.get(stationFileURL.toURI()).toFile();
            try (Writer writer = new FileWriter(stationFile)) {
                gson.toJson(stations, writer);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("錯誤", "無法保存站點數據。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("錯誤", "無法加載站點數據。");
        }
    }

    /**
     * 處理返回按鈕事件，返回車輛調度管理界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當FXML加載失敗時拋出此異常。
     */
    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        Parent menuView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/vehicle-dispatch-view.fxml"));
        Scene menuScene = new Scene(menuView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(menuScene);
        stage.show();
    }

    /**
     * 顯示信息提示框，用於錯誤、警告或通知用戶信息。
     * @param title 提示框的標題。
     * @param message 提示框的訊息內容。
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
