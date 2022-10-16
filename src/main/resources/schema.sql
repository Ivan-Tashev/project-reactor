drop table if exists Player;
drop table if exists Team;

create table Player
(
    id   identity,
    name varchar(10) not null
);

insert into Player values (1, 'A'),
       (2, 'B'),
       (3, 'C');

create table Team
(
    id         identity,
    name       varchar(10) not null,
    player_ids BIGINT ARRAY
);

insert into Team
values (1, 'T1', ARRAY [1, 2]),
       (2, 'T2', ARRAY [3]);