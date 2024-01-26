package BookStoreApi.BookStoreApiProject.service;

import BookStoreApi.BookStoreApiProject.model.OrderItem;
import BookStoreApi.BookStoreApiProject.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    OrderItemRepository orderItemRepository;

    public List<OrderItem> getAllProducts(){
        return orderItemRepository.findAll();
    }
}
