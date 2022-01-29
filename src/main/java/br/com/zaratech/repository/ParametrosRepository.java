package br.com.zaratech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.zaratech.model.ParametrosSistema;

public interface ParametrosRepository extends JpaRepository<ParametrosSistema, String> {

	List<ParametrosSistema> findAll();
	
	ParametrosSistema findByParametroId(Long parametroId);
	
	@Query("select p from ParametrosSistema p where p.chave = ?1")
	ParametrosSistema findByChave(String chave);
	
}
