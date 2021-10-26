package edu.tongji.aising.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * test model
 */
public class Person implements Serializable {


    private String Id;

    private String name;

    private  String password;

    private List<User> userList;

    public String address;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "Id='" + Id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
