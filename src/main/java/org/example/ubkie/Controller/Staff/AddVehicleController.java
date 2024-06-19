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
 * 控制器類別用於添加車輛至車柱。
 * 提供UI界面以進行車輛添加操作，並實時更新車柱和車輛數據。
 */
public class AddVehicleController {

    @FXML
    private Label stationNameLabel;
    @FXML
    private TextField bikeUIDField;
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
     * 設置站點名稱，移除前綴，僅顯示後續部分。
     * @param stationName 完整的站點名稱。
     */
    public void setStationName(String stationName) {
        String displayName = stationName.split("YouBike2.0_")[1];
        stationNameLabel.setText(displayName);
    }

    /**
     * 從JSON文件加載車柱數據，並根據提供的站點UID過濾。
     * @param stationUID 站點的唯一標識符。
     */
    public void loadDocks(String stationUID) {
        String dockFileName = "/Dock_data.json";

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(dockFileName))) {
            Gson gson = new Gson();
            Type dockListType = new TypeToken<List<Dock>>() {}.getType();
            docks = gson.fromJson(reader, dockListType);

            dockList = FXCollections.observableArrayList(
                    docks.stream()
                            .filter(d -> d.getStationUID().equals(stationUID))
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
     * 初始化UI組件，設置列的工廠方法和樣式。
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
     * 處理添加按鈕的事件。根據用戶輸入的車號將車輛添加到選中的車柱。
     * @param event 事件對象。
     */
    @FXML
    private void handleAddButtonAction(ActionEvent event) {
        Dock selectedDock = dockTable.getSelectionModel().getSelectedItem();
        if (selectedDock == null) {
            showAlert("錯誤", "請選擇一個車柱。");
            return;
        }

        if (selectedDock.getBikeUID() != null && !selectedDock.getBikeUID().isEmpty()) {
            showAlert("錯誤", "該車柱已存放車輛，請選擇其他空的車柱。");
            return;
        }

        String newBikeUID = bikeUIDField.getText();
        if (newBikeUID == null || newBikeUID.isEmpty()) {
            showAlert("錯誤", "請輸入車號。");
            return;
        }

        selectedDock.setStatus("Parked");
        selectedDock.setBikeUID(newBikeUID);

        try {
            saveDockData();
            updateStationData(selectedDock.getStationUID(), 1);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            showAlert("錯誤", "無法保存數據。");
            return;
        }

        updateBikeStatus(newBikeUID, selectedDock);

        dockTable.refresh();

        //HelloApplication.syncDatabase();
        showAlert("成功", "增加車輛成功。");
    }

    /**
     * 保存車柱數據到本地文件。
     * @throws IOException 當文件讀寫出錯時拋出。
     * @throws URISyntaxException 當URL不正確時拋出。
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
     * 更新車輛的狀態和位置信息。
     * @param bikeUID 車輛的唯一標識符。
     * @param selectedDock 選中的車柱。
     */
    private void updateBikeStatus(String bikeUID, Dock selectedDock) {
        String bikeFileName = "/ubike_data.json";

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(bikeFileName))) {
            Gson gson = new Gson();
            Type bikeListType = new TypeToken<List<Vehicle>>() {}.getType();
            List<Vehicle> bikes = gson.fromJson(reader, bikeListType);

            Vehicle bike = bikes.stream().filter(b -> b.getBikeUID().equals(bikeUID)).findFirst().orElse(null);
            if (bike != null) {
                bike.setStatus("In_operation");
                bike.setDockUID(selectedDock.getDockUID());
                bike.setStationUID(selectedDock.getStationUID());

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
     * 更新站點的可用車輛數量。
     * @param stationUID 站點的唯一標識符。
     * @param increment 增量值。
     */
    private void updateStationData(String stationUID, int increment) {
        String stationFileName = stationUID.startsWith("NWT") ? "/NewTaipei.json" : "/Taipei.json";

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(stationFileName))) {
            Gson gson = new Gson();
            Type stationListType = new TypeToken<List<Station>>() {}.getType();
            stations = gson.fromJson(reader, stationListType);

            for (Station station : stations) {
                if (station.getStationUID().equals(stationUID)) {
                    station.setBike_available(station.getBike_available() + increment);
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
     * 處理返回按鈕的事件，導航回主菜單。
     * @param event 事件對象。
     * @throws IOException 當文件讀寫出錯時拋出。
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
     * 顯示警告提示框。
     * @param title 提示框標題。
     * @param message 提示信息。
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
