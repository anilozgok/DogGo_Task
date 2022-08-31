package com.company;

import java.util.*;

public class engTrConverter {


    /**
     * Splitting words in each line.
     * <p>
     * @param lines Array of string which store lines.
     * @return List => returns List of string lists.
     */

    private static List<List<String>> linesToWords(String[] lines) throws Exception {

        try {

            //list for each line
            List<List<String>> lineWords = new ArrayList<>();

            //iterating in lines
            for (String item : lines)
                //splitting lines to words
                lineWords.add(Arrays.stream(item.split(" ")).toList());

            return lineWords;

        } catch (Exception e) {
            throw new Exception("Error occured while splitting words. Error Message: "+e.getMessage());
        }
    }

    /**
     * Removes duplicate words from list.
     * <p>
     * @param words List of string List which store words in each line.
     * @return List<<String/String> => returns List of string.
     */

    private static List<String> removeDuplicate(List<List<String>> words) throws Exception {
        try {

            //list for unduplicated words
            List<String> undupliactedWords = new ArrayList<>();

            //iterating in lines ehich is a list of words in each line
            for (List<String> item : words) {

                //itearating in words
                for (String item2 : item) {

                    //checking if the list contains duplicated words
                    if (!undupliactedWords.contains(item2.toLowerCase()))
                        undupliactedWords.add(item2.toLowerCase());//adding words
                }
                undupliactedWords.add("\n");//adding endLine for end lines
            }

            //printing unduplicated input line by line
            for (String undupliactedWord : undupliactedWords) {
                if (!undupliactedWord.equals("\n"))
                    System.out.print(undupliactedWord + " ");
                else
                    System.out.println();
            }

            return undupliactedWords;

        } catch (Exception e) {
            throw new Exception("Error occured while removing duplicates. Error Message: "+e.getMessage());
        }

    }

    /**
     * Counts duplicated words in words.
     * <p>
     * @param lineWords List of string List which store words in each line.
     * @param singleWords List of strings which store every single word.
     */

    private static void countWords(List<List<String>> lineWords, List<String> singleWords) throws Exception {
        try {

            //list for all words to comparison
            List<String> allWords = new ArrayList<>();

            //converting all words to lowercase
            for (List<String> item : lineWords) {
                for (String item2 : item) {
                    allWords.add(item2.toLowerCase());
                }
            }

            int count = 0;

            for (String singleWord : singleWords) {
                for (String allWord : allWords) {
                    //counting duplicate words
                    if (allWord.equals(singleWord)) {
                        count++;
                    }
                }
                //printing duplicated words and how many times the duplicated
                if (!singleWord.equals("\n"))
                    System.out.println(singleWord + " " + count);
                count = 0;
            }
        } catch (Exception e) {
            throw new Exception("Error occured while counting duplicated words. Error Message: "+e.getMessage());
        }

    }

    /**
     * Checks user input if it is valid or not.
     * <p>
     * @param input user input which is String.
     * @return true if input is valid otherwise false.
     */

    private static boolean isInputValid(String input) throws Exception{

        try{//removing new lines from input
            input = input.replaceAll("[\n\r]", "");

            //converting pnput to char array
            char[] chars = input.toCharArray();

            //iterating in char array
            for (char c : chars) {
                //checking if there is any digit or symbol. NOTE: we used isWhiteSpace for spaces they must count valid as well.
                if (!Character.isLetter(c) && !Character.isWhitespace(c))
                    return false;
            }
            return true;
        }catch (Exception e){
            throw new Exception("An error occured while checking input. Error Message: "+e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {

            //multiline string input
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter two tab after you enter your text. And text shouldn't contain any number, digit or symbol.");
            String input;

            //After input you need to press two tab for breaking the loop
            scan.useDelimiter("\\t");

            input = scan.next();


            if (isInputValid(input)) {
                //test input
                /*String input="Happy hour hour class class classs\n" +
                    "Do you you go go go go to the the gym\n" +
                    "Okey okey bro Bro";*/

                //splitting input into lines
                String[] lines = input.split("\n");

                //splitting lines to words
                List<List<String>> words = linesToWords(lines);

                //removing duplicates
                List<String> undupliactedWords = removeDuplicate(words);

                System.out.println("*******************");

                //counting duplicate words
                countWords(words, undupliactedWords);
            } else
                System.out.println("Please check your input");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
