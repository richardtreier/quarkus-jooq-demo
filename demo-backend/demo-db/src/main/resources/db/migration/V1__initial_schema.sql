create table "novel" (
    "id" bigserial primary key,
    "title" varchar(511) not null
);

create table "chapter" (
    "id" bigserial primary key,
    "novel_id" bigserial,
    "index" int not null,
    "title" varchar(511) not null,

   constraint fk_ch_n foreign key("novel_id") references "novel"("id")
);
