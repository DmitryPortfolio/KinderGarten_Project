
package org.Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static org.connector.connector.ConnectDB;
import org.model.ModelParents;

public class ParentsAddController implements Initializable {

    @FXML
    private TextField TfFIO;
    @FXML
    private TextField TfTelefon;
    @FXML
    private TextField TfAdres;
    @FXML
    private TextField Tfpasport;
    @FXML
    private ComboBox<String> CbIdChild;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnExit;
    private Connection conn;
    private Map<Integer, String> idToFioMap = new HashMap<>();
    private ModelParents parents;
    private boolean isEditMode = false;
    private ObservableList<String> childrenFIOs;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = ConnectDB();
        loadFIOs();
    }

    public void loadFIOs() {
        childrenFIOs = FXCollections.observableArrayList();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT FIO FROM children");
            while (rs.next()) {
                childrenFIOs.add(rs.getString("FIO"));
            }
            CbIdChild.setItems(childrenFIOs);
        } catch (SQLException ex) {
            
            ex.printStackTrace();
        }
    }


    void setModelParentsToEdit(ModelParents selectedParents) {
        this.parents = selectedParents;
        isEditMode = true;

        TfFIO.setText(selectedParents.getFIO());
        TfTelefon.setText(selectedParents.getTelefon());
        TfAdres.setText(selectedParents.getArdres());
        Tfpasport.setText(selectedParents.getPassport());

        try {
            int childId = Integer.parseInt(selectedParents.getId_Children());
            String childFIO = getFIOById(childId);
            CbIdChild.setValue(childFIO);
        } catch (NumberFormatException e) {
           
        }
    }

    private String getFIOById(int childId) {
        String fio = null;
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT FIO FROM children WHERE ID = ?")) {
            pstmt.setInt(1, childId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                fio = rs.getString("FIO");
            }
        } catch (SQLException e) {
           
        }
        return fio;
    }



    @FXML
    private void Add(ActionEvent event) {
        String FioP = TfFIO.getText();
        String Telefon = TfTelefon.getText();
        String Adres = TfAdres.getText();
        String Pasport = Tfpasport.getText();
        String fioChild = CbIdChild.getValue();

        if (FioP == null || Telefon == null || Adres == null || Pasport == null || fioChild == null) {
            showAlert("Пожалуйста, заполните все поля.");
            return;
        }

        Integer childrenId = getChildrenIdByFIO(fioChild);
        if (childrenId == null) {
            
            return;
        }

        if (isEditMode) {
            updateTarif(parents.getID(), FioP, Telefon, Adres, Pasport, childrenId);
        } else {
            insertTarif(FioP, Telefon, Adres, Pasport, childrenId);
        }

        Stage stage = (Stage) BtnAdd.getScene().getWindow();
        stage.close();
    }

    private Integer getChildrenIdByFIO(String fioChild) {
        Integer id = null;
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT ID FROM children WHERE FIO = ?")) {
            pstmt.setString(1, fioChild);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("ID");
            }
        } catch (SQLException e) {
           
        }
        return id;
    }


    @FXML
    private void Exit(ActionEvent event) {
        Stage stage = (Stage) BtnExit.getScene().getWindow();
        stage.close();
    }

    private void updateTarif(int id, String FioP, String Telefon, String Adres, String Pasport, int childrenId) {
        String sql = "UPDATE parents SET FIO = ?, Telefon = ?, Ardres = ?, Passport = ?, Id_Children = ? WHERE ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, FioP);
            pstmt.setString(2, Telefon);
            pstmt.setString(3, Adres);
            pstmt.setString(4, Pasport);
            pstmt.setInt(5, childrenId);
            pstmt.setInt(6, id);

            pstmt.executeUpdate();
            System.out.println("Родитель обновлен");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert("Ошибка при обновлении родителя: " + e.getMessage());
        }
    }

    private void insertTarif(String FioP, String Telefon, String Adres, String Pasport, int childrenId) {
        String sql = "INSERT INTO parents (FIO, Telefon, Ardres, Passport, Id_Children) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, FioP);
            pstmt.setString(2, Telefon);
            pstmt.setString(3, Adres);
            pstmt.setString(4, Pasport);
            pstmt.setInt(5, childrenId);

            pstmt.executeUpdate();
            System.out.println("Родитель добавлен");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert("Ошибка при добавлении родителя: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
