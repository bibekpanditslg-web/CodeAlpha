import java.util.ArrayList;
import java.util.Scanner;

class Student {
    private String name;
    private ArrayList<Integer> grades = new ArrayList<>();

    public Student(String name) {
        this.name = name;
    }

    public void addGrade(int grade) {
        grades.add(grade);
    }

    public double getAverage() {
        if (grades.isEmpty()) return 0.0;
        int total = 0;
        for (int grade : grades) total += grade;
        return (double) total / grades.size();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getGrades() {
        return grades;
    }

    public String getGradeReport() {
        return "Name: " + name + ", Grades: " + grades + ", Average: " + String.format("%.2f", getAverage());
    }
}

public class StudentGradeManager {
    private static ArrayList<Student> students = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n===== Student Grade Management System =====");
            System.out.println("1. Add Student");
            System.out.println("2. Add Grade");
            System.out.println("3. View All Students");
            System.out.println("4. View Student Report");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addGrade();
                case 3 -> viewAllStudents();
                case 4 -> viewStudentReport();
                case 5 -> System.out.println("Exiting... Thank you!");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    private static void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        students.add(new Student(name));
        System.out.println("Student added successfully!");
    }

    private static void addGrade() {
        if (students.isEmpty()) {
            System.out.println("No students available. Add a student first.");
            return;
        }

        System.out.println("Select a student:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i).getName());
        }

        System.out.print("Enter student number: ");
        int index = scanner.nextInt() - 1;
        if (index < 0 || index >= students.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        System.out.print("Enter grade (0–100): ");
        int grade = scanner.nextInt();
        if (grade < 0 || grade > 100) {
            System.out.println("Invalid grade.");
            return;
        }

        students.get(index).addGrade(grade);
        System.out.println("Grade added successfully!");
    }

    private static void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students to display.");
            return;
        }

        System.out.println("\n--- All Students ---");
        for (Student student : students) {
            System.out.println(student.getGradeReport());
        }
    }

    private static void viewStudentReport() {
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        System.out.print("Enter student name: ");
        String name = scanner.nextLine();

        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                System.out.println("\n--- Student Report ---");
                System.out.println(student.getGradeReport());
                return;
            }
        }

        System.out.println("Student not found.");
    }
}
