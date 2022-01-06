package com.example.socialnetwork.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate birthdate;
    private List<Friend> friendsList;
    private String profilePicture;

    public User(){}

    public User(String firstName, String lastName, String gender, LocalDate birthdate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthdate = birthdate;
        this.friendsList = new ArrayList<>();
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public List<Friend> getFriendsList(){
        return friendsList;
    }

    public void setFriendsList(List<Friend> friends){
        this.friendsList = friends;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthdate=" + birthdate +
                ", friendsList=" + friendsList +
                ", profilePicture='" + profilePicture + '\'' +
                '}';
    }
}