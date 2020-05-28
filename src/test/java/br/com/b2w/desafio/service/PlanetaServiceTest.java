package br.com.b2w.desafio.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.b2w.desafio.entity.Planeta;
import br.com.b2w.desafio.repository.PlanetaRepository;
import br.com.b2w.desafio.vo.PlanetaVo;

@RunWith(SpringRunner.class)
public class PlanetaServiceTest {

    @TestConfiguration
	static class planetaServiceImplTestContextConfiguration {
		@Bean
		public PlanetaService planetaService() {
			return new PlanetaServiceImpl();
		}
	}

    @Autowired
    private PlanetaService service;

    @MockBean
    private PlanetaRepository repository;

    private PageRequest pageRequest = new PageRequest(0, 10);

    private Planeta p1 = new Planeta("Mercurio", "Desertico", "planicies, montanhas");
    private Planeta p2 = new Planeta("Venus", "Tropical", "montanhas");
    private Planeta p3 = new Planeta("Terra", "Temperado", "florestas, montanhas");
    private Planeta pSave = new Planeta("9", "Save", "Clima Save", "terreno Save");
    private Planeta pFindById = new Planeta("99", "FindById", "Clima FindById", "terreno FindById");

    @Before
    public void setUp() {

        List<Planeta> planetas = Arrays.asList(p1, p2, p3);
		
        Page<Planeta> planetasPage = new PageImpl<Planeta>(planetas, pageRequest, planetas.size());

        Page<Planeta> p3Page = new PageImpl<Planeta>( Arrays.asList(p3) );
        
        Mockito.when( repository.findAll(pageRequest) ).thenReturn( planetasPage );
        Mockito.when( repository.findByNome(p3.getNome(), pageRequest) ).thenReturn( p3Page );
        Mockito.when( repository.findById(pFindById.getId())).thenReturn( Optional.of(pFindById) );
    }

    @Test
	public void saveSucesso() {
        Mockito.when(repository.save(pSave)).thenReturn(pSave);
        Planeta retorno = service.salvar(pSave);

        assertEquals(retorno, pSave);
    }

    @Test
	public void listarTudoSucesso() {
        Page<PlanetaVo> planetasVo = service.listar(null, pageRequest);
        List<PlanetaVo> listaVo = planetasVo.getContent();
        
        assertEquals(listaVo.get(0).getNome(), p1.getNome());
    }
    
    @Test
	public void buscarPorNomeNull() {
        Page<Planeta> planetasPage = service.buscarPorNome("NOME_ERRADO", pageRequest);
        assertNull(planetasPage);
    }

	@Test
	public void buscarPorNomeSucesso() {
        Page<Planeta> planetasPage = service.buscarPorNome(p3.getNome(), pageRequest);
        String nameReturned = planetasPage.getContent().get(0).getNome();
        
        assertEquals(p3.getNome(), nameReturned);
    }

    @Test
	public void buscarPorIdSucesso() {
        Optional<Planeta> planeta = service.buscarPorId(pFindById.getId());
       
        assertEquals(Optional.of(pFindById), planeta);
    }

    @Test
	public void buscarPorIdNull() {
        Optional<Planeta> planeta = service.buscarPorId("ID_ERRADO");
        assertEquals(Optional.empty(), planeta);
    }
    
}