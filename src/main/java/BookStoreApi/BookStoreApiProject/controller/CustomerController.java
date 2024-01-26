package BookStoreApi.BookStoreApiProject.controller;

import BookStoreApi.BookStoreApiProject.global.GlobalData;
import BookStoreApi.BookStoreApiProject.model.*;
import BookStoreApi.BookStoreApiProject.repository.OrderItemRepository;
import BookStoreApi.BookStoreApiProject.repository.UserRepository;
import BookStoreApi.BookStoreApiProject.service.CategoryService;
import BookStoreApi.BookStoreApiProject.service.OrderItemService;
import BookStoreApi.BookStoreApiProject.service.OrderService;
import BookStoreApi.BookStoreApiProject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CustomerController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    UserRepository userRepository;
    @Autowired
  private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderService orderService;

    @GetMapping({"/", "/home"})
    public String homePage(Model model) {
        model.addAttribute("cartCount", GlobalData.cart.size());
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("userDetails", userDetails);


        return "index";
    }

    @GetMapping("/shop")
    public String shopPage(Model model) {
        model.addAttribute("categories", categoryService.getAllCat());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("cartCount", GlobalData.cart.size());


        return "shop";
    }


    @GetMapping("/shop/category/{id}")
    public String filterByCategory(Model model, @PathVariable int id) {
        model.addAttribute("categories", categoryService.getAllCat());
        model.addAttribute("cartCount", GlobalData.cart.size());

        model.addAttribute("products", productService.getAllProductsByCategoryId(id));

        return "shop";
    }

    @GetMapping("/shop/viewproduct/{id}")
    public String viewProduct(Model model, @PathVariable int id) {
        model.addAttribute("product", productService.getProductById(id).get());

        return "viewProduct";
    }

    @PostMapping("/checkout/add")
    public String createOrder(@ModelAttribute("order") @Valid Order order, Principal principal, Model model) {
        String userEmail = principal.getName();

        Optional<User> userOptional = userRepository.findUserByEmail(userEmail);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            List<OrderItem> orderItemList = new ArrayList<>();

            for (Product product : GlobalData.cart) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(1);
                orderItem.setOrder(order);
                orderItemList.add(orderItem);
            }

            order.setOrderItems(orderItemList);

            order.setUser(user);

            order.setTotal(orderItemList.stream().mapToDouble(item -> item.getProduct().getPrice()).sum());
            System.out.println(order.getOrderItems());

            orderService.saveOrder(order);

            orderItemRepository.saveAll(order.getOrderItems());

            System.out.println(orderItemList);
            return "shop";
        } else {
            return "shop";
        }
    }


}
