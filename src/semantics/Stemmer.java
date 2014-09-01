package semantics;

class Stemmer
{
    public static boolean isVowel(String word, int i)
    {
        char c = Character.toLowerCase(word.charAt(i));
        boolean flag = false;

        switch (c) {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
                flag = true;
                break;

            case 'y':
                if (i != 0) {
                    if (!isVowel(word, i - 1)) {
                        flag = true;
                    }
                }
                break;
        }

        return flag;
    }

    // public static void getM(String word)
    // {

    // }

    // public static void stem(String word)
    // {

    // }
}
