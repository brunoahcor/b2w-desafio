# b2w-desafio
==================================================

Contexto:
--------------------------------------------------
Nossos associados são aficionados por Star Wars e com isso, queremos criar um jogo com algumas informações da franquia.
Para possibilitar a equipe de front criar essa aplicação, queremos desenvolver uma API que contenha os dados dos planetas.

Funcionalidades:
--------------------------------------------------
* Adicionar um planeta (com nome, clima e terreno)
* Listar planetas
* Buscar por nome
* Buscar por ID
* Remover planeta

Requisitos:
--------------------------------------------------
* [Java Platform (JDK) 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Apache Maven 3.x](http://maven.apache.org/)
* MongoDB
* Docker
* docker-compose

Iniciando a aplicação
--------------------------------------------------
1. Execute o comando no terminal `mvn spring-boot:run`
3. Para ver a documentação da API Rest, basta digitar no navegador o endereço [http://localhost:8089/swagger-ui.html](http://localhost:8089/swagger-ui.html).

Iniciando a aplicação com docker e docker-compose
--------------------------------------------------
1. Execute o comando no terminal `mvn clean install`
2. docker-compose up
3. Para ver a documentação da API Rest, basta digitar no navegador o endereço [http://localhost:8089/swagger-ui.html](http://localhost:8089/swagger-ui.html).
4. Para acessar o MONGO-EXPRESS, basta digitar no navegador o endereço [http://localhost:8081/](http://localhost:8081/).