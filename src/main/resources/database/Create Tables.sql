CREATE TABLE Words (
    id INTEGER PRIMARY KEY,
    headword TEXT NOT NULL UNIQUE
);

CREATE TABLE Definitions (
    id INTEGER PRIMARY KEY,
    definition TEXT NOT NULL UNIQUE
);

CREATE TABLE Words_Definitions (
    id INTEGER PRIMARY KEY,
    word_id INTEGER REFERENCES Words(id),
    definition_id INTEGER REFERENCES Definitions(id)
);

CREATE TABLE Words_Lists (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL
);


CREATE TABLE Words_List_Items (
    words_list_id INTEGER REFERENCES Words_Lists(id),
    word_id INTEGER REFERENCES Words(id)
);