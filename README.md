# ProjetoIntegrador - Mercado Livre: Frescos

Este documento está dividido em duas partes, 1. Requisito 6 e o 2. ProjetoIntegrador.

## 1.Requisito 6
### Nome: Receita
Este requisito tem como proposta a realização de compras atráves de um "livro de receita".

Quando um usuário adiciona produtos ao carrinho de compra, é necessário que ele selecione 
produto por produto. Esta sulução propõe que o usuário possa escolher uma receita e todos 
os ingredientes necessários para isso é adicionado ao carrinho de uma vez.

####Execução
* [Postman collection](https://github.com/fmyose/ProjetoIntegrador-MeliFrescos/blob/feature/requisito6/src/main/resources/Recipe.postman_collection.json)
* Vide [Características e Tecnologias](#características-e-tecnologias) abaixo para a execução do projeto.

#### Diagrama com o requisito 6
Em destaque (verde) temos as entidades que pertence a este requisito.
![Diagrama](https://user-images.githubusercontent.com/101267133/167029661-be37ce80-2f47-4fb4-887e-aef572b91c6d.png)



## 2. ProjetoIntegrador

Este Projeto Integrador se destina a simular uma nova modalidade de armazenamento, transporte e comercialização de produtos frescos, congelados e refrigerados do Mercado Livre através de uma API REST em Java com Spring Boot e suas respectivas dependências.

### Características e Tecnologias:
- Java 11;
- Spring Security e Token JWT;
- Spring Validations;
- Spring Data JPA;
- Banco de Dados relacional Postgres (local).

### Instruções para a instalação:

Para acesso local do banco de dados, é necessário a inserção da variável de ambiente abaixo na IDE:

```sh
HOST=jdbc:postgresql://localhost:5432/PIDB;USERNAME=(seu_nome_de_usuário);PASSWORD=(sua_senha_definida)
```

### Collection com os End-points no Postman:

Encontra-se dentro do projeto, no diretório abaixo:

```sh
src/main/resources/PostmanCollection.json
```


- Quadro Kanban com as tasks realizadas [Disponível aqui](https://github.com/juliocesargama/ProjetoIntegrador-MeliFrescos/projects/1).

### Diagramas de Classe
![DC](https://user-images.githubusercontent.com/70298438/166481858-c235e35d-9865-4d2c-a556-cd2ab11989a9.jpg)


### Documentação, Referencial utilizados e Cronologia dos requisitos:

[Enunciado Base](https://drive.google.com/file/d/1bBOM49bxqRR7apxP3sgV7_LRiTq9xQD2/view)

[Requisito 1](https://drive.google.com/file/d/1rbT3upYAwN-CrOVtze0M2Fq7Cobuj7FD/view) (Início em: 22/04/22, Término em: 27/04/22)

[Requisito 2](https://drive.google.com/file/d/1M66St3F6TwWJ6WG_s1in75_bMyeKb8PM/view) (Início em: 26/04/22, Término em: 02/05/22)

[Requisito 3](https://drive.google.com/file/d/1GnTl6sHhdvyKjR0oz0nXlyvzH-oW_2Jv/view) (Início em: 28/04/22, Término em: 29/04/22)

[Requisito 4](https://drive.google.com/file/d/1kNZLztafr2tXuDU24W9xwUu09va2kMP0/view) (Início em: 29/04/22, Término em: 02/05/22)

[Requisito 5](https://drive.google.com/file/d/1yiEzdwI87K7AO9bgPffHbb0DPjVKM-oP/view) (Início em: 29/04/22, Término em: 03/05/22)

[Requisito 6](https://drive.google.com/file/d/1zlRtIPjK4r0WdrzFs7LIVA_8Q5HyDgXz/view) (Início em: 03/05/22, Término em: 06/05/22)
