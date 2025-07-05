# Frilas2.0.com 💻🤝💸

Este trabalho tem como objetivo o desenvolvimento de um banco de dados para a Frilas.com, uma plataforma que conecta freelancers da área de Tecnologia da Informação (TI) a contratantes em busca de serviços. A aplicação permite que contratantes publiquem projetos com descrições e orçamentos, recebam propostas de profissionais interessados e escolham aquele que melhor atende às suas necessidades. Após a execução do projeto, é possível avaliar o freelancer.

O banco de dados foi estruturado para armazenar informações sobre freelancers, contratantes, projetos, propostas, pagamentos e avaliações. Ainda, permite a geração de três relatórios: propostas enviadas para um projeto, histórico de transações financeiras por contratante e avaliações recebidas por freelancer.

A versão Frilas 2.0 conta com um banco NoSQL (MongoDB).

## Pré-requisitos

1. MongoDB Compass; 
2. Java (21);
3. Maven

## Instalação

Clonar o repositório
```
git clone https://github.com/dud4rech/frilas2.0
cd frilas
```
Instalar as dependências
```
mvn clean install
```

## Configuração do banco

1. No MongoDB Compass, criar uma nova conexão chamada 'frilas';
2. Nessa nova conexão, criar uma base de dados chamada 'frilas';
3. Para cada uma das collections na pasta frilas_collections_dump faça:
   1. Criar a collection manualmente (por exemplo, para freelancer.json criar a collection freelancer);
   2. Clicar em Add Data > Import Json;
   3. Selecionar o json correspondente e confirmar importação;
   
## Compilar e executar a aplicação
```
mvn clean compile
```

```
mvn exec:java -Dexec.mainClass="org.project.Main"
```