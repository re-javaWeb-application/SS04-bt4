package com.restaurant.bai5.service;

import com.restaurant.bai5.model.Order;
import com.restaurant.bai5.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order getOrderById(int id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(String itemName, int quantity) {
        Order order = new Order(0, itemName, quantity, "ĐANG XỬ LÝ");
        return orderRepository.save(order);
    }

    public String cancelOrder(int id) {
        boolean removed = orderRepository.deleteById(id);
        if (removed) {
            return "Đơn hàng #" + id + " đã được hủy thành công.";
        }
        return "Đơn hàng #" + id + " không tồn tại hoặc đã bị hủy trước đó.";
    }
}
