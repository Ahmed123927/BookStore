package BookStoreApi.BookStoreApiProject.repository;

import BookStoreApi.BookStoreApiProject.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
