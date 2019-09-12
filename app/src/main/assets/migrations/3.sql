ALTER TABLE audioArticle ADD COLUMN code VARCHAR;
ALTER TABLE audioArticle ADD COLUMN amount FLOAT;
ALTER TABLE audioArticle ADD COLUMN is_parted TinyInt(1) DEFAULT 0;
ALTER TABLE audioArticle ADD COLUMN authPaid TinyInt(1) DEFAULT 0;
