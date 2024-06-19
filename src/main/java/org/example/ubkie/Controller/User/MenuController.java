package org.example.ubkie.Controller.User;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ubkie.GetAndSet.Member;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * 控制器類別，負責管理主選單的操作。
 * 用戶可以選擇租借、歸還車輛，查詢站點，查看租借記錄等操作。
 */
public class MenuController {

    @FXML
    private Button rentButton;

    @FXML
    private Button returnButton;

    @FXML
    private Button stationQueryButton;

    @FXML
    private Button rentReturnRecordButton;

    @FXML
    private Button cardRechargeButton;

    @FXML
    private Button maintenanceReportButton;

    @FXML
    private Button passwordSettingButton;

    private Preferences prefs;

    /**
     * 初始化控制器，設置偏好設置節點。
     */
    @FXML
    private void initialize() {
        prefs = Preferences.userNodeForPackage(MenuController.class);
    }

    /**
     * 根據會員ID獲取會員信息。
     * @param memberID 會員ID。
     * @return 對應的會員信息，若無則返回null。
     */
    private Member getMemberByID(String memberID) {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/member_id.json"))) {
            Gson gson = new Gson();
            List<Member> members = gson.fromJson(reader, new TypeToken<List<Member>>(){}.getType());
            for (Member member : members) {
                if (member.getMemberID().equals(memberID)) {
                    return member;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
     * 檢查會員是否已註冊悠遊卡。
     * @param memberID 會員ID。
     * @return 若已註冊則返回true，否則返回false。
     */
    private boolean isYoyoCardRegistered(String memberID) {
        Member currentMember = getMemberByID(memberID);
        return currentMember != null && currentMember.getYOYO_ID() != null && !currentMember.getYOYO_ID().isEmpty();
    }

    /**
     * 檢查會員是否有足夠的餘額。
     * @param memberID 會員ID。
     * @return 若餘額足夠則返回true，否則返回false。
     */
    private boolean hasSufficientBalance(String memberID) {
        Member currentMember = getMemberByID(memberID);
        return currentMember != null && Integer.parseInt(currentMember.getYOYO_Card()) > 0;
    }

    /**
     * 處理租借按鈕的操作，檢查會員的租借狀態和餘額，並跳轉到租借界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleRentButtonAction(ActionEvent event) throws IOException {
        String currentMemberID = prefs.get("currentMemberID", "");
        if (isYoyoCardRegistered(currentMemberID)) {
            if (hasSufficientBalance(currentMemberID)) {
                Member currentMember = getMemberByID(currentMemberID);
                if (currentMember != null && "not using".equals(currentMember.getRent_status())) {
                    Parent rentView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/rent-view.fxml"));
                    Scene rentScene = new Scene(rentView);
                    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                    stage.setScene(rentScene);
                    stage.show();
                } else {
                    showAlert("警告", "已有租用車輛尚未歸還");
                }
            } else {
                showAlert("警告", "餘額不足不得租車");
            }
        } else {
            showAlert("警告", "尚未登記悠遊卡號，請至帳號設定頁面登記");
        }
    }

    /**
     * 處理歸還按鈕的操作，檢查會員的租借狀態，並跳轉到歸還界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleReturnButtonAction(ActionEvent event) throws IOException {
        String currentMemberID = prefs.get("currentMemberID", "");
        if (isYoyoCardRegistered(currentMemberID)) {
            if (isMemberRenting(currentMemberID)) {
                Parent returnView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/return-view.fxml"));
                Scene returnScene = new Scene(returnView);
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(returnScene);
                stage.show();
            } else {
                showAlert("錯誤", "目前尚未租借任何車輛");
            }
        } else {
            showAlert("警告", "尚未登記悠遊卡號，請至帳號設定頁面登記");
        }
    }

    /**
     * 檢查會員是否正在租借車輛。
     * @param memberID 會員ID。
     * @return 若會員正在租借車輛則返回true，否則返回false。
     */
    private boolean isMemberRenting(String memberID) {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/member_id.json"))) {
            Gson gson = new Gson();
            Type memberListType = new TypeToken<List<Member>>() {}.getType();
            List<Member> members = gson.fromJson(reader, memberListType);
            for (Member member : members) {
                if (member.getMemberID().equals(memberID) && "using".equals(member.getRent_status())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 處理站點查詢按鈕的操作，跳轉到站點查詢界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleStationQueryButtonAction(ActionEvent event) throws IOException {
        Parent stationQueryView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/station-query-view.fxml"));
        Scene stationQueryScene = new Scene(stationQueryView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(stationQueryScene);
        stage.show();
    }

    /**
     * 處理租借歸還記錄按鈕的操作，檢查會員是否已註冊悠遊卡，並跳轉到租借歸還記錄界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleRentReturnRecordButtonAction(ActionEvent event) throws IOException {
        String currentMemberID = prefs.get("currentMemberID", "");
        if (isYoyoCardRegistered(currentMemberID)) {
            Parent rentReturnRecordView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/rent-return-record-view.fxml"));
            Scene rentReturnRecordScene = new Scene(rentReturnRecordView);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(rentReturnRecordScene);
            stage.show();
        } else {
            showAlert("警告", "尚未登記悠遊卡號，請至帳號設定頁面登記");
        }
    }

    /**
     * 處理卡片信息按鈕的操作，跳轉到卡片信息界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleCardInfoButtonAction(ActionEvent event) throws IOException {
        Parent cardInfoView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/card-info-view.fxml"));
        Scene cardInfoScene = new Scene(cardInfoView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(cardInfoScene);
        stage.show();
    }

    /**
     * 處理維修報告按鈕的操作，跳轉到維修報告界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleMaintenanceReportButtonAction(ActionEvent event) throws IOException {
        Parent maintenanceReportView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/maintenance-report-view.fxml"));
        Scene maintenanceReportScene = new Scene(maintenanceReportView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(maintenanceReportScene);
        stage.setMinWidth(360);
        stage.setMinHeight(640);
        stage.setMaxWidth(360);
        stage.setMaxHeight(640);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * 處理帳號設置按鈕的操作，跳轉到帳號設置界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleAccountSettingButtonAction(ActionEvent event) throws IOException {
        Parent accountSettingsView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/account-settings-view.fxml"));
        Scene accountSettingsScene = new Scene(accountSettingsView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(accountSettingsScene);
        stage.show();
    }
}
