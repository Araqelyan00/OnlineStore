package model;

import model.enums.OrderStatus;
import model.enums.PaymentMethod;

import java.util.Date;
import java.util.Objects;

public class Order {
    private String id;
    private User user;
    private int count;
    private Date date;
    private double price;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private Product product;

    public Order() {

    }

    public Order(String id,User user, int count, Date date, double price, OrderStatus status, PaymentMethod paymentMethod, Product product) {
        this.id = id;
        this.user = user;
        this.count = count;
        this.date = date;
        this.price = price;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Order id : " + id + ", user : " + user +
                "\nCount : " + count +
                ", date : " + date +
                ", price : " + price +
                "\nStatus : " + status +
                ", paymentMethod : " + paymentMethod +
                "\nProduct : " + product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        if (count != order.count) return false;
        if(Double.compare(order.price, price) != 0) return false;
        if(!Objects.equals(id, order.id)) return false;
        if(!Objects.equals(user, order.user)) return false;
        if(!Objects.equals(date, order.date)) return false;
        if(!Objects.equals(status, order.status)) return false;
        if(!Objects.equals(product, order.product)) return false;
        return paymentMethod == order.paymentMethod;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        long temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + count;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (paymentMethod != null ? paymentMethod.hashCode() : 0);
        return result;
    }
}
