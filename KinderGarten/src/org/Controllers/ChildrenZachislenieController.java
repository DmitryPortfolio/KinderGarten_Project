package org.Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import static org.connector.connector.ConnectDB;
import org.model.ModelChildren;

/**
 * FXML Controller class
 *
 * @author benzi
 */
public class ChildrenZachislenieController implements Initializable {

    @FXML
    private ComboBox<String> tfGroup;
    @FXML
    private DatePicker dpReceipt;
    @FXML
    private DatePicker dpDeparture;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnCancel;

    private ModelChildren child;
    private ChildrenController childrenController;

    Connection conn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadGroups();
    }

    public void setChild(ModelChildren child) {
        this.child = child;
    }

    public void setChildrenController(ChildrenController controller) {
        this.childrenController = controller;
    }

    private void loadGroups() {
        ObservableList<String> groupList = FXCollections.observableArrayList();
        Connection conn = ConnectDB();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT Name_groups FROM groups");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                groupList.add(rs.getString("Name_groups"));
            }
            tfGroup.setItems(groupList);
        } catch (SQLException ex) {
            showAlert("Ошибка при загрузке групп: " + ex.getMessage(), Alert.AlertType.ERROR);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void Add(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        Connection conn = null;
        try {
            conn = ConnectDB();

            String groupName = tfGroup.getValue();
            int groupId = getGroupIdByName(groupName, conn);

            if (groupId == -1) {
                showAlert("Группа не найдена.", Alert.AlertType.ERROR);
                return;
            }

            String sql = "UPDATE children SET Date_receipt = ?, Date_departure = ?, Id_Groups = ?, Status = ? WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dpReceipt.getValue().toString());
            ps.setString(2, dpDeparture.getValue() != null ? dpDeparture.getValue().toString() : null);
            ps.setInt(3, groupId);
            ps.setString(4, "Учащийся");
            ps.setInt(5, child.getID());

            ps.executeUpdate();

            showAlert("Ребенок успешно зачислен.", Alert.AlertType.INFORMATION);

            if (childrenController != null) {
                childrenController.UpdateTable();
            }

            closeWindow();

        } catch (SQLException ex) {
            showAlert("Ошибка при зачислении ребенка: " + ex.getMessage(), Alert.AlertType.ERROR);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private boolean validateInput() {
        if (dpReceipt.getValue() == null) {
            showAlert("Пожалуйста, выберите дату поступления.", Alert.AlertType.WARNING);
            return false;
        }
        if (tfGroup.getValue() == null) {
            showAlert("Пожалуйста, выберите группу.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private int getGroupIdByName(String name, Connection conn) throws SQLException {
        String sql = "SELECT ID FROM groups WHERE Name_groups = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("ID");
        }
        return -1;
    }

    @FXML
    private void Cancel(ActionEvent event) {
        Stage stage = (Stage) BtnCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType == Alert.AlertType.ERROR ? "Ошибка" : "Информация");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) BtnCancel.getScene().getWindow();
        stage.close();
    }

}