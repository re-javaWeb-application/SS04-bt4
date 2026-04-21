/*
1. data tráp
- khi truyền ID là chữ cái (vd: /orders/abc), Spring không thể ép "abc" thành int
  nên ném ra MethodArgumentTypeMismatchException.
- nên sửu dụng dùng @ExceptionHandler để bắt ngoại lệ này và trả về thông báo lỗi thay
vì để trang hiển thị 1 đống lỗi

2. idempotent
- DELETE và UPDATE là idempotent: Gọi DELETE /orders/5 lần đầu sẽ xóa đơn hàng #5. Gọi lần 2, 3
  thì đơn hàng #5 đã không còn nên kết quả vẫn như cũ (không có gì thay đổi, không làm
  hỏng dữ liệu). Nhân viên bấm 3 lần do lag mạng vẫn an toàn.

- POST KHÔNG idempotent: Mỗi lần gọi POST /orders đều tạo ra một đơn hàng mới với ID mới.
  Nếu nhân viên bấm 3 lần do lag mạng sẽ sinh ra 3 đơn hàng rác.
*/

package com.restaurant.bai5.controller;

import com.restaurant.bai5.model.Order;
import com.restaurant.bai5.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable("id") int id, ModelMap model) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            model.addAttribute("message", "Không tìm thấy đơn hàng có ID = " + id);
        } else {
            model.addAttribute("message",
                    "Đơn hàng #" + order.getId()
                    + " | Món: " + order.getItemName()
                    + " | SL: " + order.getQuantity()
                    + " | Trạng thái: " + order.getStatus());
        }
        return "order-result";
    }

    @PostMapping
    public String createOrder(@RequestParam("itemName") String itemName, @RequestParam("quantity") int quantity, ModelMap model) {
        Order order = orderService.createOrder(itemName, quantity);
        model.addAttribute("message",
                "Đã tạo đơn hàng mới #" + order.getId()
                + " | Món: " + order.getItemName()
                + " | SL: " + order.getQuantity());
        return "order-result";
    }

    @DeleteMapping("/{id}")
    public String cancelOrder(@PathVariable("id") int id, ModelMap model) {
        String result = orderService.cancelOrder(id);
        model.addAttribute("message", result);
        return "order-result";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleException(ModelMap model) {
        model.addAttribute("message", "Lỗi: ID đơn hàng phải là một số. Vui lòng kiểm tra lại.");
        return "order-result";
    }
}
