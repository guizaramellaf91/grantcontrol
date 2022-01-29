package br.com.zaratech.repository;

import br.com.zaratech.model.TipoDepartamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TipoDepartamentoRepository extends JpaRepository<TipoDepartamento, Long> {

    @Query(value = "SELECT * FROM departamento td ORDER BY td.nome_departamento asc;",
            countQuery = "SELECT count(*) FROM departamento",
            nativeQuery = true)
    List<TipoDepartamento> findAllTiposDepartamento();

}
