package ru.job4j.controller;

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

    public void save(Order o) {
        or.save(o);
    }

    @PutMapping("/save")
    public ResponseEntity<Order> save1(@Valid @RequestBody Order passport) {
        return new ResponseEntity<Order>(
                this.or.save(passport),
                HttpStatus.CREATED
        );
    }

    @PostMapping
    public void sendOrder(@RequestBody Order msg){
       save(msg);
       kafkaTemplate.send("order", msg.getId(), msg);
    }
}
