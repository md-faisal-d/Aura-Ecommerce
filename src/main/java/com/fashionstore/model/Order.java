// Order.java

package com.fashionstore.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {

    private int id;
    private int userId;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String city;
    private String state;
    private String pincode;
    private String paymentMethod;
    private String orderStatus;
    private Timestamp orderedAt;

    public Order() {
    }

    public Order(int id, int userId, BigDecimal totalAmount,
                 String shippingAddress, String city, String state,
                 String pincode, String paymentMethod,
                 String orderStatus, Timestamp orderedAt) {

        this.id = id;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
        this.orderedAt = orderedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Timestamp getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(Timestamp orderedAt) {
        this.orderedAt = orderedAt;
    }
}