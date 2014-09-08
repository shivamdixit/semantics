/**
 * Copyright (c) 2014 Shivam Dixit <shivamd001@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2,
 * or (at your option) any later version, as published by the Free
 * Software Foundation
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package semantics;

/**
 * Implementation of Porter stemming algorithm
 *
 * It is used for extracting the root from a word. It has many
 * application in search engines, semantic relations, data mining etc
 */
public class Stemmer
{
    public static boolean ends(String s, String substr)
    {
        // Case insensitive comparison
        if (s.toLowerCase().endsWith(substr.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean containsVowel(String s)
    {
        for (int i = 0; i < s.length(); i++) {
            if (isVowel(s, i)) {
                return true;
            }
        }

        return false;
    }

    public static boolean endsWithDoubleConsonant(String s)
    {
        int lastIndex = s.length() - 1;

        if (s.charAt(lastIndex) == s.charAt(lastIndex-1)) {
            if (!isVowel(s, lastIndex) && !isVowel(s, lastIndex - 1)) {
                return true;
            }
        }

        return false;
    }

    public static boolean endsWithCVC(String s)
    {
        char lastChar = Character.toLowerCase(s.charAt(s.length() - 1));

        if (s.length() >= 3) {
            if (lastChar != 'w' && lastChar != 'x' && lastChar != 'y') {
                if (
                    !isVowel(s, s.length() - 1) &&
                    isVowel(s, s.length() - 2) &&
                    !isVowel(s, s.length() - 3)
                ) {
                    return true;
                }
            }
        }

        return false;
    }

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

    public static int getM(String word)
    {
        int m = 0;

        for (int i = 0; i < word.length() ; i++) {
            if (isVowel(word, i)) {
                if ((i + 1) < word.length()) {
                    if (!isVowel(word, i + 1)) {
                        m++;
                    }
                }
            }
        }

        return m;
    }

    public static String step1(String word)
    {
        boolean flag = false;

        // Step 1a
        if (ends(word, "SSES")) {
            word = word.replaceAll("(\\w+)sses$", "$1ss");
        } else if (ends(word, "IES")) {
            word = word.replaceAll("(\\w+)ies$", "$1i");
        } else if (ends(word, "SS")) {
            // Do nothing. Required to skip the last if
        } else if (ends(word, "S")) {
            word = word.replaceAll("(\\w+)s$", "$1");
        }

        // Step 1b
        if (ends(word, "EED")) {
            if (getM(word.replaceAll("(\\w+)eed$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)d$", "$1");
            }
        } else if (ends(word, "ED")) {
            if (containsVowel(word.replaceAll("(\\w+)ed$", "$1"))) {
                word = word.replaceAll("(\\w+)ed$", "$1");
                flag = true;
            }
        } else if (ends(word, "ING")) {
            if (containsVowel(word.replaceAll("(\\w+)ing$", "$1"))) {
                word = word.replaceAll("(\\w+)ing$", "$1");
                flag = true;
            }
        }

        if (flag) {
            if (ends(word, "AT") || ends(word, "BL") || ends(word, "IZ")) {
                word = word + "e";
            } else if (
                endsWithDoubleConsonant(word) &&
                !(
                    ends(word, "L") ||
                    ends(word, "S") ||
                    ends(word, "Z")
                )
            ) {
                word = word.substring(0, word.length() - 1);
            } else if (endsWithCVC(word) && (getM(word) == 1)) {
                word = word + 'e';
            }
        }

        if (ends(word, "Y")) {
            if (containsVowel(word.replaceAll("(\\w+)y$", "$1"))) {
                word = word.replaceAll("(\\w+)y$", "$1i");
            }
        }

        return word;
    }

    public static String step2(String word)
    {
        if (ends(word, "ATIONAL")) {
            if (getM(word.replaceAll("(\\w+)ational$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)ational$", "$1ate");
            }

        } else if (ends(word, "TIONAL")) {
            if (getM(word.replaceAll("(\\w+)tional$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)tional$", "$1tion");
            }

        } else if (ends(word, "ENCI")) {
            if (getM(word.replaceAll("(\\w+)enci$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)enci$", "$1ence");
            }

        } else if (ends(word, "ANCI")) {
            if (getM(word.replaceAll("(\\w+)anci$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)anci$", "$1ance");
            }

        } else if (ends(word, "IZER")) {
            if (getM(word.replaceAll("(\\w+)izer$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)izer$", "$1ize");
            }

        } else if (ends(word, "ABLI")) {
            if (getM(word.replaceAll("(\\w+)abli$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)abli$", "$1able");
            }

        } else if (ends(word, "ALLI")) {
            if (getM(word.replaceAll("(\\w+)alli$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)alli$", "$1al");
            }

        } else if (ends(word, "ENTLI")) {
            if (getM(word.replaceAll("(\\w+)entli$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)entli$", "$1ent");
            }

        } else if (ends(word, "ELI")) {
            if (getM(word.replaceAll("(\\w+)eli$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)eli$", "$1e");
            }

        } else if (ends(word, "OUSLI")) {
            if (getM(word.replaceAll("(\\w+)ousli$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)ousli$", "$1ous");
            }

        } else if (ends(word, "IZATION")) {
            if (getM(word.replaceAll("(\\w+)ization$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)ization$", "$1ize");
            }

        } else if (ends(word, "ATION")) {
            if (getM(word.replaceAll("(\\w+)ation$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)ation$", "$1ate");
            }

        } else if (ends(word, "ATOR")) {
            if (getM(word.replaceAll("(\\w+)ator$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)ator$", "$1ate");
            }

        } else if (ends(word, "ALISM")) {
            if (getM(word.replaceAll("(\\w+)alism$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)alism$", "$1al");
            }

        } else if (ends(word, "IVENESS")) {
            if (getM(word.replaceAll("(\\w+)iveness$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)iveness$", "$1ive");
            }

        } else if (ends(word, "FULNESS")) {
            if (getM(word.replaceAll("(\\w+)fulness$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)fulness$", "$1ful");
            }

        } else if (ends(word, "OUSNESS")) {
            if (getM(word.replaceAll("(\\w+)ousness$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)ousness$", "$1ous");
            }

        } else if (ends(word, "ALITI")) {
            if (getM(word.replaceAll("(\\w+)aliti$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)aliti$", "$1al");
            }

        } else if (ends(word, "IVITI")) {
            if (getM(word.replaceAll("(\\w+)iviti$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)iviti$", "$1ive");
            }

        } else if (ends(word, "BILITI")) {
            if (getM(word.replaceAll("(\\w+)biliti$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)biliti$", "$1ble");
            }
        }

        return word;
    }

    public static String step3(String word)
    {
        if (ends(word, "ICATE")) {
            if (getM(word.replaceAll("(\\w+)icate$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)icate$", "$1ic");
            }

        } else if (ends(word, "ATIVE")) {
            if (getM(word.replaceAll("(\\w+)ative$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)ative$", "$1");
            }

        } else if (ends(word, "ALIZE")) {
            if (getM(word.replaceAll("(\\w+)alize$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)alize$", "$1al");
            }

        } else if (ends(word, "ICITI")) {
            if (getM(word.replaceAll("(\\w+)iciti$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)iciti$", "$1ic");
            }

        } else if (ends(word, "FUL")) {
            if (getM(word.replaceAll("(\\w+)ful$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)ful$", "$1");
            }

        } else if (ends(word, "NESS")) {
            if (getM(word.replaceAll("(\\w+)ness$", "$1")) > 0) {
                word = word.replaceAll("(\\w+)ness$", "$1");
            }
        }

        return word;
    }

    public static String step4(String word)
    {
        String rootWord;

        if (ends(word, "AL")) {
            rootWord = word.replaceAll("(\\w+)al$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "ANCE")) {
            rootWord = word.replaceAll("(\\w+)ance$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "ENCE")) {
            rootWord = word.replaceAll("(\\w+)ence$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "ER")) {
            rootWord = word.replaceAll("(\\w+)er$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "IC")) {
            rootWord = word.replaceAll("(\\w+)ic$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "ABLE")) {
            rootWord = word.replaceAll("(\\w+)able$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "IBLE")) {
            rootWord = word.replaceAll("(\\w+)ible$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "ANT")) {
            rootWord = word.replaceAll("(\\w+)ant$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "EMENT")) {
            rootWord = word.replaceAll("(\\w+)ement$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "MENT")) {
            rootWord = word.replaceAll("(\\w+)ment$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "ENT")) {
            rootWord = word.replaceAll("(\\w+)ent$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "ION")) {
            rootWord = word.replaceAll("(\\w+)ion$", "$1");

            if (
                getM(rootWord) > 1 &&
                (ends(rootWord, "S") || ends(rootWord, "T"))
            ) {
                word = rootWord;
            }

        } else if (ends(word, "OU")) {
            rootWord = word.replaceAll("(\\w+)ou$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "ISM")) {
            rootWord = word.replaceAll("(\\w+)ism$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "ATE")) {
            rootWord = word.replaceAll("(\\w+)ate$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "ITI")) {
            rootWord = word.replaceAll("(\\w+)iti$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "OUS")) {
            rootWord = word.replaceAll("(\\w+)ous$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "IVE")) {
            rootWord = word.replaceAll("(\\w+)ive$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }

        } else if (ends(word, "IZE")) {
            rootWord = word.replaceAll("(\\w+)ize$", "$1");

            if (getM(rootWord) > 1) {
                word = rootWord;
            }
        }

        return word;
    }

    public static String step5(String word)
    {
        String rootWord = word.replaceAll("(\\w+)e$", "$1");

        if (getM(rootWord) > 1) {
            word = rootWord;
        } else if (getM(rootWord) == 1 && !(endsWithCVC(word))) {
            word = rootWord;
        }

        return word;
    }

    public static String stem(String word)
    {
        return new String(step5(step4(step3(step2(step1(word))))));
    }
}
