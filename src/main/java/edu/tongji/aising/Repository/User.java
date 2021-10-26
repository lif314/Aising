package edu.tongji.aising.Repository;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;


@Table(name = "users")
@Entity
public class User implements Serializable {
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    @JsonProperty("id")
    private Integer userId;

    @Column(name = "user_name", nullable = false, length = 20)
    @JsonProperty("name")
    private String userName;

    @Column(name = "user_passwd", nullable = false, length = 30)
    @JsonProperty("password")
    private String userPasswd;

    @Column(name = "user_photo", length = 3000)
    @JsonProperty("face")
    private String userPhoto;

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserPasswd() {
        return userPasswd;
    }

    public void setUserPasswd(String userPasswd) {
        this.userPasswd = userPasswd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPasswd='" + userPasswd + '\'' +
                ", userPhoto='" + userPhoto + '\'' +
                '}';
    }
}