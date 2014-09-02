package tests;
import org.junit.Test;
import static org.junit.Assert.*;
import semantics.*;

public class TestStemmer
{

    @Test
    public void testIsVowel()
    {
        assertFalse("'s' is not a vowel", Stemmer.isVowel("ShivamDixit", 0));
        assertFalse("'h' is not a vowel", Stemmer.isVowel("ShivamDixit", 1));
        assertFalse("'v' is not a vowel", Stemmer.isVowel("ShivamDixit", 3));
        assertFalse("'y' is not a vowel", Stemmer.isVowel("Yamata", 0));
        assertTrue("'a' is a vowel". Stemmer.isVowel("Yamata", 1));
        assertTrue("'a' is a vowel", Stemmer.isVowel("ShivamDixit", 4));
        assertTrue("'O' is a vowel", Stemmer.isVowel("Oliver", 0));
        assertTrue(Stemmer.isVowel("Olyver", 2));
        assertTrue(Stemmer.isVowel("HaPpY", 4));
    }
}
