SELECT * FROM Words_Lists;

SELECT * FROM (Words_Lists JOIN Words_List_Items
ON Words_Lists.id = Words_List_Items.words_list_id)
JOIN Words ON Words.id = Words_List_Items.word_id;
