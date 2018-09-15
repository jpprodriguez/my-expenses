# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table my_expense (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  name                      varchar(255),
  total                     double,
  expense_month             bigint,
  total_credit              double,
  constraint pk_my_expense primary key (id))
;

create table my_money (
  id                        bigint auto_increment not null,
  concept                   varchar(255),
  type                      varchar(255),
  money_date                integer,
  money_value               double,
  MY_EXPENSE_ID             bigint,
  constraint pk_my_money primary key (id))
;

create table my_user (
  id                        bigint auto_increment not null,
  user                      varchar(255),
  password                  varchar(255),
  constraint pk_my_user primary key (id))
;

alter table my_expense add constraint fk_my_expense_user_1 foreign key (user_id) references my_user (id) on delete restrict on update restrict;
create index ix_my_expense_user_1 on my_expense (user_id);
alter table my_money add constraint fk_my_money_expense_2 foreign key (MY_EXPENSE_ID) references my_expense (id) on delete restrict on update restrict;
create index ix_my_money_expense_2 on my_money (MY_EXPENSE_ID);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table my_expense;

drop table my_money;

drop table my_user;

SET FOREIGN_KEY_CHECKS=1;

