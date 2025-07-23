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
import org.model.ModelClassroom;

/**
 * FXML Controller class
 *
 * @author Пользователь
 */
public class ClassroomController implements Initializable {

    @FXML
    private TableColumn<ModelClassroom, String> TcNameRoom;
    @FXML
    private TableColumn<ModelClassroom, String> TcNomerRoom;
    @FXML
    private TableView<ModelClassroom> TvClassroom;
ObservableList<ModelClassroom> list;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnEdit;
    @FXML
    private Button BtnDelete;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConnectDB();
        UpdateTable();
    }
public static ObservableList<ModelClassroom> getData() {
        ObservableList<ModelClassroom> ModelClassroom = FXCollections.observableArrayList();
        Connection conn = ConnectDB();

        try {

            
            PreparedStatement ps = conn.prepareStatement("SELECT *FROM classroom");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModelClassroom.add(new ModelClassroom(Integer.parseInt(
                        rs.getString("ID")),
                        rs.getString("Name_room"),            
                        rs.getString("namber_room")));

            }
        } catch (SQLException ex) {

        }
        return ModelClassroom;
    }

    public void UpdateTable() {
        TcNameRoom.setCellValueFactory(new PropertyValueFactory<ModelClassroom, String>("Name_room"));
        TcNomerRoom.setCellValueFactory(new PropertyValueFactory<ModelClassroom, String>("namber_room"));
        list = getData();
        TvClassroom.setItems(list);

    }

    @FXML
    private void Add(ActionEvent event) throws IOException {
         FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLClassroomAdd.fxml"));
        Pane page = (Pane) loader.load();

        ClassroomAddController addController = loader.getController();


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
          ModelClassroom selectedClassroom = TvClassroom.getSelectionModel().getSelectedItem();

        if (selectedClassroom == null) {
            showAlert("Выберите запись для редактирования."); 
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLClassroomAdd.fxml"));
        Pane page = (Pane) loader.load();

        ClassroomAddController addController = loader.getController();
        addController.setModelClassroomToEdit(selectedClassroom);

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
         ModelClassroom selected = TvClassroom.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Выберите запись для удаления.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы уверены, что хотите удалить эту запись?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.setTitle("Подтверждение");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            deleteClassroom(selected.getID());
            UpdateTable();
    }
}
    private void deleteClassroom(int id) {
        String sql = "DELETE FROM classroom WHERE ID = ?";
        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Запись удалена");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert("Ошибка при удалении записи: " + e.getMessage());
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