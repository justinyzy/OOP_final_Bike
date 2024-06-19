package org.example.ubkie.Controller.User;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import org.example.ubkie.GetAndSet.Member;
import org.example.ubkie.HelloApplication;

/**
 * 控制器類別，負責管理帳戶設定的操作，包括顯示和修改用戶資料。
 */
public class AccountSettingsController {

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField idNumberField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField cardNumberField;

    private Preferences prefs;
    private List<Member> members;
    private Member currentMember;

    /**
     * 初始化控制器，載入用戶資料並顯示在界面上。
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

        phoneNumberField.setText(currentMemberID);
        if (currentMember != null) {
            idNumberField.setText(currentMember.getId_number() != null ? currentMember.getId_number() : "");
            emailField.setText(currentMember.getEmail() != null ? currentMember.getEmail() : "");
            cardNumberField.setText(currentMember.getYOYO_ID() != null ? currentMember.getYOYO_ID() : "");

            if (currentMember.getYOYO_ID() != null && !currentMember.getYOYO_ID().isEmpty()) {
                cardNumberField.setDisable(true);
            } else {
                cardNumberField.setDisable(false);
            }
        } else {
            idNumberField.setText("");
            emailField.setText("");
            cardNumberField.setText("");
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
        } catch (IOException e) {
            e.printStackTrace();
            members = new ArrayList<>();
        }
    }

    /**
     * 將所有會員資料保存到JSON文件中。
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
     * 處理保存按鈕事件，檢查用戶輸入是否合法，並保存修改。
     * @param event 觸發此操作的事件對象。
     */
    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        if (validateFields() && currentMember != null) {
            currentMember.setId_number(idNumberField.getText().trim());
            currentMember.setEmail(emailField.getText().trim());
            currentMember.setYOYO_ID(cardNumberField.getText().trim());

            saveMembers();
            resetFieldStyles();

            // Disable the card number field after saving
            cardNumberField.setDisable(true);
        }
        //HelloApplication.syncDatabase();
    }

    /**
     * 驗證用戶輸入的所有欄位是否合法。
     * @return 如果所有欄位均合法，返回true，否則返回false。
     */
    private boolean validateFields() {
        boolean isValid = true;

        String idNumber = idNumberField.getText().trim();
        String email = emailField.getText().trim();
        String cardNumber = cardNumberField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();

        if (!phoneNumber.matches("09\\d{8}")) {
            setFieldInvalid(phoneNumberField);
            isValid = false;
        }

        if (!idNumber.matches("[A-Z][12]\\d{8}")) {
            setFieldInvalid(idNumberField);
            isValid = false;
        }

        if (!isValidEmail(email)) {
            setFieldInvalid(emailField);
            isValid = false;
        }

        if (!cardNumber.matches("\\d{16}")) {
            setFieldInvalid(cardNumberField);
            isValid = false;
        }

        return isValid;
    }

    /**
     * 驗證給定的電子郵件地址是否合法。
     * @param email 要驗證的電子郵件地址。
     * @return 如果電子郵件地址合法，返回true，否則返回false。
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 設置輸入框的樣式為無效狀態。
     * @param field 要設置為無效狀態的輸入框。
     */
    private void setFieldInvalid(TextField field) {
        field.setStyle("-fx-border-color: red;");
    }

    /**
     * 重置所有輸入框的樣式。
     */
    private void resetFieldStyles() {
        phoneNumberField.setStyle("");
        idNumberField.setStyle("");
        emailField.setStyle("");
        cardNumberField.setStyle("");
    }

    /**
     * 處理修改密碼按鈕事件，跳轉到修改密碼界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleChangePasswordButtonAction(ActionEvent event) throws IOException {
        Parent changePasswordView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/change-password-view.fxml"));
        Scene changePasswordScene = new Scene(changePasswordView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(changePasswordScene);
        stage.setMinWidth(360);
        stage.setMinHeight(640);
        stage.setMaxWidth(360);
        stage.setMaxHeight(640);
        stage.setResizable(false);
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
