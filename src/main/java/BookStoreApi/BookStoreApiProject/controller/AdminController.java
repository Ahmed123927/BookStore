package BookStoreApi.BookStoreApiProject.controller;

import BookStoreApi.BookStoreApiProject.dto.ProductDTO;
import BookStoreApi.BookStoreApiProject.model.Category;
import BookStoreApi.BookStoreApiProject.model.Order;
import BookStoreApi.BookStoreApiProject.model.OrderItem;
import BookStoreApi.BookStoreApiProject.model.Product;
import BookStoreApi.BookStoreApiProject.repository.ProductRepository;
import BookStoreApi.BookStoreApiProject.service.CategoryService;
import BookStoreApi.BookStoreApiProject.service.OrderItemService;
import BookStoreApi.BookStoreApiProject.service.OrderService;
import BookStoreApi.BookStoreApiProject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;


    @Autowired
    private OrderService orderService;
    public static String uploadDir=System.getProperty("user.dir")+"/src/main/resources/static/productImages";

    @GetMapping("/admin")
    public String adminHome(){
        return "adminHome";
    }

    @GetMapping("/admin/categories")
    public String getCategories(Model model){
        model.addAttribute("categories",categoryService.getAllCat());
        return "categories";
    }
    @GetMapping("/admin/categories/add")
    public String getCategoriesAdd(Model model){
        model.addAttribute("category",new Category());
        return "categoriesAdd";
    }
    @PostMapping ("/admin/categories/add")
    public String PostCategoriesAdd(@ModelAttribute("category")Category category){
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }
    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id){
        categoryService.deleteCategoryById(id);
        return "redirect:/admin/categories";
    }
    @GetMapping("/admin/categories/update/{id}")
    public String updateCategory(@PathVariable int id,Model model){
        Optional<Category> category=categoryService.getCategoryById(id);
        if(category.isPresent()){
            model.addAttribute("category",category.get());
            return "categoriesAdd";
        }
        return "404Page";
    }
//////////////////////////////////Product Apis
@GetMapping("/admin/products")
public String getAllProducts(Model model){
        model.addAttribute("products",productService.getAllProducts());
        return "products";
}
    @GetMapping("/admin/products/add")
    public String addProductPage(Model model){
        model.addAttribute("productDTO",new ProductDTO());
        model.addAttribute("categories",categoryService.getAllCat());
        return "productsAdd";
    }

    @PostMapping("/admin/products/add")
    public String productAdd(@ModelAttribute("productDTO") ProductDTO productDTO,
                             @RequestParam("productImage") MultipartFile file,
                             @RequestParam("imgName") String imgName) throws IOException {

        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).orElse(null));
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setDescription(productDTO.getDescription());

        String imageUUID;

        if (!file.isEmpty()) {
            imageUUID = file.getOriginalFilename();

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path fileNameAndPath = uploadPath.resolve(imageUUID);

            Files.write(fileNameAndPath, file.getBytes());

        } else {
            imageUUID = imgName;
        }

        product.setImageName(imageUUID);
        productService.addProduct(product);

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id){
        productService.removeProduct(id);
        return "redirect:/admin/products";
    }
    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable long id,Model model){
        Product product=productService.getProductById(id).get();
        ProductDTO productDTO=new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageName(product.getImageName());
        model.addAttribute("categories",categoryService.getAllCat());
        model.addAttribute("productDTO",productDTO);
        return "productsAdd";
    }

///////////////////////////orderApis

   @GetMapping("/admin/orders")
    public String getAll(Model model){
        model.addAttribute("order",orderService.getAllOrders());
        return "orders";
   }
    @GetMapping("admin/orders/view/{orderId}")
    public String viewOrder(@PathVariable int orderId, Model model) {
        Order order = orderService.findOrderById(orderId);

        model.addAttribute("order", order);
        List<OrderItem> products=order.getOrderItems();
        model.addAttribute("items",products);
        return "viewOrder";
    }

}
