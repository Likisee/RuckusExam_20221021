create table user_info
(
    id                   bigint not null auto_increment comment 'dummy id',
    user_id              varchar(10) not null,
    user_name            varchar(30) not null,
    user_role            varchar(30) not null,
    user_password        varchar(64) not null,
    is_del               int  not null comment '0-未刪, 1-已刪',
    update_time          timestamp  not null default current_timestamp,
    primary key (id)
);
alter table user_info;
create index idx_ui_user_id on user_info(user_id);
alter table user_info add constraint unique_ui_user_id unique (user_id);

create table ticket_info
(
    id                   bigint not null auto_increment comment 'dummy id',
    ticket_id            bigint not null,
    ticket_type          varchar(20) not null,
    ticket_content       varchar(200),
    ticket_status        varchar(10) not null,
    severity_level       int not null DEFAULT 3 comment 'severity等級=1,2,3,4,5',
    severity_user        varchar(10)  not null DEFAULT 'Default',
    priority_level       int not null DEFAULT 3 comment 'priority等級=1,2,3,4,5',
    priority_user        varchar(10) not null DEFAULT 'Default',
    update_user          varchar(10) not null comment 'ticket狀態',
    update_time          timestamp not null default current_timestamp,
    primary key (id)
);
alter table ticket_info;
create index idx_ti_ticket_id on ticket_info(ticket_id);
create index idx_ti_ticket_id_time on ticket_info(ticket_id, update_time);

create table comment_info
(
    id                   bigint not null auto_increment comment 'dummy id',
    outer_ticket_id      bigint not null,
    comment_content      varchar(200),
    update_user          varchar(10) not null,
    update_time          timestamp not null default current_timestamp,
    primary key (id)
);
alter table comment_info;
create index idx_c_ticket_id on comment_info(outer_ticket_id);