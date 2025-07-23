/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static javafx.application.Platform.exit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Пользователь
 */
public class MainController implements Initializable {
    
    private Label label;
    @FXML
    private Button Children;
    @FXML
    private MenuItem KinderGarten;
    @FXML
    private Button BtnGroup;
    @FXML
    private Button BtnTimeTable;
    @FXML
    private Button BtnAttendance;
    @FXML
    private Button BtnMenu;
    @FXML
    private MenuItem BtnTeacher;
    @FXML
    private MenuItem BtnParents;
    @FXML
    private MenuItem BtnRoom;
    
     private Stage dialogStage;
    @FXML
    private MenuItem Exit;
    @FXML
    private MenuItem BtnLessons;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void BtnChildren(ActionEvent event) throws IOException {
       Parent root = FXMLLoader.load(getClass().getResource("/org/Views/FXMLChildren.fxml"));
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        
           
 stage.setTitle("Воспитанники");
        stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/org/img/r2.png")));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void BtnKinderGarten(ActionEvent event) throws IOException {
          Parent root = FXMLLoader.load(getClass().getResource("/org/Views/FXMLKinderGarten.fxml"));
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
         stage.setTitle("Об организации");
         stage.setResizable(false);
        stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/org/img/r2.png")));
        stage.show();
    }

    @FXML
    private void Groups(ActionEvent event) throws IOException {
          Parent root = FXMLLoader.load(getClass().getResource("/org/Views/FXMLGroups.fxml"));

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Группы");
        stage.setResizable(false);
        stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/org/img/r2.png")));
        stage.show();
    }

    @FXML
    private void TimeTable(ActionEvent event) throws IOException {
      Parent root = FXMLLoader.load(getClass().getResource("/org/Views/FXMLTimetable.fxml"));
        
       
        Scene scene = new Scene(root);
        Stage stage = new Stage();
         stage.setTitle("Расписание");
         stage.setResizable(false);
        stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/org/img/r2.png")));
        stage.setScene(scene);
        stage.show();  
    }

    @FXML
    private void Attendance(ActionEvent event) throws IOException {
         FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLAttendance.fxml"));
        Pane page = (Pane) loader.load();
          AttendanceController Controller = loader.getController();
        Stage duStage = new Stage();
        Stage stage = new Stage();
 duStage.setTitle("Посещаемость");
 
        duStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/org/img/r2.png")));
        Scene scene = new Scene(page);
        stage.setResizable(false);
        duStage.setScene(scene);
        
        duStage.showAndWait();
       
    }
 

    @FXML
    private void Menu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/Views/FXMLMenu.fxml"));
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
           
 stage.setTitle("Меню");
        stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/org/img/r2.png")));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void Teacher(ActionEvent event) throws IOException {
         Parent root = FXMLLoader.load(getClass().getResource("/org/Views/FXMLTeacher.fxml"));
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
           
 stage.setTitle("Воспитатели");
        stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/org/img/r2.png")));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void Parents(ActionEvent event) throws IOException {
         Parent root = FXMLLoader.load(getClass().getResource("/org/Views/FXMLParents.fxml"));
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
     
 stage.setTitle("Родители воспитанников");
        stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/org/img/r2.png")));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void Room(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/Views/FXMLClassroom.fxml"));
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
          
 stage.setTitle("Комнаты");
        stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/org/img/r2.png")));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    @FXML
    private void OpenLessons(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/Views/FXMLLessons.fxml"));
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
          
 stage.setTitle("Список занятий");
        stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/org/img/r2.png")));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    @FXML
    private void miExit(ActionEvent event) {
        exit();
    }

    
    
}
