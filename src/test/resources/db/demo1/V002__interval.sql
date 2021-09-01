insert into messages (id, username, text)
values (nextval('hibernate_sequence'), 'Dani_Rojas', 'Football is life!');

create table if not exists old_messages AS select * from messages;

select * from old_messages;

alter table messages
    add column date_of_expiration date;

update messages
set date_of_expiration = current_date + interval '1 month';

select * from messages;


