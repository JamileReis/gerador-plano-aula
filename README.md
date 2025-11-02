# 🎓 Gerador de Planos de Aula com IA

🧠 Gerador de Planos de Aula com IA

Resumo:
Aplicação em Java (Spring Boot 3.2) que utiliza a API Gemini do Google para gerar planos de aula personalizados em formato JSON, salvando-os no Supabase.

🚀 Principais Funcionalidades

Formulário para o usuário inserir dados do plano de aula.

Integração com a Gemini API (modelo gemini-2.5-flash) para gerar conteúdo estruturado (introdução, BNCC, etapas e rubrica).

Armazenamento automático dos planos gerados no Supabase.

Exibição do resultado diretamente na interface web.

🧩 Tecnologias Utilizadas
Categoria	Tecnologia	Função
Backend	Java 21 + Spring Boot 3.2	Núcleo da aplicação
Inteligência Artificial	Google Gemini API	Geração dos planos de aula
Banco de Dados	Supabase	Armazenamento e autenticação
Comunicação Web	Spring WebFlux / WebClient	Requisições assíncronas e reativas
Serialização	Jackson Databind	Manipulação de JSON
