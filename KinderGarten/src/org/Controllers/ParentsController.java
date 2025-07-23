package org.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
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
import org.model.ModelParents;

public class ParentsController implements Initializable {

    @FXML
    private TableView<ModelParents> TvParents;
    @FXML
    private TableColumn<ModelParents, String> TcFIO;
    @FXML
    private TableColumn<ModelParents, String> TcTelefon;
    @FXML
    private TableColumn<ModelParents, String> TCAdres;
    @FXML
    private TableColumn<ModelParents, String> TcPassport;
    @FXML
    private TableColumn<ModelParents, String> TcChildrenFIO;
    ObservableList<ModelParents> list;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnEdt;
    @FXML
    private Button BtnDelete;
    @FXML
    private ComboBox<String> FiltrFIOSChild;
    @FXML
    private CheckBox CheckFioChild;
    @FXML
    private ComboBox<String> FiltrFIOS1;
    @FXML
    private CheckBox CheckFio1;

    private ObservableList<ModelParents> originalList;
    private FilteredList<ModelParents> filteredData;
    private Connection conn;
    @FXML
    private Button BtnPrint;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConnectDB();
        UpdateTable();
        loadChildrenFIOs();
        loadParentsFIOs();

        disableFiltersChild();
        disableFiltersParents();

