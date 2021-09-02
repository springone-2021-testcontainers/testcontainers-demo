insert into messages (id, username, text)
values (nextval('hibernate_sequence'), 'Dani_Rojas', 'Football is life!');

alter table messages
    add column date_of_expiration date;

update messages
set date_of_expiration = current_date + interval '1 month';

select * from messages;


