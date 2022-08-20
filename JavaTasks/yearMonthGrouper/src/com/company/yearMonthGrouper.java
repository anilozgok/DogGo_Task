package com.company;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class yearMonthGrouper {


    /**
     * Generates n random dates between given start and end years.
     * <p>
     *
     * @param startYear Start year for random dates.
     * @param endYear   End year for random dates.
     * @param n         The number of dates that generated.
     * @return List<< LocalDate / LocalDate> = List of local date which stores random dates that generated.
     */

    private static List<LocalDate> randomDateGenerator(int startYear, int endYear, int n) throws Exception {
        try {
            List<LocalDate> randDates = new ArrayList<>();

            //generating n random dates
            for (int i = 0; i < n; ++i) {
                //generating random day
                int day = randBetween(1, 28);

                //generating random month
                int month = randBetween(1, 12);

                //generating random year
                int year = randBetween(startYear, endYear);

                //merging rand numbers and creating date and adding into the list
                randDates.add(LocalDate.of(year, month, day));
            }

            return randDates;
        } catch (Exception e) {
            throw new Exception("Error occured while generating random dates. Error Message:" + e.getMessage());
        }
    }


    /**
     * Generates random number between given numbers.
     * <p>
     *
     * @param start Start limit of range.
     * @param end   End limit of range.
     * @return int => returns random number that generated.
     */

    private static int randBetween(int start, int end) throws Exception {

        try {//generating random numbers and returning them
            return start + (int) Math.round(Math.random() * (end - start));
        } catch (Exception e) {
            throw new Exception("Error occured while generating random numbers. Error Message: " + e.getMessage());
        }
    }


    /**
     * Converts local dates to MMM-yy formatted date.
     * <p>
     *
     * @param dates List of dates.
     * @return List<< String / String> => returns MMM-yy formatted dates in a list of string.
     */

    private static List<String> convertShortDate(List<LocalDate> dates) throws Exception {

        try {
            List<String> shortDates = new ArrayList<>();

            for (LocalDate date : dates) {
                //getting MMM
                String month = date.getMonth().toString().substring(0, 3);
                //getting yy
                int year = date.getYear() % 100;
                //merging and adding into the list
                shortDates.add(month + "-" + year);
            }
            System.out.println(shortDates);
            return shortDates;
        } catch (Exception e) {
            throw new Exception("Error occured while converting dates to short date. Error Message: " + e.getMessage());
        }
    }


    /**
     * Groups dates according to their year and months.
     * <p>
     *
     * @param shortDates List of short dates.
     */

    private static void yearMonthGrouperFunc(List<String> shortDates) throws Exception {

        try {//HashMap that will store the grouped dates
            HashMap<String, Integer> groupCounts = new HashMap<>();

            int counter = 0;

            //iterating in dates
            for (int i = 0; i < shortDates.size(); ++i) {
                //second for loop for comparison
                for (int j = 1; j < shortDates.size() - 1; ++j) {
                    //comparing the dates if they are in same month and year
                    if (shortDates.get(i).equals(shortDates.get(j))) {
                        counter++;
                    }
                }
                //if counter is 0 we need to add 1 to the HashMap
                if (counter == 0)
                    counter = 1;
                //putting them into the HashMap
                groupCounts.put(shortDates.get(i), counter);
                counter = 0;
            }

            System.out.println(groupCounts);

        } catch (Exception e) {
            throw new Exception("Error occured while grouping dates. Error Message: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        try {

            Scanner scan=new Scanner(System.in);
            System.out.println("Please enter start year, end year, and number of dates.");

            int startYear=scan.nextInt();
            int endYear=scan.nextInt();
            int n=scan.nextInt();

            //generating random dates
            List<LocalDate> dates = randomDateGenerator(startYear, endYear, n);

            //converting dates to the MMM-yy
            List<String> shortDates = convertShortDate(dates);

            //Grouping dates
            yearMonthGrouperFunc(shortDates);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}
