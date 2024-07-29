//import command.Commands;
//import model.Order;
//import model.Product;
//import model.User;
//import model.enums.OrderStatus;
//import model.enums.PaymentMethod;
//import model.enums.ProductType;
//import model.enums.UserType;
//import storage.OrderStorage;
//import storage.ProductStorage;
//import storage.UserStorage;
//import util.IdGenerator;
//import util.StorageSerializeUtil;
//
//import java.text.ParseException;
//import java.util.Date;
//import java.util.Scanner;
//
//public class Main implements Commands {
//
//    private final static Scanner scanner = new Scanner(System.in);
//    private final static OrderStorage ORDER_STORAGE = StorageSerializeUtil.deserializeOrderStorage();
//    private final static ProductStorage PRODUCT_STORAGE = StorageSerializeUtil.deserializeProductStorage();
//    private final static UserStorage USER_STORAGE = StorageSerializeUtil.deserializeUserStorage();
//
//    private static User currentUser = null;
//
//    public static void main(String[] args) throws ParseException {
//        boolean isRun = true;
//
//        while (isRun) {
//            Commands.printMainCommands();
//            String command = scanner.nextLine();
//            switch (command) {
//                case EXIT:
//                    isRun = false;
//                    break;
//                case LOGIN:
//                    login();
//                    break;
//                case REGISTER:
//                    register();
//                    break;
//                default:
//                    System.out.println("Invalid command");
//            }
//        }
//    }
//
//    private static void login() {
//        System.out.println("Please enter your email,password");
//        String loginDataStr = scanner.nextLine();
//        String[] loginDataArr = loginDataStr.split(",");
//        User user = USER_STORAGE.getByEmail(loginDataArr[0]);
//        if (user == null || !user.getPassword().equals(loginDataArr[1])){
//            System.out.println("Invalid email or password");
//            return;
//        }
//        currentUser = user;
//        if(user.getUserType() == UserType.ADMIN){
//            adminCommands();
//        }else if(user.getUserType() == UserType.USER){
//            userCommands();
//        }
//    }
//
//    private static void register(){
//        System.out.println("Please enter your name,email,password,user type(User || Admin)");
//        String userDataStr = scanner.nextLine();
//        String[] userDataArr = userDataStr.split(",");
//        User user = USER_STORAGE.getByEmail(userDataArr[1]);
//        if(user != null){
//            System.out.println("User already exists");
//        }
//        try{
//            user = new User(IdGenerator.generateId(),userDataArr[0],userDataArr[2],userDataArr[1],UserType.valueOf(userDataArr[3].toUpperCase()));
//            USER_STORAGE.add(user);
//            System.out.println("User successfully registered");
//        }
//        catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e){
//            System.out.println("Invalid data !");
//        }
//    }
//
//    private static void adminCommands(){
//        boolean isAdmin = true;
//        while(isAdmin){
//            Commands.printAdminCommands();
//            String command = scanner.nextLine();
//            switch (command){
//                case LOGOUT:
//                    isAdmin = false;
//                    break;
//                case ADD_PRODUCT:
//                    addProduct();
//                    break;
//                case REMOVE_PRODUCT_BY_ID:
//                    removeProductById();
//                    break;
//                case PRINT_PRODUCTS:
//                    PRODUCT_STORAGE.print();
//                    break;
//                case PRINT_USERS:
//                    USER_STORAGE.print();
//                    break;
//                case PRINT_ORDERS:
//                    ORDER_STORAGE.print();
//                    break;
//                case CHANGE_ORDERS_STATUS_BY_ID:
//                    changeOrdersStatusById();
//                    break;
//                default:
//                    System.out.println("Invalid command");
//            }
//        }
//    }
//
//    private static void userCommands(){
//        boolean isUser = true;
//        while(isUser){
//            Commands.printUserCommands();
//            String command = scanner.nextLine();
//            switch (command){
//                case LOGOUT:
//                    isUser = false;
//                    break;
//                case PRINT_ALL_PRODUCTS:
//                    PRODUCT_STORAGE.print();
//                    break;
//                case BUY_PRODUCT:
//                    buyProduct();
//                    break;
//                case PRINT_MY_ORDERS:
//                    printOrders();
//                    break;
//                case CANCEL_ORDER_BY_ID:
//                    cancelOrderById();
//                    break;
//                default:
//                    System.out.println("Invalid command");
//            }
//        }
//    }
//
//    private static void addProduct(){
//        System.out.print("Please enter product's name : ");
//        String name = scanner.nextLine();
//        System.out.print("Please enter product's description : ");
//        String description = scanner.nextLine();
//        System.out.print("Please enter product's count in stock : ");
//        int stock = scanner.nextInt();
//        System.out.print("Please enter product's price : ");
//        double price = scanner.nextDouble();
//        System.out.print("Please enter product's type (ELECTRONICS | CLOTHES | BOOKS) : ");
//        String type = scanner.nextLine().toUpperCase();
//        String id = IdGenerator.generateId();
//        Product product = new Product(id,name,description,stock,price, ProductType.valueOf(type));
//        PRODUCT_STORAGE.add(product);
//        System.out.println("Product successfully added");
//    }
//
//    private static void removeProductById(){
//        System.out.print("Please enter product's id : ");
//        String id = scanner.nextLine();
//        PRODUCT_STORAGE.delete(id);
//        System.out.println("Product successfully removed");
//    }
//
//    private static void changeOrdersStatusById(){
//        ORDER_STORAGE.print();
//        System.out.println();
//        System.out.print("Please enter order's id : ");
//        String id = scanner.nextLine();
//        System.out.println(ORDER_STORAGE.getById(id).toString());
//        System.out.println("Please enter order's new status : ");
//        String status = scanner.nextLine().toUpperCase();
//        ORDER_STORAGE.getById(id).setStatus(OrderStatus.valueOf(status));
//        System.out.println("Order's status successfully changed");
//    }
//
//    private static void printOrders(){
//        System.out.println("Printing all orders");
//        ORDER_STORAGE.printByUser(currentUser);
//        System.out.println();
//    }
//
//    private static void cancelOrderById(){
//        System.out.print("Please enter order's id : ");
//        String id = scanner.nextLine();
//        ORDER_STORAGE.getById(id).setStatus(OrderStatus.CANCELLED);
//        System.out.println("Order is successfully cancelled");
//    }
//
//    private static void buyProduct(){
//        PRODUCT_STORAGE.print();
//        System.out.print("Please enter product's id : ");
//        String productId = scanner.nextLine();
//        System.out.print("Please enter count : ");
//        int quantity = scanner.nextInt();
//        int stackQuantity = PRODUCT_STORAGE.getByID(productId).getStockQty() - quantity;
//        if (stackQuantity < 0){
//            System.out.println("Invalid stack quantity");
//        }else {
//            PRODUCT_STORAGE.getByID(productId).setStockQty(stackQuantity);
//        System.out.print("Please input payment method (CASH | CARD | IDRAM) : ");
//        String paymentMethod = scanner.nextLine().toUpperCase();
//        double productPrice = quantity * (PRODUCT_STORAGE.getByID(productId).getPrice());
//        double bill =(productPrice * 10 / 100);
//        double orderPrice = productPrice + bill;
//        Order order = new Order(IdGenerator.generateId(),currentUser,quantity,new Date(),orderPrice,OrderStatus.NEW, PaymentMethod.valueOf(paymentMethod),PRODUCT_STORAGE.getByID(productId));
//        ORDER_STORAGE.add(order);
//        }
//    }
//
//
//}
import command.Commands;
import model.Order;
import model.Product;
import model.User;
import model.enums.OrderStatus;
import model.enums.PaymentMethod;
import model.enums.ProductType;
import model.enums.UserType;
import storage.OrderStorage;
import storage.ProductStorage;
import storage.UserStorage;
import util.IdGenerator;
import util.StorageSerializeUtil;

