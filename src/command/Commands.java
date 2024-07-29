package command;

public interface Commands {

    //Main commands
    String EXIT = "0";
    String LOGIN = "1";
    String REGISTER = "2";

    //User commands
    String LOGOUT = "0";
    String PRINT_ALL_PRODUCTS = "1";
    String BUY_PRODUCT = "2";
    String PRINT_MY_ORDERS = "3";
    String CANCEL_ORDER_BY_ID = "4";

    //Admin commands
    String ADD_PRODUCT = "1";
    String REMOVE_PRODUCT_BY_ID = "2";
    String PRINT_PRODUCTS = "3";
    String PRINT_USERS = "4";
    String PRINT_ORDERS = "5";
    String CHANGE_ORDERS_STATUS_BY_ID = "6";

    static void printMainCommands(){
        System.out.println("Input " + EXIT + " to exit");
        System.out.println("Input " + LOGIN + " to login");
        System.out.println("Input " + REGISTER + " to register");
        System.out.print("Input command :");
    }

    static void printUserCommands(){
        System.out.println("Input " + LOGOUT + " to logout");
        System.out.println("Input " + PRINT_ALL_PRODUCTS + " to print all products");
        System.out.println("Input " + BUY_PRODUCT + " to buy");
        System.out.println("Input " + PRINT_MY_ORDERS + " to print my orders");
        System.out.println("Input " + CANCEL_ORDER_BY_ID + " to delete order by id");
        System.out.print("Input command :");
    }

    static void printAdminCommands(){
        System.out.println("Input " + LOGOUT + " to logout");
        System.out.println("Input " + ADD_PRODUCT + " to add product");
        System.out.println("Input " + REMOVE_PRODUCT_BY_ID + " to remove product");
        System.out.println("Input " + PRINT_PRODUCTS + " to print all products");
        System.out.println("Input " + PRINT_USERS + " to print all users");
        System.out.println("Input " + PRINT_ORDERS + " to print all orders");
        System.out.println("Input " + CHANGE_ORDERS_STATUS_BY_ID + " to change orders status by id");
        System.out.print("Input command :");
    }

}
