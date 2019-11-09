package controller;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import connection.ConnectionManager;
import entity.EntityWorkDay;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import manger.TableMangaer;
import sun.awt.WindowClosingListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.crypto.Data;

public class IndexController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Tab workHour;

    @FXML
    private Button workHourAdd;

    @FXML
    private Button workHourEdit;

    @FXML
    private Button workHourDelete;
    @FXML
    private Button od;

    @FXML
    private Label workHourLabelTableViewMonth;

    @FXML
    private Label workHourLabelTable;

    @FXML
    private ListView<Month> hourMonth;

    @FXML
    private TableView<EntityWorkDay> table;

//    @FXML
//    private TableColumn<EntityWorkDay, String> hourTableDayId;


    @FXML
    private TableColumn<EntityWorkDay, Timestamp> hourTableFrom;

    @FXML
    private TableColumn<EntityWorkDay, Timestamp> hourTableTo;

    @FXML
    private TableColumn<EntityWorkDay, String> hourTableWorkHour;

    @FXML
    private TableColumn<EntityWorkDay, String> hourTableNadgodziny;
    @FXML
    private TextField hourSum;

    @FXML
    private TextField NadHourSum;

    @FXML
    private Tab workList;

    @FXML
    private AnchorPane apane;

    public void setTable(TableView<EntityWorkDay> table) {
        this.table = table;
    }

    public TableView<EntityWorkDay> getTable() {
        return table;
    }

    //   Baza dni tygodnia
    ObservableList<EntityWorkDay> dataBase = FXCollections.observableArrayList();
    protected static EntityWorkDay EditEntityWorkDay;

    @FXML
    void initialize() throws SQLException {


//        Uzupełnienie tabeli godziny pracy
        table.setItems(loadDataFromDB("SELECT * FROM work.workhour;"));

//        Uzupełnienie pól sumujących godziny pracy
        AssignmentOfAcolumnInAtableAoAColumnInadatabase();


//        Uzypełnienie listy miesięcy
        AssigningAvalueToTheListOfMonths();

//       Obsługa przycisków
        ButtonOperation();

//        Pobranie danych encji
        getDataEntity();

    }

    protected void updateDataBeforeAddedRow() throws SQLException {
        System.out.println("kupsko");
        //        Pobranie danych encji
        getDataEntity();
//        Uzupełnienie tabeli godziny pracy
        table.setItems(loadDataFromDB("SELECT * FROM work.workhour;"));

//        Uzupełnienie pól sumujących godziny pracy
        AssignmentOfAcolumnInAtableAoAColumnInadatabase();

    }

    public static EntityWorkDay getEditEntityWorkDay() {
        return EditEntityWorkDay;
    }

    private EntityWorkDay getDataEntity() {
        EntityWorkDay EntityWorkDay = table.getSelectionModel().getSelectedItem();
        return EntityWorkDay;
    }

    // Główna metoda pobierająca informacje z bazy danych i wypełniająca tabele
    public ObservableList<EntityWorkDay> loadDataFromDB(String sqlAction) throws SQLException {
        dataBase.clear();
//        Tworzę obiekt do wypełnienia tabeli i przypisuje to do kolekcji
        try {
            Connection connection = ConnectionManager.getConnection();
//            Przekompilowana i przchowywana instrukcja SQL - może być urzyta wiele razy
//            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `hourTableDayId`, CAST(`hourTableFrom` as time) AS " + "hourTableFrom " + " ,CAST(`hourTableTo` as time) AS "+ "hourTableTo " +" ,CAST(`hourTableWorkHour` as time)    AS " + "hourTableWorkHour " + " ,CAST( `hourTableNadgodziny` as time) AS " + "hourTableNadgodziny " + " ,CAST( `hourTableDay` as date) AS " +"hourTableDay " + "FROM work.workhour");
            PreparedStatement preparedStatement = connection.prepareStatement(sqlAction);
//            Reprezentuje zestaw wyników bazy danych
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                dataBase.addAll(new EntityWorkDay(rs.getInt("hourTableDayId"), rs.getTimestamp("hourTableFrom"), rs.getTimestamp("hourTableTo"), rs.getString("hourTableWorkhour"), rs.getString("hourTableNadgodziny")));
                System.out.println(dataBase);
            }
            return dataBase;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return dataBase;
    }

    //Wypełnienie ListView wartościami
    private ObservableList<Month> loadDataMonthFromDB() {
        ObservableList<Month> dataBase2 = FXCollections.observableArrayList();
        dataBase2.addAll(Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL,
                Month.MAY, Month.JUNE, Month.JULY,
                Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER,
                Month.NOVEMBER, Month.DECEMBER);

        return dataBase2;

    }

    private void openAddWindow(String title, String path) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            Parent root2 = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root2));
            //            Blokada kliknięć macierzystego okna
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.show();

