create table Player
(
    id   identity,
    name varchar(10) not null
);

insert into Player
values (1, 'A'),
       (2, 'B'),
       (3, 'C');

create table Team
(
    id         identity,
    name       varchar(10) not null,
    player_ids array
);

insert into Team
values (1, 'T1', '[1, 2]'),
       (2, 'T2', '[3]');