import java.util.Date;
import java.util.Scanner;

public class Main implements Commands {

    private final static Scanner SCANNER = new Scanner(System.in);
    private final static OrderStorage ORDER_STORAGE = StorageSerializeUtil.deserializeOrderStorage();
    private final static ProductStorage PRODUCT_STORAGE = StorageSerializeUtil.deserializeProductStorage();
    private final static UserStorage USER_STORAGE = StorageSerializeUtil.deserializeUserStorage();

    private static User currentUser = null;

    public static void main(String[] args) {
        boolean isRun = true;
        while (isRun) {
            Commands.printMainCommands();
            String command = SCANNER.nextLine();
            switch (command) {
                case EXIT:
                    isRun = false;
                    break;
                case LOGIN:
                    login();
                    break;
                case REGISTER:
                    register();
                    break;
                default:
                    System.out.println("Unknown Command!");
            }
        }
    }

    private static void register() {
        System.out.println("Please input name,email,password,userType(ADMIN,USER)");
        String userDataStr = SCANNER.nextLine();
        String[] userDataArr = userDataStr.split(",");
        User user = USER_STORAGE.getByEmail(userDataArr[1]);
        if (user != null) {
            System.out.println("User already exists!");
            return;
        }
        try {
            user = new User(IdGenerator.generateId(), userDataArr[0], userDataArr[1], userDataArr[2], UserType.valueOf(userDataArr[3].toUpperCase()));
            USER_STORAGE.add(user);
            System.out.println("User registered!");
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid data or user type!");
        }
    }

