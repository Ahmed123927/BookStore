package BookStoreApi.BookStoreApiProject.service;

import BookStoreApi.BookStoreApiProject.model.Order;
import BookStoreApi.BookStoreApiProject.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public void saveOrder(Order order){
        orderRepository.save(order);
    }
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
    public Order findOrderById(int id){
        return orderRepository.getById(id);
    }
}
