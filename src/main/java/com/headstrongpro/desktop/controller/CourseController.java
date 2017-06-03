package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.DbLayer.DBCourse;
import com.headstrongpro.desktop.DbLayer.DBResources;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * c.c.catch.
 */
public class CourseController implements Refreshable, IContentController<Course> {

    private DBCourse dbCourse;
    private DBResources dbResources;
    private List<Course> courses;

    public CourseController() {
        dbCourse = new DBCourse();
        dbResources = new DBResources();
        courses = new ArrayList<>();
    }

    @Concurrent
    @Override
    public void refresh() throws ModelSyncException {
        courses = dbCourse.getAll();
    }

    @Override
    public ObservableList<Course> getAll() throws ModelSyncException {
        refresh();
        /*courses.forEach(e -> {
            try {
                e.setResources(dbResources.getByCourseID(e.getId()));
            } catch (ModelSyncException e1) {
                e1.printStackTrace();
            }
        });*/
        return FXCollections.observableArrayList(courses);
    }

    @Override
    public Course createNew(Course course) throws ModelSyncException {
        if (course == null) throw new IllegalArgumentException("Cannot be null");
        return dbCourse.persist(course);
    }

    @Override
    public void edit(Course course) throws DatabaseOutOfSyncException, ModelSyncException {
        dbCourse.update(course);
    }

    @Override
    public void delete(Course course) throws DatabaseOutOfSyncException, ModelSyncException {
        if (course == null) throw new IllegalArgumentException("Cannot be nullu");
        dbCourse.delete(course);
    }

    @Override
    public Course getByID(int id) throws ModelSyncException {
        return dbCourse.getById(id);
    }

    @Override
    public ObservableList<Course> searchByPhrase(String phrase) {
        return FXCollections.observableArrayList(courses.stream().filter(
                e -> e.getName().toLowerCase().contains(phrase.toLowerCase())
        ).collect(Collectors.toList()));
    }
}