    private static void login() {
        System.out.println("Please input email,password");
        String loginDataStr = SCANNER.nextLine();
        String[] loginDataArr = loginDataStr.split(",");
        User user = USER_STORAGE.getByEmail(loginDataArr[0]);
        if (user == null || !user.getPassword().equals(loginDataArr[1])) {
            System.out.println("email or password is incorrect!");
            return;
        }
        currentUser = user;
        if (user.getUserType() == UserType.ADMIN) {
            adminCommands();
        } else if (user.getUserType() == UserType.USER) {
            userCommands();
        }

    }


    private static void changeOrderStatusById() {
        ORDER_STORAGE.print();
        System.out.println("Please input order id,new status(NEW,DELIVERED,CANCELED)");
        String orderDataStr = SCANNER.nextLine();
        String[] orderDataArr = orderDataStr.split(",");
        Order order = ORDER_STORAGE.getById(orderDataArr[0]);
        if (order == null) {
            System.out.println("Order does not exists");
            return;
        }
        OrderStatus newStatus = OrderStatus.valueOf(orderDataArr[1]);
        if (order.getStatus() == OrderStatus.NEW
                && newStatus == OrderStatus.DELIVERED) {
            if (order.getProduct().getStockQty() < order.getCount()) {
                System.out.println("You do not have enough product qty");
                return;
            }
            order.getProduct().setStockQty(order.getProduct().getStockQty() - order.getCount());
            order.setStatus(newStatus);
            System.out.println("Order status has changed!");
            StorageSerializeUtil.serializeOrderStorage(ORDER_STORAGE);
        }
    }

    private static void removeProductById() {
        PRODUCT_STORAGE.print();
        System.out.println("Please input product id");
        String productId = SCANNER.nextLine();
        Product product = PRODUCT_STORAGE.getByID(productId);
        if (product == null) {
            System.out.println("Wrong product Id");
            return;
        }
        PRODUCT_STORAGE.delete(productId);
        StorageSerializeUtil.serializeProductStorage(PRODUCT_STORAGE);
    }

