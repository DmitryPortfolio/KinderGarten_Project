
package org.Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static org.connector.connector.ConnectDB;
import org.model.ModelChildren;

public class ChildrenAllEditController implements Initializable {

    @FXML
    private TextField TfFIO;
    @FXML
    private DatePicker TfBirthday;
    @FXML
    private DatePicker TfDateReceipt;
    @FXML
    private DatePicker TfDateDeparture;
    @FXML
    private ComboBox<String> CbGroup;
    @FXML
    private ComboBox<String> CbStatus;
    @FXML
    private TextField TfBirthdayCertificate;
    @FXML
    private TextField Tfretirement;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnExit;

    private ModelChildren child;
    private ChildrenController childrenController;

    private Connection conn;
    private ObservableList<String> groupNames;
    private ObservableList<String> statuses;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = ConnectDB();
    }

    public void setChild(ModelChildren child) {
        this.child = child;
        populateFields();
    }

    public void setChildrenController(ChildrenController childrenController) {
        this.childrenController = childrenController;
    }

    public void loadGroups() {
        groupNames = FXCollections.observableArrayList();
        try {
            Statement st = conn.createStatement();
            java.sql.ResultSet rs = st.executeQuery("SELECT Name_groups FROM groups");
            while (rs.next()) {
                groupNames.add(rs.getString("Name_groups"));
            }
            CbGroup.setItems(groupNames);
        } catch (SQLException ex) {
            showAlert("Ошибка загрузки групп: " + ex.getMessage());
        }
    }

    public void loadStatuses() {
        statuses = FXCollections.observableArrayList("Кандидат", "Учащийся", "Выбывший");
        CbStatus.setItems(statuses);
    }

    private void populateFields() {
        TfFIO.setText(child.getFIO());

        if (child.getBirthday() != null && !child.getBirthday().isEmpty()) {
            TfBirthday.setValue(LocalDate.parse(child.getBirthday()));
        }
        if (child.getDate_receipt() != null && !child.getDate_receipt().isEmpty()) {
            TfDateReceipt.setValue(LocalDate.parse(child.getDate_receipt()));
        }
        if (child.getDate_departure() != null && !child.getDate_departure().isEmpty()) {
            TfDateDeparture.setValue(LocalDate.parse(child.getDate_departure()));
        }

        CbGroup.setValue(child.getId_Groups());
        CbStatus.setValue(child.getStatus());
        TfBirthdayCertificate.setText(child.getBirthdayCertificate());
        Tfretirement.setText(child.getRetirement());
    }

    @FXML
    private void Add(ActionEvent event) {
        try {
            String fio = TfFIO.getText();
            LocalDate birthday = TfBirthday.getValue();
            LocalDate dateReceipt = TfDateReceipt.getValue();
            LocalDate dateDeparture = TfDateDeparture.getValue();
            String groupName = CbGroup.getValue();
            String status = CbStatus.getValue();
            String birthdayCertificate = TfBirthdayCertificate.getText();
            String retirement = Tfretirement.getText();


            String sql = "UPDATE children SET FIO = ?, Birthday = ?, Date_receipt = ?, Date_departure = ?, "
                    + "Id_Groups = (SELECT ID FROM groups WHERE Name_groups = ?), "
                    + "BirthdayCertificate = ?, retirement = ?, Status = ? WHERE ID = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fio);
            pstmt.setString(2, (birthday != null) ? birthday.toString() : null);
            pstmt.setString(3, (dateReceipt != null) ? dateReceipt.toString() : null);
            pstmt.setString(4, (dateDeparture != null) ? dateDeparture.toString() : null);

             if (groupName != null && !groupName.isEmpty()) {
                pstmt.setString(5, groupName);
            } else {
                pstmt.setNull(5, java.sql.Types.VARCHAR);
            }
            pstmt.setString(6, birthdayCertificate);
            pstmt.setString(7, retirement);

            if (status != null && !status.isEmpty()) {
                pstmt.setString(8, status);
            } else {
                pstmt.setNull(8, java.sql.Types.VARCHAR);
            }
            
            pstmt.setInt(9, child.getID());
            pstmt.executeUpdate();

            showAlert("Запись успешно обновлена.");
            childrenController.UpdateTable();
            Stage stage = (Stage) BtnAdd.getScene().getWindow();
            stage.close();

        } catch (SQLException ex) {
            showAlert("Ошибка при обновлении записи: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void Exit(ActionEvent event) {
        Stage stage = (Stage) BtnExit.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Оповещение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
