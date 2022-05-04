package service;

import domain.Grade;
import domain.Homework;
import domain.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;


import static org.mockito.Mockito.*;

public class ServiceMockTest {
    @Mock
    private static StudentXMLRepository studentXmlRepoMock;
    @Mock
    private static HomeworkXMLRepository homeworkXMLRepositoryMock;
    @Mock
    private static GradeXMLRepository gradeXMLRepositoryMock;
    private static Service service;
    private static final String STUDENT_ID = "2";
    private static final String NAME = "Jancsi";
    private static final int GROUP = 522;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeAll
    public static void setUp() {
        studentXmlRepoMock = mock(StudentXMLRepository.class);
        homeworkXMLRepositoryMock = mock(HomeworkXMLRepository.class);
        gradeXMLRepositoryMock = mock(GradeXMLRepository.class);
        service = new Service(studentXmlRepoMock, homeworkXMLRepositoryMock, gradeXMLRepositoryMock);
    }

    @Test
    void shouldSaveStudent() {
        when(studentXmlRepoMock.save(new Student(STUDENT_ID, NAME, GROUP))).thenReturn(null);
        int result = service.saveStudent(STUDENT_ID, NAME, GROUP);
        Assertions.assertEquals(1, result);
    }

    @Test
    void deleteNotExistentStudent() {
        when(studentXmlRepoMock.delete(STUDENT_ID)).thenReturn(null);
        int result = service.deleteStudent(STUDENT_ID);
        Assertions.assertEquals(0, result);
    }

    @Test
    void shouldSaveHomework() {
        homeworkXMLRepositoryMock.save(new Homework("12", "description", 6, 4));
        verify(homeworkXMLRepositoryMock).save(new Homework("12", "description", 6, 4));
    }
}
