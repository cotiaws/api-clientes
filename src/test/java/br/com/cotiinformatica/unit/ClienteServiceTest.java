package br.com.cotiinformatica.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.github.javafaker.Faker;

import br.com.cotiinformatica.domain.dtos.AlterarClienteDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteDto;
import br.com.cotiinformatica.domain.entities.Cliente;
import br.com.cotiinformatica.domain.interfaces.ClienteService;
import br.com.cotiinformatica.domain.services.ClienteServiceImpl;
import br.com.cotiinformatica.infrastructure.repositories.ClienteRepository;

public class ClienteServiceTest {

	// Atributos
	private ClienteRepository clienteRepository;
	private ClienteService clienteService;

	@BeforeEach
	public void setUp() {

		// Criando os mocks (simulações)
		clienteRepository = mock(ClienteRepository.class);

		// Injeção de dependência da classe de serviço (que será testada)
		clienteService = new ClienteServiceImpl(clienteRepository);
	}

	@Test
	@DisplayName("Deve criar um cliente com sucesso.")
	public void deveCriarCliente() throws Exception {

		// ARRANGE
		var dto = getCriarClienteDto(); // objeto DTO (requisição)

		var cliente = new Cliente(); // objeto Cliente (banco de dados)
		cliente.setId(UUID.randomUUID());
		cliente.setNome(dto.getNome());
		cliente.setEmail(dto.getEmail());
		cliente.setCpf(dto.getCpf());
		cliente.setAtivo(true);
		cliente.setDataHoraCriacao(LocalDateTime.now());
		cliente.setDataHoraUltimaAlteracao(LocalDateTime.now());

		// Configurando o comportamento do banco de dados(MOCKITO)
		// Quando o repositório salvar algum cliente ele deve retornar os dados do
		// cliente cadastrado
		when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

		// ACT
		// Executando o serviço de criação de cliente
		var response = clienteService.criar(dto);

		// ASSERT
		assertNotNull(response);
		assertEquals(response.getNome(), cliente.getNome());
		assertEquals(response.getEmail(), cliente.getEmail());
		assertEquals(response.getCpf(), cliente.getCpf());
		assertNotNull(response.getDataHoraCriacao());
		assertNotNull(response.getDataHoraUltimaAlteracao());
	}

	@Test
	@DisplayName("Deve atualizar um cliente com sucesso.")
	public void deveAtualizarCliente() throws Exception {

		// ARRANGE
		var id = UUID.randomUUID();
		var clienteExistente = new Cliente();
		clienteExistente.setId(id);
		clienteExistente.setNome("Nome Antigo");
		clienteExistente.setEmail("email@antigo.com");
		clienteExistente.setCpf("12345678900");
		clienteExistente.setAtivo(true);

		var dto = getAlterarClienteDto(id);

		when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));
		when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteExistente);

		// ACT
		var response = clienteService.alterar(dto);

		// ASSERT
		assertNotNull(response);
		assertEquals(dto.getNome(), response.getNome());
		assertEquals(dto.getEmail(), response.getEmail());
		assertEquals(dto.getCpf(), response.getCpf());
	}

	@Test
	@DisplayName("Deve inativar um cliente com sucesso.")
	public void deveInativarCliente() throws Exception {

		// ARRANGE
		var id = UUID.randomUUID();
		var cliente = new Cliente();
		cliente.setId(id);
		cliente.setNome("Cliente Teste");
		cliente.setAtivo(true);

		when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
		when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

		// ACT
		var response = clienteService.inativar(id);

		// ASSERT
		assertNotNull(response);
		assertFalse(cliente.getAtivo());
	}

	@Test
	@DisplayName("Deve consultar clientes de forma paginada com sucesso.")
	public void deveConsultarClientes() throws Exception {

		// ARRANGE
		var cliente = new Cliente();
		cliente.setId(UUID.randomUUID());
		cliente.setNome("Cliente Teste");
		cliente.setEmail("teste@teste.com");
		cliente.setCpf("12345678900");
		cliente.setAtivo(true);

		Page<Cliente> page = new PageImpl<>(List.of(cliente));

		when(clienteRepository.findByAtivoTrue(any(PageRequest.class))).thenReturn(page);

		// ACT
		var response = clienteService.consultarAtivos(0, 10, "nome", "asc");

		// ASSERT
		assertNotNull(response);
		assertEquals(1, response.getTotalElements());
		assertEquals("Cliente Teste", response.getContent().get(0).getNome());
	}

	@Test
	@DisplayName("Deve obter 1 cliente pelo ID com sucesso.")
	public void deveObterCliente() throws Exception {

		// ARRANGE
		var id = UUID.randomUUID();
		var cliente = new Cliente();
		cliente.setId(id);
		cliente.setNome("Cliente Teste");
		cliente.setEmail("teste@teste.com");
		cliente.setCpf("12345678900");
		cliente.setAtivo(true);

		when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

		// ACT
		var response = clienteService.obterAtivoPorId(id);

		// ASSERT
		assertNotNull(response);
		assertEquals(cliente.getId(), response.getId());
		assertEquals(cliente.getNome(), response.getNome());
		assertEquals(cliente.getEmail(), response.getEmail());
	}

	/*
	 * Método auxiliar para gerar um objeto 'CriarClienteDto'
	 */
	private CriarClienteDto getCriarClienteDto() {

		var faker = new Faker();

		var request = new CriarClienteDto();
		request.setNome(faker.name().fullName());
		request.setEmail(faker.internet().emailAddress());
		request.setCpf(faker.number().digits(11));

		return request;
	}

	/*
	 * Método auxiliar para gerar um objeto 'AlterarClienteDto'
	 */
	private AlterarClienteDto getAlterarClienteDto(UUID id) {

		var faker = new Faker();

		var request = new AlterarClienteDto();
		request.setId(id);
		request.setNome(faker.name().fullName());
		request.setEmail(faker.internet().emailAddress());
		request.setCpf(faker.number().digits(11));

		return request;
	}
}
