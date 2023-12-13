import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
import school.*;
import highschool.*;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

class DB {
    static Connection con;
    static Statement stmt;
    static PreparedStatement pstmt;

    static final String DB_URL = "jdbc:mysql://localhost:3306/Java_Proj";
    static final String USER = "root";
    static final String PWD = "1739";

     // 1. Load and register Driver

     static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully");

        } catch (ClassNotFoundException e) {
            System.out.println("here error");
            System.out.println(e);
        }
    }

    // 2.create connection

    static Connection createConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(DB_URL, USER, PWD);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return con;
    }

    // // 3.Create Statement
    static Statement createStatement() {
        try {
            stmt = con.createStatement();
            System.out.println("Statement created.");
        } catch (SQLException e) {
            System.out.println(e);
        }
        return stmt;
    }

    // // 5.Insert Data
    static void InsertData(String name, String address, String branch) {
        String sqlquery = "Insert into student(name,address,branch) values(?,?,?)";
        try {
            pstmt = con.prepareStatement(sqlquery);
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, branch);
            int affectedRows = pstmt.executeUpdate();
            System.out.println(affectedRows + " are affected ");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // // 6.Extract Data
    static void extractData() {
        String sqlquery = "select name,address,branch from student";
        ResultSet rs;
        try {
            rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {
                System.out.println(rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    static void extractFeedback() {
        String sqlquery = "select name,feedback from student";
        ResultSet rs;
        try {
            rs = stmt.executeQuery(sqlquery);
            while (rs.next()) {
                System.out.println(rs.getString(1) + ":" + rs.getString(2));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // // 7,8.Update and delete Data
    static boolean updateStudent(String type, String val, String name) {
        try {
            String query = String.format("update student set %s='%s' where name='%s' ", type, val,name);
            stmt.executeUpdate(query);
            con.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static boolean deleteStudent(String name) {
        try {
            String query = String.format("delete from student where name='%s' ",name);
            stmt.executeUpdate(query);
            con.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static void displayStudent(String name) {
        String query = String.format("select name,address,branch from student where name='%s'", name);
        ResultSet rs;
        try {
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println(rs.getString(1) + ":" + rs.getString(2) + ":" + rs.getString(3));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    static boolean deleteStudentbyName(String name) {
        try {
            String query = String.format("delete from student where name='%s' ",name);
            stmt.executeUpdate(query);
            con.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

public class main extends DB {

    private static final String ADMIN_USERNAME = "Admin";
    private static final String USER_USERNAME = "User";
    public static School[] students = new School[10];
    static int numCurrentStudents = 0;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        loadDriver();
        con = createConnection();
        stmt = createStatement();
        // admin and user password
        final String ADMIN_PASSWORD = "admin123";
        final String USER_PASSWORD = "user123";

        boolean exitProgram = false;

        while (!exitProgram) {
            System.out.println("Welcome to the School Management System!");
            System.out.println("Select an option:");
            System.out.println("1. Admin");
            System.out.println("2. User");
            System.out.println("3. Exit");

            int userTypeChoice;
            try {
                userTypeChoice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid option.");
                scanner.nextLine();
                continue;
            }

            switch (userTypeChoice) {
                case 1:
                    // Admin portal
                    System.out.print("Enter admin username: ");
                    String adminUsername = scanner.next();
                    System.out.print("Enter admin password: ");
                    String adminPassword = scanner.next();

                    if (adminUsername.equals(ADMIN_USERNAME) && adminPassword.equals(ADMIN_PASSWORD)) {
                        adminMenu(scanner);
                    } else {
                        System.out.println("Incorrect username or password. Try again.");
                        if (!School.handleIncorrectPassword()) {
                            continue;
                        }
                    }
                    break;

                case 2:
                    // User portal
                    System.out.print("Enter user username: ");
                    String userUsername = scanner.next();
                    System.out.print("Enter user password: ");
                    String userPassword = scanner.next();

                    if (userUsername.equals(USER_USERNAME) && userPassword.equals(USER_PASSWORD)) {
                        userMenu(scanner);
                    } else {
                        System.out.println("Incorrect username or password. Try again.");
                        if (!School.handleIncorrectPassword()) {
                            continue;
                        }
                    }
                    break;

                case 3:
                    exitProgram = true;
                    System.out.println("Exiting the program.");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void rF(){
        String path = "D:/java_proj";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
        if (listOfFiles[i].isFile()) {
            File f = new File(path+"/"+listOfFiles[i].getName());
            if(f.exists()){
                try{                       
                    BufferedReader fR = new BufferedReader(new FileReader(path+"/"+listOfFiles[i].getName()));
                    String name,address,branch;
                    double [] marks = {0,0,0,0};
                    fR.readLine();
                    name = fR.readLine();
                    branch = fR.readLine();
                    String st = fR.readLine();
                    String m[] = {"","","",""};
                    if(st!=null){
                         m = st.split(", ");
                    }
                    
                    for(int r=0;r<4;r++){    
                        marks[r] = Double.parseDouble(m[r]);
                    }
                    address = fR.readLine();
                    students[i] = new HighSchool(name, address, branch, marks);
                    numCurrentStudents++;
                    fR.close();
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
        }
    }

    private static void readFeed(){
        try{
            String path = "D:/java_proj/feedback/feedback.txt";
            BufferedReader feedBk = new BufferedReader(new FileReader(path));
            String st;
            while((st = feedBk.readLine()) != null) {
                School.setFeedback(st);
            }
            feedBk.close();
        }catch(Exception e){

        }
    }

    private static void mF(String name) {
        try{
            String path = "D:/java_proj/"+name+".txt";
            File f= new File(path);
            if(f.createNewFile()){
                System.out.println("Creating new tab");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }   
    }

    private static void wF(School s) {
        try{           
            
            String path = "D:/java_proj/"+s.name+".txt";
            File f= new File(path);
            if(f.exists()){
                FileWriter a = new FileWriter(path);
                a.write(s.rollNo+"\n"+s.name+"\n"+s.branch+"\n"+s.marks[0] + ", " + s.marks[1] + ", " + s.marks[2] + ", " + s.marks[3]+"\n"+s.address+"\n"+s.percentage);
                a.close();
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }


    private static void dF(School s){
        try{
            
            String path = "D:/java_proj/"+s.name+".txt";
            File f= new File(path);
            System.out.println("Deleted"); 
            f.delete();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private static void writeFeedback(String feedback){
        try{
            
            String path = "D:/java_proj/feedback/feedback.txt";
            File f= new File(path);
            FileWriter a = new FileWriter(path);
            a.write(feedback);
            a.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private static void adminMenu(Scanner scanner) {
        scanner.nextLine();
       rF();
       readFeed();
        String rollNoToRemove;
        boolean logout = false;

        int choice = 0;

        while (!logout) {
            // Admin menu code
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Enter information of students");
            System.out.println("2. Print information of all students");
            System.out.println("3. Print information of a particular student");
            System.out.println("4. Remove a particular student");
            System.out.println("5. Update student information");
            System.out.println("6. View Statistics");
            System.out.println("7. Read Feedback");
            System.out.println("8. Logout");
            System.out.print("Enter your choice: ");
            
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter the number of students you want to add: ");
                    int numToAdd;
                    try {
                        numToAdd = scanner.nextInt();
                        scanner.nextLine();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        scanner.nextLine();
                        continue;
                    }
                    for (int i = 0; i < numToAdd; i++) {
                        if (numCurrentStudents >= students.length) {
                            System.out.println("Not enough space for additional students.");
                            break;
                        }
                        System.out.println("Enter information for Student " + (numCurrentStudents + 1) + ":");
                        System.out.print("Name: ");
                        String name = scanner.nextLine();
                        mF(name);
                        System.out.print("Address: ");
                        String address = scanner.nextLine();
                        System.out.print("Branch: ");
                        String branch = scanner.nextLine();
                        try {
                            System.out.print("Enter marks in 4 subjects (each out of 100, on a new line): ");
                            double[] marks = new double[4];
                            for (int j = 0; j < 4; j++) {
                                marks[j] = scanner.nextDouble();
                                scanner.nextLine();
                                if (marks[j] < 0 || marks[j] > 100) {
                                    System.out.println("Invalid marks. Marks should be between 0 and 100.");
                                    j = -1;
                                }
                            }
                            students[numCurrentStudents] = new HighSchool(name, address, branch, marks);
                            DB.InsertData(name,address,branch);
                            wF(students[numCurrentStudents]);
                            numCurrentStudents++;
                        } catch (InputMismatchException | NumberFormatException e) {
                            System.out.println("Invalid marks format. Please enter valid numeric values.");
                            scanner.nextLine();
                            i--;
                        }
                    }
                    break;

                case 2:
                    System.out.println("Information of all students:");
                    if (numCurrentStudents == 0) {
                        System.out.println("No data available");
                    } else {
                        for (int i = 0; i < numCurrentStudents; i++) {
                            if (students[i] != null) {
                                students[i].displayStudentInfo();
                            }
                        }
                    }
                    break;

                case 3:
                    System.out.println("Select search criteria:");
                    System.out.println("1. Search by Roll Number");
                    System.out.println("2. Search by Name");
                    System.out.print("Enter your choice: ");
                    int searchChoice;
                    try {
                    searchChoice = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        scanner.nextLine();
                        continue;
                    }
                    scanner.nextLine();
                
                    switch (searchChoice) {
                        case 1:
                            System.out.print("Enter the roll number of the student to display: ");
                            String rollNo = scanner.nextLine();
                            boolean foundByRollNo = false;
                            if (!rollNo.matches("BML230\\d+")) {
                                System.out.println("Invalid roll number format. Please enter a valid roll number.");
                                break;
                            }
                            for (int i = 0; i < numCurrentStudents; i++) {
                                if (students[i] != null && students[i].rollNo.equals(rollNo)) {
                                    students[i].displayStudentInfo();
                                    foundByRollNo = true;
                                    break;
                                }
                            }
                            if (!foundByRollNo) {
                                System.out.println("Student with roll number " + rollNo + " not found.");
                            }
                            break;
                
                        case 2:
                            System.out.print("Enter the name of the student to display: ");
                            String nameToSearch = scanner.nextLine();
                            boolean foundByName = false;
                            for (int i = 0; i < numCurrentStudents; i++) {
                                if (students[i] != null && students[i].name.equalsIgnoreCase(nameToSearch)) {
                                    students[i].displayStudentInfo();
                                    foundByName = true;
                                    break;
                                }
                            }
                            if (!foundByName) {
                                System.out.println("Student with name " + nameToSearch + " not found.");
                            }
                            break;
                
                        default:
                            System.out.println("Invalid choice for search criteria.");
                            break;
                    }
                    break;


                case 4:
                    System.out.println("Select criteria:");
                    System.out.println("1. Enter the roll number of the student to remove: ");
                    System.out.println("2. Enter the Name of the student to remove: ");
                    System.out.print("Enter your choice: ");
                    int SearchChoice;
                    try {
                    SearchChoice = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        scanner.nextLine();
                        continue;
                    }
                    scanner.nextLine();
        
                    switch (SearchChoice) {
                        case 1:
                            System.out.print("Enter the roll number of the student to remove: ");
                            rollNoToRemove = scanner.nextLine();
                            if (!rollNoToRemove.matches("BML230\\d+")) {
                                System.out.println("Invalid roll number format. Please enter a valid roll number.");
                                break;
                            }
                            boolean foundByRollNo = false;
                            for (int i = 0; i < numCurrentStudents; i++) {
                                if (students[i] != null && students[i].rollNo.equals(rollNoToRemove)) {
                                    dF(students[i]);
                                    students[i] = null;
                                    System.out.println("Student with roll number " + rollNoToRemove + " has been removed.");
                                    foundByRollNo = true;
                                    break;
                                }
                            }
                            if (!foundByRollNo) {
                                System.out.println("Student with roll number " + rollNoToRemove + " not found.");
                            }
                            break;
        
                        case 2:
                            System.out.print("Enter the Name of the student to remove: ");
                            rollNoToRemove = scanner.nextLine();
                            boolean foundByName = false;
                            for (int i = 0; i < numCurrentStudents; i++) {
                                if (students[i] != null && students[i].name.equals(rollNoToRemove)) {
                                    dF(students[i]);
                                    students[i] = null;
                                    System.out.println("Student with name " + rollNoToRemove + " has been removed.");
                                    foundByName = true;
                                    break;
                                }
                            }
                            if (!foundByName) {
                                System.out.println("Student with name " + rollNoToRemove + " not found.");
                            }
                            break;
        
                        default:
                            System.out.println("Invalid choice for search criteria.");
                            break;
                    }
                    break;
                    

                case 5:
                  
                  System.out.println("Select criteria:");
                    System.out.println("1. Enter the roll number of the student to update: ");
                    System.out.println("2. Enter the Name of the student to update: ");
                    System.out.print("Enter your choice: ");
                    boolean foundByRollNo = false;
                    
            try {
                searchChoice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
                continue;
            }
            
                  switch (searchChoice){
                    case 1:System.out.print("Enter the roll number of the student to update: ");
                    scanner.nextLine();
                    String rollNoToUpdate = scanner.nextLine();
                    if (!rollNoToUpdate.matches("BML230\\d+")) {
                        System.out.println("Invalid roll number format. Please enter a valid roll number.");
                        break;
                    }
                    for (int i = 0; i < numCurrentStudents; i++) {
                        if (students[i] != null && students[i].rollNo.equals(rollNoToUpdate)) {
                            foundByRollNo=true;
                            System.out.print("Enter updated name (press Enter to leave it unchanged): ");
                            String updatedName = scanner.nextLine();
                            if (!updatedName.isEmpty()) {
                                students[i].name = updatedName;
                            }
                        
                            System.out.print("Enter updated address (press Enter to leave it unchanged): ");
                            String updatedAddress = scanner.nextLine();
                            if (!updatedAddress.isEmpty()) {
                                students[i].address = updatedAddress;
                            }
                        
                            System.out.print("Enter updated branch (press Enter to leave it unchanged): ");
                            String updatedBranch = scanner.nextLine();
                            if (!updatedBranch.isEmpty()) {
                                students[i].branch = updatedBranch;
                            }
                        
                            try {
                                System.out.print("Enter updated marks in 4 subjects (each out of 100, on a new line): ");
                                double[] marks = new double[4];
                                String c = scanner.nextLine();
                                if(!c.isEmpty()){
                                    for (int j = 0; j < 4; j++) {
                                    marks[j] = scanner.nextDouble();
                                    scanner.nextLine();
                                    if (marks[j] < 0 || marks[j] > 100) {
                                        System.out.println("Invalid marks. Marks should be between 0 and 100.");
                                        j = -1;
                                    }
                                }
                                students[i].marks = marks;
                              }
                                
                                } catch (InputMismatchException | NumberFormatException e) {
                                    System.out.println("Invalid marks format. Please enter valid numeric values.");
                                    scanner.nextLine();
                                    i--;
                                }
                    
                                students[i].calculatePercentage();
                                students[i].calculateGrade();
                                wF(students[i]);
                                System.out.println("Student information updated successfully.");
                                break;
                            }
                    }
                    if (!foundByRollNo) {
                        System.out.println("Student with roll number " + rollNoToUpdate + " not found.");
                    }
                    break;
                    case 2: 
                    System.out.print("Enter the Name of the student to update: ");
                    scanner.nextLine();
                    boolean foundByName = false;
                            rollNoToRemove = scanner.nextLine(); 
                            System.out.println(rollNoToRemove);                        
                            for (int i = 0; i < numCurrentStudents; i++) {
                                if (students[i] != null && students[i].name.equals(rollNoToRemove)) {
                                    foundByName=true;
                            System.out.print("Enter updated name (press Enter to leave it unchanged): ");
                            String updatedName = scanner.nextLine();
                            if (!updatedName.isEmpty()) {
                                students[i].name = updatedName;
                            }
                        
                            System.out.print("Enter updated address (press Enter to leave it unchanged): ");
                            String updatedAddress = scanner.nextLine();
                            if (!updatedAddress.isEmpty()) {
                                students[i].address = updatedAddress;
                            }
                        
                            System.out.print("Enter updated branch (press Enter to leave it unchanged): ");
                            String updatedBranch = scanner.nextLine();
                            if (!updatedBranch.isEmpty()) {
                                students[i].branch = updatedBranch;
                            }
                        
                            try {
                                System.out.print("Enter updated marks in 4 subjects (each out of 100, on a new line): ");
                                double[] marks = new double[4];
                                String c = scanner.nextLine();
                                if(!c.isEmpty()){
                                    for (int j = 0; j < 4; j++) {
                                    marks[j] = scanner.nextDouble();
                                    scanner.nextLine();
                                    if (marks[j] < 0 || marks[j] > 100) {
                                        System.out.println("Invalid marks. Marks should be between 0 and 100.");
                                        j = -1;
                                    }
                                }
                                students[i].marks = marks;
                              }
                                
                                } catch (InputMismatchException | NumberFormatException e) {
                                    System.out.println("Invalid marks format. Please enter valid numeric values.");
                                    scanner.nextLine();
                                    i--;
                                }
                    
                                students[i].calculatePercentage();
                                students[i].calculateGrade();
                                wF(students[i]);
                                System.out.println("Student information updated successfully.");
                                    break;
                                }
                            }
                            if (!foundByName) {
                                System.out.println("Student with name " + rollNoToRemove + " not found.");
                            }
                            break;
                    default : System.out.println("Invalid choice for search criteria.");
                            break;
                  }
                    break;
            
                case 6:
                    School.displayStatistics(students, numCurrentStudents);
                    break;

                case 7:
                    System.out.println("All Feedbacks:");
                    System.out.println(School.getFeedback());
                    break;


                case 8:
                    System.out.println("Logging out from Admin Portal.");
                    logout = true;
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void userMenu(Scanner scanner) {
        boolean logout = false;

        int choice = 0;
       if(students.length==0){
            rF();
       }  
       readFeed();

        while (!logout) {
            // User menu code
            System.out.println("\nUser Menu:");
            System.out.println("1. Print information of all students");
            System.out.println("2. Print information of a particular student");
            System.out.println("3. Submit Feedback");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
                continue;
            }
            switch (choice) {
                case 1:
                    System.out.println("Information of all students:");
                    if (numCurrentStudents == 0) {
                        System.out.println("No data available");
                    } else {
                        for (int i = 0; i < numCurrentStudents; i++) {
                            if (students[i] != null) {
                                students[i].displayStudentInfo();
                            }
                        }
                    }
                    break;

                case 2:
                    System.out.println("Select search criteria:");
                    System.out.println("1. Search by Roll Number");
                    System.out.println("2. Search by Name");
                    System.out.print("Enter your choice: ");
                    int searchChoice;
            try {
                 searchChoice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
                continue;
            }
                    scanner.nextLine();
                
                    switch (searchChoice) {
                        case 1:
                            System.out.print("Enter the roll number of the student to display: ");
                            String rollNo = scanner.nextLine();
                            boolean foundByRollNo = false;
                            for (int i = 0; i < numCurrentStudents; i++) {
                                if (students[i] != null && students[i].rollNo.equals(rollNo)) {
                                    students[i].displayStudentInfo();
                                    foundByRollNo = true;
                                    break;
                                }
                            }
                            if (!foundByRollNo) {
                                System.out.println("Student with roll number " + rollNo + " not found.");
                            }
                            break;
                
                        case 2:
                            System.out.print("Enter the name of the student to display: ");
                            String nameToSearch = scanner.nextLine();
                            boolean foundByName = false;
                            for (int i = 0; i < numCurrentStudents; i++) {
                                if (students[i] != null && students[i].name.equalsIgnoreCase(nameToSearch)) {
                                    students[i].displayStudentInfo();
                                    foundByName = true;
                                    break;
                                }
                            }
                            if (!foundByName) {
                                System.out.println("Student with name " + nameToSearch + " not found.");
                            }
                            break;
                
                        default:
                            System.out.println("Invalid choice for search criteria.");
                            break;
                    }
                    break;

                case 3:
                    System.out.print("Enter your name: ");
                    scanner.nextLine();
                    String userName = scanner.nextLine();
                    
                    boolean userFound = false;
                    for (int i = 0; i < numCurrentStudents; i++) {
                        if (students[i] != null && students[i].name.equalsIgnoreCase(userName)) {
                            userFound = true;
                        }
                    }

                    if (!userFound) {
                        System.out.println("Please enter a valid student name.");
                        continue;
                    }

                    System.out.print("Enter your feedback: ");
                    String userFeedback = scanner.nextLine();
                    if (userFeedback.isEmpty()) {
                        System.out.println("Feedback cannot be empty. Please provide feedback.");
                        continue;
                    }
                    School.setFeedback(userFeedback);
                    writeFeedback(School.getFeedback());
                    System.out.println("Feedback submitted. Thank you!");
                    break;

                case 4:
                    System.out.println("Logging out from User Portal.");
                    logout = true;
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
}