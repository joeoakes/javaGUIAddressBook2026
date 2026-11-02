/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;
/**
 *
 * @author Gokhan
 */
/** A simple entity representing one row in the CONTACT database table. */
public class Contact 
{
    // Entity fields map to ID, FIRSTNAME, LASTNAME and PHONENUMBER columns.
    private int ID;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    
    public Contact(int ID, String firstName, String lastName, String phoneNumber)
    {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public int getID() {
        return ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Contact{" + "ID=" + ID + ", firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber=" + phoneNumber + '}';
    }
}
