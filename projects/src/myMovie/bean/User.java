package myMovie.bean;

import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public enum gender{MALE,FEMALE};
    private String loginName;
    private String password;
    private gender gender;
    private String phone;
    private double money;

    public User() {
    }

    public User(String loginName, String password, User.gender gender, String phone, double money) {
        this.loginName = loginName;
        this.password = password;
        this.gender = gender;
        this.phone = phone;
        this.money = money;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User.gender getGender() {
        return gender;
    }

    public void setGender(User.gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