        CheckFioChild.setOnAction(this::handleCheckFioChildAction);
        CheckFio1.setOnAction(this::handleCheckFio1Action);
    }

    private void loadChildrenFIOs() {
        ObservableList<String> childrenFIOs = FXCollections.observableArrayList();
        conn = ConnectDB();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT FIO FROM children")) {
            while (rs.next()) {
                String fio = rs.getString("FIO");
                childrenFIOs.add(fio);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Ошибка при загрузке ФИО детей: " + ex.getMessage());
        }
        FiltrFIOSChild.setItems(childrenFIOs);
    }

    private void loadParentsFIOs() {
        ObservableList<String> parentsFIOs = FXCollections.observableArrayList();
        conn = ConnectDB();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT FIO FROM parents")) {
            while (rs.next()) {
                String fio = rs.getString("FIO");
                parentsFIOs.add(fio);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Ошибка при загрузке ФИО родителей: " + ex.getMessage());
        }
        FiltrFIOS1.setItems(parentsFIOs);
    }


   public static ObservableList<ModelParents> getData() {
    ObservableList<ModelParents> ModelParents = FXCollections.observableArrayList();
    Connection conn = ConnectDB();

    try {
        PreparedStatement ps = conn.prepareStatement("SELECT *FROM parents\n"
                + "INNER JOIN children\n"
                + "ON parents.Id_Children = children.ID\n");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ModelParents.add(new ModelParents(Integer.parseInt(
                    rs.getString("parents.ID")),
                    rs.getString("parents.FIO"),
                    rs.getString("parents.Telefon"),
                    rs.getString("parents.Ardres"),
                    rs.getString("parents.Passport"),
                    rs.getString("children.FIO")));
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return ModelParents;
}

    public void UpdateTable() {

        TcFIO.setCellValueFactory(new PropertyValueFactory<>("FIO"));
        TcTelefon.setCellValueFactory(new PropertyValueFactory<>("Telefon"));
        TCAdres.setCellValueFactory(new PropertyValueFactory<>("Ardres"));
        TcPassport.setCellValueFactory(new PropertyValueFactory<>("Passport"));
        TcChildrenFIO.setCellValueFactory(new PropertyValueFactory<>("Id_Children"));

        list = getData();
        originalList = FXCollections.observableArrayList(list);
        filteredData = new FilteredList<>(originalList, p -> true);
        TvParents.setItems(filteredData);
    }

    @FXML
    private void Add(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLParentsAdd.fxml"));
        Pane page = loader.load();

        ParentsAddController addController = loader.getController();


        Stage duStage = new Stage();
duStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/add.png")));
     
        duStage.setTitle("Добавить запись");
        Scene scene = new Scene(page);
        duStage.setScene(scene);
        duStage.setResizable(false);
        duStage.showAndWait();
        UpdateTable();
    }

    @FXML
    private void Edit(ActionEvent event) throws IOException {
        ModelParents selectedParents = TvParents.getSelectionModel().getSelectedItem();

        if (selectedParents == null) {
            showAlert("Выберите родителя для редактирования.");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLParentsAdd.fxml"));
        Pane page = loader.load();

        ParentsAddController addController = loader.getController();
        addController.setModelParentsToEdit(selectedParents);

        Stage duStage = new Stage();
duStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/edit.png")));
     
        duStage.setTitle("Редактировать запись");
        Scene scene = new Scene(page);
        duStage.setScene(scene);
        duStage.setResizable(false);
        duStage.showAndWait();
        UpdateTable();
    }

    @FXML
    private void Delete(ActionEvent event) {
        ModelParents selected = TvParents.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Выберите запись для удаления.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы уверены, что хотите удалить эту запись?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.setTitle("Подтверждение");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            deleteParents(selected.getID());
            UpdateTable();
        }
    }

    private void deleteParents(int id) {
        String sql = "DELETE FROM parents WHERE ID = ?";
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

    @FXML
    private void FiltrFIOChild(ActionEvent event) {
        applyFIOChildFilter();
    }

    @FXML
    private void OnFiltrFIOChild(ActionEvent event) {
        FiltrFIOChild(event);
    }

    @FXML
    private void FiltrFIO(ActionEvent event) {
        applyFIO1Filter();
    }

    @FXML
    private void OnFiltrFIO1(ActionEvent event) {
        FiltrFIO(event);
    }

    private void applyFIOChildFilter() {
        String selectedFIO = FiltrFIOSChild.getValue();

        if (selectedFIO == null || selectedFIO.isEmpty()) {
            filteredData.setPredicate(p -> true);
        } else {
            filteredData.setPredicate(p -> p.getId_Children().equalsIgnoreCase(selectedFIO)); // Сравниваем с ФИО ребенка
        }
    }


    private void applyFIO1Filter() {
        String selectedFIO = FiltrFIOS1.getValue();

        if (selectedFIO == null || selectedFIO.isEmpty()) {
            filteredData.setPredicate(p -> true);
        } else {
            filteredData.setPredicate(p -> p.getFIO().equalsIgnoreCase(selectedFIO)); // Сравниваем с ФИО родителя
        }
    }

    void handleCheckFioChildAction(ActionEvent event) {
        if (CheckFioChild.isSelected()) {
            enableFiltersChild();
        } else {
            disableFiltersChild();
            resetFiltersChild();
        }
    }

    void handleCheckFio1Action(ActionEvent event) {
        if (CheckFio1.isSelected()) {
            enableFiltersParents();
        } else {
            disableFiltersParents();
            resetFiltersParents();
        }
    }

    private void resetFiltersChild() {
        FiltrFIOSChild.getSelectionModel().clearSelection();
        filteredData.setPredicate(p -> true);
    }

    private void disableFiltersChild() {
        FiltrFIOSChild.setDisable(true);
        FiltrFIOSChild.setOnAction(null);
    }

    private void enableFiltersChild() {
        FiltrFIOSChild.setDisable(false);
        FiltrFIOSChild.setOnAction(this::FiltrFIOChild);
    }

    private void resetFiltersParents() {
        FiltrFIOS1.getSelectionModel().clearSelection();
        filteredData.setPredicate(p -> true);
    }

    private void disableFiltersParents() {
        FiltrFIOS1.setDisable(true);
        FiltrFIOS1.setOnAction(null);
    }

    private void enableFiltersParents() {
        FiltrFIOS1.setDisable(false);
        FiltrFIOS1.setOnAction(this::FiltrFIO);
    }

    @FXML
    private void Print(ActionEvent event) throws JRException {
          Connection conn = ConnectDB();
            JasperDesign jDesign = JRXmlLoader.load(getClass().getResourceAsStream("/org/Reports/parents.jrxml"));
            JasperReport jReport = JasperCompileManager.compileReport(jDesign);
            JasperPrint jPrint = JasperFillManager.fillReport(jReport, null, conn);
            JasperViewer viewer = new JasperViewer(jPrint, false);
            viewer.setTitle("Report parents");
            viewer.show();  
    }
}