package com.erp;

import java.util.*;

class Student extends User {
    private int semester;
    private String name;
    private int credits;
    private final List<Course> courses;
    private Map<String, Double> grades;
    private List<Complaint> complaints;
    private List<Course> registeredCourses;
    private List<Course> completedCourses;
    private List<Feedback<?>> feedbacks;

    // Constructor
    public Student(String email, String password) {
        super(email, password);
        this.semester = 1; // Start from semester 1
        this.courses = new ArrayList<>();
        this.grades = new HashMap<>();
        this.complaints = new ArrayList<>();
        this.completedCourses = new ArrayList<>();
        this.registeredCourses = new ArrayList<>();
        this.credits = 0;
    }
    Scanner scanner = new Scanner(System.in);

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getName() {
        return name;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public List<Course> getCompletedCourses() {
        return completedCourses;
    }

    public void setCompletedCourses(List<Course> completedCourses) {
        this.completedCourses = completedCourses;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public int getCredits() {
        return credits;
    }

    public void setRegisteredCourses(List<Course> registeredCourses) {
        this.registeredCourses = registeredCourses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getGrades() {
        return grades;
    }

    public void setGrades(Map<String, Double> grades) {
        this.grades = grades;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public List<Course> getRegisteredCourses() {
        return registeredCourses;
    }

    public void viewCourses(List<Course> allCourses) {
        System.out.println("Available Courses for Semester " + semester + ":");
        for (Course course : allCourses) {
            // Check if the course is available in the current semester
            if (course.getSemester() == semester) {
                System.out.println(course);
            }
        }
    }

    public void registerCourse(List<Course> courses) throws CourseFullException{
        System.out.print("Enter Course Code to Register: ");
        String codeToRegister = scanner.nextLine().trim(); // Read and trim input
        Course selectedCourseToRegister = findCourseByCode(codeToRegister, courses); // Find the course

        if (selectedCourseToRegister != null) {
            // Proceed with registration logic
            if (selectedCourseToRegister.getSemester() != semester) {
                System.out.println("Course is not available in this semester.");
                return;
            }

            if (!hasCompletedPrerequisites(selectedCourseToRegister)) {
                System.out.println("You have not completed the prerequisites for this course.");
                return;
            }

            if (!canRegisterForCourse(selectedCourseToRegister)) {
                System.out.println("You cannot register for this course as it will exceed your credit limit.");
                return;
            }

            if (registeredCourses.contains(selectedCourseToRegister)) {
                System.out.println("You are already registered for this course.");
                return;
            }

            try {
                selectedCourseToRegister.enrollStudent(this);
                registeredCourses.add(selectedCourseToRegister);
                credits += selectedCourseToRegister.getCredits();
                System.out.println("Successfully registered for the course: " + selectedCourseToRegister.getCourseCode());
            } catch (CourseFullException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Course not found.");
        }
    }

    private boolean hasCompletedPrerequisites(Course course) {
        List<Course> prerequisites = course.getPrerequisites();
        return completedCourses.containsAll(prerequisites);
    }

    private boolean canRegisterForCourse(Course course) {
        int totalCredits = getTotalCredits();
        return (totalCredits + course.getCredits()) <= 20;
    }

    public void viewSchedule() {
        System.out.println("Weekly Schedule for Semester " + semester + ":");
        boolean hasCourses = false;

        for (Course course : registeredCourses) {
            if (course.getSemester() == semester) {
                System.out.println(course.getTitle() + " - " + course.getTimings() + " - " + course.getProfessor());
                hasCourses = true;
            }
        }

        if (!hasCourses) {
            System.out.println("No courses registered for the current semester.");
        }
    }


    public void trackProgress() {
        if (grades.isEmpty()) {
            System.out.println("No grades available to calculate SGPA/CGPA.");
            return;
        }

        // Display grades
        System.out.println("Grades for " + name + ":");
        for (Map.Entry<String, Double> entry : grades.entrySet()) {
            System.out.printf("Course: %s, Grade: %.2f\n", entry.getKey(), entry.getValue());
        }

        double totalCredits = 0;
        double totalPoints = 0;

        for (Course course : registeredCourses) {
            if (grades.containsKey(course.getCourseCode())) {
                double grade = grades.get(course.getCourseCode());
                totalCredits += course.getCredits();
                totalPoints += grade * course.getCredits();
            }
        }

        double SGPA = 0;
        if (totalCredits > 0) {
            SGPA = totalPoints / totalCredits;
        }

        double CGPA = 0;
        double cumulativeTotalCredits = getCumulativeCredits();
        double cumulativeTotalPoints = getCumulativePoints();

        if (cumulativeTotalCredits > 0) {
            CGPA = cumulativeTotalPoints / cumulativeTotalCredits;
        }

        System.out.printf("SGPA: %.2f\n", SGPA);
        System.out.printf("CGPA: %.2f\n", CGPA);
    }
    private double getCumulativeCredits() {
        double totalCredits = 0;

        // Include credits from registered courses
        for (Course course : registeredCourses) {
            totalCredits += course.getCredits();
        }

        // Include credits from completed courses
        for (Course course : completedCourses) {
            totalCredits += course.getCredits();
        }

        return totalCredits;
    }

    private double getCumulativePoints() {
        double totalPoints = 0;

        for (Course course : registeredCourses) {
            if (grades.containsKey(course.getCourseCode())) {
                double grade = grades.get(course.getCourseCode());
                totalPoints += grade * course.getCredits();
            }
        }

        for (Course course : completedCourses) {
            if (grades.containsKey(course.getCourseCode())) {
                double grade = grades.get(course.getCourseCode());
                totalPoints += grade * course.getCredits();
            }
        }

        return totalPoints;
    }

    // Drop a course
    public void dropCourse(List<Course> courses) throws DropDeadlinePassedException{
        System.out.print("Enter Course Code to Drop: ");
        String codeToDrop = scanner.nextLine().trim();
        Course selectedCourseToDrop = findCourseByCode(codeToDrop, courses);

        if (selectedCourseToDrop != null) {
            if (registeredCourses.contains(selectedCourseToDrop)) {
                try {
                    if (selectedCourseToDrop.unenrollStudent(this)) {
                        registeredCourses.remove(selectedCourseToDrop);
                        credits -= selectedCourseToDrop.getCredits();
                        System.out.println("Successfully dropped " + selectedCourseToDrop.getTitle());
                    } else {
                        System.out.println("Failed to unenroll from " + selectedCourseToDrop.getTitle());
                    }
                } catch (DropDeadlinePassedException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println("An error occurred while trying to drop the course: " + e.getMessage());
                }
            } else {
                System.out.println("Not registered for " + selectedCourseToDrop.getTitle());
            }
        } else {
            System.out.println("Course not found.");
        }
    }


    public void submitComplaint() {
        System.out.print("Enter complaint description: ");
        String complaintDescription = scanner.nextLine();

        Complaint complaint = new Complaint(complaintDescription);
        complaints.add(complaint);
        System.out.println("Complaint submitted: " + complaintDescription);
    }

    public void viewComplaints() {
        if (complaints.isEmpty()) {
            System.out.println("No complaints submitted.");
        } else {
            System.out.println("Submitted Complaints:");
            for (Complaint complaint : complaints) {
                System.out.println("Complaint: " + complaint.getDescription() + ", Status: " + complaint.getStatus());
            }
        }
    }

    private int getTotalCredits() {
        int totalCredits = 0;
        for (Course course : registeredCourses) {
            totalCredits += course.getCredits();
        }
        return totalCredits;
    }

    public boolean canAdvanceToNextSemester() {
        final int MAXIMUM_CREDITS_REQUIRED = 20;

        for (Course course : registeredCourses) {
            if (course.getSemester() == semester && !isCourseCompleted(course)) {
                return false;
            }
        }

        int totalCredits = 0;
        for (Course course : registeredCourses) {
            if (course.getSemester() == semester && isCourseCompleted(course)) {
                totalCredits += course.getCredits();
            }
        }

        if (totalCredits <= MAXIMUM_CREDITS_REQUIRED) {
            return true;
        } else {
            System.out.println("Cannot advance. Minimum credit requirement not met.");
            return false;
        }
    }

    private boolean isCourseCompleted(Course course) {
        Double grade = grades.get(course.getCourseCode());
        return grade != null && grade >= 4;
    }

    public void advanceToNextSemester() {
        if (canAdvanceToNextSemester()) {
            for (Course course : registeredCourses) {
                if (isCourseCompleted(course)) {
                    completedCourses.add(course);
                }
            }
            registeredCourses.clear();
            semester++;
            credits = 0;
            System.out.println("Advanced to next Semester");
        } else {
            System.out.println("Cannot advance. Ensure all requirements are met.");
        }
    }
    public void submitRating(Course course, int rating) {
        if (rating < 1 || rating > 5) {
            System.out.println("Invalid rating. Please enter a value between 1 and 5.");
        } else {
            Feedback<Integer> feedback = new Feedback<>(this.name, rating);
            course.courseFeedback(feedback);
            System.out.println("Rating submitted successfully.");
        }
    }
    public void provideFeedbackForCourse(List<Course> courses) {
        System.out.print("Enter Course Code to Provide Feedback: ");
        String courseCod = scanner.nextLine();
        Course selectedCourse = findCourseByCode(courseCod, courses);

        if (selectedCourse != null) {
            if (getRegisteredCourses().contains(selectedCourse)) {
                System.out.print("Enter your rating (1-5): ");
                int rating = scanner.nextInt();
                scanner.nextLine();

                if (rating < 1 || rating > 5) {
                    System.out.println("Invalid rating. Please enter a value between 1 and 5.");
                } else {
                    submitRating(selectedCourse, rating);
                    System.out.print("Enter your feedback comment: ");
                    String feedbackComment = scanner.nextLine();
                    submitFeedback(selectedCourse, feedbackComment);

                    System.out.println("Feedback submitted successfully.");
                }
            } else {
                System.out.println("You are not registered in this course, so you cannot provide feedback.");
            }
        } else {
            System.out.println("Course not found.");
        }
    }


    public void submitFeedback(Course course, String comment) {
        Feedback<String> feedback = new Feedback<>(this.name, comment);
        course.courseFeedback(feedback);
        System.out.println("Comment submitted successfully.");
    }
    private static Course findCourseByCode(String code, List<Course> courses) {
        return courses.stream()
                .filter(course -> course.getCourseCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

}
