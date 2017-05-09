package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.model.Course;
import com.headstrongpro.desktop.model.CourseCategory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * course category tests
 */
public class DBCourseCategoryTest {
    private IDataAccessObject<CourseCategory> ccDAO;

    @Before
    public void setUp() throws Exception {
        CollectionFactory<CourseCategory> collectionFactory = new CollectionFactory<>();
        ccDAO = collectionFactory.getCollection(CollectionType.COURSE_CATEGORY);
    }

    @Test
    public void getAll() throws Exception {
        List<CourseCategory> courseCategories = ccDAO.getAll();

        assertNotNull("Course categories object is null", courseCategories);
        assertNotEquals("Course categories table empty", 0, courseCategories);
    }

    @Test
    public void getById() throws Exception {
        CourseCategory cc = ccDAO.getById(1);

        assertNotNull("Course category object is null", cc);
    }

    @Test
    public void create() throws Exception {
        //todo
    }

    @Test
    public void update() throws Exception {
        //todo
    }

    @Test
    public void delete() throws Exception {
        //todo
    }

}