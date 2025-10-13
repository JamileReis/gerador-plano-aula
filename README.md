# Gerador de Plano de Aula com IA (Java)

Instalação

1. Configurar variáveis de ambiente
   GEMINI_API_KEY
   GEMINI_MODEL (opcional)
   SUPABASE_URL
   SUPABASE_KEY

2. Criar tabela no Supabase usando sql/create_tables.sql

3. Buildar e rodar
   mvn package
   java -jar target/plano-aula-generator-1.0.0.jar

Observações
O código envia um prompt com um schema JSON e tenta parsear a resposta.
Ajuste o endpoint da API Gemini se necessário.
