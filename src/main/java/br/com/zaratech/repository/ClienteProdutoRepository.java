package br.com.zaratech.repository;

import br.com.zaratech.model.ClienteProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ClienteProdutoRepository extends JpaRepository<ClienteProduto, String> {

    ClienteProduto findByClienteProdutoId(long clienteProdutoId);

    @Query(value = "SELECT cp from ClienteProduto cp WHERE cp.tipoProduto.tipoProdutoId=:tipoProdutoId and cp.cliente.clienteId=:clienteId")
    List<ClienteProduto> buscarClienteProdutoTipoProdutoIdclienteId(@Param("tipoProdutoId") long tipoProdutoId, @Param("clienteId") long clienteId);

    @Query(value = "SELECT cp from ClienteProduto cp WHERE cp.tipoProduto.tipoProdutoId not in (:tipoProdutoId) and cp.cliente.clienteId=:clienteId")
    List<ClienteProduto> buscarClienteProdutoTipoProdutoIdclienteIdWithout(@Param("tipoProdutoId") long[] tipoProdutoId, @Param("clienteId") long clienteId);

    @Transactional
    void delete(ClienteProduto clienteProdutoId);

    @Query(value = "SELECT COUNT(cp) FROM ClienteProduto cp")
    long totalClienteProdutoTodos();

    @Query(value = "SELECT COUNT(cp) FROM ClienteProduto cp WHERE cp.cliente.clienteId = :clienteId")
    long totalClienteProduto(@Param("clienteId") long clienteId);
}