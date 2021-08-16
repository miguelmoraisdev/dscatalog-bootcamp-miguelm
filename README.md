# DS-CATALOG
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/miguelmoraisdev/dscatalog-bootcamp-miguelm/blob/master/LICENSE) 

# Sobre o projeto
O DS-CATALOG é uma aplicação full stack web responsiva que está sendo construída durante o Bootcamp da [DevSuperior](https://devsuperior.com "Site da DevSuperior") usando Spring Boot e React-Js.

A aplicação consiste em uma API Rest de um sistema de catálogo de produtos.  As rotas e funcionalidades são protegidas com autorização e autenticação usando JWT e Oauth2.

## Layout web
![Web 1](https://github.com/miguelmoraisdev/dscatalog-bootcamp-miguelm/blob/master/_assets/home.png)

![Web 2](https://github.com/miguelmoraisdev/dscatalog-bootcamp-miguelm/blob/master/_assets/catalogo.png)

## Modelo conceitual
![Modelo Conceitual](https://github.com/miguelmoraisdev/dscatalog-bootcamp-miguelm/blob/master/_assets/ModeloConceitual.png)

## Padrão Camadas

![Padrão Camadas](https://github.com/miguelmoraisdev/dscatalog-bootcamp-miguelm/blob/master/_assets/camadas.png)

- Usamos o padrão camadas separando a aplicação backend em três camadas: Controladores Rest, Camada de Serviços e Camada de Acesso aos Dados (+ Entidades).

# Tecnologias utilizadas
## Back end
- Java
- Spring Boot
- JPA / Hibernate
- Maven
- H2 database
- Spring Security (JWT, Oauth2 e Bcrypt)
- Postman (Testes de Requisições)
- Testes (Junit e Mockito)
## Front end
- HTML / CSS / JS / TypeScript
- ReactJS (EM ANDAMENTO)

## Implantação em produção
- EM ANDAMENTO

# Como executar o projeto

## Back end
Pré-requisitos: Java 11

```bash
# clonar repositório
git clone https://github.com/miguelmoraisdev/dscatalog-bootcamp-miguelm

# entrar na pasta do projeto back end
cd backend

# executar o projeto
./mvnw spring-boot:run
```
# Autor

Miguel Augusto de Morais Junior

https://www.linkedin.com/in/miguel-morais-04a9ab1b0/

