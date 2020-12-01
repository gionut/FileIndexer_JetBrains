package tests;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import model.StopWordsTokenizer;
import model.WordsTokenizer;
import org.junit.jupiter.api.Test;

public class TokenizersTest {

    @Test
    public void wordsTokenizer_tokenize_should_return_words_in_String()
    {
        String text = "Five little ducks went out one day\n" +
                            "Mother duck said, \"Quack, quack, quack, quack\"";
        String[] expectedResult = new String[]{"five", "little", "ducks", "went", "out", "one", "day", "mother",
        "duck", "said", "quack", "quack", "quack", "quack"};

        WordsTokenizer wordsTokenizer = new WordsTokenizer();
        String[] words = wordsTokenizer.tokenize(text);
        assertArrayEquals(words, expectedResult);
    }

    @Test
    public void StopWordsTokenizer_should_return_words_in_string_without_stopWords()
    {
        String text = "Five little ducks went out one day\n" +
                "Mother duck said, \"Quack, quack, quack, quack\"";
        String[] expectedResult = new String[]{"ducks", "day", "mother",
                "duck", "quack", "quack", "quack", "quack"};

        StopWordsTokenizer stopWordsTokenizer = new StopWordsTokenizer();
        String[] words = stopWordsTokenizer.tokenize(text);
        assertArrayEquals(words, expectedResult);
    }

    public void run()
    {
        wordsTokenizer_tokenize_should_return_words_in_String();
        StopWordsTokenizer_should_return_words_in_string_without_stopWords();
    }
}
