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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.ubkie.GetAndSet.Member;
import org.example.ubkie.HelloApplication;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * 控制器類別，負責管理用戶更改密碼的操作。
 */
public class ChangePasswordController {

    @FXML
    private PasswordField currentPasswordField;
    @FXML
    private TextField currentPasswordVisibleField;

    @FXML
    private PasswordField newPasswordField;
    @FXML
    private TextField newPasswordVisibleField;

    @FXML
    private PasswordField confirmNewPasswordField;
    @FXML
    private TextField confirmNewPasswordVisibleField;

    @FXML
    private ImageView eyeIconCurrent;
    @FXML
    private ImageView eyeIconNew;
    @FXML
    private ImageView eyeIconConfirmNew;

    @FXML
    private Button saveButton;

    private boolean isCurrentPasswordVisible = false;
    private boolean isNewPasswordVisible = false;
    private boolean isConfirmNewPasswordVisible = false;

    private Preferences prefs;
    private List<Member> members;
    private Member currentMember;

    /**
     * 初始化控制器，載入用戶資料並設置密碼字段的可見性切換功能。
     */
    @FXML
    private void initialize() {
        prefs = Preferences.userNodeForPackage(ChangePasswordController.class);

        loadMembersData();
        String phoneNumber = prefs.get("currentMemberID", "");
        currentMember = members.stream().filter(member -> member.getMemberID().equals(phoneNumber)).findFirst().orElse(null);

        System.out.println("Loaded currentMemberID from preferences: " + phoneNumber);
        if (currentMember != null) {
            System.out.println("Current member password from database: " + currentMember.getMember_password());
        }

        currentPasswordVisibleField.setVisible(false);
        currentPasswordField.managedProperty().bind(currentPasswordVisibleField.visibleProperty().not());
        currentPasswordVisibleField.managedProperty().bind(currentPasswordVisibleField.visibleProperty());
        currentPasswordVisibleField.textProperty().bindBidirectional(currentPasswordField.textProperty());

        newPasswordVisibleField.setVisible(false);
        newPasswordField.managedProperty().bind(newPasswordVisibleField.visibleProperty().not());
        newPasswordVisibleField.managedProperty().bind(newPasswordVisibleField.visibleProperty());
        newPasswordVisibleField.textProperty().bindBidirectional(newPasswordField.textProperty());

        confirmNewPasswordVisibleField.setVisible(false);
        confirmNewPasswordField.managedProperty().bind(confirmNewPasswordVisibleField.visibleProperty().not());
        confirmNewPasswordVisibleField.managedProperty().bind(confirmNewPasswordVisibleField.visibleProperty());
        confirmNewPasswordVisibleField.textProperty().bindBidirectional(confirmNewPasswordField.textProperty());

        saveButton.setDisable(false);
    }

    /**
     * 從JSON文件中載入所有會員的資料。
     */
    private void loadMembersData() {
        try {
            Gson gson = new Gson();
            Type memberListType = new TypeToken<List<Member>>() {}.getType();
            members = gson.fromJson(new InputStreamReader(getClass().getResourceAsStream("/member_id.json")), memberListType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 切換當前密碼字段的可見性。
     */
    @FXML
    private void toggleCurrentPasswordVisibility() {
        isCurrentPasswordVisible = !isCurrentPasswordVisible;
        if (isCurrentPasswordVisible) {
            currentPasswordVisibleField.setText(currentPasswordField.getText());
            currentPasswordVisibleField.setVisible(true);
            currentPasswordField.setVisible(false);
            eyeIconCurrent.setImage(new Image(getClass().getResourceAsStream("/eye_open.png")));
        } else {
            currentPasswordField.setText(currentPasswordVisibleField.getText());
            currentPasswordField.setVisible(true);
            currentPasswordVisibleField.setVisible(false);
            eyeIconCurrent.setImage(new Image(getClass().getResourceAsStream("/eye_close.png")));
        }
    }

    /**
     * 切換新密碼字段的可見性。
     */
    @FXML
    private void toggleNewPasswordVisibility() {
        isNewPasswordVisible = !isNewPasswordVisible;
        if (isNewPasswordVisible) {
            newPasswordVisibleField.setText(newPasswordField.getText());
            newPasswordVisibleField.setVisible(true);
            newPasswordField.setVisible(false);
            eyeIconNew.setImage(new Image(getClass().getResourceAsStream("/eye_open.png")));
        } else {
            newPasswordField.setText(newPasswordVisibleField.getText());
            newPasswordField.setVisible(true);
            newPasswordVisibleField.setVisible(false);
            eyeIconNew.setImage(new Image(getClass().getResourceAsStream("/eye_close.png")));
        }
    }

    /**
     * 切換確認新密碼字段的可見性。
     */
    @FXML
    private void toggleConfirmNewPasswordVisibility() {
        isConfirmNewPasswordVisible = !isConfirmNewPasswordVisible;
        if (isConfirmNewPasswordVisible) {
            confirmNewPasswordVisibleField.setText(confirmNewPasswordField.getText());
            confirmNewPasswordVisibleField.setVisible(true);
            confirmNewPasswordField.setVisible(false);
            eyeIconConfirmNew.setImage(new Image(getClass().getResourceAsStream("/eye_open.png")));
        } else {
            confirmNewPasswordField.setText(confirmNewPasswordVisibleField.getText());
            confirmNewPasswordField.setVisible(true);
            confirmNewPasswordVisibleField.setVisible(false);
            eyeIconConfirmNew.setImage(new Image(getClass().getResourceAsStream("/eye_close.png")));
        }
    }

    /**
     * 處理保存按鈕事件，檢查用戶輸入是否合法，並保存修改。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleSaveButtonAction(ActionEvent event) throws IOException {
        String currentPassword = currentPasswordField.getText().trim();
        String newPassword = newPasswordField.getText().trim();
        String confirmNewPassword = confirmNewPasswordField.getText().trim();

        if (currentMember == null || !currentPassword.equals(currentMember.getMember_password())) {
            showAlert("錯誤", "原始密碼錯誤");
            return;
        }

        if (newPassword.length() < 8 || newPassword.length() > 20) {
            showAlert("錯誤", "新密碼不符合格式");
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            showAlert("錯誤", "兩次新密碼輸入不同");
            return;
        }

        currentMember.setMember_password(newPassword);
        saveMembersData();

        Parent menuView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/menu-view.fxml"));
        Scene menuScene = new Scene(menuView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(menuScene);
        stage.show();
        //HelloApplication.syncDatabase();
    }

    /**
     * 將所有會員資料保存到JSON文件中。
     */
    private void saveMembersData() {
        try {
            Gson gson = new Gson();
            try (FileWriter writer = new FileWriter(Paths.get(getClass().getResource("/member_id.json").toURI()).toFile())) {
                gson.toJson(members, writer);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 顯示錯誤提示框。
     * @param title 提示框的標題。
     * @param message 提示框的內容。
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * 處理返回按鈕事件，返回帳號設定界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        Parent accountSettingsView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/account-settings-view.fxml"));
        Scene accountSettingsScene = new Scene(accountSettingsView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(accountSettingsScene);
        stage.setMinWidth(360);
        stage.setMinHeight(640);
        stage.setMaxWidth(360);
        stage.setMaxHeight(640);
        stage.setResizable(false);
        stage.show();
    }
}
