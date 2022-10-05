package ru.job4j.domain;

import lombok.Data;
import ru.job4j.domain.Order;
import javax.persistence.*;

@Data
@Entity
@Table(name = "kitchen")
public class Kitchen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int ingredients = 5;
    private int reqIngredients;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "order_id")
    private Order order;
}
