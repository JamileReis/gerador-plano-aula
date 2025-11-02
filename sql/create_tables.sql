DROP TABLE IF EXISTS public.planos_aula;

CREATE TABLE public.planos_aula (
 id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
 user_id uuid,
 tema text NOT NULL,
 nivel text NOT NULL,
 idade text NOT NULL,
 duracao integer NOT NULL,
 introducao text NOT NULL,
 objetivo_bncc text NOT NULL,
 passo_a_passo JSONB NOT NULL,
 rubrica JSONB NOT NULL,
 created_at timestamptz DEFAULT now()
);