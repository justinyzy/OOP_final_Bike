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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.ubkie.Controller.User.LoginController;
import org.example.ubkie.GetAndSet.History;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * 控制器類別，負責顯示用戶的租還車紀錄。
 */
public class RentReturnRecordController {

    @FXML
    private TableView<History> historyTableView;
    @FXML
    private TableColumn<History, String> rentTimeColumn;
    @FXML
    private TableColumn<History, String> returnTimeColumn;
    @FXML
    private TableColumn<History, String> rentPlaceColumn;
    @FXML
    private TableColumn<History, String> returnPlaceColumn;
    @FXML
    private TableColumn<History, String> rentBikeUIDColumn; // 新增的列
    @FXML
    private TableColumn<History, Integer> rentFeeColumn;

    private Preferences prefs;

    /**
     * 初始化控制器，設置表格列並加載當前用戶的租還車紀錄。
     */
    @FXML
    private void initialize() {
        prefs = Preferences.userNodeForPackage(LoginController.class);
        String currentMemberID = prefs.get("currentMemberID", "");

        rentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("rent_time"));
        returnTimeColumn.setCellValueFactory(new PropertyValueFactory<>("return_time"));
        rentPlaceColumn.setCellValueFactory(new PropertyValueFactory<>("rent_place"));
        returnPlaceColumn.setCellValueFactory(new PropertyValueFactory<>("return_place"));
        rentBikeUIDColumn.setCellValueFactory(new PropertyValueFactory<>("rent_BikeUID")); // 新增的列設置
        rentFeeColumn.setCellValueFactory(new PropertyValueFactory<>("rent_fee"));

        loadHistoryData(currentMemberID);
    }

    /**
     * 加載指定會員的租還車紀錄。
     * @param memberID 會員ID。
     */
    private void loadHistoryData(String memberID) {
        Gson gson = new Gson();

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/history.json"))) {
            Type historyListType = new TypeToken<List<History>>() {}.getType();
            List<History> histories = gson.fromJson(reader, historyListType);

            ObservableList<History> filteredHistories = FXCollections.observableArrayList();
            for (History history : histories) {
                if (history.getMemberID().equals(memberID)) {
                    filteredHistories.add(history);
                }
            }

            historyTableView.setItems(filteredHistories);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("錯誤", "無法加載租還車紀錄。");
        }
    }

    /**
     * 顯示提示框。
     * @param title 提示框的標題。
     * @param message 提示框的內容。
     */
    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
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
