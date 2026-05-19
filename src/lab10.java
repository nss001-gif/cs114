import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Course {
    private String courseCode;
    private String courseTitle;
    private int creditHours;
    private int maxCapacity;
    private int numEnrolled;

    public Course(String courseCode, String courseTitle, int creditHours, int maxCapacity) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.creditHours = creditHours;
        this.maxCapacity = maxCapacity;
        this.numEnrolled = 0;
    }

    public String getCourseCode() { return courseCode; }
    public String getCourseTitle() { return courseTitle; }
    public int getCreditHours() { return creditHours; }
    public int getMaxCapacity() { return maxCapacity; }
    public int getNumEnrolled() { return numEnrolled; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public boolean isAvailable() { return numEnrolled < maxCapacity; }
    public void incrementEnrolled() { numEnrolled++; }
    public void decrementEnrolled() { numEnrolled--; }

    @Override
    public String toString() {
        return courseCode + " - " + courseTitle + " (" + creditHours + " cr) ["
                + numEnrolled + "/" + maxCapacity + " enrolled]";
    }
}

class Student {
    private String fullName;
    private String studentId;
    private double gradePointAverage;
    private List<Course> enrolledCourses;

    public Student(String fullName, String studentId, double gradePointAverage) {
        this.fullName = fullName;
        this.studentId = studentId;
        this.gradePointAverage = gradePointAverage;
        this.enrolledCourses = new ArrayList<>();
    }

    public String getFullName() { return fullName; }
    public String getStudentId() { return studentId; }
    public double getGradePointAverage() { return gradePointAverage; }
    public List<Course> getEnrolledCourses() { return enrolledCourses; }

    public void enroll(Course targetCourse) {
        for (Course currentCourse : enrolledCourses) {
            if (currentCourse.getCourseCode().equals(targetCourse.getCourseCode())) {
                throw new IllegalStateException(
                        fullName + " is already enrolled in " + targetCourse.getCourseCode());
            }
        }
        if (!targetCourse.isAvailable()) {
            throw new IllegalArgumentException(
                    "Error: " + targetCourse.getCourseCode() + " is at full capacity. Could not enroll " + fullName + ".");
        }
        enrolledCourses.add(targetCourse);
    }

    public void drop(String targetCode) {
        Iterator<Course> courseIterator = enrolledCourses.iterator();
        while (courseIterator.hasNext()) {
            Course currentCourse = courseIterator.next();
            if (currentCourse.getCourseCode().equals(targetCode)) {
                courseIterator.remove();
                currentCourse.decrementEnrolled();
                return;
            }
        }
        throw new IllegalArgumentException(fullName + " is not enrolled in course: " + targetCode);
    }

    public int getTotalCredits() {
        int totalCredits = 0;
        for (Course currentCourse : enrolledCourses) {
            totalCredits += currentCourse.getCreditHours();
        }
        return totalCredits;
    }

    @Override
    public String toString() {
        StringBuilder studentInfo = new StringBuilder();
        studentInfo.append("Student: ").append(fullName)
                .append(" (ID: ").append(studentId).append(")")
                .append(" | GPA: ").append(gradePointAverage)
                .append(" | Credits: ").append(getTotalCredits()).append("\n");
        for (Course currentCourse : enrolledCourses) {
            studentInfo.append("  - ").append(currentCourse.getCourseCode())
                    .append(": ").append(currentCourse.getCourseTitle())
                    .append(" (").append(currentCourse.getCreditHours()).append(" cr)\n");
        }
        return studentInfo.toString();
    }
}

class Registrar {
    private List<Course> availableCourses;
    private List<Student> registeredStudents;

    public Registrar() {
        this.availableCourses = new ArrayList<>();
        this.registeredStudents = new ArrayList<>();
    }

    public void addCourse(Course newCourse) { availableCourses.add(newCourse); }
    public void addStudent(Student newStudent) { registeredStudents.add(newStudent); }

    public void register(Student targetStudent, Course targetCourse) {
        targetStudent.enroll(targetCourse);
        targetCourse.incrementEnrolled();
    }

    public Course findCourse(String targetCode) {
        for (Course currentCourse : availableCourses) {
            if (currentCourse.getCourseCode().equals(targetCode)) return currentCourse;
        }
        return null;
    }

    public void printRoster(String targetCode) {
        Course targetCourse = findCourse(targetCode);
        if (targetCourse == null) {
            System.out.println("Course not found: " + targetCode);
            return;
        }
        System.out.println(targetCourse.getCourseCode() + " - " + targetCourse.getCourseTitle()
                + " [" + targetCourse.getNumEnrolled() + "/" + targetCourse.getMaxCapacity() + "]");
        StringBuilder rosterLine = new StringBuilder("  ");
        boolean firstStudent = true;
        for (Student currentStudent : registeredStudents) {
            for (Course currentCourse : currentStudent.getEnrolledCourses()) {
                if (currentCourse.getCourseCode().equals(targetCode)) {
                    if (!firstStudent) rosterLine.append(" | ");
                    rosterLine.append(currentStudent.getFullName())
                            .append(" (").append(currentStudent.getStudentId()).append(")");
                    firstStudent = false;
                }
            }
        }
        System.out.println(rosterLine.toString());
    }
}

public class lab10 {
    public static void main(String[] args) {

        Registrar universityRegistrar = new Registrar();

        Course introProgramming = new Course("CS101", "Intro to Programming", 3, 25);
        Course calculusOne = new Course("MATH201", "Calculus I", 3, 30);
        Course technicalWriting = new Course("ENG102", "Technical Writing", 3, 20);

        universityRegistrar.addCourse(introProgramming);
        universityRegistrar.addCourse(calculusOne);
        universityRegistrar.addCourse(technicalWriting);

        Student studentAlice = new Student("Alice", "S001", 3.7);
        Student studentBob = new Student("Bob", "S002", 3.2);
        Student studentCarol = new Student("Carol", "S003", 3.5);
        Student studentDave = new Student("Dave", "S004", 2.9);

        universityRegistrar.addStudent(studentAlice);
        universityRegistrar.addStudent(studentBob);
        universityRegistrar.addStudent(studentCarol);
        universityRegistrar.addStudent(studentDave);

        universityRegistrar.register(studentAlice, introProgramming);
        universityRegistrar.register(studentAlice, calculusOne);
        universityRegistrar.register(studentAlice, technicalWriting);
        universityRegistrar.register(studentBob, introProgramming);
        universityRegistrar.register(studentBob, calculusOne);
        universityRegistrar.register(studentCarol, introProgramming);
        universityRegistrar.register(studentCarol, technicalWriting);
        universityRegistrar.register(studentDave, calculusOne);

        System.out.println("// After enrollments:");
        System.out.print(studentAlice.toString());

        System.out.println("\n// Dropping Dave from Calculus I:");
        studentDave.drop("MATH201");
        System.out.println("Dave successfully dropped MATH201.");

        introProgramming.setMaxCapacity(introProgramming.getNumEnrolled());

        System.out.println("\n// Attempt to enroll in full course:");
        try {
            universityRegistrar.register(studentDave, introProgramming);
        } catch (IllegalArgumentException exceptionMessage) {
            System.out.println(exceptionMessage.getMessage());
        }

        System.out.println("\n// Roster for CS101:");
        universityRegistrar.printRoster("CS101");
    }
}