package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Messenger;
import ru.job4j.domain.Order;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import ru.job4j.repository.MessengerRepository;


@EnableKafka
@SpringBootApplication
@RestController
@RequestMapping("/messenger")
public class MessengerController {
    private final MessengerRepository pr;

    public MessengerController(MessengerRepository pr) {
        this.pr = pr;
    }

    @KafkaListener(topics="order")
    public void save(Order record) {
        Messenger m = new Messenger();
        m.setOrder(record);
        pr.save(m);
    }

    @GetMapping("/getorder/{id}")
    public ResponseEntity<String> findUnavaliabe(@PathVariable int id) {
        var rsl = this.pr.findById(id);
        String m = rsl.get().getOrder().getName();
        return new ResponseEntity<>(
                m,
                HttpStatus.OK
        );
    }
}
