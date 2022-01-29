package br.com.zaratech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.zaratech.model.TipoProduto;

public interface TipoProdutoRepository extends JpaRepository<TipoProduto, String> {
	
	TipoProduto findByTipoProdutoId(long tipoProdutoId);
	
	TipoProduto findByNomeProduto(String nomeProduto);
	
	List<TipoProduto> findAllByOrderByTipoProdutoIdAsc();
	
	List<TipoProduto> findAllByOrderByNomeProdutoAsc();
}
