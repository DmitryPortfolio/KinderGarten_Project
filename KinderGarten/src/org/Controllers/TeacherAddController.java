/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.model.ModelTeacher;

/**
 * FXML Controller class
 *
 * @author benzi
 */
public class TeacherAddController implements Initializable {

    @FXML
    private TextField tfPhone;
     @FXML
    private TextField TfAdres;
    @FXML
    private TextField tfSpecialization;
    @FXML
    private TextField tfFIO;
  
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnCancel;
    
    private ModelTeacher teacherToEdit;
    private boolean isEditMode = false;
    
    
   
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }  
    

    
    void setModelTeacherToEdit(ModelTeacher selectedAdress) {
        this.teacherToEdit = selectedAdress;
        isEditMode = true;

        tfFIO.setText(selectedAdress.getFIO());
        tfPhone.setText(selectedAdress.getTelefon());
        TfAdres.setText(selectedAdress.getAdres());
        tfSpecialization.setText(selectedAdress.getSpecialization());

       

    }
    
    @FXML
    private void Add(ActionEvent event) {
        String fio = tfFIO.getText();
        String phone = tfPhone.getText();
         String Adres = TfAdres.getText();
        String special = tfSpecialization.getText();

       

        if (fio.isEmpty() || phone.isEmpty() || Adres.isEmpty() || special.isEmpty() ) {
            showAlert("Пожалуйста, заполните все поля.");
            return;
        }

        if (isEditMode) {
            updateTarif(teacherToEdit.getID(), fio, phone, Adres, special);
        } else {
            insertTarif(fio, phone, Adres, special);
        }

        closeWindow(event);
    }
    
    private void insertTarif(String fio, String phone,  String Adres, String special) {
        String sql = "INSERT INTO teacher (FIO, Telefon, Adres, Specialization) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fio);
            pstmt.setString(2, phone);
            pstmt.setString(3, Adres);
            pstmt.setString(4, special);
           

            pstmt.executeUpdate();
            System.out.println("Учитель добавлен");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert("Ошибка при добавлении учителя.");
        }
    }

    private void updateTarif(int id, String fio, String phone, String Adres, String special) {
        String sql = "UPDATE teacher SET FIO = ?, Telefon = ?, Adres = ?, Specialization = ? WHERE ID = ?";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fio);
            pstmt.setString(2, phone);
            pstmt.setString(3, Adres);
            pstmt.setString(4, special);
            pstmt.setInt(5, id);

            pstmt.executeUpdate();
            System.out.println("Учитель обновлен");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert("Ошибка при обновлении учителя.");
        }
    }
    
    @FXML
    private void Cancel(ActionEvent event) {
          Stage stage = (Stage) BtnCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    

    
    
}