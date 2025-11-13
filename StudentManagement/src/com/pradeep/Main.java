package com.pradeep;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        StudentDAO dao = new StudentDAOImpl();

        while (true) {
            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Add Student");
            System.out.println("2. Update Student");
            System.out.println("3. Delete Student");
            System.out.println("4. View Student by ID");
            System.out.println("5. View All Students");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Branch: ");
                    String branch = sc.nextLine();
                    
                    Student s = new Student(id, name, branch);
                    if (dao.insert(s)) {
                        System.out.println("Student added successfully.");
                    } else {
                        System.out.println("Failed to add student.");
                    }
                    break;

                case 2:
                    System.out.print("Enter ID to update: ");
                    int uid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter New Name: ");
                    String newName = sc.nextLine();
                    System.out.print("Enter New Branch: ");
                    String newBranch = sc.nextLine();

                    Student updateStudent = new Student(uid, newName, newBranch);
                    if (dao.update(updateStudent)) {
                        System.out.println("Student updated successfully.");
                    } else {
                        System.out.println("Failed to update student.");
                    }
                    break;

                case 3:
                    System.out.print("Enter ID to delete: ");
                    int did = sc.nextInt();
                    if (dao.delete(did)) {
                        System.out.println("Student deleted successfully.");
                    } else {
                        System.out.println("No student found with given ID.");
                    }
                    break;

                case 4:
                    System.out.print("Enter ID to search: ");
                    int sid = sc.nextInt();
                    Student stu = dao.getById(sid);
                    if (stu != null) {
                        System.out.println("ID: " + stu.getId());
                        System.out.println("Name: " + stu.getName());
                        System.out.println("Branch: " + stu.getBranch());
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 5:
                    List<Student> list = dao.getAll();
                    if (list.isEmpty()) {
                        System.out.println("No students found.");
                    } else {
                        System.out.println("\n--- All Students ---");
                        for (Student st : list) {
                            System.out.println(st.getId() + " | " + st.getName() + " | " + st.getBranch());
                        }
                    }
                    break;

                case 6:
                    System.out.println("Exiting... Thank you!");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
