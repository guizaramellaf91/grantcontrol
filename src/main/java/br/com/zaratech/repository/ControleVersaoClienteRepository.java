package br.com.zaratech.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.zaratech.model.ControleVersaoCliente;

@SuppressWarnings("SpellCheckingInspection")
public interface ControleVersaoClienteRepository extends JpaRepository<ControleVersaoCliente, String> {
	
	ControleVersaoCliente findByControleVersaoClienteId(long clienteId);
	
	List<ControleVersaoCliente> findByCliente(long clienteId);
	
	@Query(value = "select c from ControleVersaoCliente c where c.cliente.clienteId = ?1")
	List<ControleVersaoCliente> findByClienteId(long clienteId);
	
	@Query(value = "select cvc from ControleVersaoCliente cvc where cvc.tipoSubProduto.tipoSubProdutoId = ?1 and cvc.cliente.clienteId = ?2")
	ControleVersaoCliente buscarPorTipoSubProdutoIdEclienteId(long tipoSubProdutoId, long clienteId);
	
	@Query(value = "select * from controle_versao_cliente where cliente_id = ?1", nativeQuery = true)
	Page<ControleVersaoCliente> buscarControleVersaoPorCliente(long clienteId, PageRequest pageRequest);
	
	@Query(value = "select count(*) from controle_versao_cliente where cliente_id = ?1", nativeQuery = true)
	public long totalControleVersaoCliente(long clienteId);
}
