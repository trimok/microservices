drop table if exists patient;
create table patient (id bigint not null auto_increment, adresse_postale varchar(128), date_naissance datetime(6), genre varchar(1), nom varchar(64), prenom varchar(64), telephone varchar(32), primary key (id));