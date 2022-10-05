package ru.job4j.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Kitchen;
import ru.job4j.domain.Order;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import ru.job4j.repository.KitchenRepository;
import ru.job4j.repository.OrderRepository;


@EnableKafka
@EnableScheduling
@SpringBootApplication
@RestController
@RequestMapping("/kitchen")
public class KitchenController {

    private final KitchenRepository kr;

    public KitchenController(KitchenRepository kr) {
        this.kr = kr;
    }

    @Autowired
    private KafkaTemplate<Integer, Order> kafkaTemplate;

    @KafkaListener(topics = "preorder")
    public void saveFromOrders(Order record) throws InterruptedException {
        Kitchen m = new Kitchen();
        record.setStatus("В работе");
        sendOrder(record);
        m.setOrder(record);
        m.setReqIngredients((int) getRandomIntegerBetweenRange(1, 10));
        kr.save(m);
        checkOrder(m);
    }

    public void checkOrder(Kitchen k) throws InterruptedException {
        Order o = k.getOrder();
        if (k.getReqIngredients() < k.getIngredients()) {
            o.setStatus("Готов к выдаче");
        } else {
            o.setStatus("Заказ отменен");
        }
        Thread.sleep(60000);
        sendOrder(o);
    }

    public void sendOrder(Order msg) {
        kafkaTemplate.send("cooked_order", msg.getId(), msg);
    }

    public static double getRandomIntegerBetweenRange(double min, double max) {
        double x = (int) (Math.random() * ((max - min) + 1)) + min;
        return x;
    }
}
