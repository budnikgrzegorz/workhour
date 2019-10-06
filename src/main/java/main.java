import connection.ConnectionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import manger.TableMangaer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class main extends Application {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        launch(args);

    }


    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("index.fxml"));
        primaryStage.setTitle("Lista obecności");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();

    }


}

