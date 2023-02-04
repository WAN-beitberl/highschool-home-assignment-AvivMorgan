import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Main {
    static final String DB_URL = "jdbc:mysql://localhost:3306/highschool";
    static final String USER = "root";
    static final String PASS = "password";
    static final String QUERY = "SELECT id, first, last, age FROM Employees";

    // Input 1
    public static double GetSchoolAvg(Statement statement){
        String query = "SELECT avg(grades_avg) FROM highschool.highschool_data";
        double avg = 0;
        try{
            ResultSet resultSet = statement.executeQuery(query);
            avg = resultSet.getDouble("avg(grades_avg)");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return avg;
    }
    // Input 2
    public static double GetMalesAvg(Statement statement){
        String query = "SELECT avg(grades_avg) FROM highschool.highschool_data where gender like 'Male'";
        double avg = 0;
        try{
            ResultSet resultSet = statement.executeQuery(query);
            avg = resultSet.getDouble("avg(grades_avg)");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return avg;
    }
    // Input 3
    public static double GetFemalesAvg(Statement statement){
        String query = "SELECT avg(grades_avg) FROM highschool.highschool_data where gender like 'Female'";
        double avg = 0;
        try{
            ResultSet resultSet = statement.executeQuery(query);
            avg = resultSet.getDouble("avg(grades_avg)");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return avg;
    }
    // Input 4
    public static double GetTallsHeightAvg(Statement statement){
        String query = "SELECT avg(cm_height) FROM highschool.highschool_data where cm_height >= 200 and car_color like 'Purple'";
        double avg = 0;
        try{
            ResultSet resultSet = statement.executeQuery(query);
            avg = resultSet.getDouble("avg(cm_height)");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return avg;
    }
    // Input 5
    public static int[] GetFriendsCycle(Statement statement, String id) {
        String query = "SELECT * FROM highschool.highschool_friendships_data where friend_id = ";
        int[] friends = {};
        try{
            ResultSet resultSet = statement.executeQuery(query + id);
            int index = 0;
            while (resultSet.next()){
                // First friends cycle
                friends[index] = resultSet.getInt("other_friend_id");
                index++;
                ResultSet resultSet2 = statement.executeQuery(query + resultSet.getInt("other_friend_id"));
                // Second friends cycle
                while (resultSet2.next()){
                    friends[index] = resultSet2.getInt("other_friend_id");
                    index++;
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return friends;
    }
    // Input 6
    public static int[] GetFriendshipsInfo(Statement statement){
        int totalStudents = 1, studentFriendsCount = 0;
        int[] popularity_precent = {0, 0, 0};
        String query = "SELECT Count(*) FROM highschool.highschool_data";
        try{
            ResultSet resultSet = statement.executeQuery(query);
            totalStudents = resultSet.getInt("Count(*)");
            for (int i = 1; i <= totalStudents; i++){
                resultSet = statement.executeQuery("SELECT Count(*) FROM highschool.highschool_friendships_data where friend_id = " + Integer.toString(i));
                studentFriendsCount = resultSet.getInt("Count(*)");
                if(studentFriendsCount >= 2)
                    popularity_precent[0]++;
                if(studentFriendsCount == 1)
                    popularity_precent[1]++;
                if(studentFriendsCount == 0)
                    popularity_precent[2]++;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        popularity_precent[0] = (popularity_precent[0] * 100) / totalStudents;
        popularity_precent[1] = (popularity_precent[1] * 100) / totalStudents;
        popularity_precent[2] = (popularity_precent[2] * 100) / totalStudents;
        return popularity_precent;
    }
    // Input 7
    public static double GetStudentAvg(Statement statement, String studentId){
        String query = "SELECT * FROM highschool.avg_view where identification_card = " + studentId;
        double avg = 0;
        try{
            ResultSet resultSet = statement.executeQuery(query);
            avg = resultSet.getDouble("grades_avg");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return avg;
    }
    public static void main(String[] args) {
        try{
            // Open a connection
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = connection.createStatement();

            // Summery System Manu
            int[] tempFriendsCycle = {}, tempFriendshipsInfo = {};
            Scanner selection = new Scanner(System.in);
            System.out.println("\n\n\n\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
            System.out.println("\t\t\tWelcome to the Sima's school summery system!\n\n");
            System.out.println("\t\t\tEnter #1- to get the average of the school's grades.\n");
            System.out.println("\t\t\tEnter #2- to get the average of the male students grades.\n");
            System.out.println("\t\t\tEnter #3- to get the average of the female students grades.\n");
            System.out.println("\t\t\tEnter #4- to get the height average of the 200cm+ student that also have purple cars.\n");
            System.out.println("\t\t\tEnter #5- to get the friends list of student.\n");
            System.out.println("\t\t\tEnter #6- to get the friendships information of highschool students.\n");
            System.out.println("\t\t\tEnter #7- to get the grades average of student.\n");
            System.out.println("\t\t\tEnter #8- to exit.\n");
            System.out.println("\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n\n");
            int input = selection.nextInt();
            while (input != 8){
                switch (input){
                    case 1:
                        System.out.println("The school avg is: " + GetSchoolAvg(statement));
                        break;
                    case 2:
                        System.out.println("The male students avg is: " + GetMalesAvg(statement));
                        break;
                    case 3:
                        System.out.println("The female students avg is: " + GetFemalesAvg(statement));
                        break;
                    case 4:
                        System.out.println("The average of them is: " + GetTallsHeightAvg(statement));
                        break;
                    case 5:
                        System.out.println("Enter the student id: ");
                        String id = selection.nextLine();
                        tempFriendsCycle = GetFriendsCycle(statement, id);
                        System.out.print("[");
                        for (int i = 0; i < tempFriendsCycle.length; i++){
                            System.out.println(tempFriendsCycle[i] + ", ");
                        }
                        System.out.print("].\n");
                        break;
                    case 6:
                        tempFriendshipsInfo = GetFriendshipsInfo(statement);
                        System.out.println("The popular students are " + tempFriendshipsInfo[0] + " from the school.");
                        System.out.println("The regular students are " + tempFriendshipsInfo[1] + " from the school.");
                        System.out.println("The single students are " + tempFriendshipsInfo[2] + " from the school.");
                        break;
                    case 7:
                        System.out.println("Enter the student id: ");
                        String studentId = selection.nextLine();
                        double avg = GetStudentAvg(statement, studentId);
                        System.out.print("The average of grades of " + studentId + " is " + Double.toString(avg));
                        break;
                }
                System.out.println("\n\n\n\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
                System.out.println("\t\t\tWelcome to the Sima's school summery system!\n\n");
                System.out.println("\t\t\tEnter #1- to get the average of the school's grades.\n");
                System.out.println("\t\t\tEnter #2- to get the average of the male students grades.\n");
                System.out.println("\t\t\tEnter #3- to get the average of the female students grades.\n");
                System.out.println("\t\t\tEnter #4- to get the height average of the 200cm+ student that also have purple cars.\n");
                System.out.println("\t\t\tEnter #5- to get the friends list of student.\n");
                System.out.println("\t\t\tEnter #6- to get the friendships information of highschool students.\n");
                System.out.println("\t\t\tEnter #7- to get the grades average of student.\n");
                System.out.println("\t\t\tEnter #8- to exit.\n");
                System.out.println("\t\t\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n\n");
                input = selection.nextInt();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}