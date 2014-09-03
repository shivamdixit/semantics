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
        assertTrue("'a' is a vowel", Stemmer.isVowel("Yamata", 1));
        assertTrue("'a' is a vowel", Stemmer.isVowel("ShivamDixit", 4));
        assertTrue("'O' is a vowel", Stemmer.isVowel("Oliver", 0));
        assertTrue(Stemmer.isVowel("Olyver", 2));
        assertTrue(Stemmer.isVowel("HaPpY", 4));
    }

    @Test
    public void testGetM()
    {
        assertEquals(0, Stemmer.getM("TR"));
        assertEquals(0, Stemmer.getM("EE"));
        assertEquals(0, Stemmer.getM("TREE"));
        assertEquals(0, Stemmer.getM("Y"));
        assertEquals(0, Stemmer.getM("BY"));

        assertEquals(1, Stemmer.getM("TROUBLE"));
        assertEquals(1, Stemmer.getM("OATS"));
        assertEquals(1, Stemmer.getM("TREES"));
        assertEquals(1, Stemmer.getM("IVY"));

        assertEquals(2, Stemmer.getM("Shivam"));
        assertEquals(2, Stemmer.getM("PRIVATE"));
        assertEquals(2, Stemmer.getM("ORRERY"));
    }

    @Test
    public void testEnds()
    {
        assertFalse(Stemmer.ends("shivam", "a"));
        assertFalse(Stemmer.ends("SHIVAM", "IV"));
        assertTrue(Stemmer.ends("SHIVAM", "vam"));
        assertTrue(Stemmer.ends("Quora", "A"));    // Case insensitive
        assertTrue(Stemmer.ends("ShIvAM", "m"));   // Case insensitive
        assertTrue(Stemmer.ends("a", "a"));        // Boundary case
    }

    @Test
    public void testContainsVowel()
    {
        assertFalse(Stemmer.containsVowel("PHP"));
        assertFalse(Stemmer.containsVowel("TWNDLLNGS"));
        assertTrue(Stemmer.containsVowel("KVY"));
        assertTrue(Stemmer.containsVowel("SuN"));
        assertTrue(Stemmer.containsVowel("JOHN"));
    }
}
