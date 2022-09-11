-- Test Data: ATG Novel
insert into "novel" ("id", "title") values (100, 'Against The Gods');
insert into "chapter" ("id", "novel_id", "index", "title")
  values
    (101, 100, 1, 'ATG Chapter 1'),
    (102, 100, 2, 'ATG Chapter 2'),
    (103, 100, 3, 'ATG Chapter 3');
