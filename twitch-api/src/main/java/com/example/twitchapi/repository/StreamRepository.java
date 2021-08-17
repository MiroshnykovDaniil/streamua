package com.example.twitchapi.repository;

import com.example.twitchapi.model.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StreamRepository extends JpaRepository<Stream,String> {

    @Query
            (nativeQuery = true,
            value = "Select * from stream st where st.user_id = ?1"
            )
    Stream findByUser_id(String id);

}
