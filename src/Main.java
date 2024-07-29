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