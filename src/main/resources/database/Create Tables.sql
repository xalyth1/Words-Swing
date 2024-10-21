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
    FOREIGN KEY word_id INTEGER REFERENCES Words(id),
    FOREIGN KEY definition_id INTEGER REFERENCES Definitions(id)
);

CREATE TABLE Words_Lists (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL
);


CREATE TABLE Words_List_Items (
    words_list_id INTEGER,
    word_id INTEGER,
    PRIMARY KEY (words_list_id, word_id),
    FOREIGN KEY (words_list_id) REFERENCES Words_Lists(id),
    FOREIGN KEY (word_id) REFERENCES Words(id)
);