
package org.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.layout.AnchorPane;
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
import org.model.ModelChildren;

public class ChildrenController implements Initializable {

    @FXML
    private TableView<ModelChildren> TVChildren;
    private TableColumn<ModelChildren, Integer> TcNomer;
    @FXML
    private TableColumn<ModelChildren, String> TcFIO;
    @FXML
    private TableColumn<ModelChildren, String> TcBirthday;
    @FXML
    private TableColumn<ModelChildren, String> TcDateReceipt;
    @FXML
    private TableColumn<ModelChildren, String> TcDateDeparture;
    @FXML
    private TableColumn<ModelChildren, String> TcNomerGroups;
    private TableColumn<ModelChildren, String> TcParents;

    ObservableList<ModelChildren> list;
    Connection conn;
    int idChildren;
    @FXML
    private Button BtnChildren;
    private Stage dialogStage;
    @FXML
    private AnchorPane DatePostuppppS;
    @FXML
    private ComboBox<String> FiltrStatus;
    @FXML
    private ComboBox<String> FiltrGroup;
    @FXML
    private DatePicker DateRozS;
    @FXML
    private DatePicker DatePostS;
    @FXML
    private DatePicker DateZayvkaS;
    @FXML
    private DatePicker DateRozPo;
    @FXML
    private DatePicker DatePostPo;
    @FXML
    private DatePicker DateZayvkaPo;
    private ComboBox<String> FiltrFIO;
    @FXML
    private Button BtnPrint;
    @FXML
    private Button BtnExit;
    @FXML
    private Button BtnDelete;
    private FilteredList<ModelChildren> filteredData;
    @FXML
    private TableColumn<ModelChildren, String> TcSveden;
    @FXML
    private TableColumn<ModelChildren, String> TcStatus;
    private boolean isFIOFilterEnabled = false;

    public ObservableList<String> comboboxList = FXCollections.observableArrayList();
    @FXML
    private CheckBox CheckFio;
    @FXML
    private ComboBox<String> FiltrFIOS;
    @FXML
    private Button btnZach;
    @FXML
    private Button btnDer;
    @FXML
    private Button BtnCrediting;
    @FXML
    private TableColumn<ModelChildren, String> TcRetirement;
    @FXML
    private Button BtnEdit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConnectDB();
        UpdateTable();
        FiltrGroup.setItems(FXCollections.observableArrayList(getGroup()));
        FiltrFIOS.setItems(FXCollections.observableArrayList(getFIO()));

        filteredData = new FilteredList<>(list, p -> true);
        TVChildren.setItems(filteredData);

        comboboxList.addAll("Учащийся", "Кандидат", "Выбывший");
        FiltrStatus.setItems(comboboxList);

        disableFilters();

        CheckFio.setOnAction(this::handleCheckFioAction);

