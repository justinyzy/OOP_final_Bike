package org.example.ubkie.Controller.User;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.ubkie.Controller.User.AccountSettingsController;
import org.example.ubkie.GetAndSet.Member;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;
import java.util.prefs.Preferences;

/**
 * 控制器類別，負責顯示和管理用戶的卡片資訊。
 * 包括顯示卡片號碼和餘額，以及處理充值和交易記錄的操作。
 */
public class CardInfoController {

    @FXML
    private Label cardNumberLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Button topUpButton;
    @FXML
    private Button transactionHistoryButton;

    private Preferences prefs;
    private List<Member> members;
    private Member currentMember;
    public static int balance;

    /**
     * 初始化控制器，載入用戶資料並顯示卡片資訊。
     */
    @FXML
    private void initialize() {
        prefs = Preferences.userNodeForPackage(AccountSettingsController.class);
        loadMembers();

        String currentMemberID = prefs.get("currentMemberID", "");
        System.out.println("Loaded currentMemberID from preferences: " + currentMemberID); // Debugging line

        if (members == null) {
            System.out.println("Members list is null"); // Debugging line
            members = new ArrayList<>();
        }
        currentMember = members.stream()
                .filter(member -> member.getMemberID().equals(currentMemberID))
                .findFirst()
                .orElse(null);

        System.out.println("Current member: " + (currentMember != null ? currentMember.getMemberID() : "null")); // Debugging line

        if (currentMember != null) {
            String yoyoID = currentMember.getYOYO_ID();
            String yoyoCard = currentMember.getYOYO_Card();

            System.out.println("YOYO_ID: " + yoyoID);
            System.out.println("YOYO_Card: " + yoyoCard);

            if (yoyoID == null || yoyoID.isEmpty()) {
                cardNumberLabel.setText("請前往帳號設定頁面登記");
                topUpButton.setDisable(true);
                transactionHistoryButton.setDisable(true);
            } else {
                cardNumberLabel.setText(yoyoID);
                topUpButton.setDisable(false);
                transactionHistoryButton.setDisable(false);
            }

            balance = yoyoCard == null || yoyoCard.isEmpty() ? 0 : Integer.parseInt(yoyoCard);
            balanceLabel.setText(String.valueOf(balance));
        } else {
            cardNumberLabel.setText("請前往帳號設定頁面登記");
            topUpButton.setDisable(true);
            transactionHistoryButton.setDisable(true);
        }
    }

    /**
     * 從JSON文件中載入所有會員的資料。
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
     * 處理充值按鈕事件，跳轉到充值界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleTopUpButtonAction(ActionEvent event) throws IOException {
        Parent topUpView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/top-up-view.fxml"));
        Scene topUpScene = new Scene(topUpView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(topUpScene);
        stage.show();
    }

    /**
     * 處理交易記錄按鈕事件，跳轉到交易記錄界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleTransactionHistoryButtonAction(ActionEvent event) throws IOException {
        Parent transactionHistoryView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/transaction-history-view.fxml"));
        Scene transactionHistoryScene = new Scene(transactionHistoryView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(transactionHistoryScene);
        stage.show();
    }

    /**
     * 處理返回按鈕事件，返回主菜單界面。
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
