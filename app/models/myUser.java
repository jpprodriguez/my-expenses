/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import play.db.ebean.Model;

/**
 *
 * @author JP Rodriguez
 */
@Entity
public class myUser extends Model{
    @Id
    private Long id;
    private String user;
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public static void create(myUser user)throws Exception{
        user.save();
    }
    
    public static void update(myUser user) throws Exception{
        user.update();
    }
    
     public static void delete(myUser user)throws Exception{
        user.delete();
    }
    
}
