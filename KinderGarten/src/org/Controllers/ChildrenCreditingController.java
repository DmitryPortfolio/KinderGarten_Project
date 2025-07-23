
package org.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.connector.connector;
import org.model.ModelChildren;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChildrenCreditingController implements Initializable {

    @FXML
    private TextField TfFIO;
    @FXML
    private DatePicker dpDateBirth;
    private ComboBox<String> CbParents;
    @FXML
    private TextField TfBirthInformation;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnExit;
    private ModelChildren childrenToEdit;
    private boolean isEditMode = false;
    private Map<String, String> parentsIdMap = new HashMap<>();
    private ChildrenController childrenController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dpDateBirth.setValue(LocalDate.now());
    
    }

    private void populateComboBoxes() {


    
    }

    private ObservableList<String> ParentsNames = FXCollections.observableArrayList();
    private Map<String, String> ParentsIdMap = new HashMap<>();

   

    

    public void setChildrenController(ChildrenController childrenController) {
        this.childrenController = childrenController;
    }

    @FXML
    private void Add(ActionEvent event) {
        String FIO = TfFIO.getText();
        LocalDate dateBirthValue = dpDateBirth.getValue();
        String BirthInformation = TfBirthInformation.getText();
        

        String DateBirth = null;
        if (dateBirthValue != null) {
            DateBirth = dateBirthValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        if (FIO == null || DateBirth == null || BirthInformation == null) {
            showAlert("Пожалуйста, заполните все поля.");
            return;
        }

        if (isEditMode) {
            updateTarif(childrenToEdit.getID(), FIO, DateBirth, BirthInformation);
        } else {
            insertTarif(FIO, DateBirth, BirthInformation);
        }

        Stage stage = (Stage) BtnAdd.getScene().getWindow();
        stage.close();
        childrenController.UpdateTable();
    }

    void setModelChildrenToEdit(ModelChildren selectedChildren) {
        this.childrenToEdit = selectedChildren;
        isEditMode = true;

        TfFIO.setText(selectedChildren.getFIO());
        dpDateBirth.setValue(LocalDate.parse(selectedChildren.getBirthday()));

        TfBirthInformation.setText(selectedChildren.getBirthdayCertificate());
    }

    private String getChildrenName(String id) {
        for (Map.Entry<String, String> entry : ParentsIdMap.entrySet()) {
            if (entry.getValue().equals(id)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void insertTarif(String FIO, String DateBirth, String BirthInformation) {
        String sql = "INSERT INTO children (FIO, Birthday, BirthdayCertificate, Status) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = connector.ConnectDB();
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, FIO);
            pstmt.setString(2, DateBirth);
            pstmt.setString(3, BirthInformation);
            pstmt.setString(4, "Кандидат");

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            if (rowsAffected > 0) {
                System.out.println("Учитель добавлен");
            } else {
                System.out.println("Ошибка при добавлении учителя");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert("Ошибка при добавлении учителя.");
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private void updateTarif(int id, String FIO, String DateBirth, String BirthInformation) {

        String sql = "UPDATE children SET FIO = ?, Birthday = ?, BirthdayCertificate = ?, Status = ? WHERE ID = ?";

        try (Connection conn = connector.ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, FIO);
            pstmt.setString(2, DateBirth);
            pstmt.setString(3, BirthInformation);
            pstmt.setString(4, "Кандидат");
            pstmt.setInt(5, id);

            pstmt.executeUpdate();
            System.out.println("Тариф обновлен");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert("Ошибка при обновлении тарифа.");
        }
    }

    @FXML
    private void Exit(ActionEvent event) {
        Stage stage = (Stage) BtnExit.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

}
