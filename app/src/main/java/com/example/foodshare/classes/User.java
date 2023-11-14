package com.example.foodshare.classes;

import java.io.Serializable;

public class User implements Serializable {
    public int Id;
    public String Name;
    public String Email;
    public String Pass;

    public User() {
    }
    public User(int id, String name, String email, String pass) {
        this.Id = id;
        this.Name = name;
        this.Email = email;
        this.Pass = pass;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
