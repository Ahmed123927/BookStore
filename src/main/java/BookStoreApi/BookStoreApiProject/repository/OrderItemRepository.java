package BookStoreApi.BookStoreApiProject.repository;

import BookStoreApi.BookStoreApiProject.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
}
