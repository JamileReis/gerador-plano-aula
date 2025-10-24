CREATE TABLE IF NOT EXISTS public.planos_aula (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id uuid REFERENCES auth.users NOT NULL,
    tema text,
    nivel text,
    idade text,
    duracao integer,
    introducao text,
    objetivo_bncc text,
    passo_a_passo JSONB,
    rubrica JSONB,
    created_at timestamptz DEFAULT now()
    );

ALTER TABLE public.planos_aula ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Usuários podem gerenciar seus planos" ON public.planos_aula
    FOR ALL USING (auth.uid() = user_id)
    WITH CHECK (auth.uid() = user_id);