package br.com.cotiinformatica.domain.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;

import br.com.cotiinformatica.domain.dtos.AlterarClienteDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteDto;
import br.com.cotiinformatica.domain.dtos.ObterClienteDto;

public interface ClienteService {

	//Criar cliente
	ObterClienteDto criar(CriarClienteDto dto);
	
	//Alterar cliente
	ObterClienteDto alterar(AlterarClienteDto dto);
	
	//Inativar cliente
	ObterClienteDto inativar(UUID id);
	
	//Consultar clientes ativos com paginação
	Page<ObterClienteDto> consultarAtivos(int page, int size, String sortBy, String direction);
	
	//Obter 1 cliente ativo através do ID
	ObterClienteDto obterAtivoPorId(UUID id);
}
