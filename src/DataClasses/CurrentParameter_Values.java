package DataClasses;

import Database.Queries;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by kost on 01.12.2014.
 */
public class CurrentParameter_Values {

    private static String lot = "";
    private static String lotdate = "";
    private static String choice = "5-Oxo Pro";
    private static double average_level1, average_level2, sigma_level1, sigma_level2 = 0.00;
    private static ArrayList<Double> scores_level1 = new ArrayList<Double>(),
                                     scores_level2 = new ArrayList<Double>();
    private static ArrayList<String> filedate = new ArrayList<String>();

    public static void updateValues() {

        if (Queries.select_Lot().size() != 0) {
            filedate.clear();
            for (int i = 0; i < Queries.select_Files().size(); i++){
                if (Queries.select_Files().get(i).get(1).equals(CurrentParameter_Values.getLot())){
                    filedate.add(Queries.select_Files().get(i).get(3));
                }
            }

            scores_level1 = Queries.select_FilesData(choice).get(0);
            scores_level2 = Queries.select_FilesData(choice).get(1);
            average_level1 = Queries.select_LotData(choice).get(0);
            average_level2 = Queries.select_LotData(choice).get(1);
            sigma_level1 = Queries.select_LotData(choice).get(2);
            sigma_level2 = Queries.select_LotData(choice).get(3);
        }
    }

    public static void setLot(String lot) {        CurrentParameter_Values.lot = lot;    }
    public static String getLot() {        return lot;    }

    public static void setLotdate(String date) {        CurrentParameter_Values.lotdate = date;    }
    public static String getLotdate(){ return lotdate;}

    public static void setChoice(String choice) {        CurrentParameter_Values.choice = choice;    }
    public static String getChoice() {        return choice;    }

    public static void setAverage_level1(double average_level1) {        CurrentParameter_Values.average_level1 = average_level1;}
    public static double getAverage_level1() {        return average_level1;    }

    public static void setAverage_level2(double average_level2) {        CurrentParameter_Values.average_level2 = average_level2;    }
    public static double getAverage_level2() {        return average_level2;    }

    public static void setSigma_level1(double sigma_level1) {        CurrentParameter_Values.sigma_level1 = sigma_level1;    }
    public static double getSigma_level1() {        return sigma_level1;    }

    public static void setSigma_level2(double sigma_level2) {        CurrentParameter_Values.sigma_level2 = sigma_level2;    }
    public static double getSigma_level2() {        return sigma_level2;    }

    public static void setScores_level1(ArrayList<Double> scores_level1) {        CurrentParameter_Values.scores_level1 = scores_level1;    }
    public static ArrayList<Double> getScores_level1() {        return scores_level1;    }

    public static void setScores_level2(ArrayList<Double> scores_level2) {        CurrentParameter_Values.scores_level2 = scores_level2;    }
    public static ArrayList<Double> getScores_level2() {        return scores_level2;    }

    public static void setFiledate(ArrayList<String> filedate) {        CurrentParameter_Values.filedate = filedate;    }
    public static ArrayList<String> getFiledate() {        return filedate;    }
}
