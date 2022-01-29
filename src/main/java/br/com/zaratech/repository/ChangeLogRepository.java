package br.com.zaratech.repository;

import br.com.zaratech.model.ChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, String> {

    ChangeLog findByChangeLogId(long clienteProdutoId);

    @Query(value = "SELECT c FROM ChangeLog c ORDER BY c.dataAlteracao desc")
    List<ChangeLog> findAllChangeLogs();

    @Transactional
    void delete(ChangeLog changeLog);

    @Query(value = "SELECT COUNT(c) FROM ChangeLog c where c.changeLogId = :changeLogId")
    public long totalChangeLog(@Param("changeLogId") long changeLogId);
}