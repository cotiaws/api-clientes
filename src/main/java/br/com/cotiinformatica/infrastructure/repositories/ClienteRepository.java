package br.com.cotiinformatica.infrastructure.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cotiinformatica.domain.entities.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

	//Verificar se já existe cliente com um Cpf
	boolean existsByCpf(String cpf);
		
	//Verificar se já existe cliente com um email
	boolean existsByEmail(String email);
	
	//Buscar cliente pelo Cpf
	Optional<Cliente> findByCpf(String cpf);
	
	//Buscar cliente pelo Email
	Optional<Cliente> findByEmail(String email);
	
	//Buscar todos os clientes ativos com paginação
	Page<Cliente> findByAtivoTrue(Pageable pageable);
}
