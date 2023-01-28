package controllers;

import play.mvc.*;
import java.util.*;
import models.*;
import play.data.validation.*;
import play.Logger;
import play.mvc.results.RenderText;

import java.util.regex.*;

import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Application extends Controller {

    public static boolean BDinicializada = false;
    public static boolean OfertaAplicada = false;
    public static ArrayList<Product> carrito = new ArrayList<Product>();
    public static ArrayList<Product> productosEnOferta = new ArrayList<Product>();
    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    //prepara usuario para HTML
    @Before
    static void addUser() {
        Usuario user = connected();
        if(user != null) {
            renderArgs.put("Usuario", user);
        }
    }

    //Comprobación de existencia y estado de usuario
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

    //Pantalla index HTML
    public static void index() {
        if (BDinicializada != true){
            inicializarBD();
        }
        if(connected() != null) {
            String username = session.get("user");
            Tienda();
        }
        render();
    }

    //Pantalla settings HTML
    public static void settings() {
        render();
    }

    //Pantalla principal HTML
    public static void Tienda(){
        List<Product> products = Product.findAll();
        renderArgs.put("products", products);
        render();
    }
    //Pantalla administrador HTML
    public static void Admin(){
        render();
    }

    //Lista de Productos
    static String YerbaMate = "Yerba Mate";
    static String CamisetaArgentina = "Camiseta de la seleccion Argentina";
    static String Mate = "Mate argentino";
    static String Termo = "Termo Lumilagro";
    static String Facon = "Facon de Gaucho";
    static String Bombilla = "Bombilla para Mate";

    //Lista de Precios
    static double precioYerbaMate = 6.55;
    static double precioCamisetaArgentina = 120.0;
    static double precioMate = 35.0;
    static double precioTermo = 49.0;
    static double precioFacon = 795.0;
    static double precioBombilla = 5.0;

    //stock permanente
    static int stock = 10;

    //imagenes url
    static String YerbaMateURL = "https://argentinisimo.es/wp-content/uploads/2020/03/ROSAMONTE-de-500-Gr.jpg";
    static String CamisetaArgentinaURL = "https://i.etsystatic.com/36038611/r/il/d77b30/4513398905/il_794xN.4513398905_bipn.jpg";
    static String MateURL = "https://i.etsystatic.com/15792369/r/il/b5c8b2/2122596297/il_570xN.2122596297_2689.jpg";
    static String TermoURL = "https://m.media-amazon.com/images/I/31wMs54EBAL._AC_.jpg";
    static String FaconURL = "https://s.lamason.us/arandu.com.ar/media/2021/08/20620P-4-min-300x300.jpg";
    static String BombillaURL = "https://www.herbolariodharma.com/pics/contenido/bombilla_curvamediana.jpg";

    //Inizializa la base de datos
    public  static void inicializarBD() {

        //public User(String email, String password, String username)
        new Usuario("juan@gmail.com", "juan", "juan").save();
        new Usuario("david@gmail.com", "david" , "david").save();
        new Usuario("dolors@gmail.com", "dolors" , "dolors").save();
        new Usuario("Admin","Admin","Admin").save();

        //Product(String name, double price, String description, int stock, String image)
        new Product(YerbaMate, precioYerbaMate,
        "<p> Producto en Tienda desde ‏ : ‎ 9 de Julio del 1810</p>"+
        "<p> Fabricante ‏ : ‎ Rosamonte</p> Número de modelo del producto ‏ : ‎ 6HGA34</p>",
        stock, YerbaMateURL).save();

        new Product(CamisetaArgentina, precioCamisetaArgentina,
        "<p> Fabricante	Adidas</p>"+
        "<p> Identificador de producto del fabricante	‎Fafeicywx7bougksg-01</p>"+
                "<p> Producto en Tienda desde ‏ : ‎ 13 de Enero de 2022</p>"+
                "<p> Referencia del fabricante	‎  27509FHH5</p>",
        stock,
        CamisetaArgentinaURL).save();

        new Product(Mate, precioMate,
                "<p> Fabricante	Martin Fierro</p>"+
                        "<p> Identificador de producto del fabricante	‎475hc25</p>"+
                        "<p> Producto en Tienda desde ‏ : ‎ 1872</p>"+
                        "<p> Referencia del fabricante	‎  rgwgr4v</p>",
                stock, MateURL).save();
        new Product(Termo, precioTermo,
                "<p> Fabricante	Lumilagro</p>"+
                        "<p> Identificador de producto del fabricante	‎475hc25</p>"+
                        "<p> Producto en Tienda desde ‏ : ‎ 2003</p>"+
                        "<p> Referencia del fabricante	‎  rgwgr4v</p>",
                stock, TermoURL).save();
        new Product(Facon, precioFacon,
                "<p> Fabricante	Martin Fierro</p>"+
                        "<p> Identificador de producto del fabricante	‎475hc25</p>"+
                        "<p> Producto en Tienda desde ‏ : ‎ 1853</p>"+
                        "<p> Referencia del fabricante	‎  rgwgr4v</p>",
                stock, FaconURL).save();
        new Product(Bombilla, precioBombilla,
                "<p> Fabricante	Martin Fierro</p>"+
                        "<p> Identificador de producto del fabricante	‎475hc25</p>"+
                        "<p> Producto en Tienda desde ‏ : ‎ 1872</p>"+
                        "<p> Referencia del fabricante	‎  rgwgr4v</p>" ,
                stock, BombillaURL).save();

        //lista de productos en oferta
        Product mate = Product.find("byName", Mate).first();
        productosEnOferta.add(mate);
        Product yerba = Product.find("byName", YerbaMate).first();
        productosEnOferta.add(yerba);
        Product bombilla = Product.find("byName", Bombilla).first();
        productosEnOferta.add(bombilla);
        Product termo = Product.find("byName", Termo).first();
        productosEnOferta.add(termo);

        BDinicializada = true;
        Logger.info("Base de datos inicializada: %s", BDinicializada);
        List<Usuario> ListUsers = Usuario.findAll();
        index();
    }

    //Inicio de sesión de usuario
    public static void login (String user, String pass) {
        Usuario usuario = Usuario.find("byUsernameAndPassword", user, pass).first();

      if (usuario!=null) {
            if (usuario.username== "Admin") {
                Admin();
            }
            else {
                session.put("user", usuario.username);
                flash.success("¡Te extrañamos!, " + usuario.username);
                Tienda();
            }
        }
      else {
            flash.put("username", user);
            flash.error("nombre o contraseña incorrectos");
            index();
        }
    }

    //Inicio de sesión para Android
    public static void loginAndroid (String user, String pass){
            Usuario userl = Usuario.find("byUsernameAndPassword", user, pass).first();
            if (userl!=null){
                session.put("user", userl.username);
                renderText("¡Te extrañamos!, "+user);
            }
            else{
                renderText("nombre o contraseña incorrectos");
            }
        }

    //Cerrar sesión
    public static void logout() {
        session.clear();
        index();
    }

    //Cambiar contraseña
    public static void saveSettings(String password, String verifyPassword) {
        Usuario connected = connected();
        connected.password = password;
        validation.valid(connected);
        validation.required(verifyPassword);
        validation.equals(verifyPassword, password).message("Las contraseñas no coinciden");
        if(validation.hasErrors()) {
            render("@settings", connected, verifyPassword);
        }
        connected.save();
        flash.success("Contraseña actualizada");
        Tienda();
    }

    //Crear usuario
    public static void saveUser(@Valid Usuario user, String verifyPassword) {
        validation.required(verifyPassword);
        validation.equals(verifyPassword, user.password).message("Las contraseñas no coinciden");

        //Notifica al usuario de errores en la verificación.
        if(validation.hasErrors()) {
            render("@register", user, verifyPassword);
        }

        //Comprobación de formato de correo
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(user.email);
        if (matcher.find() == true) {
            Logger.info("Match found");
            //Verificación de existencia del usuario
            try {
                Usuario usuarioEnBD = Usuario.find("byUsername", user.username).first();
                if (usuarioEnBD.username != user.username) {
                    flash.success("El usuario ya existe");
                    render("@register");
                }
            } catch (Exception e) {
                user.create();
                session.put("user", user.username);
                flash.success("¡Te extrañamos!, " + user.username);
                Tienda();
            }
        }
        else {
            flash.success("Formato incorrecto. Por favor, introduzca un email válido.");
            render("@register");
        }
    }

    //Eliminar usuario
    public static void unsuscribe (String connectedDelete){
        Usuario connected = connected();
        validation.valid(connected);
        validation.required(connectedDelete);
        validation.equals(connectedDelete, connected.password).message("Las contraseñas no coinciden");
        if(validation.hasErrors()) {
            render("@settings", connectedDelete);
        }
        Usuario j = Usuario.find("byUsername", connected.username).first();
        j.delete();
        flash.success("Usuario correctamente eliminado");
        index();
        session.clear();
    }

    //Registro de usuario en Android
    public static void saveUserAndroid(String email, String username, String password, String verifyPassword) {
        if(password.equals(verifyPassword)){
            new Usuario(email, password, username).save();
            loginAndroid(username,password);
        }
        else{
           renderText("Contraseña incorrecta");
        }
    }


    //Objeto Producto. Búsqueda por ID
   public static void Product(int n){
       List<Product> products = Product.findAll();
       Product a = products.get(n);
       renderArgs.put("products", a);
       render();
   }

    //Pantalla carrito HTML
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
            for (Product item : productosEnOferta) {
                descuento = descuento + (int) (item.price * (0.5-1));
            }
            total = totalAntes + descuento;
        }
        else
            total = totalAntes;
        render(totalAntes, descuento, total);
    }

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

    //Oferta del 50% para productos en lista de ofertas
    public static void aplicarOferta() {
        int i = 0;
        for (Product item : carrito) {
            if (productosEnOferta.contains(Product.find("byName", item.name).first())) {
                i++;
            }
        }
        if (i==productosEnOferta.size()) {
            List<Product> products = Product.findAll();
            OfertaAplicada = true;
            Logger.info("Oferta Aplicada");
            flash.success("Has añadido el kit de mate al carrito. ¡Te lo rebajamos a mitad de precio!");
        }
        else {
            OfertaAplicada = false;
            Logger.info("No goza de oferta");
        }
        Tienda();
    }

    public  static void comprar(){
        LocalDateTime now = LocalDateTime.now();
        for (Product item : carrito)
        {
            Product p = Product.find("byName", item.name).first();
            if(p.stock<=0 || p.stock<= item.cantidadC)
            {
                flash.success("Lo sentimos, este producto está agotado.");
            }
            if (p.stock >= 0)
            {
                p.stock -= item.cantidadC;
                p.save();
                item.cantidadC += p.stock;
                flash.success("Felicidades, ya haz realizado tu compra.");
            }
            Compra c = new Compra(item.cantidadC, now.toString()).save();
            c.ProductsCompra= item;
            Usuario userc = Usuario.find("byUsername", session.get("user")).first();
            c.UserCompra = userc;
            c.save();
        }
        carrito.clear();
        OfertaAplicada = false;
        Logger.info("El usuario ha realizado una compra.");
        flash.success("Compra Realizada. ¿Quieres algo más?");

        Tienda();
    }

    //Cambiar precio de producto (solo admin)
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
}