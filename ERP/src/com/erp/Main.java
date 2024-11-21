package com.erp;

import java.util.*;
import java.time.LocalDate;


public class Main {
    private static List<Complaint> complaints;

    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        List<Professor> professors = new ArrayList<>();
        List<TeachingAssistant> TAs = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        complaints = new ArrayList<>();

        setupSampleData(students, professors, courses, TAs); // Include TAs

        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                System.out.println("\n Welcome to the Course Registration System ");
                System.out.print("Login as (Student/Professor/Administrator/TA) or type 'exit' to quit: ");
                String role = scanner.nextLine().trim().toLowerCase();

                if (role.equals("exit")) {
                    System.out.println("Exiting the application. Goodbye!");
                    break;
                }

                switch (role) {
                    case "student":
                        handleStudentLogin(scanner, students, courses);
                        break;
                    case "professor":
                        handleProfessorLogin(scanner, professors, courses,TAs);
                        break;
                    case "administrator":
                        handleAdministratorLogin(scanner, students, professors, courses, complaints);
                        break;
                    case "ta":
                        handleTAlogin(scanner, TAs, courses);
                        break;
                    default:
                        System.out.println("Invalid role. Please enter Student, Professor, Administrator, TA, or 'exit' to quit.");
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            scanner.close();
            System.out.println("Scanner closed. Application exited.");
        }
    }


    private static void setupSampleData(List<Student> students, List<Professor> professors, List<Course> courses, List<TeachingAssistant> TAs) {// Sample Student
        Student student1 = new Student("student1@gmail.com", "12345");
        students.add(student1);
        Professor professor1 = new Professor("professor1@gmail.com", "123456");
        professors.add(professor1);
        Course LA = new Course("MATH101", "Linear Algebra", professors.get(0), 4, 1, new ArrayList<>(), "Mon/Wed 10-11 AM LHC101", LocalDate.of(2024, 12, 1));
        Course AP = new Course("CSE201", "Advanced Programming", professors.get(0), 4, 2, new ArrayList<>(), "Mon/Wed 10-11 AM LHC102", LocalDate.of(2025, 5, 1));
        courses.add(LA);
        courses.add(AP);
        TeachingAssistant ta1 = new TeachingAssistant("ta1@gmail.com", "12345");
        TAs.add(ta1);

        for (Student student : students) {
            student.setComplaints(complaints);
        }
        for (TeachingAssistant TA : TAs) {
            TA.setComplaints(complaints);
        }
    }

    private static void handleStudentLogin(Scanner scanner, List<Student> students, List<Course> courses) {
        System.out.println("Login/Signup");
        String loginSignup = scanner.nextLine().toLowerCase();

        switch (loginSignup) {
            case "login":
                System.out.print("Email: ");
                String email = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();

                Student loggedInStudent = null;

                // Check for valid login
                try {
                    for (Student student : students) {
                        if (student.getEmail().equals(email) && student.getPassword().equals(password)) {
                            loggedInStudent = student;
                            break;
                        }
                    }

                    if (loggedInStudent == null) {
                        throw new InvalidLoginException(email);
                    }

                    boolean studentSessionActive = true;

                    while (studentSessionActive) {
                        System.out.println("\nStudent Menu:");
                        System.out.println("1. View Available Courses");
                        System.out.println("2. Register for a Course");
                        System.out.println("3. View Schedule");
                        System.out.println("4. Track Academic Progress");
                        System.out.println("5. Drop a Course");
                        System.out.println("6. Submit a Complaint");
                        System.out.println("7. View Complaints");
                        System.out.println("8. Submit Feedback for a Course");
                        System.out.println("9. Logout");

                        int choice = -1;
                        if (scanner.hasNextInt()) {
                            choice = scanner.nextInt();
                            scanner.nextLine();
                        } else {
                            System.out.println("Invalid input. Please enter a number.");
                            scanner.nextLine();
                            continue;
                        }

                        switch (choice) {
                            case 1:
                                loggedInStudent.viewCourses(courses);
                                break;
                            case 2:
                                try {
                                    loggedInStudent.registerCourse(courses);
                                } catch (CourseFullException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case 3:
                                loggedInStudent.viewSchedule();
                                break;
                            case 4:
                                loggedInStudent.trackProgress();
                                break;
                            case 5:
                                try {
                                    loggedInStudent.dropCourse(courses);
                                } catch (DropDeadlinePassedException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case 6:
                                loggedInStudent.submitComplaint();
                                break;
                            case 7:
                                loggedInStudent.viewComplaints();
                                break;
                            case 8:
                                loggedInStudent.provideFeedbackForCourse(courses);
                                break;
                            case 9:
                                studentSessionActive = false;
                                break;
                            default:
                                System.out.println("Invalid choice. Please select a valid option.");
                        }
                    }
                } catch (InvalidLoginException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "signup":
                // Handle signup
                System.out.println("Enter your name: ");
                String nameSignup = scanner.nextLine();
                System.out.println("Create your email address: ");
                String emailSignup = scanner.nextLine().toLowerCase();
                String passwordSignup;
                while (true) {
                    System.out.println("Create your Password: ");
                    passwordSignup = scanner.nextLine();
                    System.out.println("Confirm your Password: ");
                    String passwordSignupCheck = scanner.nextLine();
                    if (passwordSignup.equals(passwordSignupCheck)) {
                        break;
                    } else {
                        System.out.println("Please try again.");
                    }
                }

                students.add(new Student(emailSignup, passwordSignup));
                System.out.println("Signed Up Successfully.");

                handleStudentLogin(scanner, students, courses);
                break;

            default:
                System.out.println("Invalid input. Please try again.");
        }
    }

    private static void handleProfessorLogin(Scanner scanner, List<Professor> professors, List<Course> courses, List<TeachingAssistant> TAs) {

        System.out.println("Login/Signup");
        String loginSignup = scanner.nextLine().trim().toLowerCase();

        switch (loginSignup) {
            case "login":
                System.out.print("Email: ");
                String email = scanner.nextLine().trim();
                System.out.print("Password: ");
                String password = scanner.nextLine().trim();

                // Attempt to find the logged-in professor
                Professor loggedInProfessor = null;
                for (Professor prof : professors) {
                    if (email.equalsIgnoreCase(prof.getEmail()) && password.equals(prof.getPassword())) {
                        loggedInProfessor = prof;
                        break;
                    }
                }

                try {
                    // Check if professor was found
                    if (loggedInProfessor == null) {
                        throw new InvalidLoginException("Invalid credentials. Please check your email and password.");
                    }

                    boolean professorSessionActive = true;

                    while (professorSessionActive) {
                        System.out.println("\nProfessor Menu:");
                        System.out.println("1. View All Courses");
                        System.out.println("2. Manage Courses");
                        System.out.println("3. Update Grades");
                        System.out.println("4. View Students Enrolled in a Course");
                        System.out.println("5. View Feedback for Your Courses");
                        System.out.println("6. Assign TA to a Course");
                        System.out.println("7. View Assigned Courses");
                        System.out.println("8. Logout");

                        int choice = -1;
                        if (scanner.hasNextInt()) {
                            choice = scanner.nextInt();
                            scanner.nextLine(); // Consume the newline character
                        } else {
                            System.out.println("Invalid input. Please enter a number.");
                            scanner.nextLine(); // Clear the invalid input
                            continue;
                        }

                        switch (choice) {
                            case 1:
                                System.out.println("Available Courses:");
                                courses.forEach(System.out::println);
                                break;
                            case 2:
                                loggedInProfessor.manageCourses(courses);
                                break;
                            case 3:
                                loggedInProfessor.updateGrades(courses);
                                break;
                            case 4:
                                loggedInProfessor.viewStudents(loggedInProfessor, courses);
                                break;
                            case 5:
                                loggedInProfessor.viewFeedbackForAssignedCourse(loggedInProfessor, courses);
                                break;
                            case 6:
                                loggedInProfessor.assignTA(courses, TAs); // Assign TA to a course
                                break;
                            case 7:
                                loggedInProfessor.viewAssignedCourses(courses);
                                break;
                            case 8:
                                professorSessionActive = false;
                                System.out.println("Logged out successfully.");
                                break;
                            default:
                                System.out.println("Invalid choice. Please select a valid option.");
                                break;
                        }
                    }
                } catch (InvalidLoginException e) {
                    System.out.println(e.getMessage()); // Print the custom exception message
                }
                break;

            case "signup":
                // Handle signup
                System.out.println("Enter your name: ");
                String nameSignup = scanner.nextLine().trim();
                System.out.println("Create your email address: ");
                String emailSignup = scanner.nextLine().trim().toLowerCase();

                String passwordSignup;
                String passwordSignupCheck;

                do {
                    System.out.println("Create your Password: ");
                    passwordSignup = scanner.nextLine().trim();
                    System.out.println("Confirm your Password: ");
                    passwordSignupCheck = scanner.nextLine().trim();
                    if (!passwordSignup.equals(passwordSignupCheck)) {
                        System.out.println("Passwords don't match. Please try again.");
                    }
                } while (!passwordSignup.equals(passwordSignupCheck));

                professors.add(new Professor(emailSignup, passwordSignup));
                System.out.println("Signed Up Successfully.");

                handleProfessorLogin(scanner, professors, courses, TAs);
                break;

            default:
                System.out.println("Invalid input. Please try again.");
                break;
        }
    }

    private static void handleAdministratorLogin(Scanner scanner, List<Student> students, List<Professor> professors, List<Course> courses, List<Complaint> complaints) {

        String adminEmail = "admin@example.com";
        String adminPassword = "adminpassword";

        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            // Check credentials
            if (!email.equals(adminEmail) || !password.equals(adminPassword)) {
                throw new InvalidLoginException("Invalid credentials. Please check your email and password.");
            }

            Administrator admin = new Administrator(email);
            boolean adminSessionActive = true;

            while (adminSessionActive) {
                System.out.println("\nAdministrator Menu:");
                System.out.println("1. Manage Course Catalog");
                System.out.println("2. Manage Student Records");
                System.out.println("3. Assign Professors");
                System.out.println("4. Handle Complaints");
                System.out.println("5. Logout");

                int choice = -1;
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine();
                    continue;
                }

                switch (choice) {
                    case 1:
                        admin.manageCatalog(courses);
                        break;
                    case 2:
                        admin.manageStudentRecords(students);
                        break;
                    case 3:
                        admin.assignProfessors(professors, courses);
                        break;
                    case 4:
                        admin.handleComplaints(complaints);
                        break;
                    case 5:
                        adminSessionActive = false; // Logout
                        System.out.println("Logged out successfully.");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
            }
        } catch (InvalidLoginException e) {
            System.out.println(e.getMessage()); // Print the custom exception message
        }
    }


    private static void handleTAlogin(Scanner scanner, List<TeachingAssistant> teachingAssistants, List<Course> courses) {
        System.out.print("Would you like to (Login/Signup)? ");
        String loginSignup = scanner.nextLine().toLowerCase();

        if (loginSignup.equals("login")) {
            // TA login
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            try {
                TeachingAssistant loggedInTA = teachingAssistants.stream()
                        .filter(ta -> ta.getEmail().equals(email) && ta.getPassword().equals(password))
                        .findFirst()
                        .orElse(null);

                if (loggedInTA == null) {
                    throw new InvalidLoginException("Invalid credentials. Please check your email and password.");
                }

                boolean taActive = true;

                while (taActive) {
                    System.out.println("\nTeaching Assistant Menu:");
                    System.out.println("1. View Assigned Course");
                    System.out.println("2. Update Grades");
                    System.out.println("3. View Available Courses");
                    System.out.println("4. Register for a Course");
                    System.out.println("5. View Schedule");
                    System.out.println("6. Track Academic Progress");
                    System.out.println("7. Drop a Course");
                    System.out.println("8. Submit a Complaint");
                    System.out.println("9. View Complaints");
                    System.out.println("10. View Enrolled Students in the Course");
                    System.out.println("11. Logout");

                    int choice = -1;
                    if (scanner.hasNextInt()) {
                        choice = scanner.nextInt();
                        scanner.nextLine();
                    } else {
                        System.out.println("Invalid input. Please enter a number.");
                        scanner.nextLine();
                        continue;
                    }

                    switch (choice) {
                        case 1:
                            loggedInTA.viewAssignedCourses();
                            break;
                        case 2:
                            loggedInTA.updateGrades(loggedInTA.getAssignedCourses());
                            break;
                        case 3:
                            loggedInTA.viewCourses(courses);
                            break;
                        case 4:
                            try {
                                loggedInTA.registerCourse(courses);
                            } catch (CourseFullException e) {
                                System.out.println(e.getMessage());
                            }

                            break;
                        case 5:
                            loggedInTA.viewSchedule();
                            break;
                        case 6:
                            loggedInTA.trackProgress();
                            break;
                        case 7:
                            try {
                                loggedInTA.dropCourse(courses);
                            } catch (DropDeadlinePassedException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        case 8:
                            loggedInTA.submitComplaint();
                            break;
                        case 9:
                            loggedInTA.viewComplaints();
                            break;
                        case 10:
                            loggedInTA.viewstudents(courses);
                            break;
                        case 11:
                            taActive = false;
                            System.out.println("You have been logged out successfully.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please select a valid option.");
                            break;
                    }
                }
            } catch (InvalidLoginException e) {
                System.out.println(e.getMessage());
            }
        } else if (loginSignup.equals("signup")) {
            // Handle signup process
            System.out.print("Enter your full name: ");
            String nameSignup = scanner.nextLine();
            System.out.print("Enter a new email address: ");
            String emailSignup = scanner.nextLine().toLowerCase();

            String passwordSignup;
            String passwordSignupCheck;
            do {
                System.out.print("Create your Password: ");
                passwordSignup = scanner.nextLine();
                System.out.print("Confirm your Password: ");
                passwordSignupCheck = scanner.nextLine();
                if (!passwordSignup.equals(passwordSignupCheck)) {
                    System.out.println("Passwords don't match. Please try again.");
                }
            } while (!passwordSignup.equals(passwordSignupCheck));

            teachingAssistants.add(new TeachingAssistant(emailSignup, passwordSignup));
            System.out.println("Signup successful! Welcome aboard, " + nameSignup + "!");

            handleTAlogin(scanner, teachingAssistants, courses);
        } else {
            System.out.println("Invalid option. Please enter 'Login' or 'Signup'.");
        }
    }


}