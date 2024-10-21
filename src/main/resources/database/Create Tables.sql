CREATE TABLE Words (
    id INTEGER PRIMARY KEY UNIQUE,
    headword TEXT NOT NULL UNIQUE
);
INSERT INTO Words VALUES (1, "extraordinary");

CREATE TABLE Definitions (
    id INTEGER PRIMARY KEY UNIQUE,
    definition TEXT NOT NULL UNIQUE
);
INSERT INTO Definitions VALUES (1, "wyjątkowy");

CREATE TABLE Words_Definitions (
    id INTEGER PRIMARY KEY UNIQUE,
    word_id INTEGER REFERENCES Words(id),
    definition_id INTEGER REFERENCES Definitions(id)
);

INSERT INTO Words_Definitions VALUES(1,1,1);


CREATE TABLE Words_Lists (
    id INTEGER PRIMARY KEY UNIQUE,
    name TEXT NOT NULL
);

INSERT INTO Words_Lists VALUES (1, "initial list")

CREATE TABLE Words_List_Items (
    words_list_id INTEGER REFERENCES Words_Lists(id),
    word_id INTEGER REFERENCES Words(id)
);

INSERT INTO Words_List_Items VALUES (1, 1);

SELECT * FROM Words;

SELECT Words.headword, Definitions.definition FROM
(Words JOIN Words_Definitions
ON Words.id = Words_Definitions.word_id) JOIN Definitions
ON Words_Definitions.definition_id = Definitions.id;