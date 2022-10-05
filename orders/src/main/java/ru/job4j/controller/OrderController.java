package ru.job4j.controller;

import org.springframework.kafka.annotation.KafkaListener;
import ru.job4j.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.job4j.repository.OrderRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderRepository or;

    @Autowired
    private KafkaTemplate<Integer, Order> kafkaTemplate;

    public OrderController(final OrderRepository or) {
        this.or = or;
    }

    @KafkaListener(topics = "cooked_order")
    public void save2(Order record) {
        or.save(record);
    }

    public void save(Order o) {
        o.setStatus("Заказ сформирован");
        or.save(o);
    }

    @PostMapping
    public void sendOrder(@RequestBody Order msg) {
       save(msg);
       kafkaTemplate.send("preorder", msg.getId(), msg);
    }
}
