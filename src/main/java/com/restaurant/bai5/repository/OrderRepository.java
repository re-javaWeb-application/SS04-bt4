package com.restaurant.bai5.repository;

import com.restaurant.bai5.model.Order;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderRepository {

    private final Map<Integer, Order> database = new HashMap<>();
    private int nextId = 1;

    public OrderRepository() {
        database.put(1, new Order(1, "Phở Bò", 2, "ĐANG XỬ LÝ"));
        database.put(2, new Order(2, "Bún Chả", 1, "ĐANG XỬ LÝ"));
        nextId = 3;
    }

    public Order findById(int id) {
        return database.get(id);
    }

    public Order save(Order order) {
        order.setId(nextId);
        database.put(nextId, order);
        nextId++;
        return order;
    }

    public boolean deleteById(int id) {
        return database.remove(id) != null;
    }
}
