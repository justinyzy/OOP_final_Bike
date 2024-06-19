package org.example.ubkie.Controller.Staff;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.example.ubkie.GetAndSet.Member;

import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * 控制器類別，負責管理會員資料的顯示和操作。
 * 提供了會員資料的加載、刪除和保存功能。
 */
public class AllMembersController {

    @FXML
    private TableView<Member> membersTable;

    private ObservableList<Member> membersList;
    private List<Member> members;

    /**
     * 初始化控制器，加載會員數據到表格。
     */
    @FXML
    private void initialize() {
        // Load member data
        loadMembersData();
    }

    /**
     * 從JSON文件加載會員數據到列表和表格中。
     */
    private void loadMembersData() {
        Type memberListType = new TypeToken<List<Member>>() {}.getType();
        Gson gson = new Gson();
        members = gson.fromJson(new InputStreamReader(getClass().getResourceAsStream("/member_id.json")), memberListType);
        membersList = FXCollections.observableArrayList(members);
        membersTable.setItems(membersList);
    }

    /**
     * 將修改後的會員數據保存回JSON文件。
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
     * 處理刪除按鈕事件，從表格和數據列表中移除選中的會員。
     * @param event 觸發此操作的事件對象。
     */
    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        Member selectedMember = membersTable.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "您確定要刪除選中的會員資料嗎？", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                membersTable.getItems().remove(selectedMember);
                members.remove(selectedMember);
                saveMembersData();  // Save changes to the JSON file
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "請先選擇要刪除的會員資料。", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * 處理返回按鈕事件，導航回管理界面。
     * @param event 觸發此操作的事件對象。
     * @throws IOException 當加載FXML文件失敗時拋出。
     */
    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        Parent menuView = FXMLLoader.load(getClass().getResource("/org/example/ubkie/admin-view.fxml"));
        Scene menuScene = new Scene(menuView);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(menuScene);
        stage.show();
    }
}
