package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.ResourceBundle;

import connection.ConnectionManager;
import entity.EntityWorkDay;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import lombok.Getter;
import lombok.Setter;
import org.hibernate.loader.Loader;


@Getter
@Setter
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

    EntityWorkDay entityWorkDay = null;

    //    Przypisanie zmiennej wartości domyślnej - aktualny miesiąc - FLAGA
    private Month month = LocalDate.now().getMonth();

    //    Zmienna flagujaca zaznaczoną pozycję za pomocą id bazy danych
    private int index = 0;

//    public void setTable(TableView<EntityWorkDay> table) {
//        this.table = table;
//    }
//
//    public TableView<EntityWorkDay> getTable() {
//        return table;
//    }

    //   Baza dni tygodnia
    ObservableList<EntityWorkDay> dataBase = FXCollections.observableArrayList();
    protected static EntityWorkDay EditEntityWorkDay;

    @FXML
    void initialize() throws SQLException {

//        Metoda pobierająca dane z bazy i uzupełniająca tabele
        assignmentOfAcolumnInAtableAoAColumnInadatabase("Nie udało się pobrać danych z bazy i uzupełnić tabeli.");

//        Uzypełnienie listy miesięcy
        assigningAvalueToTheListOfMonths();

//       Obsługa przycisków
        ButtonOperation();

//        Sprawdzanie czy wiersz jest zaznaczony
        selectionRowListener();
    }

    private void selectionRowListener() {
//        Wartość domyślna
        editButton(2);

//        Listener zaznaczonego wiersza
        table.getSelectionModel().selectedItemProperty().addListener((observable) -> {
//            EntityWorkDay entityWorkDay = new EntityWorkDay();
            editButton(1);
            entityWorkDay = this.entityWorkDay;
            entityWorkDay = table.getSelectionModel().getSelectedItem();
            if (entityWorkDay != null) {
                editButton(1);
            } else {
                editButton(2);
            }
        });
    }


    // Główna metoda pobierająca informacje z bazy danych i wypełniająca tabele
    public ObservableList<EntityWorkDay> loadDataFromDB(String sqlAction) throws SQLException {
        dataBase.clear();
//        Tworzę obiekt do wypełnienia tabeli i przypisuje to do kolekcji
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlAction);

            //            Reprezentuje zestaw wyników bazy danych
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                dataBase.addAll(new EntityWorkDay(rs.getInt("hourTableDayId"), rs.getTimestamp("hourTableFrom"), rs.getTimestamp("hourTableTo"), rs.getString("hourTableWorkhour"), rs.getString("hourTableNadgodziny")));

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

    //            Aktualizacja danych po dodaniu
    private void updateTableBeforeAction(Stage stage, String titleException) {
        stage.setOnHidden(event -> {
            assignmentOfAcolumnInAtableAoAColumnInadatabase(titleException);
        });
    }

    private FXMLLoader openAddWindow(String title, String path, String failureUpdateTitle, String failureOpeningTitle) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource(path));
            Parent root2 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root2));

            //            Blokada kliknięć macierzystego okna
            stage.initModality(Modality.APPLICATION_MODAL);

            updateTableBeforeAction(stage, failureUpdateTitle);
            stage.show();
            return fxmlLoader;
        } catch (IOException e) {
            informationAboutFailureToAddToTheDatabase(e, failureOpeningTitle);
            e.printStackTrace();
        }
        return fxmlLoader;
    }

    // Alert błędu z detalami
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

    private void assignmentOfAcolumnInAtableAoAColumnInadatabase(String exceptionTitle) {
        this.month = month;
        //        Zsumowanie godzin
        try {
            System.out.println(month.toString());
            sumCollumn("hourTableNadgodziny", month.getValue());
            sumCollumn("hourTableWorkHour", month.getValue());


            //        Przypisanie kolum w dabeli kolumną w bazie danych
            hourTableFrom.setCellValueFactory(new PropertyValueFactory<EntityWorkDay, Timestamp>("hourTableFrom"));
            hourTableTo.setCellValueFactory(new PropertyValueFactory<EntityWorkDay, Timestamp>("hourTableTo"));
            hourTableWorkHour.setCellValueFactory(new PropertyValueFactory<EntityWorkDay, String>("hourTableWorkHour"));
            hourTableNadgodziny.setCellValueFactory(new PropertyValueFactory<EntityWorkDay, String>("hourTableNadgodziny"));

            //     Wypełnienie tabeli bazą danych
            table.setItems(loadDataFromDB("select * FROM work.workhour where  month(`hourTableFrom`) = " + month.getValue() + ";"));
        } catch (SQLException e) {
            informationAboutFailureToAddToTheDatabase(e, exceptionTitle);
            e.printStackTrace();
        }
    }

    private void assigningAvalueToTheListOfMonths() {
//       Wypełnienie Listview wartościami miesięcy
        hourMonth.getItems().addAll(loadDataMonthFromDB());
//Przypisanie listenera do przycisków
        hourMonth.getSelectionModel().selectedItemProperty().addListener(event -> {

// Ustalenie wartości parametru "month"
            this.month = month;
            month = hourMonth.getSelectionModel().getSelectedItem();

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
        System.out.println(connection + "sum connection");
        System.out.println(month.toString());
        System.out.println(collumn.toString());
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
        deleteButton();
        refreshButton();
    }

    //    Zapisanie do stringa zaznaczongeo wierza w tabeli.
    private String printSelectedRow() {

        String id = String.valueOf(table.getSelectionModel().getSelectedItem().getHourTableDayId());
        String rozpoczecie = table.getSelectionModel().getSelectedItem().getHourTableFrom().toString();
        String zakonczenie = table.getSelectionModel().getSelectedItem().getHourTableTo().toString();
        String liczbaGodzin = table.getSelectionModel().getSelectedItem().getHourTableWorkHour().toString();
        String liczbaNadgodzin = table.getSelectionModel().getSelectedItem().getHourTableNadgodziny().toString();
        String newLine = System.getProperty("line.separator");
        StringBuilder stringBuilder = new StringBuilder("id = ");
        String printedString = stringBuilder.append(id).append(newLine).append("Rozpoczęcie pracy = ").append(rozpoczecie).append(newLine).append("Zakończenie pracy = ").append(zakonczenie).append(newLine).append("Liczba godzin pracy = ").append(liczbaGodzin).append(newLine).append("Liczba nadgodzin = ").append(liczbaNadgodzin).toString();

        return printedString;
    }


    private int flagSelectedRow(String title, String header, String content) {
        this.index = index;
        boolean flag = false;
        //           Pobranie indexu zaznaczonego wiersza
        try {


            index = table.getSelectionModel().getSelectedItem().getHourTableDayId();
        } catch (Exception e) {
//                    Wywołanie informacji o błędzie nie zaznaczenia wiersza
            shortAlertinformation(Alert.AlertType.ERROR, title,
                    header,
                    content);

        }

        return index;
    }

    private void deleteButton() {

        //        Otwarcie okna dodawania
        workHourDelete.setOnAction((event) -> {


            flagSelectedRow("Błąd!", "Brak zaznaczonego wiersza.", "Przed wykoneniem polecenia muszisz " +
                    "zaznaczyć wiersz który ma być usunięty.");


//            Otwarcia okna allertu
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Usuwanie rekordu");
            alert.setHeaderText(printSelectedRow());
            alert.setContentText("Czy na pewno chcesz usunąć rekord ?");

//            Obsługa zdarzenia usunięcia rekordu
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {


//           Sprawdzenie czy został zaznaczony wiersz
                try {

//           Nawiązanie połączenia i wykoannie instrukcji SQL
                    Connection connection = ConnectionManager.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("delete FROM work.workhour where hourTableDayId = " + index + ";");
                    preparedStatement.execute();
                    shortAlertinformation(Alert.AlertType.INFORMATION, "Usuwanie", "Usunięcie wiersza powiodło się.", printSelectedRow());

                    //                    Aktualizacja tabeli.
                    assignmentOfAcolumnInAtableAoAColumnInadatabase("Nie udało się zaktualizować tabeli po usunięciu wiersza.");
                    index = 0;

                } catch (SQLException e) {
                    informationAboutFailureToAddToTheDatabase(e, "Nie udało się nawiązać połączenia");
                }
            } else {// ... user chose CANCEL or closed the dialog
            }
        });
    }


    private void refreshButton() {

        od.setOnAction((event) -> {
            assignmentOfAcolumnInAtableAoAColumnInadatabase("Nie udało się odświeżyć tabeli.");
        });
    }

    private void editButtonOnNotSelectedRow() {
        workHourEdit.setOnAction((event) -> {
            shortAlertinformation(Alert.AlertType.ERROR, "Błąd!", "Brak zaznaczonego wiersza.", "Przed wykoneniem polecenia muszisz " +
                    "zaznaczyć wiersz który ma być usunięty.");
        });
    }

    private void editButton(int number) {
//        Opcja przy zaznaczonym wierszu
        if (number == 1) {
            workHourEdit.setOnAction((event) -> {

                EditDayController editDayController = openAddWindow("Edytuj dzień", "/editDay.fxml", "Nie udało się zaktualizować tabeli po edycji", "Nie udało sie otworzyć okna edycji").getController();
                editDayController.editMethod(table.getSelectionModel().getSelectedItem());
                editDayController.setEntityWorkDay(table.getSelectionModel().getSelectedItem());
            });


//            Opcja przy niezaznaczonym wierszu
        } else if (number == 2) {
            workHourEdit.setOnAction((event1) -> {
                shortAlertinformation(Alert.AlertType.ERROR, "Błąd!", "Brak zaznaczonego wiersza.", "Przed wykoneniem polecenia muszisz " +
                        "zaznaczyć wiersz który ma być usunięty.");
            });

        }

    }

    private void addButton() {
        //        Otwarcie okna dodawania
        workHourAdd.setOnAction((event) -> {
            openAddWindow("Dodaj dzień", "/addDay.fxml", "Nie udało się zaktualizować tabeli po dodaniu wiersza.", "Nie udało się otworzyć okna dodawania");
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


}





