package Database;


import DataClasses.CurrentParameter_Values;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Max on 01.12.2014.
 */
public class Queries {
    private static Connection connection;
    private static ResultSet resultSet;
    private static Statement statement;
    private static String names = "5-Oxo Pro , Ala , Arg , Asp , Cit , Glu , Gly , His , Leu , Met , Orn , Phe , Pro , Ser , Trp , Tyr , Val , C0 , C2 , C3 , C4 , C5 , C6 , C8 , C10 , C12 , C14 , C16 , C18";
    public static String[] names_str = names.split(" , ");

//    SELECT QUERIES-----------------------------------------------------------------------------------------------------------

    public static ArrayList<ArrayList<String>> select_Files() {
        ArrayList<ArrayList<String>> files_data = new ArrayList<ArrayList<String>>();
        ArrayList<String> file_row;
        try {
            connection = DBConnector.getConn();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from files where lot = '" + CurrentParameter_Values.getLot() + "' ;");
            while (resultSet.next()) {
                file_row = new ArrayList<String>();
                file_row.add(String.valueOf(resultSet.getInt(1)));  // add file id
                file_row.add(String.valueOf(resultSet.getInt(2)));  // add file lot
                file_row.add(resultSet.getString(3));               // add file url
                file_row.add(resultSet.getString(4));               // add file date
                files_data.add(file_row);                           // add file row to files_list
            }
            resultSet.close();
            statement.close();

        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return files_data;
    }

    public static ArrayList<ArrayList<Double>> select_FilesData(String choice) {
        ArrayList<ArrayList<Double>> both_level_scores = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> scores_lv1 = new ArrayList<Double>();
        ArrayList<Double> scores_lv2 = new ArrayList<Double>();

        try {
            connection = DBConnector.getConn();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from files_choice where choice = '" + choice + "' and file_id in (SELECT file_id FROM " +
                    " files where lot = '" + CurrentParameter_Values.getLot() + "' );");
            while (resultSet.next()) {
                scores_lv1.add(resultSet.getDouble(3));
                scores_lv2.add(resultSet.getDouble(4));
            }

            both_level_scores.add(scores_lv1);
            both_level_scores.add(scores_lv2);

            resultSet.close();
            statement.close();

        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return both_level_scores;
    }

    public static ArrayList<Double> select_LotData(String choice) {
        ArrayList<Double> average_and_sigma_values_lv1_lv2 = new ArrayList<Double>();
        try {
            connection = DBConnector.getConn();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from start_files_choice where choice = '" + choice + "' and lot = '" + CurrentParameter_Values.getLot() + "' ;");
            if (resultSet.next()) {
                average_and_sigma_values_lv1_lv2.add(resultSet.getDouble(3));  // Average value level 1
                average_and_sigma_values_lv1_lv2.add(resultSet.getDouble(4));  // Average value level 2
                average_and_sigma_values_lv1_lv2.add(resultSet.getDouble(5));  // Sigma value level 1
                average_and_sigma_values_lv1_lv2.add(resultSet.getDouble(6));  // Sigma value level 2
            }
            resultSet.close();
            statement.close();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return average_and_sigma_values_lv1_lv2;
    }

    public static ArrayList<ArrayList<String>> select_Lot() {
        ArrayList<ArrayList<String>> projects = new ArrayList<ArrayList<String>>();
        try {
            connection = DBConnector.getConn();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from projects ;");

            while (resultSet.next()) {
                ArrayList<String> project_row = new ArrayList<String>();
                project_row.add(resultSet.getString(1));    // lot
                project_row.add(resultSet.getString(2));    // url
                project_row.add(resultSet.getString(3));    // date
                projects.add(project_row);
            }
            resultSet.close();
            statement.close();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return projects;
    }

//      INSERT QUERIES---------------------------------------------------------------------------------------------------------

    public static void insertNewProject(String lot, String url, String date, ArrayList<Double> average_values_lv1, ArrayList<Double> average_values_lv2, ArrayList<Double> sigma_values_lv1, ArrayList<Double> sigma_values_lv2) {
        try {
            connection = DBConnector.getConn();
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO projects (lot , url , date ) values ( '" + lot + "' , '" + url + " ' , '" + date + "' ) ;");
            statement.close();
            statement = connection.createStatement();
            for (int i = 0; i < 29; i++) {
                statement.executeUpdate("INSERT INTO start_files_choice (lot , choice , average_value_level1 , average_value_level2 , sigma_value_level1 , sigma_value_level2 ) values ( '" + lot + "' , '" + names_str[i] + "' , " + average_values_lv1.get(i) + " , " + average_values_lv2.get(i) + " , " + sigma_values_lv1.get(i) + " , " + sigma_values_lv2.get(i) + " ) ;");
            }
            statement.close();

        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public static void insertNewFile(String url, String date, ArrayList<Double> average_values_lv1, ArrayList<Double> average_values_lv2) {
        try {
            connection = DBConnector.getConn();
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO files (lot , url , date ) values ( '" + CurrentParameter_Values.getLot() + "' , '" + url + " ' , '" + date + "' ) ;");
            statement.close();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT MAX(file_id) FROM files WHERE lot = '" + CurrentParameter_Values.getLot() + "' ;");
            resultSet.next();
            int file_id = resultSet.getInt(1);
            for (int i = 0; i < 29; i++) {
                statement.executeUpdate("INSERT INTO files_choice (file_id , choice , average_value_level1 , average_value_level2 ) values ( " + file_id + " , '" + names_str[i] + "' , " + average_values_lv1.get(i) + " , " + average_values_lv2.get(i) + " ) ;");
            }

            statement.close();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

//    UPDATE QUERIES------------------------------------------------------------------------------------------------------------------------

    public static void updateFileData(int num, String url, String date, ArrayList<Double> average_values_lv1, ArrayList<Double> average_values_lv2) {
        try {
            connection = DBConnector.getConn();
            statement = connection.createStatement();
            statement.executeUpdate("update files set url = '" + url + " ' , date = '" + date + "'  WHERE file_id = " + Queries.select_Files().get(num).get(0) + " ; ");
            for (int i = 0; i < 29; i++) {
                statement = connection.createStatement();
                statement.executeUpdate("update files_choice set average_value_level1 = " + average_values_lv1.get(i) + " , average_value_level2 = " + average_values_lv2.get(i) + "  WHERE file_id = " + Queries.select_Files().get(num).get(0) + " and choice = '" + names_str[i] + "' ;");
            }
            statement.close();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public static void updateFilesLot(int num, String lot) {
        try {
            connection = DBConnector.getConn();
            statement = connection.createStatement();
            statement.executeUpdate("update files set lot = '" + lot + "' WHERE lot = '" + Queries.select_Lot().get(num).get(0) + "' ; ");
            statement.close();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public static void updateProjectData(int num, String lot, String url, String date, ArrayList<Double> average_values_lv1, ArrayList<Double> average_values_lv2, ArrayList<Double> sigma_values_lv1, ArrayList<Double> sigma_values_lv2) {
        try {
            connection = DBConnector.getConn();
            for (int i = 0; i < 29; i++) {
                statement = connection.createStatement();
                statement.executeUpdate("update start_files_choice set lot = '" + lot + "' , " + " average_value_level1 = " + average_values_lv1.get(i) + " , average_value_level2 = " + average_values_lv2.get(i) + " , " + " sigma_value_level1 = " + sigma_values_lv1.get(i) + " , sigma_value_level2 = " + sigma_values_lv2.get(i) + "  WHERE lot = '" + Queries.select_Lot().get(num).get(0) + "' and choice = '" + names_str[i] + "' ;");
            }
            statement = connection.createStatement();
            statement.executeUpdate("update projects set lot = '" + lot + "' , url = '" + url + " ' , date = '" + date + "' WHERE lot = '" + Queries.select_Lot().get(num).get(0) + "' ; ");
            statement.close();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

//    Clear Tables Content QUERIE---------------------------------------------------------------------------------------

    public static void clear_AllTables() {
        try {
            connection = DBConnector.getConn();
            statement = connection.createStatement();
            statement.executeUpdate("DELETE  FROM files");
            statement.executeUpdate("DELETE  FROM files_choice");
            statement.executeUpdate("DELETE  FROM projects");
            statement.executeUpdate("DELETE  FROM start_files_choice");
            statement.close();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}

