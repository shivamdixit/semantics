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

import java.io.*;
import java.util.*;

/**
 * Class to generate pattern strings
 */
class PatternString
{
    private static final String dirname = "../input/Docs";
    private static final String stopWordsFileName = "../input/MYSTWORD.TXT";
    private static final String outputDir = "../output/";
    private static LinkedHashMap<String, Integer> finalMap;
    private static List<HashMap<String, Integer>> listOfMaps;
    private static int[][] frequencyMatrix;
    private static float[][] correlationMatrix;

    private static HashSet<String> stopWords;
    private static int stopWordsCount;
    private static int minCutOffFreq;
    private static int maxCutOffFreq;
    private static int numOfColsInMatrix;
    private static int numOfRowsInMatrix;

    /**
     * Class constructor
     */
    public PatternString()
    {
        stopWords = new HashSet<String>();
        finalMap = new LinkedHashMap<String, Integer>();
        // Using linked hash map to maintain an order

        listOfMaps = new ArrayList<HashMap<String, Integer>>();
        stopWordsCount = countLines(stopWordsFileName);
        frequencyMatrix = new int[20][10000];
        minCutOffFreq = 0;
        maxCutOffFreq = 0;
        numOfColsInMatrix = 0;

        loadStopWords();
    }

    /**
     * Loads stop words into a hash set
     *
     * @return void
     */
    public static void loadStopWords()
    {
        String line = new String();

        try {
            BufferedReader br = new BufferedReader(new FileReader(stopWordsFileName));
            while ((line = br.readLine()) != null) {
                stopWords.add(Stemmer.stem(line));
            }

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Get min of 2 integers
     *
     * @param int a First integer
     * @param int b Second integer
     *
     * @return int Minimum of the two integers
     */
    public static int min(int a, int b)
    {
        return ((a>b)?b:a);
    }

    /**
     * Count the number of lines in a file
     *
     * @param String filename Name of the file
     * @return int Line count
     */
    public static int countLines(String filename)
    {
        int count = 0;
        String line = new String();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null) {
                count++;
            }

        } catch(Exception e) {
            System.out.println(e);
        }

        return count;
    }

    /**
     * Function to remove special characters
     *
     * @param String Word in which you have  to remove
     *
     * @return String Word without special characters
     */
    public static String removeSpecialChars(String word)
    {
        return word.replaceAll("[^a-zA-Z]", "");
    }

    /**
     * Function to remove stop words from a file
     *
     * @param String filename Name of the file
     *
     * @return String containing file contents excluding stop words
     */
    public static String removeWords(String filename)
    {
        String line = new String();
        String[] words = new String[1000];
        String tempWord = new String("");
        String answer = new String("");
        String trailingChars;

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            while ((line = br.readLine()) != null) {
                words = line.split(" ");

                for (int i = 0; i < words.length; i++) {
                    tempWord = removeSpecialChars(words[i]);
                    tempWord = tempWord.toLowerCase();
                    tempWord = Stemmer.stem(tempWord);

                    if (!stopWords.contains(tempWord)) {
                        answer += (tempWord + " ");
                    }
                }
            }

        } catch(Exception e) {
            System.out.println(e);
        }

