package BookStoreApi.BookStoreApiProject.model;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "category_seq", allocationSize = 1)
    @Column(name = "category_Id")
    private int id ;
    private String name;


}
