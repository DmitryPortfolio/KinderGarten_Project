/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static org.connector.connector.ConnectDB;
import org.model.ModelKinderGarten;

/**
 * FXML Controller class
 *
 * @author benzi
 */
public class KinderGartenAddController implements Initializable {

    @FXML
    private TextField tfAdress;
    @FXML
    private TextField tfTelephone;
    @FXML
    private TextField tfDirector;
    @FXML
    private TextField tfName;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnCancel;
    
    private ModelKinderGarten operatorToEdit;
    private boolean isEditMode = false;
     Connection conn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
    void setModelKinderGartenToEdit(ModelKinderGarten selectedAdress) {
        this.operatorToEdit = selectedAdress;
        isEditMode = true;

        tfName.setText(selectedAdress.getName());
        tfAdress.setText(selectedAdress.getAdres());
        tfTelephone.setText(selectedAdress.getTelefon());
        tfDirector.setText(selectedAdress.getDirector());
    }

    @FXML
    private void Add(ActionEvent event) {
        String opName = tfName.getText();
        String opAdress = tfAdress.getText();
        String opPhone = tfTelephone.getText();
        String opDirector = tfDirector.getText();
        if (opName.isEmpty()) {
            //showAlert("Пожалуйста, заполните все поля.");
            return;
        }

        if (isEditMode) {
            updateOperator(operatorToEdit.getID(), opName, opAdress, opPhone, opDirector);
        } else {
            insertOperator(opName, opAdress, opPhone, opDirector);
        }

        closeWindow(event);
    }
    
    private void insertOperator(String opName, String opAdress, String opPhone, String opDirector) {
        String sql = "INSERT INTO kindergarten (Name, Adres, Telefon, Director) VALUES (?,?,?,?)";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, opName);
            pstmt.setString(2, opAdress);
            pstmt.setString(3, opPhone);
            pstmt.setString(4, opDirector);

            pstmt.executeUpdate();
            System.out.println("Детский сад добавлен");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateOperator(int id, String opName, String opAdress, String opPhone, String opDirector) {
        String sql = "UPDATE kindergarten SET Name = ?,  Adres = ?, Telefon = ?, Director = ? WHERE ID = ?";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, opName);
                pstmt.setString(2, opAdress);
                pstmt.setString(3, opPhone);
                pstmt.setString(4, opDirector);
                pstmt.setInt(5, id);

                pstmt.executeUpdate();
                System.out.println("Детский сад обновлен");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    @FXML
    private void Cancel(ActionEvent event) {
          Stage stage = (Stage) BtnCancel.getScene().getWindow();
        stage.close();
    }

    

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();  
    }

    
    
}
