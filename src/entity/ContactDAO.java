/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import core.DB;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
/**
 *
 * @author Gokhan
 */
public class ContactDAO implements DAO<Contact>
{   
    public ContactDAO() {
        
    }
    // Each query creates its own list; no shared mutable list is needed.
    /**
     * Get a single contact entity as a contact object
     * @param id
     * @return 
     */
    @Override
    public Optional<Contact> get(int id) {
        DB db = DB.getInstance();
        String sql = "SELECT * FROM Contact WHERE id = ?";
        // The question-mark placeholder is bound to the requested contact ID.
        try (PreparedStatement stmt = db.getPreparedStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Contact(rs.getInt("id"),
                            rs.getString("firstname"), rs.getString("lastname"),
                            rs.getString("phonenumber")));
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Could not retrieve contact " + id, ex);
        }
    }
    
    /**
     * Get all contact entities as a List
     * @return 
     */
    @Override
    public List<Contact> getAll() {
        DB db = DB.getInstance();
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM Contact ORDER BY id";
        // Query all rows in ID order and convert each row to a Contact object.
        try (PreparedStatement stmt = db.getPreparedStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                contacts.add(new Contact(rs.getInt("id"),
                        rs.getString("firstname"), rs.getString("lastname"),
                        rs.getString("phonenumber")));
            }
            return contacts;
        } catch (SQLException ex) {
            throw new IllegalStateException("Could not list contacts", ex);
        }
    }
    
    /**
     * Insert a contact object into contact table
     * @param contact 
     */
    @Override
    public void insert(Contact contact)
    {
        DB db = DB.getInstance();
        String sql = "INSERT INTO Contact(ID, firstName, lastName, phoneNumber) VALUES (?, ?, ?, ?)";
        // JDBC parameters keep user-entered values separate from SQL syntax.
        try (PreparedStatement stmt = db.getPreparedStatement(sql)) {
            stmt.setInt(1, contact.getID());
            stmt.setString(2, contact.getFirstName());
            stmt.setString(3, contact.getLastName());
            stmt.setString(4, contact.getPhoneNumber());
            int rowInserted = stmt.executeUpdate();
            if (rowInserted > 0) {
                System.out.println("A new contact was inserted successfully!");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Database modification failed", ex);
        }
    }
    
    /**
     * Update a contact entity in database if it exists using a contact object
     * @param contact
     */
    @Override
    public void update(Contact contact) {
        DB db = DB.getInstance();
        String sql = "UPDATE Contact SET firstName=?, lastName=?, phoneNumber=? WHERE id=?";
        // JDBC parameters keep user-entered values separate from SQL syntax.
        try (PreparedStatement stmt = db.getPreparedStatement(sql)) {
            stmt.setString(1, contact.getFirstName());
            stmt.setString(2, contact.getLastName());
            stmt.setString(3, contact.getPhoneNumber());
            stmt.setInt(4, contact.getID());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("An existing contact was updated successfully!");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Database modification failed", ex);
        }
    }
    
    /**
     * Delete a contact from contact table if the entity exists
     * @param contact 
     */
    @Override
    public void delete(Contact contact) {
        DB db = DB.getInstance();
        String sql = "DELETE FROM Contact WHERE ID = ?";
        // JDBC parameters keep user-entered values separate from SQL syntax.
        try (PreparedStatement stmt = db.getPreparedStatement(sql)) {
            stmt.setInt(1, contact.getID());
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("A contact was deleted successfully!");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Database modification failed", ex);
        }
    }
    
    /**
     * Get all column names in a list array
     * @return 
     */
    @Override
    public List<String> getColumnNames() {
        DB db = DB.getInstance();
        List<String> headers = new ArrayList<>();
        // Even an empty ResultSet exposes the column names through metadata.
        String sql = "SELECT * FROM Contact WHERE ID = -1";
        try (PreparedStatement stmt = db.getPreparedStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                headers.add(rsmd.getColumnLabel(i));
            }
            return headers;
        } catch (SQLException ex) {
            throw new IllegalStateException("Could not read table columns", ex);
        }
    }
}
