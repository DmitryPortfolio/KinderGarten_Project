/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import static org.connector.connector.ConnectDB;
import org.model.ModelLessons;

/**
 * FXML Controller class
 *
 * @author Пользователь
 */
public class LessonsController implements Initializable {

    @FXML
    private TableView<ModelLessons> TvLessons;
    @FXML
    private TableColumn<ModelLessons, String> TcLessons;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnEdit;
    @FXML
    private Button BtnDelete;
    ObservableList<ModelLessons> list;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         ConnectDB();
        UpdateTable();
    }    
    
    
    public static ObservableList<ModelLessons> getData() {
        ObservableList<ModelLessons> ModelLessons = FXCollections.observableArrayList();
        Connection conn = ConnectDB();

        try {

            PreparedStatement ps = conn.prepareStatement("select *from lessons");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModelLessons.add(new ModelLessons(Integer.parseInt(
                        rs.getString("ID")),                   
                        rs.getString("Name_Lessons")));

            }
        } catch (SQLException ex) {

        }
        return ModelLessons;
    }

    public void UpdateTable() {
      
        TcLessons.setCellValueFactory(new PropertyValueFactory<ModelLessons, String>("Name_Lessons"));
        list = getData();
        TvLessons.setItems(list);

        
 
    }
    
    @FXML
    private void Add(ActionEvent event) throws IOException {
          FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLLessonsAdd.fxml"));
        Pane page = (Pane) loader.load();
        LessonsAddController addController = loader.getController();
        

        Stage duStage = new Stage();
        duStage.setTitle("Добавить запись");
        duStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/add.png")));

        Scene scene = new Scene(page);
        duStage.setScene(scene);
        duStage.setResizable(false);
        duStage.showAndWait();
        UpdateTable();
    }

    @FXML
    private void Edit(ActionEvent event) throws IOException {
        ModelLessons selectedAdress = TvLessons.getSelectionModel().getSelectedItem();

        if (selectedAdress == null) {
          //  showAlert("Выберите адрес для редактирования.");
            return;
        }
        
          FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLLessonsAdd.fxml"));
        Pane page = (Pane) loader.load();
LessonsAddController addController = loader.getController();
         addController.setModelLessonsToEdit(selectedAdress);

        Stage duStage = new Stage();
        duStage.setTitle("Редактировать запись");
        duStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/edit.png")));

        Scene scene = new Scene(page);
        duStage.setScene(scene);
        duStage.setResizable(false);
        duStage.showAndWait();
        UpdateTable();
    }

    @FXML
    private void Delete(ActionEvent event) {
     ModelLessons selected = TvLessons.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Выберите строку для удаления");
            alert.setHeaderText("");
            alert.setTitle("Ошибка");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы действительно хотите удалить запись?", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText("");
            alert.setTitle("Подтверждение");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                int del = selected.getID();
                String sql = "DELETE FROM lessons WHERE ID = ?";

                try (Connection conn = ConnectDB();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setInt(1, del);
                    pstmt.executeUpdate();
                    System.out.println("Запись удалена");
                    UpdateTable();
                } catch (SQLException e) {
                    System.out.println("Ошибка при удалении записи: " + e.getMessage());
                    e.printStackTrace();
                    showAlert("Ошибка при удалении записи.");
                }
            }
        }
    }
    
     private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}