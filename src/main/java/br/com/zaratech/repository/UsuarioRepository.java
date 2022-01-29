package br.com.zaratech.repository;

import br.com.zaratech.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Usuario findByUsuarioId(long usuarioId);

    Usuario findByEmail(String email);

    @Query(value = "select count(*) from usuario", nativeQuery = true)
    public long totalUsuarios();

    @Query(value = "select count(*) from usuario where ativo = true;", nativeQuery = true)
    public long totalUsuariosAtivos();

    @Query(value = "select count(*) from usuario where ativo = false", nativeQuery = true)
    public long totalUsuariosInativos();

    @Query("select u from Usuario u where u.login = ?1")
    Usuario findByLogin(String login);

    @Query("select u from Usuario u where u.login = ?1 and u.senha = ?2")
    Usuario findByLoginSenha(String login, String senha);

    List<Usuario> findByNomeContaining(String nomeUsuario);

    @Query(value = "SELECT u FROM Usuario u WHERE u.login <> 'admin'")
    Page<Usuario> findAllUsuariosWithoutAdmin(PageRequest pageRequest);

    @Query(value = "SELECT * FROM usuario u ORDER BY u.nome asc",
            countQuery = "SELECT count(*) FROM usuario",
            nativeQuery = true)
    List<Usuario> findAllUsuarios();

    @Query(value = "SELECT * FROM usuario u ORDER BY u.acessos desc",
            countQuery = "SELECT count(*) FROM usuario",
            nativeQuery = true)
    List<Usuario> findAllUsuariosAcessos();
}