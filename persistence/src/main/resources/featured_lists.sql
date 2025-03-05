INSERT INTO users(userid, email, role, username, password) VALUES ( 21 , 'pawmoovie@gmail.com', 1,'Moovie', '$2a$10$ikToc9ArDQyT6IYnixyJpeiVoIEDD9IbrI390wZmBTcU1AyI107IC') ON CONFLICT DO NOTHING;

INSERT INTO moovielists(moovielistid, userid,name,description,type) VALUES(40, 21,'Top Rated Movies','These are the best rated movies according to our users!',3) ON CONFLICT DO NOTHING;
INSERT INTO moovielists(moovielistid, userid,name,description,type) VALUES(41, 21,'Top Rated TV Series','These are the best rated series according to our users!',3) ON CONFLICT DO NOTHING;
INSERT INTO moovielists(moovielistid, userid,name,description,type) VALUES(42, 21,'Top Rated Media','These are the best rated media according to our users!',3) ON CONFLICT DO NOTHING;
INSERT INTO moovielists(moovielistid, userid,name,description,type) VALUES(43, 21,'Most Popular Movies','These are the most popular movies according to our users!',3) ON CONFLICT DO NOTHING;
INSERT INTO moovielists(moovielistid, userid,name,description,type) VALUES(44, 21,'Most Popular TV Series','These are the most popular series according to our users!',3) ON CONFLICT DO NOTHING;
INSERT INTO moovielists(moovielistid, userid,name,description,type) VALUES(45, 21,'Most Popular Media','These are the most popular media according to our users!',3) ON CONFLICT DO NOTHING;