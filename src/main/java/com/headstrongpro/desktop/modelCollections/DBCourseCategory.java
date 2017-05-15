package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.CourseCategory;
import com.headstrongpro.desktop.modelCollections.util.ActionType;
import com.headstrongpro.desktop.modelCollections.util.IDataAccessObject;
import com.headstrongpro.desktop.modelCollections.util.Synchronizable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**********************************
 * course category model collection
 *********************************/
public class DBCourseCategory extends Synchronizable implements IDataAccessObject<CourseCategory> {

    private DBConnect dbConnect;

    @Override
    public List<CourseCategory> getAll() throws ModelSyncException{
        List<CourseCategory> courseCategories = new ArrayList<>();
        try{
            dbConnect = new DBConnect();
            String getAllCourseCategoriesQuery = "SELECT * FROM s_categories";
            ResultSet ccRS = dbConnect.getFromDataBase(getAllCourseCategoriesQuery);
            while(ccRS.next())
                courseCategories.add(new CourseCategory(ccRS.getInt("id"),
                                                        ccRS.getString("name")));
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load course categories.", e);
        }
        return courseCategories;
    }

    @Override
    public CourseCategory getById(int id) throws ModelSyncException{
        CourseCategory courseCategory = null;
        try{
            dbConnect = new DBConnect();
            String getByIdCourseCategoriesQuery = "SELECT * FROM s_categories WHERE id = " + id + ";";
            ResultSet rs = dbConnect.getFromDataBase(getByIdCourseCategoriesQuery);
            rs.next();
            courseCategory = new CourseCategory(rs.getInt("id"),
                                                rs.getString("name"));
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve object by ID", e);
        }
        return courseCategory;
    }

    @Override
    public CourseCategory create(CourseCategory newCourseCategory) throws ModelSyncException{
        try{
            dbConnect = new DBConnect();
            //language=TSQL
            String createCourseCategoryQuery = "INSERT INTO s_categories(name) VALUES (?);";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(createCourseCategoryQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, newCourseCategory.getName());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newCourseCategory.setId(generatedKeys.getInt(1));
                    logChange("s_categories", newCourseCategory.getId(), ActionType.CREATE);
                } else {
                    throw new ModelSyncException("Creating course category failed. No ID retrieved!");
                }
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not create new course category!", e);
        }
        return newCourseCategory;
    }

    @Override
    public void update(CourseCategory courseCategory) throws ModelSyncException, DatabaseOutOfSyncException {
        if(verifyIntegrity(courseCategory.getId())){
            try{
                dbConnect = new DBConnect();
                //language=TSQL
                String updateCourseCategoryQuery = "UPDATE s_categories SET name=? WHERE id=?;";
                PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(updateCourseCategoryQuery);
                preparedStatement.setString(1, courseCategory.getName());
                preparedStatement.setInt(2, courseCategory.getId());
                dbConnect.uploadSafe(preparedStatement);
                logChange("s_categories", courseCategory.getId(), ActionType.UPDATE);
            } catch (SQLException | ConnectionException e) {
                throw new ModelSyncException("WARNING! Could not update the course category of id [" + courseCategory.getId() + "]!", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    @Override
    public void delete(CourseCategory courseCategory) throws ModelSyncException, DatabaseOutOfSyncException {
        if(verifyIntegrity(courseCategory.getId())){
            try{
                dbConnect = new DBConnect();
                String deleteCourseCategoryQuery = "DELETE FROM s_categories WHERE id=?;";
                PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(deleteCourseCategoryQuery);
                preparedStatement.setInt(1, courseCategory.getId());
                preparedStatement.execute();
                logChange("s_categories", courseCategory.getId(), ActionType.DELETE);
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("Couldn't delete course category of id=" + courseCategory.getId(), e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    @Override
    protected boolean verifyIntegrity(int itemID) throws ModelSyncException {
        return true; //TODO: to be implemented
    }
}
