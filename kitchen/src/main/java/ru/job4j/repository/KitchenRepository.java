package ru.job4j.repository;

import ru.job4j.domain.Kitchen;
import org.springframework.data.repository.CrudRepository;

public interface KitchenRepository extends CrudRepository<Kitchen, Integer> {
}

