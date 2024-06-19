package org.example.ubkie.Controller.User;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.ubkie.Controller.User.AccountSettingsController;
import org.example.ubkie.GetAndSet.Member;
import org.example.ubkie.GetAndSet.YoyoHistory;
import org.example.ubkie.HelloApplication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * 控制器類別，負責處理用戶的儲值操作。
 */
public class TopUpController {

    @FXML
    private Label cardNumberLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private TextField topUpAmountField;
    @FXML
    private Button topUpButton;

    private Preferences prefs;
    private List<Member> members;
    private Member currentMember;
    private List<YoyoHistory> yoyoHistories;

    /**
     * 初始化控制器，載入用戶信息和悠遊卡歷史記錄。
     */
    @FXML
    private void initialize() {
        prefs = Preferences.userNodeForPackage(AccountSettingsController.class);
        loadMembers();
        loadYoyoHistories();

        String currentMemberID = prefs.get("currentMemberID", "");
        System.out.println("Loaded currentMemberID from preferences: " + currentMemberID);

        if (members == null) {
            System.out.println("Members list is null");
            members = new ArrayList<>();
        }
        currentMember = members.stream()
                .filter(member -> member.getMemberID().equals(currentMemberID))
                .findFirst()
                .orElse(null);

        if (currentMember != null) {
            String yoyoID = currentMember.getYOYO_ID();
            int balance = Integer.parseInt(currentMember.getYOYO_Card());

            cardNumberLabel.setText(yoyoID);
            balanceLabel.setText(String.valueOf(balance));
        } else {
            cardNumberLabel.setText("未設定");
            balanceLabel.setText("0");
            topUpButton.setDisable(true);
        }
    }

    /**
     * 從 JSON 文件載入會員數據。
     */
    private void loadMembers() {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/member_id.json"))) {
            Gson gson = new Gson();
            Type memberListType = new TypeToken<List<Member>>() {}.getType();
            members = gson.fromJson(reader, memberListType);
            System.out.println("Loaded members data: " + members.size() + " members");
        } catch (IOException e) {
            e.printStackTrace();
            members = new ArrayList<>();
        }
    }

    /**
     * 將會員數據保存到 JSON 文件。
     */
    private void saveMembers() {
        try {
            Path path = Paths.get(getClass().getResource("/member_id.json").toURI());
            Gson gson = new Gson();
            try (Writer writer = Files.newBufferedWriter(path)) {
                gson.toJson(members, writer);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 從 JSON 文件載入悠遊卡歷史記錄數據。
     */
    private void loadYoyoHistories() {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/yoyo_history.json"))) {
            Gson gson = new Gson();
            Type yoyoHistoryListType = new TypeToken<List<YoyoHistory>>() {}.getType();
            yoyoHistories = gson.fromJson(reader, yoyoHistoryListType);
            if (yoyoHistories == null) {
                yoyoHistories = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            yoyoHistories = new ArrayList<>();
        }
    }

    /**
     * 將悠遊卡歷史記錄數據保存到 JSON 文件。
     */
    private void saveYoyoHistories() {
        try {
            Path path = Paths.get(getClass().getResource("/yoyo_history.json").toURI());
            Gson gson = new Gson();
            try (Writer writer = Files.newBufferedWriter(path)) {
                gson.toJson(yoyoHistories, writer);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 處理儲值按鈕的操作，進行儲值並更新會員餘額和歷史記錄。
     * @param event 觸發此操作的事件對象。
     */
    @FXML
    private void handleTopUpButtonAction(ActionEvent event) {
        try {
            int topUpAmount = Integer.parseInt(topUpAmountField.getText().trim());

            if (topUpAmount <= 0) {
                showAlert("錯誤", "儲值數必須為正數");
                return;
            }

            int currentBalance = Integer.parseInt(currentMember.getYOYO_Card());
            int newBalance = currentBalance + topUpAmount;

            currentMember.setYOYO_Card(String.valueOf(newBalance));
            saveMembers();

            addYoyoHistory(currentMember.getMemberID(), "+" + topUpAmount, "TopUp");

            balanceLabel.setText(String.valueOf(newBalance));
            topUpAmountField.clear();
        } catch (NumberFormatException e) {
            showAlert("錯誤", "請輸入數字");
        }
        //HelloApplication.syncDatabase();
    }

    /**
     * 新增一條悠遊卡歷史記錄。
     * @param memberID 會員ID。
     * @param transaction 交易金額。
     * @param action 動作類型。
     */
    private void addYoyoHistory(String memberID, String transaction, String action) {
        YoyoHistory newHistory = new YoyoHistory();
        newHistory.setMemberID(memberID);
        newHistory.setTransaction(transaction);
        newHistory.setAction(action);
        newHistory.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        yoyoHistories.add(newHistory);
        saveYoyoHistories();
    }

    /**
     * 顯示提示訊息框。
     * @param title 提示框標題。
     * @param message 提示框內容。
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * 處理返回按鈕的操作，導航回卡片信息視圖。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        Parent cardInfoView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/card-info-view.fxml"));
        Scene cardInfoScene = new Scene(cardInfoView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(cardInfoScene);
        stage.show();
    }
}
