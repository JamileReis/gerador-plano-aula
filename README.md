# 🎓 Gerador de Planos de Aula com IA

🧠 Gerador de Planos de Aula com IA

Backend (Java e Frameworks)
Spring Boot (3.2.0)

O principal framework de desenvolvimento, facilitando a configuração e execução da aplicação.

Spring WebFlux

Utilizado para construir a API REST de forma reativa e não-bloqueante, conforme solicitado na sua pergunta.

Project Reactor

A biblioteca de programação reativa (implementando o padrão Reactive Streams), utilizada com tipos como Mono para lidar com a comunicação assíncrona com os serviços externos (Gemini e Supabase).

Spring WebClient

O cliente HTTP reativo e não-bloqueante, utilizado especificamente para se comunicar com as APIs do Gemini e do Supabase.

Reactor Netty e Netty

O framework de I/O não-bloqueante de baixo nível (Netty) é usado pelo Reactor Netty, que por sua vez é o servidor web/cliente HTTP subjacente padrão do Spring WebFlux/WebClient.

Linguagem e Ferramentas
Java 21: A linguagem de programação principal, configurada para ser compilada com suporte a recursos de preview.

Maven: O sistema de automação de build e gerenciamento de dependências (pom.xml).

Jackson (jackson-databind): Biblioteca utilizada para manipulação e serialização/desserialização de JSON, essencial para construir o payload da API Gemini e mapear a resposta para o DTO.

Jakarta Bean Validation: Utilizada para validar os objetos de requisição (@Valid em PlanoController.java).

Serviços Externos e Integrações
Google Gemini API (gemini-2.5-flash): O serviço de Inteligência Artificial generativa usado para criar o conteúdo do plano de aula (o componente central do aplicativo).

Supabase: Serviço de Backend as a Service (BaaS) utilizado para persistir/salvar os planos de aula gerados em um banco de dados externo.
