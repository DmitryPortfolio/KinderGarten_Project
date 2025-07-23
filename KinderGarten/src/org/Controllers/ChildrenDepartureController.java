package org.Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import static org.connector.connector.ConnectDB;
import org.model.ModelChildren;

public class ChildrenDepartureController implements Initializable {

    @FXML
    private ComboBox<String> cbReason;
    @FXML
    private DatePicker dpDeparture;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnCancel;

    private ModelChildren child;
    private ChildrenController childrenController;

    private final ObservableList<String> reasons = FXCollections.observableArrayList("Переезд", "Смена детского сада", "Медицинские обстоятельства", "По достижению возраста");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbReason.setItems(reasons);
    }

    public void setChild(ModelChildren child) {
        this.child = child;
        if (child.getDate_departure() != null && !child.getDate_departure().isEmpty()) {
            dpDeparture.setValue(LocalDate.parse(child.getDate_departure()));
        }

    }

    public void setChildrenController(ChildrenController childrenController) {
        this.childrenController = childrenController;
    }

    @FXML
    private void Add(ActionEvent event) {
       LocalDate departureDate = dpDeparture.getValue();
        String reason = cbReason.getValue();

        if (departureDate == null || reason == null || reason.isEmpty()) {
            showAlert("Пожалуйста, заполните дату выбытия и причину выбытия.");
            return;
        }

        try {
            String sql = "UPDATE children SET Date_departure = ?, retirement = ?, Status = ? WHERE ID = ?";
            Connection conn = ConnectDB();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, departureDate.toString());
            ps.setString(2, reason);
            ps.setString(3, "Выбывший");
            ps.setInt(4, child.getID());
            ps.executeUpdate();
            showAlert("Данные успешно обновлены.");

            if (childrenController != null) {
                childrenController.UpdateTable();
            }
            Stage stage = (Stage) BtnAdd.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            showAlert("Ошибка при обновлении данных: " + e.getMessage());
        }
    }

    @FXML
    private void Cancel(ActionEvent event) {
        Stage stage = (Stage) BtnCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
