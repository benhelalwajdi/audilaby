package com.audiolaby.persistence.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.androidannotations.annotations.EBean;

import java.io.Serializable;
import java.util.List;

@Table(name = "user")
@EBean
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends Model implements Serializable {
    @Column(name = "user_id")
    private String user_id;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "image")
    private String image;
    @Column(name = "country")
    private String country;
    @Column(name = "login_type")
    private String login_type;
    @Column(name = "gender")
    private String gender;
//    @Column(name = "birthday")
//    private Long birthday;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getFullName() {
        return firstName+" "+lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

//    public Long getBirthday() {
//        return birthday;
//    }
//
//    public void setBirthday(Long birthday) {
//        this.birthday = birthday;
//    }
}
