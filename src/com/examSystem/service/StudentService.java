package com.examSystem.service;

import com.examSystem.model.Student;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class for Student operations: registration, authentication.
 */
public class StudentService {

    // In-memory student database (studentId -> Student)
    private Map<String, Student> studentDatabase;

    // Constructor
    public StudentService() {
        studentDatabase = new HashMap<>();
        loadDefaultStudents();
    }

    /**
     * Pre-loads demo student accounts.
     */
    private void loadDefaultStudents() {
        registerStudent(new Student("STU001", "Alice Johnson", "alice123"));
        registerStudent(new Student("STU002", "Bob Smith", "bob456"));
        registerStudent(new Student("STU003", "Carol White", "carol789"));
        registerStudent(new Student("STU004", "David Brown", "david000"));
        registerStudent(new Student("ADMIN", "Admin User", "admin"));
    }

    /**
     * Registers a new student.
     */
    public boolean registerStudent(Student student) {
        if (studentDatabase.containsKey(student.getStudentId())) {
            return false; // Already exists
        }
        studentDatabase.put(student.getStudentId(), student);
        return true;
    }

    /**
     * Authenticates a student by ID and password.
     * @return the Student object if authenticated, null otherwise.
     */
    public Student authenticate(String studentId, String password) {
        Student student = studentDatabase.get(studentId);
        if (student != null && student.getPassword().equals(password)) {
            return student;
        }
        return null;
    }

    /**
     * Returns total registered students count.
     */
    public int getTotalStudents() {
        return studentDatabase.size();
    }

    /**
     * Displays all registered students.
     */
    public void listAllStudents() {
        System.out.println("\n  Registered Students:");
        System.out.println("  ─────────────────────────────");
        for (Student s : studentDatabase.values()) {
            System.out.printf("  ID: %-10s | Name: %s%n", s.getStudentId(), s.getName());
        }
        System.out.println("  ─────────────────────────────");
    }
}
