package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Product extends Model {

    public String name;
    public double price;
    public  int stock;
    public int cantidadC;
    public String image;

    @Lob
    public String description;

    @ManyToMany
    public List<Usuario> buyers;

//     @ManyToOne
//     public Compra compraP;


    public Product(String name, double price, String description, int stock, String image){
        //this.buyer = buyer;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.image = image;
    }

}