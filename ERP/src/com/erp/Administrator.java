package com.erp;

import java.time.LocalDate;
import java.util.*;

class Administrator extends User {

    public Administrator(String email) {
        super(email, "fixed_password");
    }


    public void manageCatalog(List<Course> courses) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nCourse Catalog Management:");
            System.out.println("1. View All Courses");
            System.out.println("2. Add New Course");
            System.out.println("3. Remove Course");
            System.out.println("4. Update Course");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // View all courses
                    System.out.println("Course Catalog:");
                    for (Course course : courses) {
                        System.out.println(course);
                    }
                    break;

                case 2:
                    // Add a new course
                    System.out.print("Enter Semester: ");
                    int semester = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Course Code: ");
                    String code = scanner.nextLine();
                    System.out.print("Enter Course Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Course Credits: ");
                    int credits = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Course Timings: ");
                    String timings = scanner.nextLine();

                    // Handle prerequisites
                    System.out.print("Enter number of prerequisites (0 if none): ");
                    int numPrerequisites = scanner.nextInt();
                    scanner.nextLine();

                    List<Course> prerequisites = new ArrayList<>();
                    for (int i = 0; i < numPrerequisites; i++) {
                        System.out.print("Enter prerequisite course code: ");
                        String prereqCode = scanner.nextLine();
                        Course prerequisiteCourse = null;
                        for (Course c : courses) {
                            if (c.getCourseCode().equals(prereqCode)) {
                                prerequisiteCourse = c;
                                break;
                            }
                        }
                        if (prerequisiteCourse != null) {
                            prerequisites.add(prerequisiteCourse);
                        } else {
                            System.out.println("Prerequisite course not found: " + prereqCode);
                        }
                    }

                    // Add new course to the catalog
                    Course newCourse = new Course(code, title,null, credits, semester, prerequisites,timings, LocalDate.of(2025, 5, 1));
                    courses.add(newCourse);
                    System.out.println("Course added successfully.");
                    break;



                case 3:
                    // Remove a course
                    System.out.print("Enter Course Code to Remove: ");
                    String codeToRemove = scanner.nextLine();
                    Course courseToRemove = null;

                    for (Course course : courses) {
                        if (course.getCourseCode().equals(codeToRemove)) {
                            courseToRemove = course;
                            break;
                        }
                    }

                    if (courseToRemove != null) {
                        courses.remove(courseToRemove);
                        System.out.println("Removed course: " + courseToRemove);
                    } else {
                        System.out.println("Course not found.");
                    }
                    break;

                case 4:
                    // Update a course
                    System.out.print("Enter Course Code to Update: ");
                    String codeToUpdate = scanner.nextLine();
                    Course courseToUpdate = null;

                    for (Course course : courses) {
                        if (course.getCourseCode().equals(codeToUpdate)) {
                            courseToUpdate = course;
                            break;
                        }
                    }

                    if (courseToUpdate != null) {
                        System.out.print("Enter New Title (leave empty to keep current): ");
                        String newTitle = scanner.nextLine();
                        if (!newTitle.isEmpty()) {
                            courseToUpdate.setTitle(newTitle);
                        }

                        System.out.print("Enter New Credits (leave empty to keep current): ");
                        String newCreditsStr = scanner.nextLine();
                        if (!newCreditsStr.isEmpty()) {
                            int newCredits = Integer.parseInt(newCreditsStr);
                            courseToUpdate.setCredits(newCredits);
                        }

                        System.out.print("Enter New Timings (leave empty to keep current): ");
                        String newTimings = scanner.nextLine();
                        if (!newTimings.isEmpty()) {
                            courseToUpdate.setTimings(newTimings);
                        }

                        System.out.println("Course updated: " + courseToUpdate);
                    } else {
                        System.out.println("Course not found.");
                    }
                    break;

