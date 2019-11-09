package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import com.sun.org.apache.xerces.internal.impl.dv.xs.DateTimeDV;
import connection.ConnectionManager;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.Hours;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;


public class AddDayContoller {


    @FXML
    private Button addButton;

    @FXML
    private Button beforebutton;

    @FXML
    private Label mainLabel;

    @FXML
    private Label labelFrom;

    @FXML
    private Label labelTo;

    @FXML
    private JFXDatePicker fromDatePicker;

    @FXML
    private JFXTimePicker fromTimePicker;

    @FXML
    private JFXDatePicker toDatePicker;

    @FXML
    private JFXTimePicker toTimePicker;


    IndexController indexController;


    // Zmienne które posłużą do nośnika danych za pomocą którego dane zostaną przekazane do bazy danych
    private Time startHourText = null;
    private Time endHourText = null;
    private Date dayDateText = null;

    @FXML
    void initialize() {


        fromDatePicker.setValue(LocalDate.now());
        fromTimePicker.setValue(LocalTime.now());
        toDatePicker.setValue(LocalDateTime.now().plusHours(8).toLocalDate());
        toTimePicker.setValue(LocalTime.now().plusHours(8));


//        dayDate.setValue(LocalDate.now());
//        startHour.setText(LocalTime.now().toString());
//        endHour.setText(LocalTime.now().plusHours(8).toString());
        addButton.setOnAction(event -> {
            try {
                addToDatabase();
                informationAboutAddingToTheDatabase();
                closeWindow();
            } catch (ParseException e) {
                e.printStackTrace();
                informationAboutFailureToAddToTheDatabase(e, "Nie udało się dodać dnia do bazy danych!");
            }
        });
        beforebutton.setOnAction(event -> {
            closeWindow();
        });
    }


    private void closeWindow() {
        Stage stage = (Stage) addButton.getScene().getWindow();
//        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
//                System.out.println("so");
//            }
//        });

        stage.close();
    }


    private void addToDatabase() throws ParseException {
        Time hourNadgodziny = null;
        Time workHour = null;

        try {
            LocalDateTime fromLocalDateTime = LocalDateTime.of(fromDatePicker.getValue().getYear(), fromDatePicker.getValue().getMonth(), fromDatePicker.getValue().getDayOfMonth(), fromTimePicker.getValue().getHour(), fromTimePicker.getValue().getMinute());
            LocalDateTime toLocalDateTime = LocalDateTime.of(toDatePicker.getValue().getYear(), toDatePicker.getValue().getMonth(), toDatePicker.getValue().getDayOfMonth(), toTimePicker.getValue().getHour(), toTimePicker.getValue().getMinute());
            System.out.println(fromLocalDateTime.toString());
            System.out.println(toLocalDateTime.toString());
            Connection connection = ConnectionManager.getConnection();
//            Przekompilowana i przchowywana instrukcja SQL - może być urzyta wiele razy
            PreparedStatement preparedStatement = connection.prepareStatement(" INSERT INTO `work`.`workhour`( `hourTableTo`, `hourTableFrom`, `hourTableWorkHour`, `hourTableNadgodziny`) VALUES ('" + toLocalDateTime + "', '" + fromLocalDateTime + "', timediff(`hourTableTo`, `hourTableFrom`), timediff(`hourTableWorkHour`, '8:00:00'));   ");
//            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `work`.`workhour` (`hourTableFrom`, `hourTableTo`, `hourTableDay`, `hourTableWorkHour`, `hourTableNadgodziny`) VALUES ('" + setTimeValue(startHour.getText().toString()) + "','" + setTimeValue(endHour.getText().toString()) + "','" + setDateValue(dayDate) + "','" + "`hourTableWorkHour` = timediff(hourTableTo, hourTableFrom)" + "','" + hourNadgodziny + "');");
//            Reprezentuje zestaw wyników bazy danych
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            informationAboutFailureToAddToTheDatabase(e, "Nie udało się zakończyć metody dodającej do bazy danych!");
        }
    }

    //    Pobranie danych z datepickera i zwrócenie to jako obiekt daty
    private LocalDate setDateValue(DatePicker datePicker) {
        LocalDate date = datePicker.getValue();
        return date;
    }

    //    Pobranie danych z textFiledów i zwórcenie obiektu czasu
    private Time setTimeValue(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        try {
            long parseString = simpleDateFormat.parse(s).getTime();
            Time time = new Time(parseString);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
            informationAboutFailureToAddToTheDatabase(e, "Nie udało się sprasować wartości to formatu czasu!");
        }
        return null;
    }

    //    Pobranie danych z textFiledów i zwórcenie obiektu czasu
    private Time setTimeValue2(Long s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        try {
            long parseString = simpleDateFormat.parse(s.toString()).getTime();
            Time time = new Time(parseString);


            return time;
        } catch (ParseException e) {
            e.printStackTrace();
            informationAboutFailureToAddToTheDatabase(e, "Nie udało się sprasować wartości to formatu czasu!");
        }
        return null;
    }

//    private void summedMins(){
//        int sum = 0
//        for( String hhmm : workingTimes ){
//            String[] split = hhmm.split( ":", 2 );
//            int mins = Integer.valueOf(split[ 0 ]) * 60 + Integer.valueOf( split[ 1 ] );
//            sum += mins;
//        }
//
//        String formattedWorkingTime = (int)Math.floor(sum/60) + ":" + ( sum % 60 );
//    }

    private void informationAboutAddingToTheDatabase() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dodanie dania");
        alert.setHeaderText("Dodanie dnia powiodło się!");
        alert.setContentText("Zapis wykoanny poprawnie");
//        odswiezenie bazy danych po zamknięciu allertu

        alert.showAndWait();
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


    public Label getMainLabel() {
        return mainLabel;
    }
}
