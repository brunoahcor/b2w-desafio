FROM java:8

VOLUME /tmp

ADD target/b2w-desafio-1.0.0-SNAPSHOT.jar b2w-desafio.jar

RUN bash -c 'touch /b2w-desafio.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/b2w-desafio.jar"]