                case 5:
                    // Exit
                    System.out.println("Exiting Course Catalog Management.");
                    return; // Exit the method

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }


    public void manageStudentRecords(List<Student> students) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nStudent Records Management:");
            System.out.println("1. View All Students");
            System.out.println("2. Add New Student");
            System.out.println("3. Remove Student");
            System.out.println("4. Update Student Record");
            System.out.println("5. Advance to next Semester");

            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // View all students
                    System.out.println("Student Records:");
                    for (Student student : students) {
                        System.out.println("Email: " + student.getEmail() + ", Semester: " + student.getSemester());

                    }
                    break;

                case 2:
                    // Add a new student
                    System.out.print("Enter Student Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Student Password: ");
                    String password = scanner.nextLine();

                    // Create a new student with a default semester of 1
                    Student newStudent = new Student(email, password);
                    students.add(newStudent);
                    System.out.println("New student added: " + newStudent.getEmail());
                    break;

                case 3:
                    // Remove a student
                    System.out.print("Enter Student Email to Remove: ");
                    String emailToRemove = scanner.nextLine();
                    Student studentToRemove = null;

                    for (Student student : students) {
                        if (student.getEmail().equals(emailToRemove)) {
                            studentToRemove = student;
                            break;
                        }
                    }

                    if (studentToRemove != null) {
                        students.remove(studentToRemove);
                        System.out.println("Removed student: " + studentToRemove.getEmail());
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 4:
                    // Update a student record, including personal information, semester, and grades
                    System.out.print("Enter Student Email to Update: ");
                    String emailToUpdate = scanner.nextLine();
                    Student studentToUpdate = null;

                    for (Student student : students) {
                        if (student != null && student.getEmail().equals(emailToUpdate)) {
                            studentToUpdate = student;
                            break;
                        }
                    }
                    if (studentToUpdate != null) {
                        // Personal Information Updates
                        System.out.print("Enter New Name (leave empty to keep current): ");
                        String newName = scanner.nextLine();
                        if (!newName.isEmpty()) {
                            studentToUpdate.setName(newName);
                        }

                        System.out.print("Enter New Email (leave empty to keep current): ");
                        String newEmail = scanner.nextLine();
                        if (!newEmail.isEmpty()) {
                            studentToUpdate.setEmail(newEmail);
                        }

                        // Updating Grades
                        System.out.print("Do you want to update grades? (yes/no): ");
                        String updateGrades = scanner.nextLine();
                        if (updateGrades.equalsIgnoreCase("yes")) {
                            System.out.println("Updating grades for the student...");
                            Map<String, Double> grades = studentToUpdate.getGrades();


                            for (Course course : studentToUpdate.getRegisteredCourses()) {
                                System.out.print("Enter new grade for " + course.getTitle() + " (leave empty to keep current): ");
                                String newGradeStr = scanner.nextLine();

                                if (!newGradeStr.isEmpty()) {
                                    try {
                                        double newGrade = Double.parseDouble(newGradeStr);
                                        grades.put(course.getCourseCode(), newGrade);
                                        System.out.println("Grade for " + course.getTitle() + " updated to " + newGrade);
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid input for grade. Please enter a valid number.");
                                    }
                                }
                            }
                            studentToUpdate.setGrades(grades);
                        }

                        System.out.println("Student record updated: " + studentToUpdate);
                    } else {
                        System.out.println("Student not found with the given email: " + emailToUpdate);
                    }
                    break;

                case 5:
                    System.out.print("Enter Student Email to Update: ");
                    String emailofStudent = scanner.nextLine();
                    Student student = null;

                    for (Student s : students) {
                        if (s.getEmail().equals(emailofStudent)) {
                            student = s;
                            break;
                        }
                    }

                    if (student != null) {
                        System.out.print("Enter New Semester (leave empty to keep current): ");
                        String newSemesterStr = scanner.nextLine();

                        if (!newSemesterStr.isEmpty()) {
                            try {
                                int newSemester = Integer.parseInt(newSemesterStr);

                                if (newSemester > student.getSemester()) {
                                    if (student.canAdvanceToNextSemester()) {
                                        student.advanceToNextSemester();
                                        student.setCredits(0);
                                    } else {
                                        System.out.println("Cannot advance to the next semester. Ensure all requirements are met.");
                                    }
                                } else {
                                    student.setSemester(newSemester);
                                    System.out.println("Semester updated to: " + newSemester);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input for semester. Please enter a valid integer.");
                            }
                        } else {
                            System.out.println("No changes made to the semester.");
                        }
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 6:
                    System.out.println("Exiting Student Records Management.");
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    public void assignProfessors(List<Professor> professors, List<Course> courses) {
        Scanner scanner = new Scanner(System.in);

        if (professors.isEmpty()) {
            System.out.println("No professors available to assign.");
            return;
        }

        if (courses.isEmpty()) {
            System.out.println("No courses available to assign professors.");
            return;
        }

        for (Course course : courses) {
            System.out.println("Assign a professor to " + course.getTitle());

            System.out.println("Available Professors:");
            for (int i = 0; i < professors.size(); i++) {
                System.out.println((i + 1) + ". " + professors.get(i).getEmail());
            }

            System.out.print("Enter the number of the professor to assign: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice >= 1 && choice <= professors.size()) {
                Professor selectedProfessor = professors.get(choice - 1);
                course.setProfessor(selectedProfessor);
                System.out.println("Assigned Professor: " + selectedProfessor.getEmail() + " to " + course.getTitle());
            } else {
                System.out.println("Invalid choice. No professor assigned.");
            }
        }
    }

    public void handleComplaints(List<Complaint> complaints) {
        Scanner scanner = new Scanner(System.in);

        if (complaints.isEmpty()) {
            System.out.println("No complaints to handle.");
            return;
        }

        while (true) {
            System.out.println("Complaints:");
            for (int i = 0; i < complaints.size(); i++) {
                Complaint complaint = complaints.get(i);
                System.out.println((i + 1) + ". " + complaint);
            }

            System.out.println("Enter the number of the complaint to handle (0 to exit):");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) {
                break;
            }

            if (choice < 1 || choice > complaints.size()) {
                System.out.println("Invalid choice. Try again.");
                continue;
            }

            Complaint selectedComplaint = complaints.get(choice - 1);
            System.out.println("Selected Complaint: " + selectedComplaint);

            System.out.println("1. Mark as Resolved");
            System.out.println("2. Return to Complaints List");

            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 1:
                    handleResolveComplaint(selectedComplaint);
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Invalid action. Try again.");
            }
        }

//        scanner.close(); // Close the scanner resource
    }

    private void handleResolveComplaint(Complaint complaint) {
        System.out.println("Resolving complaint: " + complaint.getDescription());

        complaint.setStatus("Resolved");

        System.out.println("Complaint resolved.");
    }


}
