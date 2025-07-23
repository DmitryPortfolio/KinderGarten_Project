package org.Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static org.connector.connector.ConnectDB;
import org.model.ModelMenu;

public class MenuAddController implements Initializable {

    @FXML
    private DatePicker DpDate;
    @FXML
    private ComboBox<String> CbTip;
    @FXML
    private TextField TfProduct;
    @FXML
    private TextField TfKolvoGramm;
    @FXML
    private TextField TfCallories;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnExit;

    private ModelMenu menuToEdit;
    private boolean isEditMode = false;

    private ObservableList<String> mealTypes = FXCollections.observableArrayList("Завтрак", "Обед", "Ужин");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CbTip.setItems(mealTypes);
    }

    @FXML
    private void Add(ActionEvent event) {
        LocalDate localDate = DpDate.getValue();
        String type = CbTip.getValue();
        String food = TfProduct.getText();
        String kolvoGramm = TfKolvoGramm.getText();
        String callories = TfCallories.getText();

        if (localDate == null || type == null || food == null || kolvoGramm == null || callories == null) {
            showAlert("Пожалуйста, заполните все поля.");
            return;
        }

        if (!kolvoGramm.endsWith("г.")) {
            kolvoGramm = kolvoGramm + "г.";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = localDate.format(formatter);

        if (isEditMode) {
            updateMenu(menuToEdit.getID(), dateString, type, food, kolvoGramm, callories);
        } else {
            insertMenu(dateString, type, food, kolvoGramm, callories);
        }

        Stage stage = (Stage) BtnAdd.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void Exit(ActionEvent event) {
        Stage stage = (Stage) BtnExit.getScene().getWindow();
        stage.close();
    }

    void setModelMenuToEdit(ModelMenu selectedMenu) {
        this.menuToEdit = selectedMenu;
        isEditMode = true;

        String dateString = selectedMenu.getDate();
        LocalDate localDate = null;

        if (dateString != null && !dateString.isEmpty()) {
            localDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        DpDate.setValue(localDate);
        CbTip.setValue(selectedMenu.getType());
        TfProduct.setText(selectedMenu.getFood());
        TfKolvoGramm.setText(selectedMenu.getNumber_grams());
        TfCallories.setText(selectedMenu.getCallories());
    }

    private void insertMenu(String date, String type, String food, String number_grams, String callories) {
        String sql = "INSERT INTO menu (`Date`, `Type`, `Food`, `Number_grams`, `Callories`) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, date);
            pstmt.setString(2, type);
            pstmt.setString(3, food);
            pstmt.setString(4, number_grams);
            pstmt.setString(5, callories);

            pstmt.executeUpdate();
            System.out.println("Запись в меню добавлена");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert("Ошибка при добавлении записи в меню.");
        }
    }

    private void updateMenu(int id, String date, String type, String food, String number_grams, String callories) {
        String sql = "UPDATE menu SET `Date` = ?, `Type` = ?, `Food` = ?, `Number_grams` = ?, `Callories` = ? WHERE ID = ?";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, date);
            pstmt.setString(2, type);
            pstmt.setString(3, food);
            pstmt.setString(4, number_grams);
            pstmt.setString(5, callories);
            pstmt.setInt(6, id);

            pstmt.executeUpdate();
            System.out.println("Запись в меню обновлена");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert("Ошибка при обновлении записи в меню.");
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
