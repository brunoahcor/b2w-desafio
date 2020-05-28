package br.com.b2w.desafio.apis;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(description = "API disponibiliza os dados dos planetas")
@RestController
@RequestMapping("/api/inicio")
public class InicioApi {

	private static Logger logger = LoggerFactory.getLogger(InicioApi.class);
	
    private static LocalDateTime dateIni = LocalDateTime.of(2020, 5, 25, 14, 0, 0);

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<?,?> getInicio(){

		logger.info("### Iniciando..");

		LocalDateTime dateNow = LocalDateTime.now();
		Long seconds = ChronoUnit.SECONDS.between(dateIni, dateNow);
		Long minutes = ChronoUnit.MINUTES.between(dateIni, dateNow);
		Long hours = ChronoUnit.HOURS.between(dateIni, dateNow);
		Long days = ChronoUnit.DAYS.between(dateIni, dateNow);

		Map<String, String> map = new HashMap<>();
		map.put("Nome", "Bruno Rocha");
		map.put("Desafio", "B2W");
		map.put("Data/Hora inicio desafio", LocalDateTime.of(2020, 5, 25, 14, 0, 0) .toString());
		map.put("Data/Hora atual", dateNow.toString());
		map.put("Tempo de inicio em SEGUNDOS", seconds.toString());
		map.put("Tempo de inicio em MINUTOS", minutes.toString());
		map.put("Tempo de inicio em HORAS", hours.toString());
		map.put("Tempo de inicio em DIAS", days.toString());

		logger.info("### map = {}",map.toString());

        return map;
    }
    
}