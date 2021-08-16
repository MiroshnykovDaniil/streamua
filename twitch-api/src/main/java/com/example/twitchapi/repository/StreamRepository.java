package com.example.twitchapi.repository;

import com.example.twitchapi.model.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreamRepository extends JpaRepository<Stream,String> {

}
