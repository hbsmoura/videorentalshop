INSERT INTO public.client VALUES ('8a217068-dbb2-4122-8ba7-2c5c99e0e043', 'Karen Peris', '$2a$10$tAsxHN7KNG6HnNCG0mDi1.fRV7CxXm.459tA4vyxoblXOI611QT0K', 'karenperis');
INSERT INTO public.client VALUES ('dd8d8b67-5c15-4f83-827e-f572ba0f71ea', 'Don Peris', '$2a$10$tAsxHN7KNG6HnNCG0mDi1.fRV7CxXm.459tA4vyxoblXOI611QT0K', 'donperis');
INSERT INTO public.client VALUES ('1b40dd45-7525-42f9-ba3d-a81abd200111', 'Mike Bitts', '$2a$10$tAsxHN7KNG6HnNCG0mDi1.fRV7CxXm.459tA4vyxoblXOI611QT0K', 'mikebitts');

INSERT INTO public.employee VALUES ('07341e10-077e-4674-a6d1-2030499e44b6', 'Robin Guthrie', '$2a$10$tAsxHN7KNG6HnNCG0mDi1.fRV7CxXm.459tA4vyxoblXOI611QT0K', 'robinguthrie', false);
INSERT INTO public.employee VALUES ('548d1b48-97b5-4723-b11a-0abf96b16837', 'Will Heggie', '$2a$10$tAsxHN7KNG6HnNCG0mDi1.fRV7CxXm.459tA4vyxoblXOI611QT0K', 'willheggie', false);
INSERT INTO public.employee VALUES ('1e47c6f2-0a3a-4cd3-a585-7f03798ccdb8', 'Simon Raymonde', '$2a$10$tAsxHN7KNG6HnNCG0mDi1.fRV7CxXm.459tA4vyxoblXOI611QT0K', 'simonraymonde', true);
INSERT INTO public.employee VALUES ('8af2b086-ed35-45b8-845a-e2fd1a5e3127', 'Elizabeth Fraser', '$2a$10$tAsxHN7KNG6HnNCG0mDi1.fRV7CxXm.459tA4vyxoblXOI611QT0K', 'bethfraser', false);

INSERT INTO public.movie VALUES ('5d21ad3e-b31a-4bba-aee0-ab49853cb069', 'Quentin Tarantino', 154, 'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.', 3, 'Pulp Fiction', 3, 1.20, 1994);
INSERT INTO public.movie VALUES ('3d697360-401a-432d-83f6-bc149612911e', 'Stanley Kubrick', 146, 'Jack Torrance becomes winter caretaker at the isolated Overlook Hotel in Colorado, hoping to cure his writer''s block. He settles in along with his wife, Wendy, and his son, Danny, who is plagued by psychic premonitions', 3, 'The Shining', 3, 1.30, 1980);
INSERT INTO public.movie VALUES ('c7ab08ed-cf72-4b21-813b-d78927977f6b', 'Quentin Tarantino', 165, 'With the help of a German bounty-hunter, a freed slave sets out to rescue his wife from a brutal plantation-owner in Mississippi', 4, 'Django Unchained', 4, 1.10, 2012);

INSERT INTO public.movie_genres VALUES ('5d21ad3e-b31a-4bba-aee0-ab49853cb069', 7);
INSERT INTO public.movie_genres VALUES ('5d21ad3e-b31a-4bba-aee0-ab49853cb069', 5);
INSERT INTO public.movie_genres VALUES ('5d21ad3e-b31a-4bba-aee0-ab49853cb069', 0);
INSERT INTO public.movie_genres VALUES ('3d697360-401a-432d-83f6-bc149612911e', 17);
INSERT INTO public.movie_genres VALUES ('3d697360-401a-432d-83f6-bc149612911e', 11);
INSERT INTO public.movie_genres VALUES ('3d697360-401a-432d-83f6-bc149612911e', 10);
INSERT INTO public.movie_genres VALUES ('c7ab08ed-cf72-4b21-813b-d78927977f6b', 9);
INSERT INTO public.movie_genres VALUES ('c7ab08ed-cf72-4b21-813b-d78927977f6b', 7);
INSERT INTO public.movie_genres VALUES ('c7ab08ed-cf72-4b21-813b-d78927977f6b', 18);
INSERT INTO public.movie_genres VALUES ('c7ab08ed-cf72-4b21-813b-d78927977f6b', 0);

INSERT INTO public.tb_themes VALUES ('5d21ad3e-b31a-4bba-aee0-ab49853cb069', 'Mafia');
INSERT INTO public.tb_themes VALUES ('5d21ad3e-b31a-4bba-aee0-ab49853cb069', 'Morality');
INSERT INTO public.tb_themes VALUES ('5d21ad3e-b31a-4bba-aee0-ab49853cb069', 'Violence');
INSERT INTO public.tb_themes VALUES ('5d21ad3e-b31a-4bba-aee0-ab49853cb069', 'Bang bang');
INSERT INTO public.tb_themes VALUES ('5d21ad3e-b31a-4bba-aee0-ab49853cb069', 'Revenge');
INSERT INTO public.tb_themes VALUES ('3d697360-401a-432d-83f6-bc149612911e', 'Fear');
INSERT INTO public.tb_themes VALUES ('3d697360-401a-432d-83f6-bc149612911e', 'Violence');
INSERT INTO public.tb_themes VALUES ('3d697360-401a-432d-83f6-bc149612911e', 'Insanity');
INSERT INTO public.tb_themes VALUES ('3d697360-401a-432d-83f6-bc149612911e', 'Murder');
INSERT INTO public.tb_themes VALUES ('3d697360-401a-432d-83f6-bc149612911e', 'Revenge');
INSERT INTO public.tb_themes VALUES ('3d697360-401a-432d-83f6-bc149612911e', 'Madness');
INSERT INTO public.tb_themes VALUES ('3d697360-401a-432d-83f6-bc149612911e', 'Paranormal Activity');
INSERT INTO public.tb_themes VALUES ('c7ab08ed-cf72-4b21-813b-d78927977f6b', 'Slavery');
INSERT INTO public.tb_themes VALUES ('c7ab08ed-cf72-4b21-813b-d78927977f6b', 'Morality');
INSERT INTO public.tb_themes VALUES ('c7ab08ed-cf72-4b21-813b-d78927977f6b', 'Violence');
INSERT INTO public.tb_themes VALUES ('c7ab08ed-cf72-4b21-813b-d78927977f6b', 'Bang bang');
INSERT INTO public.tb_themes VALUES ('c7ab08ed-cf72-4b21-813b-d78927977f6b', 'Revenge');
INSERT INTO public.tb_themes VALUES ('c7ab08ed-cf72-4b21-813b-d78927977f6b', 'Racism');
INSERT INTO public.tb_themes VALUES ('c7ab08ed-cf72-4b21-813b-d78927977f6b', 'Power');