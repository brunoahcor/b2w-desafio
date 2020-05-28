package br.com.b2w.desafio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.b2w.desafio.converter.PlanetaConverter;
import br.com.b2w.desafio.entity.Planeta;
import br.com.b2w.desafio.repository.PlanetaRepository;
import br.com.b2w.desafio.vo.PlanetaVo;

@Service
public class PlanetaServiceImpl implements PlanetaService {

   	@Autowired
    private PlanetaRepository repository;

    @Override
	public Planeta salvar(Planeta planeta) {
		return repository.save(planeta);
    }
    
	@Override
	public Page<PlanetaVo> listar(String nome, Pageable pageable) {
        Page<Planeta> planetasPage = (nome != null) ? repository.findByNome(nome, pageable) : repository.findAll(pageable);
        List<PlanetaVo> planetasVo = PlanetaConverter.planetasTOplanetasVo( planetasPage.getContent() );
        return new PageImpl<PlanetaVo>(planetasVo, pageable, planetasPage.getTotalElements());
	}
	
	@Override
	public Page<Planeta> buscarPorNome(String nome, Pageable pageable) {
		return repository.findByNome(nome, pageable);
	}

	@Override
	public Optional<Planeta> buscarPorId(String id) {
		return repository.findById(id);
	}

	@Override
	public void deletar(final Planeta planeta) {
		repository.delete(planeta);
	}
    
}