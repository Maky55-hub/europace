import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class EuropaceFirstCase {
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new BufferedReader(new FileReader("resources/hypoport.json")));
            JSONObject jsonObj = (JSONObject) obj;
            JSONArray pricesArray = (JSONArray) jsonObj.get("prices");

            // variables to find the days with smallest low and highest high
            double smallestLow =  Double.MAX_VALUE;
            double highestHigh = Double.MIN_VALUE;
            String dayWithSmallestLow = "";
            String dayWithHighestHigh = "";

            // variables to find the day with the largest difference between open and close
            double defaultDifferenceOpenClose = 0;
            String dayWithLargestDifference = "";

            double sumOfAllCloses = 0;

            for (Object dayPrice : pricesArray) {
                JSONObject singleDay = (JSONObject) dayPrice;

                Object high = singleDay.get("high");
                Object low = singleDay.get("low");
                Object open = singleDay.get("open");
                Object close = singleDay.get("close");
                String singleDayDate = singleDay.get("date").toString();

                // to find the day with smallest low (niedrigster Kurs)
                if ( low != null ) {
                    double singleDayLow = Double.parseDouble(low.toString());
                    if (singleDayLow < smallestLow) {
                        smallestLow = singleDayLow;
                        dayWithSmallestLow = singleDayDate;
                    }
                }

                // to find the day with the highest high (höchster Kurs)
                if ( high != null) {
                    double singleDayHigh = Double.parseDouble(high.toString());
                    if (singleDayHigh > highestHigh) {
                        highestHigh = singleDayHigh;
                        dayWithHighestHigh = singleDayDate;
                    }
                }


                // to find the day with largest difference between open and close
                if ( open != null && close != null ) {
                    double singleDayOpen = Double.parseDouble(singleDay.get("open").toString());
                    double singleDayClose = Double.parseDouble(singleDay.get("close").toString());
                    double differenceOpenClose = Math.abs(singleDayOpen - singleDayClose);

                    if (differenceOpenClose > defaultDifferenceOpenClose) {
                        defaultDifferenceOpenClose = differenceOpenClose;
                        dayWithLargestDifference = singleDayDate;
                    }
                }

                // code to sum up closes and find average close
                if ( close != null ) {
                    sumOfAllCloses += Double.parseDouble(close.toString());
                }

            }
            // the days with no close (null values) are considered to have close=0
            // the average is calculated irrespective of the null values for some days
            double averageOfAllCloses = sumOfAllCloses / pricesArray.size();

            System.out.println("Day with smallest low: "+dayWithSmallestLow+". The smallest low is: "+smallestLow);
            System.out.println("Day with the highest high: "+dayWithHighestHigh+". The highest high is: "+highestHigh);
            System.out.println("Day with the largest difference between open and close: "+dayWithLargestDifference+". The differece is: "+defaultDifferenceOpenClose);
            System.out.println("Average of all closes: "+ averageOfAllCloses);

        } catch (ParseException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
