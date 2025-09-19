package br.com.cotiinformatica.domain.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.cotiinformatica.domain.dtos.AlterarClienteDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteDto;
import br.com.cotiinformatica.domain.dtos.ObterClienteDto;
import br.com.cotiinformatica.domain.entities.Cliente;
import br.com.cotiinformatica.domain.interfaces.ClienteService;
import br.com.cotiinformatica.infrastructure.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

	private final ClienteRepository clienteRepository;

	@Override
	public ObterClienteDto criar(CriarClienteDto dto) {
		
		var mapper = new ModelMapper();
		
		var cliente = mapper.map(dto, Cliente.class);
		
		cliente.setDataHoraCriacao(LocalDateTime.now());
		cliente.setDataHoraUltimaAlteracao(LocalDateTime.now());
		cliente.setAtivo(true);
		
		clienteRepository.save(cliente);
		
		return mapper.map(cliente, ObterClienteDto.class);
	}

	@Override
	public ObterClienteDto alterar(AlterarClienteDto dto) {
		
		var mapper = new ModelMapper();
		
		var cliente = clienteRepository.findById(dto.getId()).get();
		
		if(dto.getNome() != null) cliente.setNome(dto.getNome());
		if(dto.getEmail() != null) cliente.setEmail(dto.getEmail());
		if(dto.getCpf() != null) cliente.setCpf(dto.getCpf());
		
		cliente.setDataHoraUltimaAlteracao(LocalDateTime.now());
		
		clienteRepository.save(cliente);
		
		return mapper.map(cliente, ObterClienteDto.class);
	}

	@Override
	public ObterClienteDto inativar(UUID id) {

		var mapper = new ModelMapper();
		
		var cliente = clienteRepository.findById(id).get();		
		
		cliente.setAtivo(false);
		
		clienteRepository.save(cliente);
		
		return mapper.map(cliente, ObterClienteDto.class);
	}

	@Override
	public Page<ObterClienteDto> consultarAtivos(int page, int size, String sortBy, String direction) {

		var mapper = new ModelMapper();
		
		var sort = direction.equalsIgnoreCase("desc")
					? Sort.by(sortBy).descending()
					: Sort.by(sortBy).ascending();
		
		var pageable = PageRequest.of(page, size, sort);
		
		return clienteRepository.findByAtivoTrue(pageable)
				.map(cliente -> mapper.map(cliente, ObterClienteDto.class));
	}

	@Override
	public ObterClienteDto obterAtivoPorId(UUID id) {
		
		var mapper = new ModelMapper();
		
		var cliente = clienteRepository.findById(id).get();		
		
		return mapper.map(cliente, ObterClienteDto.class);
	}
}
