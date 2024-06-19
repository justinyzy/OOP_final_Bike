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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

/**
 * 控制器類別，負責處理用戶的還車操作。
 */
public class ReturnController {

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
     * 初始化控制器，設置表格列並加載地區選項。
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
     * 處理搜索按鈕的操作，根據選擇的地區和輸入的站點查找相應的車柱。
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
     * 根據站點前綴和站點代碼查找車柱。
     * @param stationPrefix 站點前綴。
     * @param station 站點代碼。
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
     * 處理確認還車按鈕的操作，執行還車流程。
     */
    @FXML
    private void handleConfirmReturnButtonAction() {
        Dock selectedDock = dockTableView.getSelectionModel().getSelectedItem();
        if (selectedDock == null) {
            showAlert("警告", "請選擇一個車柱");
            return;
        }

        if (selectedDock.getBikeUID() != null && !selectedDock.getBikeUID().isEmpty()) {
            showAlert("警告", "選擇的車柱已有車號，請選擇空的車柱");
            return;
        }

        String region = regionComboBox.getValue();
        String stationPrefix = region.equals("臺北市") ? "TPE" : "NWT";
        String stationUID = selectedDock.getStationUID();
        String dockUID = selectedDock.getDockUID();
        String stationCode = stationUID.substring(3);

        String currentMemberID = Preferences.userNodeForPackage(AccountSettingsController.class).get("currentMemberID", "");
        String bikeUID = getRentedBikeUID(currentMemberID);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("確認還車");
        alert.setHeaderText(null);
        alert.setContentText("你確定在 " + stationUID + " 的 " + dockUID + " 歸還 " + bikeUID + " 這輛車嗎？");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int rentFee = updateHistory(currentMemberID, bikeUID, stationCode);
            updateMemberRentStatus(currentMemberID, "not using", rentFee);
            updateDockStatus(dockUID, "Parked", bikeUID);
            updateVehicleStatus(bikeUID, "In_operation", stationPrefix + stationCode + "_" + dockUID, stationPrefix + stationCode);
            addTransactionHistory(currentMemberID, rentFee);
            updateBikeAvailability(stationUID, 1);
            showAlert("成功", "還車成功！");
            navigateToMenuView();
        }
        //HelloApplication.syncDatabase();
    }

    /**
     * 根據會員ID獲取租借中的車輛ID。
     * @param memberID 會員ID。
     * @return 租借中的車輛ID。
     */
    private String getRentedBikeUID(String memberID) {
        String historyFileName = "/history.json";
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(historyFileName))) {
            Gson gson = new Gson();
            Type historyListType = new TypeToken<List<History>>() {}.getType();
            List<History> histories = gson.fromJson(reader, historyListType);
            for (History history : histories) {
                if (history.getMemberID().equals(memberID) && history.getReturn_time().isEmpty()) {
                    return history.getRent_BikeUID();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新會員的租借狀態及餘額。
     * @param memberID 會員ID。
     * @param status 新的租借狀態。
     * @param rentFee 租借費用。
     */
    private void updateMemberRentStatus(String memberID, String status, int rentFee) {
        String memberFileName = "/member_id.json";
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(memberFileName))) {
            Gson gson = new Gson();
            Type memberListType = new TypeToken<List<Member>>() {}.getType();
            List<Member> members = gson.fromJson(reader, memberListType);
            for (Member member : members) {
                if (member.getMemberID().equals(memberID)) {
                    member.setRent_status(status);
                    int currentBalance = Integer.parseInt(member.getYOYO_Card());
                    member.setYOYO_Card(String.valueOf(currentBalance - rentFee));
                    break;
                }
            }
            saveDataToFile(members, memberFileName);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新車柱狀態。
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
     * 更新車輛狀態。
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
     * 更新租借歷史紀錄並計算租借費用。
     * @param memberID 會員ID。
     * @param bikeUID 車輛ID。
     * @param returnPlace 歸還地點。
     * @return 租借費用。
     */
    private int updateHistory(String memberID, String bikeUID, String returnPlace) {
        String historyFileName = "/history.json";
        int rentFee = 0;
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(historyFileName))) {
            Gson gson = new Gson();
            Type historyListType = new TypeToken<List<History>>() {}.getType();
            List<History> histories = gson.fromJson(reader, historyListType);

            for (History history : histories) {
                if (history.getMemberID().equals(memberID) && history.getRent_BikeUID().equals(bikeUID) && history.getReturn_time().isEmpty()) {
                    LocalDateTime rentTime = LocalDateTime.parse(history.getRent_time(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                    LocalDateTime returnTime = LocalDateTime.now();
                    history.setReturn_time(returnTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
                    history.setReturn_place(returnPlace);
                    rentFee = calculateRentFee(rentTime, returnTime, history.getRent_place());
                    history.setRent_fee(rentFee);
                    break;
                }
            }

            saveDataToFile(histories, historyFileName);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return rentFee;
    }

    /**
     * 計算租借費用。
     * @param rentTime 租借時間。
     * @param returnTime 歸還時間。
     * @param rentPlace 租借地點。
     * @return 租借費用。
     */
    private int calculateRentFee(LocalDateTime rentTime, LocalDateTime returnTime, String rentPlace) {
        long minutes = Duration.between(rentTime, returnTime).toMinutes();
        long periods = (minutes + 29) / 30;

        String region = rentPlace.charAt(3) == '1' ? "臺北市" : "新北市";

        if (periods <= 1) {
            return region.equals("臺北市") ? 0 : 5;
        } else if (periods <= 8) {
            return (int) periods * 10;
        } else if (periods <= 16) {
            return (int) (8 * 10 + (periods - 8) * 20);
        } else {
            return (int) (8 * 10 + 8 * 20 + (periods - 16) * 40);
        }
    }

    /**
     * 添加交易歷史紀錄。
     * @param memberID 會員ID。
     * @param rentFee 租借費用。
     */
    private void addTransactionHistory(String memberID, int rentFee) {
        String historyFileName = "/yoyo_history.json";
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(historyFileName))) {
            Gson gson = new Gson();
            Type historyListType = new TypeToken<List<YoyoHistory>>() {}.getType();
            List<YoyoHistory> histories = gson.fromJson(reader, historyListType);

            YoyoHistory newHistory = new YoyoHistory();
            newHistory.setMemberID(memberID);
            newHistory.setTransaction("-" + rentFee);
            newHistory.setAction("RentCar");
            newHistory.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
            histories.add(newHistory);

            saveDataToFile(histories, historyFileName);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新站點的車輛可用數量。
     * @param stationUID 站點ID。
     * @param change 變動數量。
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
     * 將數據保存到指定文件。
     * @param data 要保存的數據列表。
     * @param fileName 文件名。
     * @param <T> 數據類型。
     * @throws IOException 當保存數據時出現IO錯誤。
     * @throws URISyntaxException 當URI語法錯誤時拋出。
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
