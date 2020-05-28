package br.com.b2w.desafio.converter;

import java.util.List;
import java.util.stream.Collectors;

import br.com.b2w.desafio.entity.Planeta;
import br.com.b2w.desafio.vo.PlanetaVo;

public class PlanetaConverter {

	
	/**
	 * Método responsável pela conversão de List<Planeta> para List<PlanetaVo>
	 * 
	 * @param planetas lista de planetas
	 * @return lista convertida 
	 */
	public static List<PlanetaVo> planetasTOplanetasVo(List<Planeta> planetas) {
		return planetas.stream().map(planeta -> {
            return planetaTOPlanetaVo(planeta);
        }).collect(Collectors.toList());
	}

	/**
	 * Método responsável pela conversão de List<PlanetaVo> para List<Planeta>
	 * 
	 * @param planetasVo lista de planetasVo
	 * @return lista convertida 
	 */
	public static List<Planeta> planetasVoTOplanetas(List<PlanetaVo> planetasVo) {
		return planetasVo.stream().map(planetaVo -> {
            return planetaVoTOPlaneta(planetaVo);
        }).collect(Collectors.toList());
	}

	/**
	 * Método responsável pela conversão de Planeta para PlanetaVo
	 * 
	 * @param planeta informacoes do planeta
	 * @return objeto convertido
	 */
	public static PlanetaVo planetaTOPlanetaVo(Planeta planeta) {

		PlanetaVo planetaVo = null;

		if (planeta != null) {
			planetaVo = new PlanetaVo();

			planetaVo.setId(planeta.getId());
			planetaVo.setNome(planeta.getNome());
			planetaVo.setClima(planeta.getClima());
			planetaVo.setTerreno(planeta.getTerreno());
			planetaVo.setAparicoes(planeta.getAparicoes());
		}

		return planetaVo;
	}

	/**
	 * Método responsável pela conversão de PlanetaVo para Planeta
	 * 
	 * @param planetaVo informacoes do planetaVo
	 * @return objeto convertido
	 */
	public static Planeta planetaVoTOPlaneta(PlanetaVo planetaVo) {

		Planeta planeta = null;

		if (planetaVo != null) {
			planeta = new Planeta();

			planeta.setId(planetaVo.getId());
			planeta.setNome(planetaVo.getNome());
			planeta.setClima(planetaVo.getClima());
			planeta.setTerreno(planetaVo.getTerreno());
			planeta.setAparicoes(planetaVo.getAparicoes());
		}

		return planeta;
	}

	public static Planeta planetaVoTOSave(PlanetaVo planetaVo, Planeta planeta) {
		
		if (planetaVo != null) {
			if (planeta == null) {
				planeta = new Planeta();
			}
			planeta.setNome(planetaVo.getNome());
			planeta.setClima(planetaVo.getClima());
			planeta.setTerreno(planetaVo.getTerreno());
			planeta.setAparicoes(planetaVo.getAparicoes());
		}

		return planeta;
	}

}
