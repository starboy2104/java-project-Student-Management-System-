package school;
import studentinfo.*;
import java.util.concurrent.TimeUnit;

public class School implements StudentInfo {
    private static int studentCounter = 1;
    private static String feedback = "";
    private static int incorrectPasswordAttempts = 0;

    public String rollNo;
    public String name;
    public String address;
    public String branch;
    public double[] marks;
    public double percentage;
    private String grade;

    public School(String name, String address, String branch, double[] marks) {
        this.rollNo = "BML" + "230" + studentCounter;
        this.name = name;
        this.address = address;
        this.branch = branch;
        this.marks = marks;
        calculatePercentage();
        calculateGrade();
        studentCounter++;
    }

    public void displayStudentInfo() {
        System.out.println("**************** Student *******************");
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Branch: " + branch);
        System.out.println("Marks in 4 Subjects: " + marks[0] + ", " + marks[1] + ", " + marks[2] + ", " + marks[3]);
        System.out.println("Percentage: " + percentage + "%");
        System.out.println("Grade: " + grade);
    }

    public void calculatePercentage() {
        double totalMarks = 0;
        for (double mark : marks) {
            totalMarks += mark;
        }
        percentage = (totalMarks / (marks.length * 100)) * 100;
    }

    public void calculateGrade() {
        if (percentage >= 85) {
            grade = "A grade";
        } else if (percentage >= 75) {
            grade = "B grade";
        } else if (percentage >= 60) {
            grade = "C grade";
        } else if (percentage >= 45) {
            grade = "D grade";
        } else {
            grade = "Fail";
        }
    }

    public static void displayStatistics(School[] students, int numCurrentStudents) {
        if (numCurrentStudents == 0) {
            System.out.println("No data available to calculate statistics.");
            return;
        }

        double totalPercentage = 0;
        double highestPercentage = students[0].percentage;
        double lowestPercentage = students[0].percentage;

        for (int i = 0; i < numCurrentStudents; i++) {
            if (students[i] != null) {
                totalPercentage += students[i].percentage;

                if (students[i].percentage > highestPercentage) {
                    highestPercentage = students[i].percentage;
                }

                if (students[i].percentage < lowestPercentage) {
                    lowestPercentage = students[i].percentage;
                }
            }
        }

        double averagePercentage = totalPercentage / numCurrentStudents;

        System.out.println("\nStatistics:");
        System.out.println("Average Percentage: " + averagePercentage + "%");
        System.out.println("Highest Percentage: " + highestPercentage + "%");
        System.out.println("Lowest Percentage: " + lowestPercentage + "%");
    }

    public static void setFeedback(String userFeedback) {
        feedback += userFeedback + "\n";
    }

    public static String getFeedback() {
        return feedback;
    }



    public static boolean handleIncorrectPassword() {
        int maxAttempts = 3;
    
        incorrectPasswordAttempts++;
    
        int remainingAttempts = maxAttempts - incorrectPasswordAttempts + 1;
    
        if (remainingAttempts > 0) {
            System.out.println("Incorrect username or password. " + remainingAttempts + " attempts remaining. Try again.");
    
            return true;
        }
    
        long delayMillis = (long) Math.pow(2, incorrectPasswordAttempts - maxAttempts) * 1000;
    
        System.out.println("Maximum attempts reached. Please try again after " + TimeUnit.MILLISECONDS.toSeconds(delayMillis) + " seconds.");
    
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    
        return false;
    }

}