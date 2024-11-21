package com.erp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Course {
    private final String courseCode;
    private String title;
    private Professor professor;
    private int credits;
    private final List<Course> prerequisites; //
    private String timings;
    private final List<Student> enrolledStudents; //
    private String syllabus;
    private int enrollmentLimit;
    private String officeHours;
    private int semester;
    private List<Feedback<?>> feedbacks;
    private TeachingAssistant TA;
    private LocalDate dropDeadline;
    public List<Feedback<?>> getFeedbacks() {
        return feedbacks;
    }

    // Constructor
    public Course(String courseCode, String title, Professor professor, int credits, int semester, List<Course> prerequisites, String timings, LocalDate localDate) {
        this.courseCode = courseCode;
        this.title = title;
        this.professor = professor;
        this.credits = credits;
        this.semester = semester;
        this.prerequisites = prerequisites != null ? new ArrayList<>(prerequisites) : new ArrayList<>();
        this.timings = timings;
        this.enrolledStudents = new ArrayList<>();
        this.syllabus = "Not defined";
        this.enrollmentLimit = 50;
        this.officeHours = "Not set";
        this.feedbacks = new ArrayList<>();
        this.TA = null;
        this.dropDeadline = localDate;
    }

    // Getters
    public String getCourseCode() {
        return courseCode;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDropDeadline() {
        return dropDeadline;
    }

    public Professor getProfessor() {
        return professor;
    }

    public TeachingAssistant getTA() {
        return TA;
    }

    public void setTA(TeachingAssistant TA) {
        this.TA = TA;
    }

    public int getCredits() {
        return credits;
    }

    public List<Course> getPrerequisites() {
        return new ArrayList<>(prerequisites); // Return a copy for encapsulation
    }

    public String getTimings() {
        return timings;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public int getEnrollmentLimit() {
        return enrollmentLimit;
    }

    public String getOfficeHours() {
        return officeHours;
    }

    public int getSemester() {
        return semester;
    }

    public List<Student> getEnrolledStudents() {
        return new ArrayList<>(enrolledStudents); // Return a copy for encapsulation
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public void setEnrollmentLimit(int enrollmentLimit) {
        this.enrollmentLimit = enrollmentLimit;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public void setOfficeHours(String officeHours) {
        this.officeHours = officeHours;
    }

    public void setPrerequisites(String prerequisitesStr, List<Course> allCourses) {
        this.prerequisites.clear();
        if (prerequisitesStr != null && !prerequisitesStr.isEmpty()) {
            for (String code : prerequisitesStr.split(",")) {
                code = code.trim();
                Course prerequisite = findCourseByCode(code, allCourses);
                if (prerequisite != null) {
                    this.prerequisites.add(prerequisite);
                }
            }
        }
    }

    public boolean enrollStudent(Student student) throws CourseFullException {
        if (enrolledStudents.size() >= enrollmentLimit) {
            throw new CourseFullException(title);
        }
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
            return true;
        }
        return false;
    }


    public boolean unenrollStudent(Student student) throws DropDeadlinePassedException {
        if (LocalDate.now().isAfter(dropDeadline)) {
            throw new DropDeadlinePassedException("Cannot drop the course after the deadline of " + dropDeadline);
        }

        if (enrolledStudents.contains(student)) {
            enrolledStudents.remove(student);
            return true;
        } else {
            System.out.println("Student is not enrolled in this course.");
            return false;
        }
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseCode='" + courseCode + '\'' +
                ", title='" + title + '\'' +
                ", professor=" + professor +
                ", credits=" + credits +
                ", prerequisites=" + prerequisitesToString() +
                ", timings='" + timings + '\'' +
                ", syllabus='" + syllabus + '\'' +
                ", enrollmentLimit=" + enrollmentLimit +
                ", officeHours='" + officeHours + '\'' +
                ", semester=" + semester +
                '}';
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseCode.equals(course.courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode);
    }


    private String prerequisitesToString() {
        if (prerequisites.isEmpty()) {
            return "None";
        }
        StringBuilder prereqStr = new StringBuilder();
        for (Course prereq : prerequisites) {
            prereqStr.append(prereq.getTitle()).append(", ");
        }
        return prereqStr.substring(0, prereqStr.length() - 2);
    }

    private Course findCourseByCode(String code, List<Course> allCourses) {
        for (Course course : allCourses) {
            if (course.getCourseCode().equalsIgnoreCase(code)) {
                return course;
            }
        }
        return null;
    }
    public void courseFeedback(Feedback<?> feedback) {
        feedbacks.add(feedback);
    }

    public void displayFeedback() {
        if (feedbacks.isEmpty()) {
            System.out.println("No feedback available for this course.");
        } else {
            for (Feedback<?> feedback : feedbacks) {
                System.out.println(feedback);
            }
        }
    }
}