    private static void addProduct() {
        System.out.println("Please input name,description,stockQty,price,productType(ELECTRONICS,CLOTHING,BOOKS)");
        String productDataStr = SCANNER.nextLine();
        String[] productDataArr = productDataStr.split(",");

        try {
            Product product = new Product();
            product.setId(IdGenerator.generateId());
            product.setName(productDataArr[0]);
            product.setDescription(productDataArr[1]);
            product.setStockQty(Integer.parseInt(productDataArr[2]));
            product.setPrice(Double.parseDouble(productDataArr[3]));
            product.setProductType(ProductType.valueOf(productDataArr[4]));
            PRODUCT_STORAGE.add(product);
            System.out.println("Product added!");
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid data: " + e.getMessage());
        }
    }

    private static void adminCommands() {
        boolean isRun = true;
        while (isRun) {
            Commands.printAdminCommands();
            String command = SCANNER.nextLine();
            switch (command) {
                case LOGOUT:
                    isRun = false;
                    currentUser = null;
                    break;
                case ADD_PRODUCT:
                    addProduct();
                    break;
                case REMOVE_PRODUCT_BY_ID:
                    removeProductById();
                    break;
                case PRINT_PRODUCTS:
                    PRODUCT_STORAGE.print();
                    break;
                case PRINT_USERS:
                    USER_STORAGE.printByType(UserType.USER);
                    break;
                case PRINT_ORDERS:
                    ORDER_STORAGE.print();
                    break;
                case CHANGE_ORDERS_STATUS_BY_ID:
                    changeOrderStatusById();
                    break;
                default:
                    System.out.println("Unknown command!");

            }
        }
    }

    private static void userCommands() {
        boolean isRun = true;
        while (isRun) {
            Commands.printUserCommands();
            String command = SCANNER.nextLine();
            switch (command) {
                case LOGOUT:
                    isRun = false;
                    currentUser = null;
                    break;
                case PRINT_ALL_PRODUCTS:
                    PRODUCT_STORAGE.print();
                    break;
                case BUY_PRODUCT:
                    buyProduct();
                    break;
                case PRINT_MY_ORDERS:
                    ORDER_STORAGE.printByUser(currentUser);
                    break;
                case CANCEL_ORDER_BY_ID:
                    cancelOrderById();
                    break;
                default:
                    System.out.println("Unknown Command!");
            }
        }
    }

    private static void buyProduct() {
        PRODUCT_STORAGE.print();
        System.out.println("Please input productId,qty,paymentMethod(CARD,CASH,PAYPAL)");
        String orderDataStr = SCANNER.nextLine();
        String[] orderDataArr = orderDataStr.split(",");
        Product product = PRODUCT_STORAGE.getByID(orderDataArr[0]);
        PaymentMethod paymentMethod = PaymentMethod.valueOf(orderDataArr[2]);
        if (product == null) {
            System.out.println("Wrong product Id");
            return;
        }
        int qty = Integer.parseInt(orderDataArr[1]);

        if (product.getStockQty() < qty) {
            System.out.println("Wrong qty");
            return;
        }
        double price = qty * product.getPrice();

        System.out.println("You want to buy " + product.getName() + " qty: " + qty + " price: " + price + " paymentMethod: " + paymentMethod + " \n Are you sure? (Yes/No)");
        String answer = SCANNER.nextLine();

        if (!answer.equalsIgnoreCase("yes")) {
            System.out.println("Order canceled!");
            return;
        }
        Order order = new Order(IdGenerator.generateId(), currentUser, qty, new Date(), price, OrderStatus.NEW, paymentMethod, product);
        ORDER_STORAGE.add(order);
    }

    private static void cancelOrderById() {
        ORDER_STORAGE.printByUser(currentUser);
        System.out.println("Please input order Id");
        String orderId = SCANNER.nextLine();
        Order order = ORDER_STORAGE.getById(orderId);
        if (order == null || !order.getUser().equals(currentUser)) {
            System.out.println("Wrong order id");
            return;
        }
        if (order.getStatus() != OrderStatus.NEW) {
            System.out.println("Order can not be canceled!");
            return;
        }
        order.setStatus(OrderStatus.CANCELLED);
        System.out.println("Order canceled!");
        StorageSerializeUtil.serializeOrderStorage(ORDER_STORAGE);
    }

}