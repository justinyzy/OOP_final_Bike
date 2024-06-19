package org.example.ubkie.Controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

/**
 * 控制器類別，負責處理用戶的站點查詢操作。
 */
public class StationQueryController {

    @FXML
    private WebView webView;

    /**
     * 初始化控制器，載入地圖視圖。
     */
    @FXML
    private void initialize() {
        WebEngine webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("/map.html").toExternalForm());
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
