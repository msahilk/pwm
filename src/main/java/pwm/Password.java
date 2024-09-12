package pwm;

import java.sql.Date;
import java.sql.Time;

import static pwm.PasswordManager.encrypt;

public class Password {


    private int id;
    private String website;
    private String username;
    private String password_encrypted;
    private Time time;
    private Date date;

    public void setTime(Time time) {
        this.time = time;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public Password(String website, String username, String password_encrypted,Date date, Time time, int id) {
        this.website = website;
        this.username = username;
        this.password_encrypted = encrypt(password_encrypted);
        this.date = date;
        this.time = time;
        this.id=id;
    }

    public Password( String website, String username, String password_encrypted){

        this.website = website;
        this.username = username;
        this.password_encrypted = password_encrypted;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword_encrypted(String password_encrypted) {
        this.password_encrypted = encrypt(password_encrypted);
    }

    public String getWebsite() {
        return website;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword_encrypted() {
        return password_encrypted;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime(){
        return time;
    }
    public int getid(){
        return id;
    }
}
