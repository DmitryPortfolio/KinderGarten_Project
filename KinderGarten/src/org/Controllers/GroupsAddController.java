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
import org.model.ModelAttendance;
import org.model.ModelGroups;

/**
 * FXML Controller class
 *
 * @author Пользователь
 */
public class GroupsAddController implements Initializable {
    ModelGroups group;
    @FXML
    private TextField TfNameGroup;
    @FXML
    private TextField TfYear;
    @FXML
    private Button BtnAdd;

  private ModelGroups operatorToEdit;
    private boolean isEditMode = false;
     Connection conn;
    @FXML
    private Button BtnExit;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
  void setModelOperatorToEdit(ModelGroups modelOperator) {
        this.operatorToEdit = modelOperator;
        isEditMode = true;

        TfNameGroup.setText(modelOperator.getName_groups());
        TfYear.setText(modelOperator.getYear());
    }
    

    @FXML
    private void Aadd(ActionEvent event) {
        
          String opName = TfNameGroup.getText();
          String opYear = TfYear.getText();
        if (opName.isEmpty()) {
            showAlert("Пожалуйста, заполните все поля.");
            return;
        }

        if (isEditMode) {
            updateOperator(operatorToEdit.getID(), opName, opYear);
        } else {
            insertOperator(opName, opYear);
        }

        closeWindow(event);

    }

    private void insertOperator(String opName, String opYear) {
       String sql = "INSERT INTO groups (Name_groups, Year) VALUES (?,?)";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, opName);
             pstmt.setString(2, opYear);

            pstmt.executeUpdate();
            System.out.println("Оператор добавлен");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateOperator(int id, String opName, String opYear) {
      String sql = "UPDATE groups SET Name_groups = ?,  Year = ? WHERE ID = ?";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           pstmt.setString(1, opName);
            pstmt.setString(2, opYear);
            pstmt.setInt(3, id);

            pstmt.executeUpdate();
            System.out.println("Оператор обновлен");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void closeWindow(ActionEvent event) {
     Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();  
    }

    private void showAlert(String пожалуйста_заполните_все_поля) {
       
    }

    @FXML
    private void Exit(ActionEvent event) {
          Stage stage = (Stage) BtnExit.getScene().getWindow();
        stage.close();
    }
}
    