        DateRozS.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        DateRozPo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        DatePostS.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        DatePostPo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        DateZayvkaS.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        DateZayvkaPo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        FiltrStatus.setOnAction(e -> applyFilters());
        FiltrGroup.setOnAction(e -> applyFilters());
        FiltrFIOS.setOnAction(e -> applyFilters());
    }

    public static ObservableList<ModelChildren> getData() {
        ObservableList<ModelChildren> ModelChildren = FXCollections.observableArrayList();
        Connection conn = ConnectDB();

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT children.ID, children.FIO, children.Birthday, children.Date_receipt, "
                    + "children.Date_departure, groups.Name_groups, "
                    + "children.BirthdayCertificate, children.retirement, children.Status "
                    + "FROM children "
                    + "LEFT JOIN groups ON children.Id_Groups = groups.ID"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModelChildren.add(new ModelChildren(
                        rs.getInt("ID"),
                        rs.getString("FIO"),
                        rs.getString("Birthday"),
                        rs.getString("Date_receipt") != null ? rs.getString("Date_receipt") : "",
                        rs.getString("Date_departure") != null ? rs.getString("Date_departure") : "",
                        rs.getString("Name_groups") != null ? rs.getString("Name_groups") : "",
                        rs.getString("BirthdayCertificate") != null ? rs.getString("BirthdayCertificate") : "",
                        rs.getString("retirement") != null ? rs.getString("retirement") : "",
                        rs.getString("Status")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Ошибка при получении данных: " + ex.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ModelChildren;
    }

    public void UpdateTable() {
        TcFIO.setCellValueFactory(new PropertyValueFactory<>("FIO"));
        TcBirthday.setCellValueFactory(new PropertyValueFactory<>("Birthday"));
        TcDateReceipt.setCellValueFactory(new PropertyValueFactory<>("Date_receipt"));
        TcDateDeparture.setCellValueFactory(new PropertyValueFactory<>("Date_departure"));
        TcNomerGroups.setCellValueFactory(new PropertyValueFactory<>("Id_Groups"));
        TcSveden.setCellValueFactory(new PropertyValueFactory<>("BirthdayCertificate"));
        TcRetirement.setCellValueFactory(new PropertyValueFactory<>("retirement"));
        TcStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        list = getData();
        TVChildren.setItems(list);

        filteredData = new FilteredList<>(list, p -> true);
        TVChildren.setItems(filteredData);
    }

    private void handleCheckFioAction(ActionEvent event) {
        if (CheckFio.isSelected()) {
            enableFilters();
        } else {
            disableFilters();
            resetFilters();
        }
    }

    private void resetFilters() {
        FiltrStatus.getSelectionModel().clearSelection();
        FiltrGroup.getSelectionModel().clearSelection();
        FiltrFIOS.getSelectionModel().clearSelection();

        DateRozS.setValue(null);
        DatePostS.setValue(null);
        DateZayvkaS.setValue(null);
        DateRozPo.setValue(null);
        DatePostPo.setValue(null);
        DateZayvkaPo.setValue(null);

        filteredData.setPredicate(p -> true);
        TVChildren.setItems(list);
    }

    private void enableFilters() {
        FiltrStatus.setDisable(false);
        FiltrGroup.setDisable(false);
        DateRozS.setDisable(false);
        DatePostS.setDisable(false);
        DateZayvkaS.setDisable(false);
        DateRozPo.setDisable(false);
        DatePostPo.setDisable(false);
        DateZayvkaPo.setDisable(false);
        FiltrFIOS.setDisable(false);
    }

    private void disableFilters() {
        FiltrStatus.setDisable(true);
        FiltrGroup.setDisable(true);
        DateRozS.setDisable(true);
        DatePostS.setDisable(true);
        DateZayvkaS.setDisable(true);
        DateRozPo.setDisable(true);
        DatePostPo.setDisable(true);
        DateZayvkaPo.setDisable(true);
        FiltrFIOS.setDisable(true);
    }

    private void applyFilters() {
        LocalDate birthdayFrom = DateRozS.getValue();
        LocalDate birthdayTo = DateRozPo.getValue();

        LocalDate receiptFrom = DatePostS.getValue();
        LocalDate receiptTo = DatePostPo.getValue();

        LocalDate departureFrom = DateZayvkaS.getValue();
        LocalDate departureTo = DateZayvkaPo.getValue();

        String selectedStatus = FiltrStatus.getValue();
        String selectedGroup = FiltrGroup.getValue();
        String selectedFIO = FiltrFIOS.getValue();

        filteredData.setPredicate(child -> {
            try {
                if (birthdayFrom != null) {
                    LocalDate bd = LocalDate.parse(child.getBirthday());
                    if (bd.isBefore(birthdayFrom)) return false;
                }
                if (birthdayTo != null) {
                    LocalDate bd = LocalDate.parse(child.getBirthday());
                    if (bd.isAfter(birthdayTo)) return false;
                }

                if (receiptFrom != null) {
                    String dr = child.getDate_receipt();
                    if (dr == null || dr.isEmpty()) return false;
                    LocalDate dateReceipt = LocalDate.parse(dr);
                    if (dateReceipt.isBefore(receiptFrom)) return false;
                }
                if (receiptTo != null) {
                    String dr = child.getDate_receipt();
                    if (dr == null || dr.isEmpty()) return false;
                    LocalDate dateReceipt = LocalDate.parse(dr);
                    if (dateReceipt.isAfter(receiptTo)) return false;
                }

                if (departureFrom != null) {
                    String dd = child.getDate_departure();
                    if (dd == null || dd.isEmpty()) return false;
                    LocalDate dateDeparture = LocalDate.parse(dd);
                    if (dateDeparture.isBefore(departureFrom)) return false;
                }
                if (departureTo != null) {
                    String dd = child.getDate_departure();
                    if (dd == null || dd.isEmpty()) return false;
                    LocalDate dateDeparture = LocalDate.parse(dd);
                    if (dateDeparture.isAfter(departureTo)) return false;
                }

                if (selectedStatus != null && !selectedStatus.isEmpty()) {
                    if (!child.getStatus().equalsIgnoreCase(selectedStatus)) return false;
                }

                if (selectedGroup != null && !selectedGroup.isEmpty()) {
                    if (!child.getId_Groups().equalsIgnoreCase(selectedGroup)) return false;
                }

                if (selectedFIO != null && !selectedFIO.isEmpty()) {
                    if (!child.getFIO().equalsIgnoreCase(selectedFIO)) return false;
                }

                return true;
            } catch (Exception e) {
                return false;
            }
        });

        TVChildren.setItems(filteredData);
    }

    public List<String> getGroup() {
        conn = ConnectDB();
        List<String> groups = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT Name_groups FROM groups");
            while (rs.next()) {
                groups.add(rs.getString("Name_groups"));
            }
        } catch (SQLException ex) {
            System.err.println("Ошибка при получении групп: " + ex.getMessage());
        }
        return groups;
    }

    public List<String> getFIO() {
        conn = ConnectDB();
        List<String> fios = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT DISTINCT FIO FROM children");
            while (rs.next()) {
                fios.add(rs.getString("FIO"));
            }
        } catch (SQLException ex) {
            System.err.println("Ошибка при получении ФИО: " + ex.getMessage());
        }
        return fios;
    }

    @FXML
    private void OpenChildren(ActionEvent event) throws IOException {
        ModelChildren tvchildren = TVChildren.getSelectionModel().getSelectedItem();
        if (tvchildren == null) {
            showAlert("Выберите запись.");
            return;
        }

        idChildren = TVChildren.getSelectionModel().getSelectedIndex();
        list.set(idChildren, tvchildren);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLChildrenEdit.fxml"));
        Pane page = (Pane) loader.load();

        ChilrenEditController EditController = loader.getController();

        Stage duStage = new Stage();
        duStage.setTitle("Карточка ребенка");
        duStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/r2.png")));

        EditController.setDialogStage(duStage);
        EditController.setChilren(tvchildren);

        Scene scene = new Scene(page);
        duStage.setScene(scene);
        duStage.setResizable(false);
        duStage.showAndWait();
        UpdateTable();
    }

    @FXML
    private void Print(ActionEvent event) throws JRException {
        Connection conn = ConnectDB();
        JasperDesign jDesign = JRXmlLoader.load(getClass().getResourceAsStream("/org/Reports/Children.jrxml"));
        JasperReport jReport = JasperCompileManager.compileReport(jDesign);
        JasperPrint jPrint = JasperFillManager.fillReport(jReport, null, conn);
        JasperViewer viewer = new JasperViewer(jPrint, false);
        viewer.setTitle("Report children");
        viewer.show();
    }

    @FXML
    private void Exit(ActionEvent event) {
        Stage stage = (Stage) BtnExit.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void Delete(ActionEvent event) {
        ModelChildren selected = TVChildren.getSelectionModel().getSelectedItem();

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
                int childId = selected.getID();

                try (Connection conn = ConnectDB()) {
                    conn.setAutoCommit(false);

                    try {
                        String sqlDeleteAttendance = "DELETE FROM attendance WHERE Id_Children = ?";
                        try (PreparedStatement pstmtAttendance = conn.prepareStatement(sqlDeleteAttendance)) {
                            pstmtAttendance.setInt(1, childId);
                            pstmtAttendance.executeUpdate();
                        }

                        String sqlDeleteParents = "DELETE FROM parents WHERE Id_Children = ?";
                        try (PreparedStatement pstmtParents = conn.prepareStatement(sqlDeleteParents)) {
                            pstmtParents.setInt(1, childId);
                            pstmtParents.executeUpdate();
                        }

                        String sqlDeleteChildren = "DELETE FROM children WHERE ID = ?";
                        try (PreparedStatement pstmtChildren = conn.prepareStatement(sqlDeleteChildren)) {
                            pstmtChildren.setInt(1, childId);
                            pstmtChildren.executeUpdate();
                        }

                        conn.commit();

                        System.out.println("Записи (ребенок, связанные родители и посещаемость) успешно удалены");
                        UpdateTable();

                    } catch (SQLException e) {
                        conn.rollback();
                        System.out.println("Ошибка при удалении записей: " + e.getMessage());
                        e.printStackTrace();
                        showAlert("Ошибка при удалении записей: " + e.getMessage());
                    } finally {
                        conn.setAutoCommit(true);
                    }
                } catch (SQLException e) {
                    System.out.println("Ошибка при подключении к базе данных или настройке транзакции: " + e.getMessage());
                    e.printStackTrace();
                    showAlert("Ошибка при подключении к базе данных или настройке транзакции: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void FiltrGroups(ActionEvent event) {
        applyFilters();
    }

    @FXML
    private void FiltrStatuss(ActionEvent event) {
        applyFilters();
    }

    @FXML
    private void FiltrFIO() {
        applyFilters();
    }

    @FXML
    private void Zachislenie(ActionEvent event) throws IOException {
        ModelChildren selectedChild = TVChildren.getSelectionModel().getSelectedItem();

        if (selectedChild == null) {
            showAlert("Выберите ребенка.");
            return;
        }

        if (!selectedChild.getStatus().equals("Кандидат")) {
            showAlert("Редактирование возможно только для детей со статусом 'Кандидат'.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/Views/FXMLChildrenZachislenie.fxml"));
        Parent root = loader.load();

        ChildrenZachislenieController controller = loader.getController();
        controller.setChild(selectedChild);
        controller.setChildrenController(this);

        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/r2.png")));
        stage.setScene(new Scene(root));
        stage.setTitle("Приказ о зачислении");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void Departure(ActionEvent event) throws IOException {
        ModelChildren selectedChild = TVChildren.getSelectionModel().getSelectedItem();

        if (selectedChild == null) {
            showAlert("Выберите ребенка.");
            return;
        }

        if (!selectedChild.getStatus().equals("Учащийся")) {
            showAlert("Редактирование возможно только для детей со статусом 'Учащийся'.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/Views/FXMLChildrenDeparture.fxml"));
        Parent root = loader.load();

        ChildrenDepartureController controller = loader.getController();
        controller.setChild(selectedChild);
        controller.setChildrenController(this);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/r2.png")));
        stage.setTitle("Приказ о выбытии");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void Crediting(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/Views/FXMLChildrenCrediting.fxml"));
        Parent root = loader.load();

        ChildrenCreditingController controller = loader.getController();
        controller.setChildrenController(this);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Приказ о кандидатуре");
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/r2.png")));
        stage.showAndWait();
        UpdateTable();
    }

    @FXML
    private void EditChildren(ActionEvent event) throws IOException {
        ModelChildren selectedChild = TVChildren.getSelectionModel().getSelectedItem();

        if (selectedChild == null) {
            showAlert("Выберите ребенка.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/Views/FXMLChildrenAllEdit.fxml"));
        Parent root = loader.load();

        ChildrenAllEditController controller = loader.getController();
        controller.setChild(selectedChild);
        controller.setChildrenController(this);

        controller.loadGroups();
        controller.loadStatuses();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/edit.png")));
        stage.setTitle("Редактировать информацию о ребенке");
        stage.setResizable(false);
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void FiltrDateRozddenie(ActionEvent event) {
    }

    @FXML
    private void FiltrDatePostup(ActionEvent event) {
    }

    @FXML
    private void FiltrDatezayvka(ActionEvent event) {
    }

    @FXML
    private void OnFiltrFIO(ActionEvent event) {
    }

}
