CREATE TABLE IF NOT EXISTS public.planos_aula (
  id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
  tema text,
  nivel text,
  idade text,
  duracao integer,
  introducao text,
  objetivo_bncc text,
  passo_a_passo text,
  rubrica text,
  created_at timestamptz DEFAULT now()
);