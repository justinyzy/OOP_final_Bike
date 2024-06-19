package org.example.ubkie.Controller.User;

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
import org.example.ubkie.Controller.User.AccountSettingsController;
import org.example.ubkie.GetAndSet.*;
import org.example.ubkie.HelloApplication;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

/**
 * 控制器類別，負責用戶租車界面的操作。
 * 用戶可以選擇地區和站點來租借車輛。
 */
public class RentController {

    @FXML
    private ComboBox<String> regionComboBox;
    @FXML
    private TextField stationTextField;
    @FXML
    private TableView<Dock> dockTableView;
    @FXML
    private TableColumn<Dock, String> dockColumn;
    @FXML
    private TableColumn<Dock, String> bikeUIDColumn;

    private ObservableList<Dock> dockList;
    private List<Dock> docks;

    /**
     * 初始化控制器，設置表格列和地區選擇框。
     */
    @FXML
    private void initialize() {
        regionComboBox.setItems(FXCollections.observableArrayList("臺北市", "新北市"));

        dockColumn.setCellValueFactory(new PropertyValueFactory<>("DockUID"));
        bikeUIDColumn.setCellValueFactory(new PropertyValueFactory<>("BikeUID"));

        dockColumn.setCellFactory(column -> new TableCell<Dock, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.length() > 2 ? item.substring(item.length() - 2) : item);
                }
            }
        });
    }

    /**
     * 處理搜索按鈕的操作，根據選擇的地區和站點顯示可用的車柱。
     */
    @FXML
    private void handleSearchButtonAction() {
        String region = regionComboBox.getValue();
        String station = stationTextField.getText().trim();

        if (region == null || station.isEmpty()) {
            showAlert("警告", "請填寫所有欄位");
            return;
        }

        String stationPrefix = region.equals("臺北市") ? "TPE" : "NWT";

        List<Dock> docks = searchDocks(stationPrefix, station);
        if (docks.isEmpty()) {
            showAlert("警告", "查無此站點");
        } else {
            dockTableView.setItems(FXCollections.observableArrayList(docks));
        }
    }

    /**
     * 搜索符合條件的車柱。
     * @param stationPrefix 站點前綴（臺北市或新北市）。
     * @param station 站點編號。
     * @return 符合條件的車柱列表。
     */
    private List<Dock> searchDocks(String stationPrefix, String station) {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/Dock_data.json"))) {
            Gson gson = new Gson();
            Type dockListType = new TypeToken<List<Dock>>() {}.getType();
            docks = gson.fromJson(reader, dockListType);
            return docks.stream()
                    .filter(dock -> dock.getStationUID().startsWith(stationPrefix) && dock.getStationUID().endsWith(station))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList();
    }

    /**
     * 處理確認租用按鈕的操作，更新相關數據並記錄租借歷史。
     */
    @FXML
    private void handleConfirmRentButtonAction() {
        Dock selectedDock = dockTableView.getSelectionModel().getSelectedItem();
        if (selectedDock == null || selectedDock.getBikeUID().isEmpty()) {
            showAlert("警告", "請選擇一輛車輛");
            return;
        }

        String bikeUID = selectedDock.getBikeUID();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("確認租用");
        alert.setHeaderText(null);
        alert.setContentText("你確定租用" + bikeUID + "這輛車嗎？");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String currentMemberID = Preferences.userNodeForPackage(AccountSettingsController.class).get("currentMemberID", "");
            updateMemberRentStatus(currentMemberID, "using");
            updateDockStatus(selectedDock.getDockUID(), "Not_parked", "");
            updateVehicleStatus(bikeUID, "Rented", "", "");
            recordHistory(currentMemberID, selectedDock.getStationUID().substring(3), bikeUID);
            updateBikeAvailability(selectedDock.getStationUID(), -1);
            navigateToMenuView();
        }
        //HelloApplication.syncDatabase();
    }

    /**
     * 更新會員的租借狀態。
     * @param memberID 會員ID。
     * @param status 新的租借狀態。
     */
    private void updateMemberRentStatus(String memberID, String status) {
        String memberFileName = "/member_id.json";
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(memberFileName))) {
            Gson gson = new Gson();
            Type memberListType = new TypeToken<List<Member>>() {}.getType();
            List<Member> members = gson.fromJson(reader, memberListType);
            for (Member member : members) {
                if (member.getMemberID().equals(memberID)) {
                    member.setRent_status(status);
                    break;
                }
            }
            saveDataToFile(members, memberFileName);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新車柱的狀態和車輛ID。
     * @param dockUID 車柱ID。
     * @param status 新的狀態。
     * @param bikeUID 車輛ID。
     */
    private void updateDockStatus(String dockUID, String status, String bikeUID) {
        String dockFileName = "/Dock_data.json";
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(dockFileName))) {
            Gson gson = new Gson();
            Type dockListType = new TypeToken<List<Dock>>() {}.getType();
            docks = gson.fromJson(reader, dockListType);
            for (Dock dock : docks) {
                if (dock.getDockUID().equals(dockUID)) {
                    dock.setStatus(status);
                    dock.setBikeUID(bikeUID);
                    break;
                }
            }
            saveDataToFile(docks, dockFileName);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新車輛的狀態、車柱ID和站點ID。
     * @param bikeUID 車輛ID。
     * @param status 新的狀態。
     * @param dockUID 車柱ID。
     * @param stationUID 站點ID。
     */
    private void updateVehicleStatus(String bikeUID, String status, String dockUID, String stationUID) {
        String bikeFileName = "/ubike_data.json";
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(bikeFileName))) {
            Gson gson = new Gson();
            Type bikeListType = new TypeToken<List<Vehicle>>() {}.getType();
            List<Vehicle> bikes = gson.fromJson(reader, bikeListType);
            for (Vehicle bike : bikes) {
                if (bike.getBikeUID().equals(bikeUID)) {
                    bike.setStatus(status);
                    bike.setDockUID(dockUID);
                    bike.setStationUID(stationUID);
                    break;
                }
            }
            saveDataToFile(bikes, bikeFileName);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 記錄租借歷史。
     * @param memberID 會員ID。
     * @param rentPlace 租借地點。
     * @param bikeUID 車輛ID。
     */
    private void recordHistory(String memberID, String rentPlace, String bikeUID) {
        String historyFileName = "/history.json";
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(historyFileName))) {
            Gson gson = new Gson();
            Type historyListType = new TypeToken<List<History>>() {}.getType();
            List<History> histories = gson.fromJson(reader, historyListType);

            History newHistory = new History();
            newHistory.setMemberID(memberID);
            newHistory.setRent_time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
            newHistory.setReturn_time("");
            newHistory.setRent_place(rentPlace);
            newHistory.setReturn_place("");
            newHistory.setRent_fee(0);
            newHistory.setRent_BikeUID(bikeUID);

            histories.add(newHistory);

            saveDataToFile(histories, historyFileName);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新站點的可用車輛數量。
     * @param stationUID 站點ID。
     * @param change 車輛數量變化。
     */
    private void updateBikeAvailability(String stationUID, int change) {
        String regionFileName = stationUID.startsWith("TPE") ? "/Taipei.json" : "/NewTaipei.json";
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(regionFileName))) {
            Gson gson = new Gson();
            Type stationListType = new TypeToken<List<Station>>() {}.getType();
            List<Station> stations = gson.fromJson(reader, stationListType);
            for (Station station : stations) {
                if (station.getStationUID().equals(stationUID)) {
                    station.setBike_available(station.getBike_available() + change);
                    break;
                }
            }
            saveDataToFile(stations, regionFileName);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 將數據保存到文件中。
     * @param data 要保存的數據列表。
     * @param fileName 文件名。
     * @param <T> 數據的類型。
     * @throws IOException 當文件寫入失敗時拋出此異常。
     * @throws URISyntaxException 當URI語法錯誤時拋出此異常。
     */
    private <T> void saveDataToFile(List<T> data, String fileName) throws IOException, URISyntaxException {
        URL fileURL = getClass().getResource(fileName);
        if (fileURL == null) {
            throw new FileNotFoundException(fileName + " 文件未找到");
        }

        File file = Paths.get(fileURL.toURI()).toFile();
        try (Writer writer = new FileWriter(file)) {
            Gson gson = new Gson();
            gson.toJson(data, writer);
        }
    }

    /**
     * 導航到菜單視圖。
     */
    private void navigateToMenuView() {
        try {
            Parent menuView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/menu-view.fxml"));
            Scene menuScene = new Scene(menuView);
            Stage stage = (Stage) dockTableView.getScene().getWindow();
            stage.setScene(menuScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 顯示提示框。
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

    /**
     * 處理返回按鈕的操作，導航回菜單視圖。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        Parent menuView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/menu-view.fxml"));
        Scene menuScene = new Scene(menuView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(menuScene);
        stage.show();
    }
}
