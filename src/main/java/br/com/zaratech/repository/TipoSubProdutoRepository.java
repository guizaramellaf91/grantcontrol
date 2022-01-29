package br.com.zaratech.repository;

import br.com.zaratech.model.TipoSubProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public interface TipoSubProdutoRepository extends JpaRepository<TipoSubProduto, String> {

    TipoSubProduto findByTipoSubProdutoId(long tipoSubProdutoId);

    TipoSubProduto findByNomeSubProduto(String nomeSubProduto);

    @Query(value = "select tsp from TipoSubProduto tsp where tsp.tipoProduto.tipoProdutoId = ?1")
    List<TipoSubProduto> buscarPorTipoProdutoId(long tipoProdutoId);

    @Query(value = "select tsp from TipoSubProduto tsp where tsp.nomeSubProduto = ?1")
    TipoSubProduto buscarTipoSubProdutoPorNome(String nomeSubProduto);
}