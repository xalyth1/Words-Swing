SELECT * FROM Words_Lists;

SELECT * FROM (Words_Lists JOIN Words_List_Items
ON Words_Lists.id = Words_List_Items.words_list_id)
JOIN Words ON Words.id = Words_List_Items.word_id;

SELECT Words.headword, Definitions.definition FROM
(Words JOIN Words_Definitions
ON Words.id = Words_Definitions.word_id) JOIN Definitions
ON Words_Definitions.definition_id = Definitions.id;


SELECT * FROM Words_Definitions GROUP BY word_id;

DELETE FROM Words_Definitions WHERE id = 6;


SELECT * FROM Words_Lists JOIN Words_List_Items ON Words_Lists.id = Words_List_Items.words_list_id
JOIN Words ON Words_List_Items.word_id = Words.id
JOIN Words_Definitions ON Words_Definitions.word_id = Words.id
JOIN Definitions ON Definitions.id = Words_Definitions.definition_id;