delete from queue_token;
delete from payment;
delete from reservation_ticket;
delete from reservation;
delete from concert_seat;
delete from concert_schedule;
delete from concert_place;
delete from concert;
delete from user_asset;

insert into 
    user_asset(user_id, balance)
values 
    (0, 0),
    (1, 0);

insert into 
    concert(id, name, description)
values 
    (0, '아이유 콘서트', '2024.07.17 ~ 2024.07.19');

insert into
    concert_place(id, place)
values
    (0, '잠실');

insert into 
    concert_schedule(id, concert_id, concert_place_id, reservation_st_date, reservation_end_date, concert_date)
values 
    (0, 0, 0, '2024-07-01', '2024-07-05', '2024-07-17'),
    (1, 0, 0, '2024-07-01', '2024-07-05', '2024-07-18'),
    (2, 0, 0, '2024-07-01', '2024-07-05', '2024-07-19');

insert into 
    concert_seat(id, concert_place_id, location, version, price)
values
    (0, 0, 'R1', 0,100000),
    (1, 0, 'R2', 0,120000),
    (2, 0, 'R3', 0,130000),
    (3, 0, 'L1', 0,140000),
    (4, 0, 'L2', 0,150000),
    (5, 0, 'L3', 0,160000),
    (6, 1, 'R1', 0,300000),
    (7, 1, 'R2', 0,320000),
    (8, 1, 'R3', 0,330000),
    (9, 1, 'L1', 0,340000),
    (10, 1, 'L2',0, 350000),
    (11, 1, 'L3',0, 360000),
    (12, 2, 'R1',0, 200000),
    (13, 2, 'R2',0, 220000),
    (14, 2, 'R3',0, 230000),
    (15, 2, 'L1',0, 240000),
    (16, 2, 'L2',0, 250000),
    (17, 2, 'L3',0, 260000);