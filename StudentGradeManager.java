import java.util.ArrayList;
import java.util.Scanner;

// Class to hold student data
class Student {
    String name;
    double grade;

    Student(String name, double grade) {
        this.name = name;
        this.grade = grade;
    }
}

// Main program
public class StudentGradeManager {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();

        System.out.println("===== Student Grade Management System =====");

        // Input loop for student details
        while (true) {
            System.out.print("\nEnter student name (or type 'done' to finish): ");
            String name = input.nextLine();

            if (name.equalsIgnoreCase("done")) {
                break;
            }

            System.out.print("Enter grade for " + name + ": ");
            double grade = input.nextDouble();
            input.nextLine(); // consume newline

            students.add(new Student(name, grade));
        }

        if (students.isEmpty()) {
            System.out.println("\nNo student data entered. Exiting program.");
            return;
        }

        // Variables for calculations
        double total = 0;
        double highest = Double.MIN_VALUE;
        double lowest = Double.MAX_VALUE;
        String topStudent = "";
        String lowStudent = "";

        // Calculate totals and find highest/lowest grades
        for (Student s : students) {
            total += s.grade;

            if (s.grade > highest) {
                highest = s.grade;
                topStudent = s.name;
            }

            if (s.grade < lowest) {
                lowest = s.grade;
                lowStudent = s.name;
            }
        }

        double average = total / students.size();

        // Display Summary Report
        System.out.println("\n===== Summary Report =====");
        System.out.printf("%-20s%-10s\n", "Student Name", "Grade");
        System.out.println("------------------------------------");

        for (Student s : students) {
            System.out.printf("%-20s%-10.2f\n", s.name, s.grade);
        }

        System.out.println("------------------------------------");
        System.out.printf("Average Grade: %.2f\n", average);
        System.out.printf("Highest Grade: %.2f (%s)\n", highest, topStudent);
        System.out.printf("Lowest Grade: %.2f (%s)\n", lowest, lowStudent);
        System.out.println("====================================");
    }
}