//            Aktualizacja danych po dodaniu
            stage.setOnHidden(event -> {
                try {
                    initialize();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
//stage.setOnCloseRequest(event -> {
//    System.out.println("pupa");

            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void informationAboutFailureToAddToTheDatabase(Exception e, String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Wystąpił błąd");
        alert.setHeaderText("Nie udało się wykonać operacji");
        alert.setContentText(s);

// Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Bład:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    private void AssignmentOfAcolumnInAtableAoAColumnInadatabase() throws SQLException {
        //        Zsumowanie godzin
        sumCollumn("hourTableNadgodziny", "month(curdate())");
        sumCollumn("hourTableWorkHour", "month(curdate())");


        //        Przypisanie kolum w dabeli kolumną w bazie danych
//        hourTableDayId.setCellValueFactory(new PropertyValueFactory<EntityWorkDay, String>("hourTableDayId"));

        hourTableFrom.setCellValueFactory(new PropertyValueFactory<EntityWorkDay, Timestamp>("hourTableFrom"));
        hourTableTo.setCellValueFactory(new PropertyValueFactory<EntityWorkDay, Timestamp>("hourTableTo"));
        hourTableWorkHour.setCellValueFactory(new PropertyValueFactory<EntityWorkDay, String>("hourTableWorkHour"));
        hourTableNadgodziny.setCellValueFactory(new PropertyValueFactory<EntityWorkDay, String>("hourTableNadgodziny"));

        //     Wypełnienie tabeli bazą danych
        table.setItems(loadDataFromDB("select * FROM work.workhour where  month(`hourTableFrom`) = month(curdate());"));
    }

    private void AssigningAvalueToTheListOfMonths() {
//       Wypełnienie Listview wartościami miesięcy
        hourMonth.getItems().addAll(loadDataMonthFromDB());
//Przypisanie listenera do przycisków
        hourMonth.getSelectionModel().selectedItemProperty().addListener(event -> {

            String sqlParameter = "select * FROM work.workhour where  month(`hourTableFrom`) = " + hourMonth.getSelectionModel().getSelectedItem().getValue() + ";";
            int sqlParameterMonth = hourMonth.getSelectionModel().getSelectedItem().getValue();
            try {
                loadDataFromDB(sqlParameter);
                sumCollumn("hourTableNadgodziny", sqlParameterMonth);
                sumCollumn("hourTableWorkHour", sqlParameterMonth);
            } catch (SQLException e1) {
                informationAboutFailureToAddToTheDatabase(e1, "Nie udało się wykonać instrukcji SQL");
            }
        });

    }

    private void sumCollumn(String collumn, Object month) throws SQLException {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
        } catch (SQLException e) {
            informationAboutFailureToAddToTheDatabase(e, "Nie udało się uzupełnić pola sumowania");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("select cast(sum(`" + collumn + "`) as time) FROM work.workhour  where  month(`hourTableFrom`) = " + month + ";");
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        String sum = rs.getString(1);
        if (collumn == "hourTableNadgodziny") {
            NadHourSum.setText(sum);
        } else {
            hourSum.setText(sum);
        }

    }

    private void ButtonOperation() {
        addButton();
        editButton();
        deleteButton();
    }

    private void deleteButton() {

        //        Otwarcie okna dodawania
        workHourDelete.setOnAction((event) -> {
            int index = 0;
//            Otwarcia okna allertu
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Usuwanie rekordu");
            alert.setHeaderText("Data");
            alert.setContentText("Czy na pewno chcesz usunąć rekord ?");

//            Obsługa zdarzenia usunięcia rekordu
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

//           Pobranie indexu zaznaczonego wiersza
                try {
                    index = table.getSelectionModel().getSelectedItem().getHourTableDayId();
                } catch (NullPointerException e) {
//                    Wywołanie informacji o błędzie nie zaznaczenia wiersza
                    shortAlertinformation(Alert.AlertType.ERROR, "Błąd!",
                            "Brak zaznaczonego wiersza.",
                            "Przed wykoneniem polecenia muszisz " +
                                    "zaznaczyć wiersz który ma być usunięty.");
                }

//           Sprawdzenie czy został zaznaczony wiersz
                try {

//           Nawiązanie połączenia i wykoannie instrukcji SQL
                    Connection connection = ConnectionManager.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("delete FROM work.workhour where hourTableDayId = " + index + ";");
                    preparedStatement.execute();
                    shortAlertinformation(Alert.AlertType.INFORMATION, "Usuwanie", "Usunięcie wiersza powiodło się."
                            , table.getSelectionModel().getSelectedItem().toString());

                } catch (SQLException e) {
                    informationAboutFailureToAddToTheDatabase(e, "Nie udało się nawiązać połączenia");
                }
            } else {// ... user chose CANCEL or closed the dialog
            }
        });
    }

    private void editButton() {
        //        Otwarcie okna dodawania


        workHourEdit.setOnAction((event) -> {
            openAddWindow("Edytuj dzień", "/editDay.fxml");
//            System.out.println(table.getSelectionModel().getSelectedIndex());
//            EntityWorkDay EntityWorkDay = table.getSelectionModel().getSelectedItem();
//            editDayController editDayController = new editDayController();
//           editDayController.setEntityWorkDay(EntityWorkDay);

//            "UPDATE `work`.`workhour` SET `hourTableFrom` = '" + dsfsd +"', `hourTableTo` = '" + ds + "', `hourTableWorkHour` = '" + ds + "', `hourTableNadgodziny` = '" + ds + ""' WHERE (`hourTableDayId` = '8');
        });
    }

    private void addButton() {
        //        Otwarcie okna dodawania
        workHourAdd.setOnAction((event) -> {
            openAddWindow("Dodaj dzień", "/addDay.fxml");
        });
    }

    private void shortAlertinformation(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public EntityWorkDay selectedEntity() {
        EntityWorkDay entityWorkDay = table.getSelectionModel().getSelectedItem();

        return entityWorkDay;
    }

    public void replace(Stage primaryStage) {
        primaryStage.setOnCloseRequest(e -> {
            System.out.println("pulok");
        });
    }
}





