package com.java.listener.repository;

import com.java.listener.entity.ListenerUrlEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ListenerUrlEventRepository extends JpaRepository<ListenerUrlEventEntity, Long> {

    Optional<ListenerUrlEventEntity> findListenerUrlEventEntityByShortUrl(String shortUrl);
}
