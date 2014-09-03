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
        if (isVowel(s, lastIndex) && isVowel(s, lastIndex - 1)) {
            return true;
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
                    isVowel(s, s.length() - 3)
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

    // public static void stem(String word)
    // {

    // }
}
