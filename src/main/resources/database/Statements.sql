SELECT * FROM Words_Lists;

SELECT * FROM
Words_Lists
JOIN Words_List_Items ON Words_Lists.id = Words_List_Items.words_list_id
JOIN Words ON Words.id = Words_List_Items.word_id;

-- Display all the head with their possibly multiple definitions concatenated and coma separated
SELECT Words.headword, GROUP_CONCAT(definition, ', ') AS defintions
FROM Words
JOIN Words_Definitions ON Words.id = Words_Definitions.word_id
JOIN Definitions ON Words_Definitions.definition_id = Definitions.id
GROUP BY Words.headword;

--Display all headwords in each lists
SELECT Words_Lists.name, Words.headword
FROM Words_Lists
INNER JOIN Words_List_Items ON Words_Lists.id = Words_List_Items.words_list_id
INNER JOIN Words ON Words_List_Items.word_id = Words.id
ORDER BY Words_Lists.name





SELECT * FROM Words_Definitions GROUP BY word_id;

DELETE FROM Words_Definitions WHERE id = 6;


SELECT * FROM Words_Lists
JOIN Words_List_Items ON Words_Lists.id = Words_List_Items.words_list_id
JOIN Words ON Words_List_Items.word_id = Words.id
JOIN Words_Definitions ON Words_Definitions.word_id = Words.id
JOIN Definitions ON Definitions.id = Words_Definitions.definition_id;