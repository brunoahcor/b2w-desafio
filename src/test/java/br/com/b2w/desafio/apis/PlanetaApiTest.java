package br.com.b2w.desafio.apis;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.b2w.desafio.entity.Planeta;
import br.com.b2w.desafio.service.PlanetaService;
import br.com.b2w.desafio.vo.PlanetaVo;

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class PlanetaApiTest {

	private MockMvc mockMvc;

	@InjectMocks
	private PlanetaApi apiMock;

	@Mock
    private PlanetaService serviceMock;
    
    private PageRequest pageRequest = new PageRequest(0, 10);

    private PlanetaVo p1 = new PlanetaVo("Mercurio", "Desertico", "planicies, montanhas");
    private PlanetaVo p2 = new PlanetaVo("Venus", "Tropical", "montanhas");
    private Planeta p3 = new Planeta("5eccfe0aecb1b847c0493481", "Terra", "Temperado", "florestas, montanhas");
    private Planeta tatooine = new Planeta("Tatooine", "arid", "desert");
    private Planeta tatooine2 = new Planeta("5ecf1f30ecb1b855c8cf311a", "Tatooine", "arid", "desert");
    private PlanetaVo alderaan = new PlanetaVo("Alderaan", "temperate", "grasslands, mountains");

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
    MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(apiMock).build();
    }

    @Test
	public void teste404() throws Exception {
        mockMvc.perform(get("/url/errada")).andExpect(status().isNotFound());
	}
    
    @Test
	public void listar200() throws Exception {
        List<PlanetaVo> planetas = new ArrayList<PlanetaVo>(Arrays.asList(p1, p2));
        Page<PlanetaVo> planetasPage = new PageImpl<PlanetaVo>(planetas, pageRequest, planetas.size());
        when(serviceMock.listar(null, pageRequest)).thenReturn(planetasPage);
        mockMvc.perform(get("/api/planetas"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect( jsonPath("$.content[0].nome", is(p1.getNome())) )
            .andExpect( jsonPath("$.content[0].clima", is(p1.getClima())) )
            .andExpect( jsonPath("$.content[0].terreno", is(p1.getTerreno())) )
            .andExpect( jsonPath("$.content[1].nome", is(p2.getNome())) )
            .andExpect( jsonPath("$.content[1].clima", is(p2.getClima())) )
            .andExpect( jsonPath("$.content[1].terreno", is(p2.getTerreno())) );
    }

    @Test
	public void listar204() throws Exception {
        mockMvc.perform(get("/api/planetas")).andExpect(status().isNoContent());
    }

    @Test
	public void buscarPorNome200() throws Exception {
        
        List<Planeta> planetas = new ArrayList<Planeta>(Arrays.asList(p3));
        Page<Planeta> planetasPage = new PageImpl<Planeta>(planetas, pageRequest, planetas.size());
        when(serviceMock.buscarPorNome( p3.getNome(),pageRequest)).thenReturn(planetasPage);
       
        mockMvc.perform(get("/api/planetas/{nome}/buscarPorNome", p3.getNome()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.nome", is( p3.getNome()) ) )
            .andExpect(jsonPath("$.clima", is( p3.getClima()) ) )
            .andExpect(jsonPath("$.terreno", is( p3.getTerreno()) ) );
    }

    @Test
	public void buscarPorNome404() throws Exception {
        
        List<Planeta> planetas = new ArrayList<Planeta>(Arrays.asList(p3));
        Page<Planeta> planetasPage = new PageImpl<Planeta>(planetas, pageRequest, planetas.size());
        when(serviceMock.buscarPorNome( p3.getNome(),pageRequest)).thenReturn(planetasPage);
       
        mockMvc.perform(get("/api/planetas/{nome}/buscarPorNome", "NOME_ERRADO")).andExpect(status().isNotFound());
    }

    @Test
	public void buscarPorId200() throws Exception {
        when(serviceMock.buscarPorId("5eccfe0aecb1b847c0493481")).thenReturn(Optional.of(p3));
        mockMvc.perform(get("/api/planetas/{id}/buscarPorId", "5eccfe0aecb1b847c0493481"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.id", is( p3.getId() )) )
            .andExpect(jsonPath("$.nome", is( p3.getNome())) );
    }

    @Test
	public void buscarPorId404() throws Exception {
        when(serviceMock.buscarPorId("5eccfe0aecb1b847c0493481")).thenReturn(Optional.of(p3));
        mockMvc.perform(get("/api/planetas/{id}/buscarPorId", "ID_ERRADO")).andExpect(status().isNotFound());
    }

    @Test
	public void salvar201() throws Exception {
		when(serviceMock.salvar(tatooine)).thenReturn(tatooine2);
        mockMvc.perform(post("/api/planetas/salvar")
            .contentType(APPLICATION_JSON_UTF8)
            .content(convertObjectToJsonBytes(tatooine)))
            .andExpect(status().isCreated());
    }
    
    @Test
	public void deletar200() throws Exception {

        when(serviceMock.buscarPorId("5eccfe0aecb1b847c0493481")).thenReturn(Optional.of(p3));
		doNothing().when(serviceMock).deletar(p3);
		
		mockMvc.perform(delete("/api/planetas/{id}/deletar", "5bf4161bd07bc8237ec34562"))
                .andExpect(status().isNoContent());

	}	
    
}