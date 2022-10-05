package ru.job4j.repository;

import ru.job4j.domain.Messenger;
import org.springframework.data.repository.CrudRepository;

public interface MessengerRepository extends CrudRepository<Messenger, Integer> {
}
