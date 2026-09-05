-- CLIENT
INSERT INTO client (first_name, last_name, email, phone_number)
VALUES ('Test', 'Client', 'test@test.com', '600123456');

-- BOATS
INSERT INTO boat (boat_name, capacity)
VALUES
    ('Concorde', 9),
    ('Lusitania', 6);


-- TRIPS - CONCORDE
INSERT INTO trip (boat_id, type, duration_minutes, price)
VALUES
    (
        (SELECT id FROM boat WHERE boat_name = 'Concorde'),
        'MORNING',
        240,
        350.00
    ),
    (
        (SELECT id FROM boat WHERE boat_name = 'Concorde'),
        'EVENING',
        240,
        350.00
    ),
    (
        (SELECT id FROM boat WHERE boat_name = 'Concorde'),
        'SUNSET',
        120,
        250.00
    );


-- TRIPS - LUSITANIA
INSERT INTO trip (boat_id, type, duration_minutes, price)
VALUES
    (
        (SELECT id FROM boat WHERE boat_name = 'Lusitania'),
        'MORNING',
        240,
        300.00
    ),
    (
        (SELECT id FROM boat WHERE boat_name = 'Lusitania'),
        'EVENING',
        240,
        300.00
    ),
    (
        (SELECT id FROM boat WHERE boat_name = 'Lusitania'),
        'SUNSET',
        120,
        220.00
    );

-- SLOTS - CONCORDE

INSERT INTO slot (trip_id, date, departure_time, availability)
VALUES
    (
        (
            SELECT t.id
            FROM trip t
                     JOIN boat b ON t.boat_id = b.id
            WHERE b.boat_name = 'Concorde'
              AND t.type = 'MORNING'
        ),
        '2026-09-15',
        '09:00',
        'AVAILABLE'
    ),
    (
        (
            SELECT t.id
            FROM trip t
                     JOIN boat b ON t.boat_id = b.id
            WHERE b.boat_name = 'Concorde'
              AND t.type = 'EVENING'
        ),
        '2026-09-15',
        '15:00',
        'AVAILABLE'
    ),
    (
        (
            SELECT t.id
            FROM trip t
                     JOIN boat b ON t.boat_id = b.id
            WHERE b.boat_name = 'Concorde'
              AND t.type = 'SUNSET'
        ),
        '2026-09-15',
        '19:30',
        'AVAILABLE'
    );


-- SLOTS - LUSITANIA

INSERT INTO slot (trip_id, date, departure_time, availability)
VALUES
    (
        (
            SELECT t.id
            FROM trip t
                     JOIN boat b ON t.boat_id = b.id
            WHERE b.boat_name = 'Lusitania'
              AND t.type = 'MORNING'
        ),
        '2026-09-15',
        '09:00',
        'AVAILABLE'
    ),
    (
        (
            SELECT t.id
            FROM trip t
                     JOIN boat b ON t.boat_id = b.id
            WHERE b.boat_name = 'Lusitania'
              AND t.type = 'EVENING'
        ),
        '2026-09-15',
        '15:00',
        'AVAILABLE'
    ),
    (
        (
            SELECT t.id
            FROM trip t
                     JOIN boat b ON t.boat_id = b.id
            WHERE b.boat_name = 'Lusitania'
              AND t.type = 'SUNSET'
        ),
        '2026-09-15',
        '19:30',
        'AVAILABLE'
    );