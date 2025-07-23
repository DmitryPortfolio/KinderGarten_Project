
package org.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
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
import org.model.ModelChildren;
import org.model.ModelKinderGarten;

/**
 * FXML Controller class
 *
 * @author Пользователь
 */
public class KinderGartenController implements Initializable {

    @FXML
    private TableView<ModelKinderGarten> TVKinderGarten;
    private TableColumn<ModelKinderGarten, Integer> TcNomer;
    @FXML
    private TableColumn<ModelKinderGarten, String> TcName;
    @FXML
    private TableColumn<ModelKinderGarten, String> TcAdres;
    @FXML
    private TableColumn<ModelKinderGarten, String> TcTelefon;
    @FXML
    private TableColumn<ModelKinderGarten, String> TcDirector;
    ObservableList<ModelKinderGarten> list;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         ConnectDB();
        UpdateTable();
    }
    
    public static ObservableList<ModelKinderGarten> getData() {
        ObservableList<ModelKinderGarten> ModelKinderGarten = FXCollections.observableArrayList();
        Connection conn = ConnectDB();

        try {

            PreparedStatement ps = conn.prepareStatement("select *from kindergarten");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModelKinderGarten.add(new ModelKinderGarten(Integer.parseInt(
                        rs.getString("ID")),
                        rs.getString("Name"),
                        rs.getString("Adres"),
                        rs.getString("Telefon"),                       
                        rs.getString("Director")));

            }
        } catch (SQLException ex) {

        }
        return ModelKinderGarten;
    }

    public void UpdateTable() {
      
        TcName.setCellValueFactory(new PropertyValueFactory<ModelKinderGarten, String>("Name"));
        TcAdres.setCellValueFactory(new PropertyValueFactory<ModelKinderGarten, String>("Adres"));
        TcTelefon.setCellValueFactory(new PropertyValueFactory<ModelKinderGarten, String>("Telefon"));
        TcDirector.setCellValueFactory(new PropertyValueFactory<ModelKinderGarten, String>("Director"));
        list = getData();
        TVKinderGarten.setItems(list);

    }      

    @FXML
    private void Add(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLKinderGartenAdd.fxml"));
        Pane page = (Pane) loader.load();
        KinderGartenAddController addController = loader.getController();
        

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
        ModelKinderGarten selectedAdress = TVKinderGarten.getSelectionModel().getSelectedItem();

        if (selectedAdress == null) {
          showAlert("Выберите Детский сад для редактирования.");
            return;
        }
        
          FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLKinderGartenAdd.fxml"));
        Pane page = (Pane) loader.load();
        KinderGartenAddController addController = loader.getController();
         addController.setModelKinderGartenToEdit(selectedAdress);

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
        ModelKinderGarten selectedAdress = TVKinderGarten.getSelectionModel().getSelectedItem();

        if (selectedAdress == null) {
            showAlert("Выберите Детский сад для удаления.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Вы уверены, что хотите удалить этот адрес?");
        alert.setContentText("Нажмите OK для подтверждения, Cancel для отмены.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteKinderGarten(selectedAdress.getID());
            UpdateTable();
        }
    }

    private void deleteKinderGarten(int id) {
        String sql = "DELETE FROM kindergarten WHERE ID = ?";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Детский сад удален");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
