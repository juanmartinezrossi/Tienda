package controllers;

import play.mvc.*;
import java.util.*;
import models.*;
import play.data.validation.*;
import play.Logger;
import play.mvc.results.RenderText;

import java.util.regex.*;

//import sun.util.locale.provider.BaseLocaleDataMetaInfo;

import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Application extends Controller {

    public static boolean DBini = false;
    public static ArrayList<Product> carrito = new ArrayList<Product>();
    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    public static boolean OfertaAplicada = false;


    //Pone el Usuario en sesion para ser usado con HTML
    @Before
    static void addUser() {
        Usuario user = connected();
        if(user != null) {
            renderArgs.put("Usuario", user);
        }
    }

    //Comprueba si hay un usuario ya loggeado
    static Usuario connected() {

        if(renderArgs.get("user") != null) {
            return renderArgs.get("user", Usuario.class);
        }
        String username = session.get("user");
        if(username != null) {
            return Usuario.find("byUsername", username).first();
        }
        return null;
    }

    //Comprueba si hay que hacer un Render del Index o de la pagina Principal
    public static void index() {
        if (DBini != true){
            iniDB();
        }
        if(connected() != null) {
            String username = session.get("user");
            Tienda();
        }
        render();
    }


    //Inizializa la base de datos
    public  static void iniDB() {

        //public User(String email, String password, String username)
        new Usuario("juan@gmail.com", "juan", "juan").save();
        new Usuario("david@gmail.com", "david" , "david").save();
        new Usuario("dolors@gmail.com", "dolors" , "dolors").save();
        new Usuario("Admin","Admin","Admin").save();

        //Product(String name, double price, String description, int stock, String image)
        new Product("Yerba Mate",
        5,

        "<p> Producto en Tienda desde ‏ : ‎ 9 de Julio del 1810</p>"+
        "<p> Fabricante ‏ : ‎ Rosamonte</p> Número de modelo del producto ‏ : ‎ 6HGA34</p>",
        30,
        "https://argentinisimo.es/wp-content/uploads/2020/03/ROSAMONTE-de-500-Gr.jpg").save();

        new Product("Camiseta Selección Argentina",
        120,
        "<p> Fabricante	Adidas</p>"+
        "<p> Identificador de producto del fabricante	‎Fafeicywx7bougksg-01</p>"+ "<p> Producto en Tienda desde ‏ : ‎ 13 de Enero de 2022</p>"+"<p> Referencia del fabricante	‎  27509FHH5</p>",
        30,
        "https://i.etsystatic.com/36038611/r/il/d77b30/4513398905/il_794xN.4513398905_bipn.jpg").save();

        new Product("Mate argentino",
                35,
                "<p> Fabricante	Martin Fierro</p>"+
                        "<p> Identificador de producto del fabricante	‎475hc25</p>"+ "<p> Producto en Tienda desde ‏ : ‎ 1872</p>"+"<p> Referencia del fabricante	‎  rgwgr4v</p>",
                30,
                "https://i.etsystatic.com/15792369/r/il/b5c8b2/2122596297/il_570xN.2122596297_2689.jpg").save();
        new Product("Termo Lumilagro",
                49,
                "<p> Fabricante	Lumilagro</p>"+
                        "<p> Identificador de producto del fabricante	‎475hc25</p>"+ "<p> Producto en Tienda desde ‏ : ‎ 2003</p>"+"<p> Referencia del fabricante	‎  rgwgr4v</p>",
                30,
                "https://m.media-amazon.com/images/I/31wMs54EBAL._AC_.jpg").save();
        new Product("Facon de Gaucho",
                795,
                "<p> Fabricante	Martin Fierro</p>"+
                        "<p> Identificador de producto del fabricante	‎475hc25</p>"+ "<p> Producto en Tienda desde ‏ : ‎ 1853</p>"+"<p> Referencia del fabricante	‎  rgwgr4v</p>",
                30,
                "https://s.lamason.us/arandu.com.ar/media/2021/08/20620P-4-min-300x300.jpg").save();
        new Product("Bombilla para Mate",
                5,
                "<p> Fabricante	Martin Fierro</p>"+
                        "<p> Identificador de producto del fabricante	‎475hc25</p>"+ "<p> Producto en Tienda desde ‏ : ‎ 1872</p>"+"<p> Referencia del fabricante	‎  rgwgr4v</p>" ,
                30,
                "https://www.herbolariodharma.com/pics/contenido/bombilla_curvamediana.jpg").save();

        DBini = true;
        Logger.info("Base De Datos Inicializada: %s",DBini);

        List<Usuario> ListUsers = Usuario.findAll();

        index();

    }

    //Hace Loggin
    public static void login (String user, String pass){
        Usuario userl = Usuario.find("byUsernameAndPassword", user, pass).first();

      if (userl!=null){
            if (userl.username== "Admin")
            {
                Admin();
            }else {
                session.put("user", userl.username);
                flash.success("Welcome, " + userl.username);
                Tienda();
            }
        } else
        {
            flash.put("username", user);
            flash.error("Login failed");
            index();
        }
            /*renderText("User o Password incorect");*/
    }

    //Hace Logging Para Android
    public static void loginAndroid (String user, String pass){
            Usuario userl = Usuario.find("byUsernameAndPassword", user, pass).first();
            if (userl!=null){
                session.put("user", userl.username);
                renderText("Hola "+user+" !");
            }
            else{
                renderText("Usuario o contraseña incorrectos");
            }
        }

    //Hace LoggOut
    public static void logout() {
        session.clear();
        index();
    }

    //Cambia la contraseña
    public static void saveSettings(String password, String verifyPassword) {
        Usuario connected = connected();
        connected.password = password;
        validation.valid(connected);
        validation.required(verifyPassword);
        validation.equals(verifyPassword, password).message("Your password doesn't match");
        if(validation.hasErrors()) {
            render("@settings", connected, verifyPassword);
        }
        connected.save();
        flash.success("Password updated");
        Tienda();
    }

    //Admin cambia el precio de producto
    public static void CambioPrecio(String Name, int precio) {
        //Busco el producto
        Product producto = Product.find("byName",Name).first();

        if (producto != null) {
            producto.price = precio;
            producto.save();
            Admin();
        }
        else
            Admin();
    }

    //Borra la cuenta
    public static void unsuscribe (String connectedDelete){
            Usuario connected = connected();
            validation.valid(connected);
            validation.required(connectedDelete);
            validation.equals(connectedDelete, connected.password).message("Your password doesn't match");
            if(validation.hasErrors()) {
                render("@settings", connectedDelete);
            }
            Usuario j = Usuario.find("byUsername", connected.username).first();
            j.delete();
            flash.success("User Deleted");
            index();
            session.clear();
        }

    //Render la ventana de settings
    public static void settings() {
        render();
    }

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    //Registra nuevo Usuario
    public static void saveUser(@Valid Usuario user, String verifyPassword) {
        validation.required(verifyPassword);
        validation.equals(verifyPassword, user.password).message("Your password doesn't match");

        //En caso que haya algun error en las validaciones, notificamos al usuario
        if(validation.hasErrors()) {
            render("@register", user, verifyPassword);
        }

        //Comprobamos que el email este en un formato correcto
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(user.email);
        if (matcher.find() == true) {
            Logger.info("he llegado");


            //Comprobamos que el usuario no existe
            try {
                Usuario userl = Usuario.find("byUsername", user.username).first();

                if (userl.username != user.username) {

                    flash.success("Este usuario ya existe");
                    render("@register");
                }
            } catch (Exception e) {
                user.create();
                session.put("user", user.username);
                flash.success("Welcome, " + user.username);
                Tienda();
            }
        }
        else {
            flash.success("El correo electronico no esta en un formato correcto");
            render("@register");
        }

    }

    //Registra nuevo Usuario Android
    public static void saveUserAndroid(String email, String username, String password, String verifyPassword) {
        if(password.equals(verifyPassword)){
            new Usuario(email, password, username).save();
            loginAndroid(username,password);
        }
        else{
           renderText("Contraseña incorrecta");
        }
    }

    //Render Pagina principal y ensena Productos
    public static void Tienda(){
        List<Product> products = Product.findAll();
        renderArgs.put("products", products);
        render();
    }
    //Render Pagina administrador
    public static void Admin(){
        render();
    }


    //Lista de Productos usada en el Carrito y pagina Principal
   public static void Product(int n){
       List<Product> products = Product.findAll();
       Product a = products.get(n);
       renderArgs.put("products", a);
       render();
   }

    //Anade compra al carrito
    public  static void anadirAlCarrito(String Item, int Quantity){
        Product producto = Product.find("byName", Item).first();
        Logger.info("producto: %s",producto);
        if(carrito.contains(producto)){
            int i = carrito.indexOf(producto);
            carrito.get(i).cantidadC += Quantity;
        }
        else{
            carrito.add(producto);
            int i = carrito.indexOf(producto);
            carrito.get(i).cantidadC = Quantity;
            aplicarOferta();
        }
        Logger.info("Carrito: %s",carrito);
        flash.success("Añadido al carrito");
        Tienda();
    }

    public static void aplicarOferta() {
        if (carrito.contains(Product.find("byName", "Yerba Mate").first()) &&
                carrito.contains(Product.find("byName", "Mate argentino").first()) &&
                carrito.contains(Product.find("byName", "Termo Lumilagro").first()) &&
                carrito.contains(Product.find("byName", "Bombilla para Mate").first())) {
            List<Product> products = Product.findAll();
            OfertaAplicada = true;
            Logger.info("Oferta Aplicada");
            flash.success("Oferta Aplicada");
            Tienda();
        }
        else {
            OfertaAplicada = false;
            Logger.info("No goza de oferta");
            flash.success("Oferta Aplicada");
            Tienda();
        }
    }

    //Render ventana carrito y productos en ella
    public static void Carrito(){
        renderArgs.put("carrito", carrito);
        int totalAntes = 0;
        int descuento = 0;
        int total = 0;
        for (Product item : carrito)
        {
            totalAntes += item.price*item.cantidadC;
        }
        if (OfertaAplicada) {
            total = (int) (totalAntes * 0.5);
            descuento = 50;
        }
        else
            total = totalAntes;
        render(totalAntes, descuento, total);
    }


    //Hace la compra del carrito
    public  static void comprar(){
        LocalDateTime now = LocalDateTime.now();
        for (Product item : carrito)
        {
            Product p = Product.find("byName", item.name).first();
            if(p.stock<=0 || p.stock<= item.cantidadC)
            {
                flash.success("No quedan más unidades en stock");
            }
            if (p.stock >= 0)
            {
                p.stock -= item.cantidadC;
                p.save();
                item.cantidadC += p.stock;
                flash.success("compra realizada con exito");
            }
            Compra c = new Compra(item.cantidadC, now.toString()).save();
            c.ProductsCompra= item;
            Usuario userc = Usuario.find("byUsername", session.get("user")).first();
            c.UserCompra = userc;
            c.save();
        }
        carrito.clear();
        Tienda();
    }
}