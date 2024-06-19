package org.example.ubkie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import org.example.ubkie.Database.DatabaseUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 主應用程序類，負責啟動JavaFX應用程序。
 */
public class HelloApplication extends Application {
    /**
     * 啟動應用程序。
     *
     * @param stage 主舞台
     * @throws IOException 如果加載FXML佈局失敗
     */
    @Override
    public void start(Stage stage) throws IOException {
        // 加載FXML佈局
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/ubkie/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 360, 640);

        // 設置標題和圖標
        stage.setTitle("UBike");
        stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("/logo.png")));

        // 設置場景並顯示
        stage.setScene(scene);
        stage.show();

        // 在應用啟動時進行資料庫同步
        // syncDatabase();
    }

    /**
     * 將JSON數據同步到資料庫。
     */
    public static void syncDatabase() {
        syncJsonFile("/Dock_id.json", "dock_data");
        syncJsonFile("/history.json", "history");
        syncJsonFile("/member_id.json", "member_id");
        syncJsonFile("/NewTaipei.json", "newtaipei");
        syncJsonFile("/Taipei.json", "taipei");
        syncJsonFile("/ubike_data.json", "ubike_data");
        syncJsonFile("/yoyo_history.json", "yoyo_history");
    }

    /**
     * 同步指定的JSON文件到資料庫。
     *
     * @param fileName JSON文件的名稱
     * @param tableName 資料庫表的名稱
     */
    public static void syncJsonFile(String fileName, String tableName) {
        try (InputStream resourceStream = HelloApplication.class.getResourceAsStream(fileName)) {
            if (resourceStream == null) {
                System.err.println("Resource not found: " + fileName);
                return;
            }
            DatabaseUtils.syncJsonToDatabase(resourceStream, tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主方法，啟動應用程序。
     *
     * @param args 命令行參數
     */
    public static void main(String[] args) {
        launch(args);
    }
}
