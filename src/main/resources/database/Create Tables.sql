CREATE TABLE IF NOT EXISTS Words (
    id INTEGER PRIMARY KEY,
    headword TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Definitions (
    id INTEGER PRIMARY KEY,
    definition TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Words_Definitions (
    id INTEGER PRIMARY KEY,
    word_id INTEGER,
    definition_id INTEGER,
    FOREIGN KEY (word_id) REFERENCES Words(id),
    FOREIGN KEY (definition_id) REFERENCES Definitions(id)
);

CREATE TABLE IF NOT EXISTS Words_Lists (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL
);


CREATE TABLE IF NOT EXISTS Words_List_Items (
    words_list_id INTEGER,
    word_id INTEGER,
    PRIMARY KEY (words_list_id, word_id),
    FOREIGN KEY (words_list_id) REFERENCES Words_Lists(id),
    FOREIGN KEY (word_id) REFERENCES Words(id)
);