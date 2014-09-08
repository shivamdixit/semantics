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
        assertEquals(2, Stemmer.getM("probat"));
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

    @Test
    public void testEndsWithDoubleConsonant()
    {
        assertFalse(Stemmer.endsWithDoubleConsonant("hello"));
        assertFalse(Stemmer.endsWithDoubleConsonant("world"));
        assertFalse(Stemmer.endsWithDoubleConsonant("shivam"));
        assertTrue(Stemmer.endsWithDoubleConsonant("fall"));
        assertTrue(Stemmer.endsWithDoubleConsonant("ball"));
        assertTrue(Stemmer.endsWithDoubleConsonant("hiss"));
        assertTrue(Stemmer.endsWithDoubleConsonant("fizz"));
    }

    @Test
    public void testEndsWithCVC()
    {
        assertFalse(Stemmer.endsWithCVC("fail"));
        assertFalse(Stemmer.endsWithCVC("hail"));
        assertFalse(Stemmer.endsWithCVC("ball"));
        assertFalse(Stemmer.endsWithCVC("bow"));
        assertTrue(Stemmer.endsWithCVC("wil"));
        assertTrue(Stemmer.endsWithCVC("fil"));
        assertTrue(Stemmer.endsWithCVC("hop"));
    }

    @Test
    public void testStep1()
    {
        assertEquals("caress", Stemmer.step1("caresses"));
        assertEquals("poni", Stemmer.step1("ponies"));
        assertEquals("caress", Stemmer.step1("caress"));
        assertEquals("cat", Stemmer.step1("cats"));
        assertEquals("agree", Stemmer.step1("agreed"));
        assertEquals("feed", Stemmer.step1("feed"));
        assertEquals("plaster", Stemmer.step1("plastered"));
        assertEquals("motor", Stemmer.step1("motoring"));
        assertEquals("sing", Stemmer.step1("sing"));
        assertEquals("conflate", Stemmer.step1("conflated"));
        assertEquals("trouble", Stemmer.step1("troubled"));
        assertEquals("size", Stemmer.step1("sized"));
        assertEquals("hop", Stemmer.step1("hopping"));
        assertEquals("hiss", Stemmer.step1("hissing"));
        assertEquals("file", Stemmer.step1("filing"));
        assertEquals("fail", Stemmer.step1("failing"));
        assertEquals("happi", Stemmer.step1("happy"));
        assertEquals("sky", Stemmer.step1("sky"));
    }

    @Test
    public void testStep2()
    {
        assertEquals("relate", Stemmer.step2("relational"));
        assertEquals("condition", Stemmer.step2("conditional"));
        assertEquals("valence", Stemmer.step2("valenci"));
        assertEquals("hesitance", Stemmer.step2("hesitanci"));
        assertEquals("digitize", Stemmer.step2("digitizer"));
        assertEquals("conformable", Stemmer.step2("conformabli"));
        assertEquals("radical", Stemmer.step2("radicalli"));
        assertEquals("different", Stemmer.step2("differentli"));
        assertEquals("vile", Stemmer.step2("vileli"));
        assertEquals("analogous", Stemmer.step2("analogousli"));
        assertEquals("rational", Stemmer.step2("rational"));
        assertEquals("callous", Stemmer.step2("callousness"));
        assertEquals("formal", Stemmer.step2("formaliti"));
        assertEquals("sensitive", Stemmer.step2("sensitiviti"));
        assertEquals("sensible", Stemmer.step2("sensibiliti"));
    }

    @Test
    public void testStep3()
    {
        assertEquals("triplic", Stemmer.step3("triplicate"));
        assertEquals("form", Stemmer.step3("formative"));
        assertEquals("formal", Stemmer.step3("formalize"));
        assertEquals("electric", Stemmer.step3("electriciti"));
        assertEquals("hope", Stemmer.step3("hopeful"));
        assertEquals("good", Stemmer.step3("goodness"));
    }

    @Test
    public void testStep4()
    {
        assertEquals("reviv", Stemmer.step4("revival"));
        assertEquals("allow", Stemmer.step4("allowance"));
        assertEquals("infer", Stemmer.step4("inference"));
        assertEquals("airlin", Stemmer.step4("airliner"));
        assertEquals("gyroscop", Stemmer.step4("gyroscopic"));

        assertEquals("adopt", Stemmer.step4("adoption"));
        assertEquals("homolog", Stemmer.step4("homologou"));
        assertEquals("commun", Stemmer.step4("communism"));
        assertEquals("activ", Stemmer.step4("activate"));
        assertEquals("angular", Stemmer.step4("angulariti"));
        assertEquals("bowdler", Stemmer.step4("bowdlerize"));
    }

    @Test
    public void testStep5()
    {
        // assertEquals("probat", Stemmer.step4("probate"));
        // assertEquals("rate", Stemmer.step4("rate"));
        // assertEquals("ceas", Stemmer.step4("cease"));
    }

    @Test
    public void testStem()
    {
        assertEquals("bowl", Stemmer.stem("bowling"));
        assertEquals("negoti", Stemmer.stem("negotiation"));
        assertEquals("farm", Stemmer.stem("farming"));
        assertEquals("malleabl", Stemmer.stem("malleable"));
        assertEquals("the", Stemmer.stem("the"));
        assertEquals("at", Stemmer.stem("at"));
        assertEquals("demonstr", Stemmer.stem("demonstrated"));
        assertEquals("femal", Stemmer.stem("females"));
        assertEquals("compon", Stemmer.stem("components"));
        assertEquals("paradigm", Stemmer.stem("paradigms"));
        assertEquals("independ", Stemmer.stem("independent"));
    }
}
