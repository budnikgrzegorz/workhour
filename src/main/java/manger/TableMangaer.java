package manger;

import com.mysql.cj.jdbc.ConnectionGroupManager;
import connection.ConnectionManager;
import entity.entityWorkDay;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TableMangaer extends ConnectionManager {

  public static void getAll() throws SQLException {
    Connection connection = ConnectionManager.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM worktable");
      ResultSet resultSet = preparedStatement.getResultSet();
      System.out.println(resultSet.next());
  }

    public static void printTrainingCoursesNamesStartingWith() throws SQLException {
        System.out.println("--------------Zadanie 2.3--------------------");
        Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM workhour");


        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println(resultSet.getString(2));            System.out.println(resultSet.getString("name"));
        }
        System.out.println("----------------------------------");
    }
}
