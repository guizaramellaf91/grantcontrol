package br.com.zaratech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.zaratech.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

    Cliente findByClienteId(long clienteId);

    Cliente findByNomeCliente(String buscaCliente);

    List<Cliente> findByNomeClienteContaining(String buscaCliente);

    @Query(value = "SELECT c FROM Cliente c")
    List<Cliente> buscarTodosClientes();

    @Query(value = "SELECT c FROM Cliente c WHERE c.nomeCliente=:buscaCliente")
    Cliente buscarPorNome(@Param("buscaCliente") String buscaCliente);

    @Query(value = "SELECT DISTINCT c FROM Cliente c JOIN ClienteProduto cp ON c.clienteId = cp.cliente.clienteId WHERE cp.ip=:buscaCliente")
    List<Cliente> buscarPorIPServidor(@Param("buscaCliente") String buscaCliente);

    @Query(value = "SELECT COUNT(c) FROM Cliente c")
    long totalClientes();
}