        return answer;
    }

    /**
     * Function to calculate frequency of words
     *
     * @param String filename Name of the file
     *
     * @return void
     */
    public static void calcFrequency(String filename)
    {
        HashMap<String, Integer> hm = new HashMap<String, Integer>();
        int count = new Integer(0);
        Set set = hm.entrySet();
        Iterator it = set.iterator();
        String line = new String();
        String[] words = new String[1000];
        String tempWord = new String();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            while ((line = br.readLine()) != null) {
                words = line.split(" ");

                for (int i = 0; i < words.length; i++) {
                    tempWord = removeSpecialChars(words[i]);
                    tempWord = tempWord.toLowerCase();

                    if (tempWord.equals("")) {
                        continue;
                    }

                    if ((hm.get(tempWord)) != null) {
                        count = hm.get(tempWord);
                        hm.put(tempWord, ((int)count + 1));
                    } else {
                        hm.put(tempWord, 1);
                    }
                }
            }

            addInFinalMap(hm);
            listOfMaps.add(hm);

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Adds a hash map to the final hash map
     *
     * @param HashMap<String, Integer> hm Hash map to add
     *
     * @return void
     */
    public static void addInFinalMap(HashMap<String, Integer> hm)
    {
        Iterator it = hm.entrySet().iterator();
        int count, oldCount;

        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();

            if (finalMap.get(pairs.getKey()) != null) {
                count = finalMap.get(pairs.getKey());
                oldCount = (Integer)pairs.getValue();

                finalMap.put((String)pairs.getKey(), (Integer)(oldCount + count));
            } else {
                finalMap.put((String)pairs.getKey(), (Integer)pairs.getValue());
            }

            // it.remove(); // avoids a ConcurrentModificationException
        }
    }

    /**
     * Function to calculate cut off frequency based
     * on min and max frequency
     *
     * @param void
     *
     * @return void
     */
    public static void calcCutOffFrequency()
    {
        int min = 99999999, max = 0;
        Iterator itr = finalMap.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry me = (Map.Entry)itr.next();
            int val = ((Integer) me.getValue()).intValue();

            if (val < min) {
                min = val;
            }

            if (val > max) {
                max = val;
            }
        }

        minCutOffFreq = min + 1;
        maxCutOffFreq = max - 1;
    }

    /**
     * Function to remove high and low frequency words
     *
     * @param void
     *
     * @return void
     */
    public static void removeHighLowFrequency()
    {
        calcCutOffFrequency();

        Iterator itr = finalMap.entrySet().iterator();
        int val;

        while(itr.hasNext()) {
            Map.Entry me = (Map.Entry)itr.next();
            val = ((Integer) me.getValue()).intValue();

            if (val <= minCutOffFreq || val >= maxCutOffFreq) {
                itr.remove();
            } else {
                numOfColsInMatrix++;
            }
        }

        createFrequencyMatrix();
    }

    /**
     * Function to create the frequency matrix based
     * on the remaining words
     *
     * @param void
     *
     * @return void
     */
    public static void createFrequencyMatrix()
    {
        int i = 0, j = 0;

        for (HashMap<String, Integer> hm: listOfMaps) {
            Iterator it = finalMap.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();

                if (hm.get(pairs.getKey()) != null) {
                    frequencyMatrix[i][j] = (int)hm.get(pairs.getKey());
                } else {
                    frequencyMatrix[i][j] = 0;
                }

                j++;
            }

            i++;
            j = 0;
        }

        numOfRowsInMatrix = i;
    }

    /**
     * Function to calculate correlation coefficient of i,j
     *
     * @param int i i-th index of the matrix
     * @param int j j-th index of the matrix
     *
     * @return float Correlation coefficient
     */
    public static float calcCorrelationCoeff(int i, int j) throws Exception
    {
        float a = 0, b = 0, b1 = 0, b2 = 0, c = 0, d = 0, e = 0, f = 0;
        int k;

        if (i == j) {
            throw new Exception("i & j cannot be equal");
        }

        for (k = 0; k < numOfRowsInMatrix; k++) {
            a += (frequencyMatrix[k][i] * frequencyMatrix[k][j]);
        }

        for (k = 0; k < numOfRowsInMatrix; k++) {
            b1 += (frequencyMatrix[k][i]);
            b2 += (frequencyMatrix[k][j]);
        }

        b = ((b1 * b2) / numOfRowsInMatrix);

        for (k = 0; k < numOfRowsInMatrix; k++) {
            c += Math.pow(frequencyMatrix[k][i], 2);
        }

        d = ((float)Math.pow(b1, 2)/numOfRowsInMatrix);

        for (k = 0; k < numOfRowsInMatrix; k++) {
            e += Math.pow(frequencyMatrix[k][j], 2);
        }

        f = ((float)Math.pow(b2, 2)/numOfRowsInMatrix);

        return (float)((a - b) / ((Math.sqrt(c - d)) * (Math.sqrt(e - f))));
    }

    /**
     * Function to generate correlation matrix
     *
     * @param void
     *
     * @return void
     */
    public static void createCorrelationMatrix()
    {
        correlationMatrix = new float[numOfColsInMatrix][numOfColsInMatrix];

        for (int i = 0; i < numOfColsInMatrix; i++) {
            for (int j = 0; j < numOfColsInMatrix; j++) {
                if (i == j) {
                    correlationMatrix[i][j] = -1;
                } else {
                    try {
                        correlationMatrix[i][j] = calcCorrelationCoeff(i, j);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        }
    }

    /**
     * Function to get maximum correlation coefficient in the matrix
     *
     * @param void
     *
     * @return int[] i-th and j-th index of max value
     */
    public static int[] getMaxCoeffIndex()
    {
        int[] result = new int[2];
        int row = 0, col = 0;
        float max = correlationMatrix[0][0];
        for (int i = 0;  i < numOfColsInMatrix; i++) {
            for (int j = 0; j < numOfColsInMatrix; j++) {
                if (correlationMatrix[i][j] > max) {
                    max = correlationMatrix[i][j];
                    row = i;
                    col = j;
                }
            }
        }

        result[0] = row;
        result[1] = col;
        return result;
    }

    /**
     * Removes a column from the frequency matrix
     *
     * @param int col Column number to remove (0 index)
     *
     * @return void
     */
    public static void removeColFromFreqMatrix(int col)
    {
        for (int j = col; j < numOfColsInMatrix - 1; j++) {
            for (int i = 0; i < numOfRowsInMatrix; i++) {
                frequencyMatrix[i][j] = frequencyMatrix[i][j+1];
            }
        }

        numOfColsInMatrix--;
    }

    /**
     * Adds new column (combining i,j) in the frequency matrix
     *
     * @param int i First column to combine
     * @param int j Second column to combine
     *
     * @return void
     */
    public static int addColInFreqMatrix(int i, int j)
    {
        int sum = 0;
        for (int k = 0; k < numOfRowsInMatrix; k++) {
            frequencyMatrix[k][numOfColsInMatrix] = min(
                frequencyMatrix[k][i], frequencyMatrix[k][j]
            );

            sum += frequencyMatrix[k][numOfColsInMatrix];
        }

        numOfColsInMatrix++;
        return sum;
    }

    /**
     * Function to generate pattern string
     *
     * @param void
     *
     * @return void
     */
    public static void generatePatternString()
    {
        createCorrelationMatrix();

        int[] maxCoeffIndex = getMaxCoeffIndex();
        Iterator it = finalMap.entrySet().iterator();
        int c = 0, totalCount = 0;
        String s0 = "", s1 = "";

        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            if (c == maxCoeffIndex[0]) {
                s0 = (String)pairs.getKey();
                it.remove();    // Remove from finalMap
                removeColFromFreqMatrix(c); // Remove col from Correlation Coeff Matrix

            } else if (c == maxCoeffIndex[1]) {
                s1 = (String)pairs.getKey();
                it.remove();
                removeColFromFreqMatrix(c); // Remove col from Correlation Coeff Matrix
            }

            c++;
        }

        totalCount = addColInFreqMatrix(maxCoeffIndex[0], maxCoeffIndex[1]);
        finalMap.put(s0 + " " + s1, totalCount);
    }

    /**
     * Function to call removeWords for each file present
     * in a directory
     *
     * @param void
     *
     * @return void
     */
    public static void remover()
    {
        File f = new File(dirname);
        String[] files = f.list();
        String textWithoutStopWords = new String();
        String outFileName = "docs.txt";

        for (int i = 0; i < files.length ; i++) {
            outFileName = (i+1) + outFileName;
            outFileName = outputDir + outFileName;

            try {
                PrintWriter out = new PrintWriter(new FileWriter(outFileName));
                textWithoutStopWords = removeWords(dirname + "/" + files[i]);
                out.println(textWithoutStopWords);
                out.close();

                calcFrequency(outFileName);
            } catch (Exception e) {
                System.out.println(e);
            }

            outFileName = "docs.txt";
        }

        removeHighLowFrequency();
    }

    /**
     * Function to print final linked hash map (for debugging)
     *
     * @param void
     *
     * @return void
     */
    public static void printFinalMap()
    {
        Iterator itr = finalMap.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry me = (Map.Entry)itr.next();
            System.out.println("Word: " + me.getKey() + ", Frequency: " + me.getValue());
        }
    }

    /**
     * Main method
     */
    public static void main(String[] args)
    {
        PatternString myStopper = new PatternString();
        myStopper.remover();
        int newColCount = 0;

        while (numOfColsInMatrix != newColCount) {
            newColCount = numOfColsInMatrix;
            generatePatternString();
        }

        Iterator itr = finalMap.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry me = (Map.Entry)itr.next();
            System.out.println("Pattern String: " + me.getKey());
        }
    }
}
