package br.com.zaratech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.zaratech.dto.GuiaWebDTO;
import br.com.zaratech.model.GuiaWeb;

public interface GuiaWebRepository extends JpaRepository<GuiaWeb, String> {
	
	GuiaWeb findByGuiaWebId(long guiaWebId);
	
	List<GuiaWeb> findByTituloContaining(String titulo);
	List<GuiaWeb> findByDescricaoContaining(String descricao);
		
	@Query
	(value = "SELECT * FROM guia_web c ORDER BY titulo asc",
	 countQuery = "SELECT count(*) FROM guia_web", 
	 nativeQuery = true)
	List<GuiaWeb> findAllCompartilhamentos();
	
	@Query(value = "select count(*) from guia_web", nativeQuery = true)
	public long totalGuiaWeb();

    void save(GuiaWebDTO guiaWeb);
}