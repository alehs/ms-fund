package com.example.resourceservice.resource.domain.ports;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.resourceservice.resource.domain.Resource;

@Repository
public interface ResourceRepository extends CrudRepository<Resource, String> {
}
