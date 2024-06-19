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

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.prefs.Preferences;

/**
 * 控制器類別，負責用戶登錄操作。
 * 包括驗證用戶名和密碼，並處理登錄和註冊按鈕的操作。
 */
public class LoginController {

    @FXML
    private TextField phoneNumberField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordVisibleField;

    @FXML
    private Button loginButton;

    @FXML
    private ImageView eyeIcon;

    private boolean isPasswordVisible = false;
    private Preferences prefs;
    private List<Member> members;

    /**
     * 初始化控制器，設置密碼字段的可見性切換功能，並載入會員資料。
     */
    @FXML
    private void initialize() {
        prefs = Preferences.userNodeForPackage(LoginController.class);
        loadMembers();

        phoneNumberField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        passwordVisibleField.setVisible(false);
        passwordVisibleField.managedProperty().bind(passwordVisibleField.visibleProperty());
        passwordField.managedProperty().bind(passwordVisibleField.visibleProperty().not());
        passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty());
    }

    /**
     * 從JSON文件中載入會員資料。
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
     * 驗證輸入字段是否合法。
     * 根據輸入的有效性設置字段樣式並更新登錄按鈕的可用性。
     */
    private void validateFields() {
        String phoneNumber = phoneNumberField.getText().trim();
        String password = passwordField.getText().trim();

        boolean isPhoneNumberValid = phoneNumber.matches("09\\d{8}");
        boolean isPasswordValid = password.length() >= 8 && password.length() <= 20;

        setFieldStyle(phoneNumberField, isPhoneNumberValid);
        setFieldStyle(passwordField, isPasswordValid);

        loginButton.setDisable(!(isPhoneNumberValid && isPasswordValid));
    }

    /**
     * 根據字段的有效性設置字段的樣式。
     * @param field 需要設置樣式的文本字段。
     * @param isValid 字段是否有效。
     */
    private void setFieldStyle(TextField field, boolean isValid) {
        if (isValid) {
            field.setStyle("");
        } else {
            field.setStyle("-fx-border-color: red;");
        }
    }

    /**
     * 切換密碼字段的可見性。
     */
    @FXML
    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            passwordVisibleField.setText(passwordField.getText());
            passwordVisibleField.setVisible(true);
            passwordField.setVisible(false);
            eyeIcon.setImage(new Image(getClass().getResourceAsStream("/eye_open.png")));
        } else {
            passwordField.setText(passwordVisibleField.getText());
            passwordField.setVisible(true);
            passwordVisibleField.setVisible(false);
            eyeIcon.setImage(new Image(getClass().getResourceAsStream("/eye_close.png")));
        }
    }

    /**
     * 處理登錄按鈕事件，驗證用戶名和密碼是否正確，並跳轉到主菜單界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException {
        String phoneNumber = phoneNumberField.getText().trim();
        String password = passwordField.getText().trim();

        Optional<Member> optionalMember = members.stream()
                .filter(member -> member.getMemberID().equals(phoneNumber))
                .findFirst();

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if (member.getMember_password().equals(password)) {
                prefs.put("currentMemberID", phoneNumber);  // 保存当前用户的 MemberID
                Parent menuView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/menu-view.fxml"));
                Scene menuScene = new Scene(menuView);
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(menuScene);
                stage.setMinWidth(360);
                stage.setMinHeight(640);
                stage.setMaxWidth(360);
                stage.setMaxHeight(640);
                stage.setResizable(false);
                stage.show();
            } else {
                showAlert(Alert.AlertType.ERROR, "密碼有誤", "請確認您的密碼是否正確。");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "此手機號碼尚未註冊", "請先註冊後再嘗試登入。");
        }
    }

    /**
     * 顯示提示框。
     * @param alertType 提示框的類型。
     * @param title 提示框的標題。
     * @param message 提示框的內容。
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * 處理註冊按鈕事件，跳轉到註冊界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleRegisterButtonAction(ActionEvent event) throws IOException {
        Parent registerView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/register-view.fxml"));
        Scene registerScene = new Scene(registerView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(registerScene);
        stage.setMinWidth(360);
        stage.setMinHeight(640);
        stage.setMaxWidth(360);
        stage.setMaxHeight(640);
        stage.setResizable(false);
        stage.show();
    }
}
