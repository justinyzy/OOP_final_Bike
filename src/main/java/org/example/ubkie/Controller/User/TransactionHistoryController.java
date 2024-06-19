package org.example.ubkie.Controller.User;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TableCell;
import org.example.ubkie.Controller.User.AccountSettingsController;
import org.example.ubkie.GetAndSet.Member;
import org.example.ubkie.GetAndSet.YoyoHistory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

/**
 * 控制器類別，負責處理用戶的交易歷史查詢操作。
 */
public class TransactionHistoryController {

    @FXML
    private Label cardNumberLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Button backButton;

    @FXML
    private TableView<YoyoHistory> transactionTable;
    @FXML
    private TableColumn<YoyoHistory, String> dateTimeColumn;
    @FXML
    private TableColumn<YoyoHistory, String> itemColumn;
    @FXML
    private TableColumn<YoyoHistory, String> amountColumn;

    private Preferences prefs;
    private List<Member> members;
    private Member currentMember;
    private ObservableList<YoyoHistory> transactions;

    /**
     * 初始化控制器，載入用戶信息和交易記錄。
     */
    @FXML
    private void initialize() {
        prefs = Preferences.userNodeForPackage(AccountSettingsController.class);
        loadMembers();

        String currentMemberID = prefs.get("currentMemberID", "");
        currentMember = members.stream()
                .filter(member -> member.getMemberID().equals(currentMemberID))
                .findFirst()
                .orElse(null);

        if (currentMember != null) {
            cardNumberLabel.setText(currentMember.getYOYO_ID());
            balanceLabel.setText(currentMember.getYOYO_Card());

            loadTransactions(currentMemberID);
        } else {
            cardNumberLabel.setText("未知");
            balanceLabel.setText("未知");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根據會員ID從 JSON 文件載入交易記錄。
     * @param memberID 當前會員ID。
     */
    private void loadTransactions(String memberID) {
        transactions = FXCollections.observableArrayList();
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/yoyo_history.json"))) {
            Gson gson = new Gson();
            Type transactionListType = new TypeToken<List<YoyoHistory>>() {}.getType();
            List<YoyoHistory> allTransactions = gson.fromJson(reader, transactionListType);
            transactions.addAll(allTransactions.stream()
                    .filter(transaction -> transaction.getMemberID().equals(memberID))
                    .collect(Collectors.toList()));

            dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
            itemColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
            amountColumn.setCellValueFactory(new PropertyValueFactory<>("transaction"));

            // 自定義單元格工廠以顯示基於操作的文本
            itemColumn.setCellFactory(new Callback<TableColumn<YoyoHistory, String>, TableCell<YoyoHistory, String>>() {
                @Override
                public TableCell<YoyoHistory, String> call(TableColumn<YoyoHistory, String> param) {
                    return new TableCell<YoyoHistory, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setText(null);
                            } else {
                                if (item.equals("TopUp")) {
                                    setText("儲值");
                                } else if (item.equals("RentCar")) {
                                    setText("租借Ubike");
                                } else {
                                    setText(item);
                                }
                            }
                        }
                    };
                }
            });

            transactionTable.setItems(transactions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 處理返回按鈕的操作，導航回卡片信息視圖。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleBackButtonAction() throws IOException {
        Parent cardInfoView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/card-info-view.fxml"));
        Scene cardInfoScene = new Scene(cardInfoView);
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(cardInfoScene);
        stage.show();
    }
}
