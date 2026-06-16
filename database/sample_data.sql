-- ============================================
-- Project Calipso
-- data set / sample data
-- Author: Lluís Bauzá
-- ============================================

USE calipso;

-- data set
-- security_questions
INSERT INTO security_questions (security_question)
VALUES
('What was the name of your first pet?'),
('What city were you born in?'),
('What was the first concert you attended?'),
('What was the name of your primary school?');

-- agencies
INSERT INTO agencies (cif, name, affiliate_code, discount)
VALUES
('x24569834', 'Atlantis', 'ATX245', 25.5),
('y19462836', 'Leviathan', 'LTX194', 31),
('z09683628', 'Kraken', 'KRX096', 21.25),
('w58739873', 'Barracuda', 'BK587', 25.75);

-- boats
INSERT INTO boats (boat_name, capacity)
VALUES
('Concorde', 9),
('Lusitania', 6),
('Both', 15);

-- clients
INSERT INTO clients (mail, phone, name)
VALUES
('john.doe@gmail.com', '447911123456', 'John Doe'),
('hans.mueller@gmx.de', '4915111122233', 'Hans Müller'),
('sophie.dubois@orange.fr', '33612345678', 'Sophie Dubois'),
('marco.rossi@gmail.com', '393401234567', 'Marco Rossi');

-- trip_types
INSERT INTO trip_types (id_boat, trip_option, duration_hours, departure_time, price)
VALUES
(1, 'Morning', 4, '10:00:00', 650.00),
(1, 'Afternoon', 3, '16:00:00', 525.00),
(1, 'Sunset', 2, '19:00:00', 375.00),
(2, 'Morning', 4, '10:00:00', 450.00),
(2, 'Afternoon', 3, '16:00:00', 325.00),
(2, 'Sunset', 2, '19:00:00', 275.00),
(3, 'Morning', 6, '10:00:00', 950.00),
(3, 'Afternoon', 4, '16:00:00', 690.00);

-- users
INSERT INTO users (
    first_name,
    last_name_1,
    last_name_2,
    must_change_password,
    current_password_hash,
    id_security_question,
    security_answer,
    mail,
    username
)
VALUES
(
    'Lucas',
    'Blackwood',
    'Reed',
    TRUE,
    '$2a$10$hash1',
    1,
    'Shadow',
    'lucas.blackwood@email.com',
    'lblackwood'
),
(
    'Evelyn',
    'Storm',
    'Hart',
    TRUE,
    '$2a$10$hash2',
    2,
    'London',
    'evelyn.storm@email.com',
    'estorm'
),
(
    'Nathan',
    'Silver',
    'Cross',
    TRUE,
    '$2a$10$hash3',
    3,
    'Coldplay',
    'nathan.silver@email.com',
    'nsilver'
),
(
    'Olivia',
    'Winter',
    'Vale',
    TRUE,
    '$2a$10$hash4',
    4,
    'Greenwood Primary',
    'olivia.winter@email.com',
    'owinter'
),
(
    'Adrian',
    'Knight',
    'Stone',
    TRUE,
    '$2a$10$hash5',
    1,
    'Max',
    'adrian.knight@email.com',
    'aknight'
);

-- password_history
INSERT INTO password_history (id_user, password_hash)
VALUES
(1, '$2a$10$Password2024Hash'),
(1, '$2a$10$Summer2025Hash'),
(1, '$2a$10$Winter2025Hash'),

(2, '$2a$10$BlueOceanHash'),
(2, '$2a$10$GreenForestHash'),

(3, '$2a$10$DragonFireHash'),
(3, '$2a$10$GoldenPhoenixHash'),

(4, '$2a$10$CoffeeMorningHash'),
(4, '$2a$10$FridayNightHash'),

(5, '$2a$10$MountainPeakHash'),
(5, '$2a$10$RiverStoneHash');

-- reservations
INSERT INTO reservations (
    reservation_code,
    id_client,
    id_trip_type,
    reservation_date,
    pax,
    allergies,
    final_price,
    id_agency,
    observations
)
VALUES
(
    'RES-2026-001',
    1,
    13,
    '2026-07-15',
    4,
    FALSE,
    650.00,
    1,
    'Family with two children.'
),
(
    'RES-2026-002',
    2,
    12,
    '2026-07-18',
    2,
    TRUE,
    375.00,
    2,
    'One passenger allergic to nuts.'
),
(
    'RES-2026-003',
    3,
    11,
    '2026-07-20',
    6,
    FALSE,
    325.00,
    3,
    'Celebrating a birthday.'
),
(
    'RES-2026-004',
    4,
    10,
    '2026-07-22',
    3,
    FALSE,
    525.00,
    1,
    'Requested swimming stop.'
),
(
    'RES-2026-005',
    1,
    9,
    '2026-07-25',
    8,
    TRUE,
    950.00,
    4,
    'Two passengers allergic to shellfish.'
);


