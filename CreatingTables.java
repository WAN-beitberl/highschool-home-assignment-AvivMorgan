import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class CreatingTables {
    /*טענת כניסה: הפעולה מקבלת את אובייקט החיבור למסד הנתונים
    טענת יציאה: הפעולה יוצרת את טבלת התיכון ואת טבלת החברויות ומכניסה לטבלאות את הנתונים מהקבצים הנתונים*/
    public static void CreatingTables(@NotNull Statement statement)
    {
        try{
            // Creating normalized tables
            String createMainTable = "CREATE TABLE highschool_data" +
                    "(id INTEGER not NULL, first_name VARCHAR(255), last_name VARCHAR(255), email VARCHAR(255), gender VARCHAR(255)," +
                    "ip_address VARCHAR(255), cm_height INTEGER, age INTEGER, has_car Boolean, car_color VARCHAR(255), grade INTEGER, "+
                    "grades_avg DOUBLE, identification_card INTEGER, PRIMARY KEY (id))";
            statement.executeUpdate(createMainTable);
            String createFriendshipsTable = "CREATE TABLE highschool_friendships_data" +
                    "(friendship_id INTEGER not NULL, friend_id INTEGER , other_friend_id INTEGER, PRIMARY KEY (friendship_id),\n" +
                    "    FOREIGN KEY (friend_id) REFERENCES highschool_data(id))";
            statement.executeUpdate(createFriendshipsTable);

            // Insert data from the csv files

            String highschool_data_path = "/C:/Users/avivm/Desktop/highschool_sql_assignment/highschool.csv";
            String friendships_data_path = "/C:/Users/avivm/Desktop/highschool_sql_assignment/highschool_friendships.csv";
            String line = "", insert_file_data_cmd = "";
            // For highschool table
            BufferedReader br =new BufferedReader((new FileReader(highschool_data_path)));
            line = br.readLine();
            while((line = br.readLine()) != null) {
                String[] values = line.split(",");
                insert_file_data_cmd = "INSERT INTO highschool_data VALUES (" + values[0] + "," + "'" + values[1].replace("'", "''") + "'" + "," +
                        "'" + values[2].replace("'", "''") + "'" + "," + "'" + values[3] + "'" + "," + "'" +
                        values[4] + "'" + "," + "'" + values[5] + "'" + "," + values[6] + "," + values[7] + "," +
                        values[8] + "," + "'" + values[9] + "'" + "," + values[10] + "," + values[11] + "," + values[12] + ")";

                statement.executeUpdate(insert_file_data_cmd);
            }

            // For friendships table
            BufferedReader br2 = new BufferedReader((new FileReader(friendships_data_path)));
            line = br2.readLine();
            while ((line = br2.readLine()) != null){
                String[] values = line.split(",");
                if(values.length == 3){
                    if(values[1] == "")
                        values[1] = "null";
                    if(values[2] == "")
                        values[2] = "null";
                    insert_file_data_cmd = "INSERT INTO highschool_friendships_data VALUES(" + values[0] + "," + values[1] + "," + values[2] + ")";
                    statement.executeUpdate(insert_file_data_cmd);
                    System.out.println(insert_file_data_cmd);
                }
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
