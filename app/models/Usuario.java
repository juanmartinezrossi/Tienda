package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Usuario extends Model {

    public String email;
    public String password;
    public String username;
    public boolean isAdmin;

    @Lob
    public String historial;

    @ManyToMany
    public List<Product> productsList;


    public Usuario(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

}
