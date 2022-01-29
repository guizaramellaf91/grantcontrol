package br.com.zaratech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.zaratech.model.ParametrosEmail;

public interface ParametrosEmailRepository extends JpaRepository<ParametrosEmail, String> {

	ParametrosEmail findByEmailFrom(String emailFrom);
	
	List<ParametrosEmail> findAll();	
	ParametrosEmail findByParametroEmailId(Long parametroId);		
}
