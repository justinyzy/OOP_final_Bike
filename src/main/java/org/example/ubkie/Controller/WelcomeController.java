package org.example.ubkie.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * 歡迎畫面的控制器類別，處理用戶和管理員的登入以及註冊操作。
 */
public class WelcomeController {

    @FXML
    private ImageView logoImage;

    /**
     * 初始化方法，設置歡迎畫面上的Logo圖片。
     */
    @FXML
    private void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/logo.png"));
        logoImage.setImage(image);
    }

    /**
     * 處理用戶按鈕的操作，導航到用戶登入畫面。
     * @param event 當用戶按下用戶按鈕時觸發的事件。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleUserButtonAction(ActionEvent event) throws IOException {
        Parent loginView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/login-view.fxml"));
        Scene loginScene = new Scene(loginView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.setMinWidth(360);
        stage.setMinHeight(640);
        stage.setMaxWidth(360);
        stage.setMaxHeight(640);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * 處理管理員按鈕的操作，導航到管理員登入畫面。
     * @param event 當用戶按下管理員按鈕時觸發的事件。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleAdminButtonAction(ActionEvent event) throws IOException {
        Parent adminView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/admin-view.fxml"));
        Scene adminScene = new Scene(adminView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(adminScene);
        stage.setMinWidth(360);
        stage.setMinHeight(640);
        stage.setMaxWidth(360);
        stage.setMaxHeight(640);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * 處理註冊按鈕的操作，導航到用戶註冊畫面。
     * @param event 當用戶按下註冊按鈕時觸發的事件。
     * @throws IOException 當加載FXML文件失敗時拋出此異常。
     */
    @FXML
    private void handleRegisterButtonAction(ActionEvent event) throws IOException {
        Parent registerView = FXMLLoader.load(getClass().getResource("register-view.fxml"));
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
