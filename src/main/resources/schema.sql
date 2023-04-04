create table if not exists bookings (
   id bigint generated by default as identity,
   end_date timestamp,
   start_date timestamp,
   status varchar(255),
   booker_id bigint,
   item_id bigint,
   primary key (id)
);

create table if not exists comments (
   id bigint generated by default as identity,
   created timestamp,
   text varchar(255),
   author_id bigint,
   item_id bigint,
   primary key (id)
);

create table if not exists items (
   id bigint generated by default as identity,
   is_available boolean not null,
   description varchar(200),
   name varchar(255),
   owner_id bigint,
   request_id bigint,
   primary key (id)
);

create table if not exists requests (
   id bigint generated by default as identity,
   requestor_id bigint,
   primary key (id)
);

create table if not exists users (
   id bigint generated by default as identity,
   email varchar(255),
   name varchar(255),
   primary key (id)
);

alter table if exists users
   add constraint users_unique_email unique (email);

alter table if exists bookings
   add constraint bookings_users_fk
   foreign key (booker_id)
   references users;

alter table if exists bookings
   add constraint bookings_items_fk
   foreign key (item_id)
   references items;

alter table if exists comments
   add constraint comments_users_fk
   foreign key (author_id)
   references users;

alter table if exists comments
   add constraint comments_items_fk
   foreign key (item_id)
   references items;

alter table if exists items
   add constraint items_users_fk
   foreign key (owner_id)
   references users;

alter table if exists items
   add constraint items_requests_fk
   foreign key (request_id)
   references requests;

alter table if exists requests
   add constraint requests_users_fk
   foreign key (requestor_id)
   references users;
