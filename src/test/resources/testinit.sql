delete from payment;
delete from reservation_ticket;
delete from reservation;
delete from concert_seat;
delete from concert_schedule;
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
    concert_schedule(id, concert_id, place, reservation_st_date, reservation_end_date, concert_date)
values 
    (0, 0, '잠실', '2024-07-01', '2024-07-05', '2024-07-17'),
    (1, 0, '잠실', '2024-07-01', '2024-07-05', '2024-07-18'),
    (2, 0, '잠실', '2024-07-01', '2024-07-05', '2024-07-19');

insert into 
    concert_seat(id, concert_schedule_id, location, price, reserved)
values
    (0, 0, 'R1', 100000, false),
    (1, 0, 'R2', 120000, false),
    (2, 0, 'R3', 130000, false),
    (3, 0, 'L1', 140000, false),
    (4, 0, 'L2', 150000, false),
    (5, 0, 'L3', 160000, false),
    (6, 1, 'R1', 300000, false),
    (7, 1, 'R2', 320000, false),
    (8, 1, 'R3', 330000, false),
    (9, 1, 'L1', 340000, false),
    (10, 1, 'L2', 350000, false),
    (11, 1, 'L3', 360000, false),
    (12, 2, 'R1', 200000, false),
    (13, 2, 'R2', 220000, false),
    (14, 2, 'R3', 230000, false),
    (15, 2, 'L1', 240000, false),
    (16, 2, 'L2', 250000, false),
    (17, 2, 'L3', 260000, false);