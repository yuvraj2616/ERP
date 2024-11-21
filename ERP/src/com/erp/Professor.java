package com.erp;

import java.util.*;

public class Professor extends User {
    private final List<Course> courses;
    private List<Course> assignedCourses;

    public List<Course> getCourses() {
        return courses;
    }

    public Professor(String email, String password) {
        super(email, password);
        this.courses = new ArrayList<>();
        this.assignedCourses = new ArrayList<>();
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }
    Scanner scanner = new Scanner(System.in);

    public List<Course> getAssignedCourses() {
        return assignedCourses;
    }

    public void setAssignedCourses(List<Course> assignedCourses) {
        this.assignedCourses = assignedCourses;
    }
    public void assignCourse(Course course) {
        assignedCourses.add(course);
    }

    public void manageCourses(List<Course> allCourses) {
        boolean keepRunning = true;

        while (keepRunning) {
            System.out.print("Enter Course Code to Update: ");
            String codeToUpdate = scanner.nextLine();
            Course courseToUpdate = findCourseByCode(codeToUpdate, allCourses);

            if (courseToUpdate != null) {
                System.out.print("Enter New Title (leave blank to keep current): ");
                String newTitle = scanner.nextLine();
                System.out.print("Enter New Credits (leave blank to keep current): ");
                String newCredits = scanner.nextLine();
                System.out.print("Enter New Timings (leave blank to keep current): ");
                String newTimings = scanner.nextLine();
                System.out.print("Enter New Syllabus (leave blank to keep current): ");
                String newSyllabus = scanner.nextLine();
                System.out.print("Enter New Prerequisites (comma-separated codes, leave blank to keep current): ");
                String newPrerequisites = scanner.nextLine();
                System.out.print("Enter New Enrollment Limit (leave blank to keep current): ");
                String newEnrollmentLimit = scanner.nextLine();
                System.out.print("Enter Office Hours (leave blank to keep current): ");
                String newOfficeHours = scanner.nextLine();

                if (!newTitle.isEmpty()) courseToUpdate.setTitle(newTitle);
                if (!newCredits.isEmpty()) courseToUpdate.setCredits(Integer.parseInt(newCredits));
                if (!newTimings.isEmpty()) courseToUpdate.setTimings(newTimings);
                if (!newSyllabus.isEmpty()) courseToUpdate.setSyllabus(newSyllabus);
                if (!newPrerequisites.isEmpty())
                    courseToUpdate.setPrerequisites(newPrerequisites, allCourses);
                if (!newEnrollmentLimit.isEmpty())
                    courseToUpdate.setEnrollmentLimit(Integer.parseInt(newEnrollmentLimit));
                if (!newOfficeHours.isEmpty()) courseToUpdate.setOfficeHours(newOfficeHours);

                System.out.println("Course updated successfully.");
            } else {
                System.out.println("Course not found.");
            }

            System.out.print("Do you want to update another course? (yes/no): ");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("yes")) {
                keepRunning = false;
            }
        }
    }

    private Course findCourseByCode(String code, List<Course> courses) {
        for (Course course : courses) {
            if (course.getCourseCode().equals(code)) {
                return course;
            }
        }
        return null;
    }

    public void viewStudents(Professor professor, List<Course> courses) {
        boolean foundCourses = false;

        for (Course course : courses) {

            if (course.getProfessor().equals(professor)) {
                foundCourses = true;
                System.out.println("Course: " + course.getTitle() + " (" + course.getCourseCode() + ")");

                List<Student> enrolledStudents = course.getEnrolledStudents();

                if (enrolledStudents.isEmpty()) {
                    System.out.println("No students are currently enrolled in this course.");
                } else {
                    System.out.println("Enrolled Students:");
                    for (Student student : enrolledStudents) {
                        System.out.println("Name: " + student.getName() + ", Email: " + student.getEmail());
                    }
                }
                System.out.println();
            }
        }

        if (!foundCourses) {
            System.out.println("The professor is not teaching any courses currently.");
        }
    }

    public void viewAssignedCourses(List<Course> allCourses) {
        List<Course> assignedCourses = new ArrayList<>();

        for (Course course : allCourses) {
            if (course.getProfessor().equals(this)) {
                assignedCourses.add(course);
            }
        }

        if (assignedCourses.isEmpty()) {
            System.out.println("The professor is not assigned to any courses.");
        } else {
            System.out.println("Courses assigned to Professor " + getEmail() + ":");
            for (Course course : assignedCourses) {
                System.out.println("Course Code: " + course.getCourseCode() +
                        ", Title: " + course.getTitle() +
                        ", Credits: " + course.getCredits() +
                        ", Enrollment Limit: " + course.getEnrollmentLimit());
            }
        }
    }
    public void viewFeedback(Course course) {
        System.out.println("Feedback for course: " + course.getCourseCode());
        course.displayFeedback();
    }
    public void updateGrades(List<Course> courses) {
        List<Course> assignedCourses = new ArrayList<>();

        for (Course course : courses) {
            if (course.getProfessor().equals(this)) {
                assignedCourses.add(course);
            }
        }

        System.out.print("Enter the course code to update/view grades: ");
        String courseCode = scanner.nextLine().trim();
        Course selectedCourse = null;

        for (Course course : assignedCourses) {
            if (course.getCourseCode().equalsIgnoreCase(courseCode)) {
                selectedCourse = course;
                break;
            }
        }

        if (selectedCourse == null) {
            System.out.println("Invalid course code or course not assigned.");
            return;
        }

        List<Student> registeredStudents = selectedCourse.getEnrolledStudents();

        if (registeredStudents.isEmpty()) {
            System.out.println("No students are in this course.");
            return;
        }

        System.out.println("Students enrolled in the course:");
        for (Student student : registeredStudents) {
            System.out.println(student.getName() + " (" + student.getEmail() + ")");
        }

        System.out.print("Enter the student's email to update/view grades: ");
        String studentEmail = scanner.nextLine().trim();
        Student studentToUpdate = null;

        for (Student student : registeredStudents) {
            if (student.getEmail().equalsIgnoreCase(studentEmail)) {
                studentToUpdate = student;
                break;
            }
        }

        if (studentToUpdate == null) {
            System.out.println("Student not found in this course.");
            return;
        }

        System.out.print("Do you want to update grades? (yes/no): ");
        String updateGrades = scanner.nextLine().trim();
        if (updateGrades.equalsIgnoreCase("yes")) {
            System.out.println("Updating grades .....");
            Map<String, Double> grades = studentToUpdate.getGrades();

            System.out.print("Enter new grade for " + selectedCourse.getTitle() + " (leave blank to keep current): ");
            String newGradeStr = scanner.nextLine().trim();

            if (!newGradeStr.isEmpty()) {
                try {
                    double newGrade = Double.parseDouble(newGradeStr);
                    grades.put(selectedCourse.getCourseCode(), newGrade);
                    System.out.println("Grade for " + selectedCourse.getTitle() + " updated to " + newGrade);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }

            studentToUpdate.setGrades(grades);
            System.out.println("Student record updated: " + studentToUpdate.getName());
        }

        System.out.print("do you want to view the grades? (yes/no): ");
        String viewGrades = scanner.nextLine().trim();

        if (viewGrades.equalsIgnoreCase("yes")) {
            Map<String, Double> viewGradesMap = studentToUpdate.getGrades();
            String courseTitle = selectedCourse.getTitle();
            String courseCodeSelected = selectedCourse.getCourseCode();

            if (viewGradesMap.containsKey(courseCodeSelected)) {
                double grade = viewGradesMap.get(courseCodeSelected);
                System.out.println("Course: " + courseTitle + " (" + courseCodeSelected + ") - Grade: " + grade);
            } else {
                System.out.println("Course: " + courseTitle + " (" + courseCodeSelected + ") - Grade not yet assigned.");
            }
        }
    }



    public void assignTA(List<Course> courses, List<TeachingAssistant> teachingAssistants) {
        List<Course> assignedCourses = new ArrayList<>();

        for (Course course : courses) {
            if (course.getProfessor().equals(this)) {
                assignedCourses.add(course);
            }
        }

        if (assignedCourses.isEmpty()) {
            System.out.println("No courses assigned to any TA.");
            return;
        }

        System.out.println("Courses you are assigned to:");
        for (Course course : assignedCourses) {
            System.out.println(course.getCourseCode() + ": " + course.getTitle());
        }

        System.out.print("Enter the courseCode of the Course to assign a TA: ");
        String courseCode = scanner.nextLine();

        Course selectedCourse = findCourseByCode(courseCode, assignedCourses);
        if (selectedCourse == null) {
            System.out.println("Invalid course code.");
            return;
        }

        System.out.println("Available TAs:");
        for (TeachingAssistant TA : teachingAssistants) {
            System.out.println(TA.getEmail());
        }

        System.out.print("Enter the email of the TA to assign: ");
        String TAEmail = scanner.nextLine();

        TeachingAssistant selectedTA = findTA(TAEmail, teachingAssistants);
        if (selectedTA == null) {
            System.out.println("Invalid TA email.");
            return;
        }

        selectedCourse.setTA(selectedTA);
        selectedTA.assignCourse(selectedCourse);
        System.out.println("Teaching Assistant " + selectedTA.getEmail() + " has been assigned to " + selectedCourse.getTitle() + ".");
    }

    private TeachingAssistant findTA(String email, List<TeachingAssistant> teachingAssistants) {
        for (TeachingAssistant TA : teachingAssistants) {
            if (TA.getEmail().equalsIgnoreCase(email)) {
                return TA;
            }
        }
        return null;
    }

    public void viewFeedbackForAssignedCourse(Professor loggedInProfessor, List<Course> courses) {
        List<Course> assignedCourses = new ArrayList<>();
        for (Course course : courses) {
            if (course.getProfessor().equals(loggedInProfessor)) {
                assignedCourses.add(course);
            }
        }

        if (assignedCourses.isEmpty()) {
            System.out.println("No courses assigned to you!.");
            return;
        }

        System.out.print("Enter the course code to view the feedback for. ");
        String courseCode = scanner.nextLine().trim();

        Course courseToView = null;
        for (Course course : assignedCourses) {
            if (course.getCourseCode().equalsIgnoreCase(courseCode)) {
                courseToView = course;
                break;
            }
        }

        if (courseToView == null) {
            System.out.println("Course not found or you are not assigned to this course.");
        } else {
            loggedInProfessor.viewFeedback(courseToView);
        }
    }

}
