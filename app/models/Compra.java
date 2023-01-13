package models;

import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Compra extends Model {

    public  int cantidadComprada;
    public  String soldDate;


    @ManyToOne
    public Usuario UserCompra;
    @ManyToOne
    public Product ProductsCompra;


    public Compra(int cantidadComprada, String soldDate){
        this.cantidadComprada = cantidadComprada;
        this.soldDate = soldDate;

    }

}