package br.com.b2w.desafio.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.b2w.desafio.entity.Planeta;

@RunWith(SpringRunner.class)
public class PlanetaRepositoryTest {

    @MockBean
    private PlanetaRepository repository;

    private Page<Planeta> planetasPageExpected;

    @Before
    public void setUp() {

        Planeta p1 = new Planeta("Mercurio", "Desertico", "planicies, montanhas");
        Planeta p2 = new Planeta("Venus", "Tropical", "montanhas");
        Planeta p3 = new Planeta("Terra", "Temperado", "florestas, montanhas");
  
        List<Planeta> planetas = Arrays.asList(p1, p2, p3);
		
		PageRequest pageRequest = new PageRequest(0, 10);
        Page<Planeta> planetasPage = new PageImpl<Planeta>(planetas, pageRequest, planetas.size());
        planetasPageExpected = planetasPage;

        Page<Planeta> p3Page = new PageImpl<Planeta>( Arrays.asList(p3) );
        
        Mockito.when( repository.findAll(pageRequest) ).thenReturn( planetasPage );
        Mockito.when( repository.findByNome(p3.getNome(), pageRequest) ).thenReturn( p3Page );
    }

    @Test
	public void findByNamoNull() {
        
		String nameExpected = "Marte";
		PageRequest pageRequest = new PageRequest(0, 10);
        Page<Planeta> planeta = repository.findByNome(nameExpected, pageRequest);
        
        assertNull(planeta);
    }
    
    @Test
	public void findByNomeSuccess() {
        
		String nameExpected = "Terra";
		PageRequest pageRequest = new PageRequest(0, 10);
        Page<Planeta> planeta = repository.findByNome(nameExpected, pageRequest);
        String nameReturned = planeta.getContent().get(0).getNome();
        
        assertEquals(nameExpected, nameReturned);
	}

    @Test
	public void findAll() {
        
		PageRequest pageRequest = new PageRequest(0, 10);
        Page<Planeta> planetas = repository.findAll(pageRequest);
        
        assertEquals(planetasPageExpected, planetas);
	}
    
}