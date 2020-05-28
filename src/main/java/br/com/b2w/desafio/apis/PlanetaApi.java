package br.com.b2w.desafio.apis;

import java.util.Optional;

import javax.validation.Valid;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.b2w.desafio.converter.PlanetaConverter;
import br.com.b2w.desafio.entity.Planeta;
import br.com.b2w.desafio.exception.PlanetNotFoundException;
import br.com.b2w.desafio.service.PlanetaService;
import br.com.b2w.desafio.vo.PlanetaVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(description = "API para efetuar as operações dos planetas")
@RestController
@RequestMapping("/api/planetas")
public class PlanetaApi {

	private transient static Logger logger = LoggerFactory.getLogger(PlanetaApi.class);

	@Autowired
	private PlanetaService service;

	private String API_STAR_WARS = "https://swapi.dev/api/";

	@ApiOperation(value = "Salva o planeta na base de dados", response = ResponseEntity.class)
	@ApiResponses(value = { 
		@ApiResponse(code = 201, message = "Registro salvo com sucesso!"),
		@ApiResponse(code = 400, message = "Requisicao invalida."),
		@ApiResponse(code = 404, message = "O recurso nao foi encontrado."),
		@ApiResponse(code = 500, message = "Ocorreu um erro inesperado, contate o administrador.") })
	@RequestMapping(value = "/salvar", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<?> salvar(@Valid @RequestBody PlanetaVo planetaVo) {

		try {
			getAparicoesFilme(planetaVo);
		} catch (PlanetNotFoundException e) {
			logger.info("### PlanetNotFoundException -> mensagem: {}",e.getMessage());
		} catch (UnirestException e) {
			logger.warn("### UnirestException -> mensagem: {}",e.getMessage());
		} catch (Exception e) {
			logger.warn("### Exception -> mensagem: {}",e.getMessage());
		}

		try {
			
			Planeta planeta = PlanetaConverter.planetaVoTOSave(planetaVo, null);
			service.salvar(planeta);

			return new ResponseEntity<Planeta>(planeta, HttpStatus.CREATED);
		} catch (PlanetNotFoundException pnfe) {
			return new ResponseEntity<Object>(pnfe.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Lista todos os planetas", response = ResponseEntity.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso!"),
			@ApiResponse(code = 204, message = "Não existe nenhum registro disponível para ser exibido."),
	        @ApiResponse(code = 404, message = "O recurso nao foi encontrado."),
	        @ApiResponse(code = 500, message = "Ocorreu um erro inesperado, contate o administrador.") })
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> listar(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "nome", required = false) String nome) {

		try {
			
			PageRequest pageRequest = new PageRequest(page, pageSize);

			Page<PlanetaVo> planetas = service.listar(nome, pageRequest);
			if (planetas != null && planetas.getTotalElements() > 0) {
				return new ResponseEntity<Page<PlanetaVo>>(planetas, HttpStatus.OK);
			}
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Busca o planeta por nome na base de dados", response = ResponseEntity.class)
	@ApiResponses(value = { 
		@ApiResponse(code = 200, message = "Registro retornardo com sucesso!"),
		@ApiResponse(code = 404, message = "O recurso nao foi encontrado."),
		@ApiResponse(code = 500, message = "Ocorreu um erro inesperado, contate o administrador.") })
	@RequestMapping(value = "/{nome}/buscarPorNome", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> buscarPorNome(@PathVariable("nome") String nome) {

		try {

			PageRequest pageRequest = new PageRequest(0, 10);
			Page<Planeta> planetaPage = service.buscarPorNome(nome, pageRequest);
			if (planetaPage == null) {
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			} 

			Planeta planeta = planetaPage.getContent().get(0);
			PlanetaVo planetaVo = PlanetaConverter.planetaTOPlanetaVo(planeta);
			return new ResponseEntity<PlanetaVo>(planetaVo, HttpStatus.OK);
		} catch (final Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Busca o planeta pelo id na base de dados", response = ResponseEntity.class)
	@ApiResponses(value = { 
		@ApiResponse(code = 200, message = "Registro retornardo com sucesso!"),
		@ApiResponse(code = 404, message = "O recurso nao foi encontrado."),
		@ApiResponse(code = 500, message = "Ocorreu um erro inesperado, contate o administrador.") })
	@RequestMapping(value = "/{id}/buscarPorId", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> buscarPorId(@PathVariable("id") String id) {

		try {

			Optional<Planeta> planeta = service.buscarPorId(id);
			if (!planeta.isPresent()) {
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			}

			PlanetaVo planetaVo = PlanetaConverter.planetaTOPlanetaVo(planeta.get());
			return new ResponseEntity<PlanetaVo>(planetaVo, HttpStatus.OK);
		} catch (final Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Exclui o planeta na base de dados.", response = ResponseEntity.class)
	@ApiResponses(value = { 
		@ApiResponse(code = 204, message = "Registro excluído com sucesso!"),
		@ApiResponse(code = 404, message = "O recurso nao foi encontrado."),
		@ApiResponse(code = 500, message = "Ocorreu um erro inesperado, contate o administrador.") })
	@RequestMapping(value = "/{id}/deletar", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> deletar(@PathVariable("id") String id) {

		try {

			Optional<Planeta> planeta = service.buscarPorId(id);
			if (!planeta.isPresent()) {
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			}

			service.deletar(planeta.get());

			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Método responsável por recuperar a quantidade de aparições por filme.
	 * 
	 * @param planetaVo
	 * @throws UnirestException 
	 */
	private void getAparicoesFilme(PlanetaVo planetaVo) throws PlanetNotFoundException, UnirestException {
		
		String url = API_STAR_WARS.concat("planets/?search=").concat(planetaVo.getNome());

		HttpResponse<JsonNode> response = Unirest.get(url).asJson();
		if (response.getStatus() == 200) {
			Integer count = Integer.parseInt(response.getBody().getObject().get("count").toString());
			if (count > 0) {
				JSONArray results = (JSONArray) response.getBody().getObject().get("results");
				JSONObject films = (JSONObject) results.get(0);
				JSONArray array = (JSONArray) films.get("films");
				planetaVo.setAparicoes(array.length());
			} else {
				throw new PlanetNotFoundException("Planeta não encontrado.");
			}
		}
	}

	

}