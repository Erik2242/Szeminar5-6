package service;

import domain.Grade;
import domain.Homework;
import domain.Student;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;
import validation.GradeValidator;
import validation.HomeworkValidator;
import validation.StudentValidator;
import validation.Validator;

import java.util.Objects;

class ServiceTest {
    private static Service service;
    private static final String STUDENT_ID = "2";
    private static final String NAME = "Jancsi";
    private static final int GROUP = 522;

    @BeforeAll
    static void setUp() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Homework> homeworkValidator = new HomeworkValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "studentsTest.xml");
        HomeworkXMLRepository fileRepository2 = new HomeworkXMLRepository(homeworkValidator, "homeworkTest.xml");
        GradeXMLRepository fileRepository3 = new GradeXMLRepository(gradeValidator, "gradesTest.xml");
        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }


    @Test
    void shouldSaveStudent() {
        if (checkIfStudentExists(STUDENT_ID)) {
            service.deleteStudent(STUDENT_ID);
        }
        service.saveStudent(STUDENT_ID, NAME, GROUP);
        Iterable<Student> allStudents = service.findAllStudents();
        boolean wasSaved = false;
        for (Student student : allStudents) {
            if (Objects.equals(student.getName(), NAME) && Objects.equals(student.getID(), STUDENT_ID) && student.getGroup() == GROUP) {
                wasSaved = true;
                break;
            }
        }
        Assertions.assertTrue(wasSaved);
    }

    @ParameterizedTest
    @CsvSource({"12,homework description1,6,4", "13,homework description2,7,5", "14,homework description3,9,8"})
    public void addHomeworks(String id, String description, int deadline, int startline) {
        service.saveHomework(id, description, deadline, startline);
        Iterable<Homework> homeworks = service.findAllHomework();
        boolean foundHomework = false;
        for (Homework homework : homeworks) {
            if (Objects.equals(homework.getID(), id)) {
                foundHomework = true;
                break;
            }
        }
        Assertions.assertTrue(foundHomework);
    }

    @Test
    void shouldDeleteStudent() {
        if (!checkIfStudentExists(STUDENT_ID)) {
            service.saveStudent(STUDENT_ID, NAME, GROUP);
        }
        Iterable<Student> allStudents = service.findAllStudents();
        service.deleteStudent(STUDENT_ID);
        boolean wasDeleted = true;
        for (Student student : allStudents) {
            if (Objects.equals(student.getID(), STUDENT_ID)) {
                wasDeleted = false;
                break;
            }
        }
        Assertions.assertTrue(wasDeleted);
    }

    @Test
    void deleteNotExistentStudent() {
        if (checkIfStudentExists(STUDENT_ID)) {
            service.deleteStudent(STUDENT_ID);
        }
        int response = service.deleteStudent(STUDENT_ID);
        Assertions.assertNotEquals(1, response);
    }

    @Test
    void saveExistentUser() {
        if (!checkIfStudentExists(STUDENT_ID)) {
            service.saveStudent(STUDENT_ID, NAME, GROUP);
        }
        int resp = service.saveStudent(STUDENT_ID, NAME, GROUP);
        Assertions.assertEquals(0, resp);
    }

    @AfterAll
    static void tearDown() {
        service.deleteHomework("12");
        service.deleteHomework("13");
        service.deleteHomework("14");
    }

    boolean checkIfStudentExists(String userId) {
        Iterable<Student> students = service.findAllStudents();
        for (Student student : students) {
            if (Objects.equals(student.getID(), userId)) {
                return true;
            }
        }
        return false;
    }
}