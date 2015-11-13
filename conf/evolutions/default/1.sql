# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  email                     varchar(255) not null,
  name                      varchar(255),
  password                  varchar(255),
  constraint pk_account primary key (email))
;

create table docente (
  id                        bigint not null,
  nome                      varchar(255),
  grad                      varchar(255),
  ie_grad                   varchar(255),
  pgrad                     varchar(255),
  ie_pgrad                  varchar(255),
  mest                      varchar(255),
  ie_mest                   varchar(255),
  dout                      varchar(255),
  ie_dout                   varchar(255),
  ano_adm                   integer,
  imgsistema                LONGBLOB,
  constraint pk_docente primary key (id))
;

create sequence account_seq;

create sequence docente_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists account;

drop table if exists docente;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists account_seq;

drop sequence if exists docente_seq;

