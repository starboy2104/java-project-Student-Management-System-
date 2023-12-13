package highschool;
import school.*;

public class HighSchool extends School {
    private String type;

    public HighSchool(String name, String address, String branch, double[] marks) {
        super(name, address, branch, marks);
        this.type = "Regular";
    }

    @Override
    public void displayStudentInfo() {
        super.displayStudentInfo();
        System.out.println("Type: " + type);
    }
}
