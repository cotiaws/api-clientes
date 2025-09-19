package br.com.cotiinformatica.application;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.domain.dtos.AlterarClienteDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteDto;
import br.com.cotiinformatica.domain.dtos.ObterClienteDto;
import br.com.cotiinformatica.domain.interfaces.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

	private final ClienteService clienteService;
	
	@PostMapping
	public ResponseEntity<ObterClienteDto> post(@RequestBody @Valid CriarClienteDto dto) {
		var response = clienteService.criar(dto);
		return ResponseEntity.status(201).body(response);
	}
	
	@PatchMapping
	public ResponseEntity<ObterClienteDto> patch(@RequestBody @Valid AlterarClienteDto dto) {
		var response = clienteService.alterar(dto);
		return ResponseEntity.status(200).body(response);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<ObterClienteDto> delete(@PathVariable UUID id) {
		var response = clienteService.inativar(id);
		return ResponseEntity.status(200).body(response);
	}
	
	@GetMapping
	public ResponseEntity<Page<ObterClienteDto>> getAll(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int size,
			@RequestParam(defaultValue = "nome") String sortBy,
			@RequestParam(defaultValue = "asc") String direction
			) {
		var response = clienteService.consultarAtivos(page, size, sortBy, direction);
		return ResponseEntity.status(200).body(response);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<ObterClienteDto> getById(@PathVariable UUID id) {
		var response = clienteService.obterAtivoPorId(id);
		return ResponseEntity.status(200).body(response);
	}
}
