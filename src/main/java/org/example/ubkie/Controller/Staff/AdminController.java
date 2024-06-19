package org.example.ubkie.Controller.Staff;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * 管理員控制器類別，負責處理來自管理界面的各種動作。
 */
public class AdminController {

    /**
     * 處理查看車輛狀態按鈕的事件。
     * 加載並顯示車輛狀態界面。
     * @param event 觸發此事件的動作事件對象。
     * @throws Exception 當加載FXML文件失敗時拋出。
     */
    @FXML
    private void handleVehicleStatusButtonAction(ActionEvent event) throws Exception {
        Parent vehicleStatusView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/vehicle-status-view.fxml"));
        Scene vehicleStatusScene = new Scene(vehicleStatusView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(vehicleStatusScene);
        stage.show();
    }

    /**
     * 處理查看車柱狀態按鈕的事件。
     * 加載並顯示車柱狀態界面。
     * @param event 觸發此事件的動作事件對象。
     * @throws Exception 當加載FXML文件失敗時拋出。
     */
    @FXML
    private void handleDockStatusButtonAction(ActionEvent event) throws Exception {
        Parent stationStatusView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/dock-status-view.fxml"));
        Scene stationStatusScene = new Scene(stationStatusView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(stationStatusScene);
        stage.show();
    }

    /**
     * 處理跨區車輛按鈕的事件。
     * 加載並顯示跨區車輛處理界面。
     * @param event 觸發此事件的動作事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出。
     */
    @FXML
    private void handleCrossRegionVehicleButtonAction(ActionEvent event) throws IOException {
        Parent crossRegionView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/cross-region-vehicle-view.fxml"));
        Scene crossRegionScene = new Scene(crossRegionView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(crossRegionScene);
        stage.show();
    }

    /**
     * 處理車輛調度按鈕的事件。
     * 加載並顯示車輛調度界面。
     * @param event 觸發此事件的動作事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出。
     */
    @FXML
    private void handleVehicleDispatchButtonAction(ActionEvent event) throws IOException {
        Parent vehicleDispatchView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/vehicle-dispatch-view.fxml"));
        Scene vehicleDispatchScene = new Scene(vehicleDispatchView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(vehicleDispatchScene);
        stage.show();
    }

    /**
     * 處理報修列表按鈕的事件。
     * 加載並顯示報修列表界面。
     * @param event 觸發此事件的動作事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出。
     */
    @FXML
    private void handleMaintenanceListButtonAction(ActionEvent event) throws IOException {
        Parent maintenanceListView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/maintenance-list-view.fxml"));
        Scene maintenanceListScene = new Scene(maintenanceListView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(maintenanceListScene);
        stage.show();
    }

    /**
     * 處理查看會員按鈕的事件。
     * 加載並顯示所有會員資料的界面。
     * @param event 觸發此事件的動作事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出。
     */
    @FXML
    private void handleViewMembersButtonAction(ActionEvent event) throws IOException {
        Parent adminView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/all-members-view.fxml"));
        Scene adminScene = new Scene(adminView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(adminScene);
        stage.show();
    }
}
