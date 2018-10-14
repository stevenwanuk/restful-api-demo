INSERT INTO User (name) VALUES  ('user_a');
INSERT INTO User (name) VALUES  ('user_b');
INSERT INTO User (name) VALUES  ('user_c');
INSERT INTO User (name) VALUES  ('user_d');
INSERT INTO User (name) VALUES  ('user_e');
INSERT INTO User (name) VALUES  ('user_f');
  
INSERT INTO Offer (name, detail, create_date, expiry_date, is_deleted, owner_id) VALUES ('offer_a_1', 'offer_1 of user_a', now() , DATEADD('DAY', 1, now() ), false, 1)
INSERT INTO Offer (name, detail, create_date, expiry_date, is_deleted, owner_id) VALUES ('offer_a_2', 'offer_2 of user_a', now() , DATEADD('DAY', 1, now() ), false, 1)
INSERT INTO Offer (name, detail, create_date, expiry_date, is_deleted, owner_id) VALUES ('offer_a_3_expired', 'offer_3 of user_a', now() , DATEADD('DAY', -1, now() ), false, 1)
INSERT INTO Offer (name, detail, create_date, expiry_date, is_deleted, owner_id) VALUES ('offer_a_4_expired', 'offer_4 of user_a', now() , DATEADD('DAY', 1, now() ), true, 1)
INSERT INTO Offer (name, detail, create_date, expiry_date, is_deleted, owner_id) VALUES ('offer_b_5', 'offer_5 of user_b', now() , DATEADD('DAY', 1, now() ), false, 2)
INSERT INTO Offer (name, detail, create_date, expiry_date, is_deleted, owner_id) VALUES ('offer_c_6', 'offer_6 of user_c', now() , DATEADD('DAY', 1, now() ), false, 3)