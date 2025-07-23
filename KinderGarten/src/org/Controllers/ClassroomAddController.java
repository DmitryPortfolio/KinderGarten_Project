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
import org.model.ModelClassroom;

/**
 * FXML Controller class
 *
 * @author Пользователь
 */
public class ClassroomAddController implements Initializable {

    @FXML
    private TextField TfNamberRoom;
    @FXML
    private TextField TfNameRoom;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnExit;
 private ModelClassroom parents;
    private boolean isEditMode = false;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    void setModelClassroomToEdit(ModelClassroom selectedClassroom) {
        this.parents = selectedClassroom;
        isEditMode = true;

        TfNamberRoom.setText(selectedClassroom.getNamber_room());
        TfNameRoom.setText(selectedClassroom.getName_room());
       

    }
    
    
    
    @FXML
    private void Add(ActionEvent event) {
          String NamberRoom = TfNamberRoom.getText();
        String NameRoom = TfNameRoom.getText();
     

        if (NamberRoom == null || NameRoom == null ) {
            //showAlert("Пожалуйста, заполните все поля.");
            return;
        }
       

        if (isEditMode) {
            updateTarif(parents.getID(), NameRoom, NamberRoom);
        } else {
            insertTarif(NameRoom, NamberRoom);
        }

        Stage stage = (Stage) BtnAdd.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void Exit(ActionEvent event) {
          Stage stage = (Stage) BtnExit.getScene().getWindow();
        stage.close();
    }

    private void insertTarif(String NameRoom, String NamberRoom) {
         String sql = "INSERT INTO classroom (Name_room, namber_room) VALUES (?, ?)";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, NameRoom);
            pstmt.setString(2, NamberRoom);
            

            pstmt.executeUpdate();
            System.out.println("Родитель добавлен");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
           // showAlert("Ошибка при добавлении родителя: " + e.getMessage());
        }
       
    }

    private void updateTarif(int id, String NameRoom, String NamberRoom) {
        String sql = "UPDATE classroom SET Name_room = ?, namber_room = ?  WHERE ID = ?";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, NameRoom);
            pstmt.setString(2, NamberRoom);
            pstmt.setInt(3, id);

            pstmt.executeUpdate();
            System.out.println("Родитель обновлен");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
           // showAlert("Ошибка при обновлении родителя: " + e.getMessage());
        }
    }
    
}
