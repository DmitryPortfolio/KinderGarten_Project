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
import org.model.ModelLessons;

/**
 * FXML Controller class
 *
 * @author Пользователь
 */
public class LessonsAddController implements Initializable {
    ModelLessons lessons;
    @FXML
    private TextField TfName;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button btnExit;

     private ModelLessons operatorToEdit;
    private boolean isEditMode = false;
     Connection conn;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

     void setModelLessonsToEdit(ModelLessons modellessons) {
        this.operatorToEdit = modellessons;
        isEditMode = true;

        TfName.setText(modellessons.getName_Lessons());
      
    }
    
    
    
    @FXML
    private void Add(ActionEvent event) {
        
          String opName = TfName.getText();
         
        if (opName.isEmpty()) {
            showAlert("Пожалуйста, заполните все поля.");
            return;
        }

        if (isEditMode) {
            updateOperator(operatorToEdit.getID(), opName);
        } else {
            insertOperator(opName);
        }

        closeWindow(event);
    }

    @FXML
    private void Exit(ActionEvent event) {
          Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();  
    }

    private void showAlert(String пожалуйста_заполните_все_поля) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void updateOperator(int id, String opName) {
         String sql = "UPDATE lessons SET Name_Lessons = ? WHERE ID = ?";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           pstmt.setString(1, opName);
            pstmt.setInt(2, id);

            pstmt.executeUpdate();
            System.out.println("Занятие обновленно");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertOperator(String opName) {
        String sql = "INSERT INTO lessons (Name_Lessons) VALUES (?)";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, opName);
            

            pstmt.executeUpdate();
            System.out.println("Занятие добавлено");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();  
    }
    
}
