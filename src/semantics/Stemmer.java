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

    // public static void stem(String word)
    // {

    // }
}
