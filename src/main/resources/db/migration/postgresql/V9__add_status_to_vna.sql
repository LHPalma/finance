ALTER TABLE vna
    ADD COLUMN status VARCHAR(2) DEFAULT 'F';

COMMENT ON COLUMN vna.status IS 'P: Projetado - Projeção do Comitê de Acompanhamento Macroeconômico; F: Fechado - Índice Fechado; NI: Não Informado';
