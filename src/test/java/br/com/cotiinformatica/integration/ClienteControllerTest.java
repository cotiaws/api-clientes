package br.com.cotiinformatica.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cotiinformatica.application.ClienteController;
import br.com.cotiinformatica.domain.dtos.AlterarClienteDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteDto;
import br.com.cotiinformatica.domain.dtos.ObterClienteDto;
import br.com.cotiinformatica.domain.interfaces.ClienteService;

@WebMvcTest(controllers = ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

	@Test
	@DisplayName("POST /api/v1/clientes - Deve retornar 201 ao cadastrar.")
	public void postClientesReturnsCreated() throws Exception {

		//Criando o DTO para enviar uma requisição POST de cadastro de cliente
		var dto = new CriarClienteDto();
        dto.setNome("Fulano Teste");
        dto.setEmail("fulano@teste.com");
        dto.setCpf("12345678900");

        //Definindo os dados que a API deverá retornar
        var response = new ObterClienteDto();
        response.setId(UUID.randomUUID());
        response.setNome(dto.getNome());
        response.setEmail(dto.getEmail());
        response.setCpf(dto.getCpf());
        response.setDataHoraCriacao(LocalDateTime.now());
        response.setDataHoraUltimaAlteracao(LocalDateTime.now());

        //Mockando o comportamento da camada de serviço
        when(clienteService.criar(any(CriarClienteDto.class))).thenReturn(response);

        //Executando a requisição para a API e verificando a resposta
        mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(response.getNome()))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
        		.andExpect(jsonPath("$.cpf").value(response.getCpf()));
	}

	@Test
	@DisplayName("PATCH /api/v1/clientes - Deve retornar 200 ao atualizar.")
	public void patchClientesReturnsOk() throws Exception {
		
		//Criando o DTO para enviar uma requisição PATCH de edição de cliente
		var dto = new AlterarClienteDto();
        dto.setId(UUID.randomUUID());
        dto.setNome("Novo Nome");
        dto.setEmail("novo@teste.com");
        dto.setCpf("98765432100");

        //Definindo os dados que a API deverá retornar
        var response = new ObterClienteDto();
        response.setId(dto.getId());
        response.setNome(dto.getNome());
        response.setEmail(dto.getEmail());
        response.setCpf(dto.getCpf());
        response.setDataHoraCriacao(LocalDateTime.now().minusDays(1));
        response.setDataHoraUltimaAlteracao(LocalDateTime.now());

        //Mockando o comportamento da camada de serviço
        when(clienteService.alterar(any(AlterarClienteDto.class))).thenReturn(response);

        //Executando a requisição para a API e verificando a resposta
        mockMvc.perform(patch("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(response.getNome()))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
        		.andExpect(jsonPath("$.cpf").value(response.getCpf()));
	}

	@Test
	@DisplayName("DELETE /api/v1/clientes - Deve retornar 200 ao excluir.")
	public void deleteClientesReturnsOk() throws Exception {
		
		//Criando o ID para enviar uma requisição DELETE de cliente
		var id = UUID.randomUUID();

		//Dados do cliente que deverá ser excluído
        var response = new ObterClienteDto();
        response.setId(id);
        response.setNome("Cliente Inativo");
        response.setEmail("inativo@teste.com");
        response.setCpf("12345678900");

        //Mockando o comportamento da camada de serviço
        when(clienteService.inativar(id)).thenReturn(response);

        //Executando a requisição para a API e verificando a resposta
        mockMvc.perform(delete("/api/v1/clientes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(response.getNome()))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
        		.andExpect(jsonPath("$.cpf").value(response.getCpf()));
	}

	@Test
	@DisplayName("GET /api/v1/clientes - Deve retornar 200 ao consultar todos.")
	public void getAllClientesReturnsOk() throws Exception {
		
		//Dados do cliente que deverá ser consultado
		var cliente = new ObterClienteDto();
        cliente.setId(UUID.randomUUID());
        cliente.setNome("Cliente Paginado");
        cliente.setEmail("paginado@teste.com");
        cliente.setCpf("11122233344");

        //Definindo os parametros da paginação
        var page = new PageImpl<>(java.util.List.of(cliente), PageRequest.of(0, 10), 1);

        //Mockando o comportamento da camada de serviço
        when(clienteService.consultarAtivos(0, 10, "nome", "asc")).thenReturn(page);

        //Executando a requisição para a API e verificando a resposta
        mockMvc.perform(get("/api/v1/clientes")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "nome")
                .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Cliente Paginado"));
	}

	@Test
	@DisplayName("GET /api/v1/clientes - Deve retornar 200 ao consultar 1 cliente por ID.")
	public void getByIdClientesReturnsOk() throws Exception {
		
		//Criando o ID para enviar uma requisição GET de cliente
		var id = UUID.randomUUID();

		//Dados do cliente que deverá ser consultado
        var response = new ObterClienteDto();
        response.setId(id);
        response.setNome("Cliente Por ID");
        response.setEmail("id@teste.com");
        response.setCpf("55566677788");

        //Mockando o comportamento da camada de serviço
        when(clienteService.obterAtivoPorId(id)).thenReturn(response);

        //Executando a requisição para a API e verificando a resposta
        mockMvc.perform(get("/api/v1/clientes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value(response.getNome()))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
        		.andExpect(jsonPath("$.cpf").value(response.getCpf()));
	}
}
