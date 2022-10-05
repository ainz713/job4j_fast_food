package ru.job4j.domain;

import lombok.Data;
import ru.job4j.domain.Order;

import javax.persistence.*;

@Data
@Entity
@Table(name = "messenger")
public class Messenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "order_id")
    private Order order;
}
