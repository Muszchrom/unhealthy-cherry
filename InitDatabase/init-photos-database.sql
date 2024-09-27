CREATE DATABASE photos_database;

\c photos_database;

CREATE TABLE IF NOT EXISTS public.category (
  id                      BIGINT NOT NULL 
                            GENERATED ALWAYS AS IDENTITY,
  category                VARCHAR(255) NOT NULL,
  category_as_path_variable VARCHAR(255) NOT NULL,

  UNIQUE(category),
  UNIQUE(category_as_path_variable),
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS public.place (
  id                      BIGINT NOT NULL 
                            GENERATED ALWAYS AS IDENTITY,
  category_id             BIGINT NOT NULL,
  place                   VARCHAR(255) NOT NULL,
  place_as_path_variable  VARCHAR(255) NOT NULL,

  UNIQUE(category_id, place),
  UNIQUE(category_id, place_as_path_variable),
  FOREIGN KEY(category_id) REFERENCES public.category(id) 
    ON DELETE CASCADE 
    ON UPDATE CASCADE,
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS public.photo (
  id                      BIGINT NOT NULL
                            GENERATED ALWAYS AS IDENTITY,
  place_id                BIGINT NOT NULL,
  is_best                 BOOLEAN NOT NULL DEFAULT FALSE,
  datetime                TIMESTAMP(6) WITHOUT TIME ZONE,
  camera                  VARCHAR(255),
  country                 VARCHAR(255),
  description             VARCHAR(255),
  file_extension          VARCHAR(255) NOT NULL,
  file_name               VARCHAR(255) NOT NULL,

  UNIQUE(file_name),
  FOREIGN KEY(place_id) REFERENCES public.place(id)
    ON DELETE CASCADE 
    ON UPDATE CASCADE,
  PRIMARY KEY(id)
);
