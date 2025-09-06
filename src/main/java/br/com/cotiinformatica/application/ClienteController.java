package br.com.cotiinformatica.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

	@PostMapping
	public ResponseEntity<?> post() {
		return ResponseEntity.status(501).body("Não implementado.");
	}
	
	@PatchMapping
	public ResponseEntity<?> patch() {
		return ResponseEntity.status(501).body("Não implementado.");
	}
	
	@DeleteMapping
	public ResponseEntity<?> delete() {
		return ResponseEntity.status(501).body("Não implementado.");
	}
	
	@GetMapping
	public ResponseEntity<?> getAll() {
		return ResponseEntity.status(501).body("Não implementado.");
	}
}
