
package org.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import static org.connector.connector.ConnectDB;
import org.model.ModelMenu;

/**
 * FXML Controller class
 *
 * @author Пользователь
 */
public class MenuController implements Initializable {

    @FXML
    private TableView<ModelMenu> TvMenu;
    @FXML
    private TableColumn<ModelMenu, String> TcDate;
    @FXML
    private TableColumn<ModelMenu, String> TcType;
    @FXML
    private TableColumn<ModelMenu, String> TCFood;
    ObservableList<ModelMenu> list;
    Connection conn;
    @FXML
    private TableColumn<ModelMenu, String> TcNumGramm;
    @FXML
    private TableColumn<ModelMenu, String> TcCallories;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnEdit;
    @FXML
    private Button BtnDelete;
    @FXML
    private DatePicker DpDateS;
    @FXML
    private DatePicker DpDatePo;
    @FXML
    private ComboBox<String> CbFiltrTip;
    @FXML
    private CheckBox CbFiltrOfon;

    private FilteredList<ModelMenu> filteredData;
    private ObservableList<ModelMenu> originalList;
    @FXML
    private Button BtnPrint;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConnectDB();
        UpdateTable();
        CbFiltrTip.getItems().addAll("Завтрак", "Обед", "Ужин");
        disableFilters();
    }

     public static ObservableList<ModelMenu> getData() {
        ObservableList<ModelMenu> ModelMenu = FXCollections.observableArrayList();
        Connection conn = ConnectDB();

        try {

            PreparedStatement ps = conn.prepareStatement("select *from menu");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModelMenu.add(new ModelMenu(Integer.parseInt(
                        rs.getString("ID")),
                        rs.getString("Date"),
                        rs.getString("Type"),
                         rs.getString("Food"),
                         rs.getString("Number_grams"),
                        rs.getString("Callories")));

            }
        } catch (SQLException ex) {
             ex.printStackTrace();
        }
        return ModelMenu;
    }

    public void UpdateTable() {
        TcDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        TcType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        TCFood.setCellValueFactory(new PropertyValueFactory<>("Food"));
        TcNumGramm.setCellValueFactory(new PropertyValueFactory<>("Number_grams"));
        TcCallories.setCellValueFactory(new PropertyValueFactory<>("Callories"));
        list = getData();

        originalList = FXCollections.observableArrayList(list);
        filteredData = new FilteredList<>(originalList, p -> true);
        TvMenu.setItems(filteredData);
    }

    @FXML
    private void Add(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/Views/FXMLMenuAdd.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/add.png")));
        stage.setTitle("Добавить запись в меню");
        stage.setResizable(false);
        stage.showAndWait();
        UpdateTable();
    }

    @FXML
    private void Edit(ActionEvent event) throws IOException {
        ModelMenu selectedMenu = TvMenu.getSelectionModel().getSelectedItem();

        if (selectedMenu == null) {
            showAlert("Выберите запись для редактирования.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/Views/FXMLMenuAdd.fxml"));
        Parent root = loader.load();

        MenuAddController controller = loader.getController();
        controller.setModelMenuToEdit(selectedMenu);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
           stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/edit.png")));
     
        stage.setTitle("Редактировать запись");
        stage.setResizable(false);
        stage.showAndWait();
        UpdateTable();
    }

    @FXML
    private void Delete(ActionEvent event) {
        ModelMenu selectedMenu = TvMenu.getSelectionModel().getSelectedItem();

        if (selectedMenu == null) {
            showAlert("Выберите строку для удаления.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы действительно хотите удалить запись?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("");
        alert.setTitle("Подтверждение");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            deleteMenu(selectedMenu.getID());
            UpdateTable();
        }
    }

    private void deleteMenu(int id) {
        String sql = "DELETE FROM menu WHERE ID = ?";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Запись удалена");
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении записи: " + e.getMessage());
            showAlert("Ошибка при удалении записи.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void DateS(ActionEvent event) {
        applyFilters();
    }

    @FXML
    private void DatePo(ActionEvent event) {
        applyFilters();
    }

    @FXML
    private void FiltrTip(ActionEvent event) {
        applyFilters();
    }

    private void applyFilters() {
        filteredData.setPredicate(menu -> {
            if (!CbFiltrOfon.isSelected()) {
                return true;
            }

            LocalDate dateS = DpDateS.getValue();
            LocalDate datePo = DpDatePo.getValue();
            String selectedTip = CbFiltrTip.getValue();

            //Date Filter
           if (dateS != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate menuDate = LocalDate.parse(menu.getDate(), formatter);
                if (menuDate.isBefore(dateS)) {
                    return false;
                }
            }
            if (datePo != null) {
                 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate menuDate = LocalDate.parse(menu.getDate(), formatter);
                if (menuDate.isAfter(datePo)) {
                    return false;
                }
            }

            //Type Filter
            if (selectedTip != null && !selectedTip.isEmpty() && !menu.getType().equals(selectedTip)) {
                return false;
            }

            return true;
        });
    }

    @FXML
    private void FiltrOfon(ActionEvent event) {
        if (CbFiltrOfon.isSelected()) {
            enableFilters();
        } else {
            disableFilters();
            resetFilters();
        }
    }

    private void enableFilters() {
        DpDateS.setDisable(false);
        DpDatePo.setDisable(false);
        CbFiltrTip.setDisable(false);
    }

    private void disableFilters() {
        DpDateS.setDisable(true);
        DpDatePo.setDisable(true);
        CbFiltrTip.setDisable(true);

        DpDateS.setValue(null);
        DpDatePo.setValue(null);
        CbFiltrTip.setValue(null);
    }

    private void resetFilters() {
        DpDateS.setValue(null);
        DpDatePo.setValue(null);
        CbFiltrTip.setValue(null);
        filteredData.setPredicate(p -> true);
    }

    @FXML
    private void Print(ActionEvent event) throws JRException {
         Connection conn = ConnectDB();
            JasperDesign jDesign = JRXmlLoader.load(getClass().getResourceAsStream("/org/Reports/menu.jrxml"));
            JasperReport jReport = JasperCompileManager.compileReport(jDesign);
            JasperPrint jPrint = JasperFillManager.fillReport(jReport, null, conn);
            JasperViewer viewer = new JasperViewer(jPrint, false);
            viewer.setTitle("Report groups");
            viewer.show();  
    }
}
