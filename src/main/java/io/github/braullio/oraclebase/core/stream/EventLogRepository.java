package io.github.braullio.oraclebase.core.stream;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {}