package br.com.zaratech.repository;

import br.com.zaratech.model.TipoAmbiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public interface TipoAmbienteRepository extends JpaRepository<TipoAmbiente, String> {

    TipoAmbiente findByTipoAmbienteId(long tipoAmbienteId);

    List<TipoAmbiente> findAllByOrderByNomeAmbienteAsc();

    @Query(value = "select ta from TipoAmbiente ta where ta.nomeAmbiente = ?1")
    TipoAmbiente buscarTipoAmbientePorNome(String nomeAmbiente);
}