drop table if exists declencheur;
create table declencheur (id bigint not null auto_increment, keyword varchar(20), primary key (id));

insert into declencheur(keyword) VALUES 
('Hémoglobine A1C'), 
('Microalbumine'),
('Taille'),
('Poids'),
('Fumeur'),
('Anormal'),
('Cholestérol'),
('Vertige'),
('Rechute'),
('Réaction'),
('Anticorps');
 

drop table if exists regle;
create table regle (
	id bigint not null auto_increment, 
	nb_declencheur int, 
	genre varchar(1),
	min_age int, 
	max_age int,
	risque int,
	primary key (id)
);
insert into regle(nb_declencheur, genre, min_age, max_age, risque) VALUES 
(0, NULL, 0, 200, 0), 
(2, NULL, 30, 200, 1),
(3, 'M', 0, 30, 2),
(4, 'F', 0, 30, 2),
(6, NULL, 30, 200, 2),
(5, 'M', 0, 30, 3),
(7, 'F', 0, 30, 3),
(8, NULL, 30, 200, 3);

