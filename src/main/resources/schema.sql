CREATE TABLE IF NOT EXISTS Films (
    id      INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         VARCHAR(50) NOT NULL,
    description  VARCHAR(250),
    releaseDate DATE        NOT NULL,
    duration     INTEGER     NOT NULL,
    MpaId   INTEGER     NOT NULL

);
CREATE TABLE IF NOT EXISTS Users (
    id  INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    VARCHAR(100) NOT NULL,
    login    VARCHAR(100) NOT NULL,
    name     VARCHAR(50)  NOT NULL,
    birthday DATE         NOT NULL
);

CREATE TABLE IF NOT EXISTS Friends (
    userId   INTEGER,
    friendId INTEGER,
    status    BOOLEAN,
    PRIMARY KEY (userId, friendId),
    FOREIGN KEY (userId) REFERENCES Users (id) ON DELETE CASCADE,
    FOREIGN KEY (friendId) REFERENCES Users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Mpa (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name   VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS Genres (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name     VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS FilmGenre (
    filmId  INTEGER,
    genreId INTEGER,
    PRIMARY KEY (filmId, genreId),
    FOREIGN KEY (filmId) REFERENCES Films (id) ON DELETE CASCADE,
    FOREIGN KEY (genreId) REFERENCES Genres (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Likes (
    filmId INTEGER,
    userId INTEGER,
    PRIMARY KEY (filmId, userId),
    FOREIGN KEY (filmId) REFERENCES Films (id) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES Users (id) ON DELETE CASCADE
);