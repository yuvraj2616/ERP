package com.erp;

import java.util.*;

public class TeachingAssistant extends Student {
    private List<Course> assignedCourses;

    public TeachingAssistant(String email, String password) {
        super(email, password);
        this.assignedCourses = new ArrayList<>();
    }

    public void viewAssignedCourses() {
        if (assignedCourses.isEmpty()) {
            System.out.println("No assigned courses.");
        } else {
            for (Course course : assignedCourses) {
                System.out.println("Assigned Course: " + course.getCourseCode());
            }
        }
    }

    public void viewstudents(List<Course> courses) {
        boolean foundCourses = false;

        for (Course course : assignedCourses) {
            if (course.getTA().equals(this)) {
                foundCourses = true;
                System.out.println("Course: " + course.getTitle() + " (" + course.getCourseCode() + ")");

                List<Student> enrolledStudents = course.getEnrolledStudents();

                if (enrolledStudents.isEmpty()) {
                    System.out.println("No students are currently in this course.");
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
            System.out.println("You haven't been assigned to any courses currently.");
        }
    }

    public void updateGrades(List<Course> courses) {
        List<Course> assignedCourses = new ArrayList<>();

        for (Course course : courses) {
            if (course.getTA().equals(this)) {
                assignedCourses.add(course);
            }
        }

        System.out.print("Enter the coursCode to update/view grades: ");
        String courseCode = scanner.nextLine().trim();
        Course selectedCourse = null;

        for (Course course : assignedCourses) {
            if (course.getCourseCode().equalsIgnoreCase(courseCode)) {
                selectedCourse = course;
                break;
            }
        }

        if (selectedCourse == null) {
            System.out.println("Invalid courseCode or course not assigned.");
            return;
        }

        List<Student> registeredStudents = selectedCourse.getEnrolledStudents();

        if (registeredStudents.isEmpty()) {
            System.out.println("No students are in this course.");
            return;
        }

        System.out.println("Students in the course:");
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

    public void assignCourse(Course course) {
        assignedCourses.add(course);
    }

    public List<Course> getAssignedCourses() {
        return this.assignedCourses;
    }
}
