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
 * 控制器類別，負責用戶註冊界面的操作。
 * 用戶可以輸入手機號碼和密碼來註冊新帳戶。
 */
public class RegisterController {

    @FXML
    private TextField phoneNumberField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordVisibleField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField confirmPasswordVisibleField;

    @FXML
    private Button registerButton;

    @FXML
    private ImageView eyeIcon;

    @FXML
    private ImageView eyeIconConfirm;

    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    private Preferences prefs;

    /**
     * 初始化控制器，設置偏好設置節點和字段監聽器。
     */
    @FXML
    private void initialize() {
        prefs = Preferences.userNodeForPackage(RegisterController.class);

        phoneNumberField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        passwordVisibleField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        confirmPasswordVisibleField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());

        passwordVisibleField.setVisible(false);
        passwordField.setVisible(true);
        confirmPasswordVisibleField.setVisible(false);
        confirmPasswordField.setVisible(true);
        eyeIcon.setImage(new Image(getClass().getResourceAsStream("/eye_close.png")));
        eyeIconConfirm.setImage(new Image(getClass().getResourceAsStream("/eye_close.png")));
    }

    /**
     * 驗證輸入字段是否合法。
     * 根據字段的填寫情況啟用或禁用註冊按鈕。
     */
    private void validateFields() {
        String phoneNumber = phoneNumberField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        boolean isPhoneNumberValid = phoneNumber.matches("09\\d{8}");
        boolean isPasswordValid = password.length() >= 8 && password.length() <= 20;
        boolean isConfirmPasswordValid = password.equals(confirmPassword);

        setFieldStyle(phoneNumberField, isPhoneNumberValid);
        setFieldStyle(passwordField, isPasswordValid);
        setFieldStyle(confirmPasswordField, isConfirmPasswordValid);

        registerButton.setDisable(!(isPhoneNumberValid && isPasswordValid && isConfirmPasswordValid));
    }

    /**
     * 設置字段的樣式。
     * @param field 要設置樣式的字段。
     * @param isValid 字段的驗證結果。
     */
    private void setFieldStyle(TextField field, boolean isValid) {
        if (isValid) {
            field.setStyle("");
        } else {
            field.setStyle("-fx-border-color: red;");
        }
    }

    /**
     * 切換密碼可見性。
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
     * 切換確認密碼的可見性。
     */
    @FXML
    private void toggleConfirmPasswordVisibility() {
        isConfirmPasswordVisible = !isConfirmPasswordVisible;
        if (isConfirmPasswordVisible) {
            confirmPasswordVisibleField.setText(confirmPasswordField.getText());
            confirmPasswordVisibleField.setVisible(true);
            confirmPasswordField.setVisible(false);
            eyeIconConfirm.setImage(new Image(getClass().getResourceAsStream("/eye_open.png")));
        } else {
            confirmPasswordField.setText(confirmPasswordVisibleField.getText());
            confirmPasswordField.setVisible(true);
            confirmPasswordVisibleField.setVisible(false);
            eyeIconConfirm.setImage(new Image(getClass().getResourceAsStream("/eye_close.png")));
        }
    }

    /**
     * 處理註冊按鈕的操作，檢查輸入是否合法，並註冊新用戶。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     * @throws URISyntaxException 當URI語法錯誤時拋出此異常。
     */
    @FXML
    private void handleRegisterButtonAction(ActionEvent event) throws IOException, URISyntaxException {
        String phoneNumber = phoneNumberField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (!phoneNumber.matches("09\\d{8}")) {
            showAlert("註冊失敗", "手機號碼格式不符", "請輸入正確的手機號碼格式（09XXXXXXXX）");
            return;
        }

        if (password.length() < 8 || password.length() > 20) {
            showAlert("註冊失敗", "密碼格式不符", "密碼必須為8到20位之間");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("註冊失敗", "兩次密碼輸入不同", "請確認兩次輸入的密碼相同");
            return;
        }

        // 加載現有的會員資料
        Gson gson = new Gson();
        Type memberListType = new TypeToken<List<Member>>() {}.getType();
        List<Member> members = gson.fromJson(new InputStreamReader(getClass().getResourceAsStream("/member_id.json")), memberListType);

        // 檢查手機號碼是否已存在
        boolean exists = members.stream().anyMatch(member -> member.getMemberID().equals(phoneNumber));

        if (exists) {
            showAlert("註冊失敗", "此手機號碼已經完成註冊", "請使用其他手機號碼註冊");
            phoneNumberField.setStyle("-fx-border-color: red;");
        } else {
            // 創建新會員並添加到列表中
            Member newMember = new Member();
            newMember.setMemberID(phoneNumber);
            newMember.setMember_password(password);
            newMember.setId_number("");
            newMember.setEmail("");
            newMember.setYOYO_Card("0");
            newMember.setYOYO_ID("");
            newMember.setRent_status("not using");
            members.add(newMember);

            // 保存更新後的會員列表到JSON文件
            try (FileWriter writer = new FileWriter(Paths.get(getClass().getResource("/member_id.json").toURI()).toFile())) {
                gson.toJson(members, writer);
            }

            prefs.put("currentMemberID", phoneNumber);
            prefs.put("password", password);

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
        }
        //HelloApplication.syncDatabase();
    }

    /**
     * 顯示提示框。
     * @param title 提示框的標題。
     * @param header 提示框的標題內容。
     * @param content 提示框的主要內容。
     */
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
