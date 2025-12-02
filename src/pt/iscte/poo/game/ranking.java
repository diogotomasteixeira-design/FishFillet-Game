package pt.iscte.poo.game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


public class ranking {

  private static String file = "ranking.txt";

  public static void score(String player, int level, int smallMoves, int bigMoves, int smallDeaths, int bigDeaths, long totalTime){
    try (PrintWriter pw = new PrintWriter(new FileWriter(file,true))){
      pw.println(player + ";" + level + ";" + smallMoves + ";" + bigMoves + ";" + smallDeaths + ";" + bigDeaths + ";" + totalTime);
    } catch (IOException e) {e.printStackTrace();}
  }

  public static List<String> getScores() {
    List<String> scores = new ArrayList<>();
    
    try (Scanner sc = new Scanner(new File(file))) {
      while (sc.hasNextLine())
      scores.add(sc.nextLine());
    } catch (IOException e) {e.printStackTrace();}
    
    return scores;
  }

  public static List<String> getScoresForLevel(int level) {
    List<String> result = new ArrayList<>();

    for (String s : getScores()) {
      try {
        String[] d = s.split(";");
        String check = d[6];
        int levelRead = Integer.parseInt(d[1]);

        if (level == levelRead) {
          result.add(s);
        }
      } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {}
    }

    ScoreComparator comparator = new ScoreComparator();
    int n = result.size();

    for (int i = 0; i < n - 1; i++) {  // exterior
      for (int j = 0; j < n - i - 1; j++) {  // interior

        String scorePlayer1 = result.get(j);
        String scorePlayer2 = result.get(j + 1);

        if (comparator.compare(scorePlayer1, scorePlayer2) > 0) {
          String temp = scorePlayer1;
          result.set(j, scorePlayer2);
          result.set(j + 1, temp);
        }
      }
    }
    return result;
  }

  public static List<String> top5(List<String> list) {
    if (list == null){
      return new ArrayList<>();
    }
    return list.size() <= 5 ? new ArrayList<>(list) : new ArrayList<>(list.subList(0, 5));
  }

    public static List<String> getFinalScores() {
        return getScoresForLevel(100); //100 é para a soma das resultados de todos os níveis
    }

  public static class ScoreComparator implements Comparator<String> {  
    @Override
    public int compare(String s1, String s2) {
      long time1 = getTimeFromScore(s1);
      long time2 = getTimeFromScore(s2);
      int timeComparison = Long.compare(time1, time2);
      
      if (timeComparison != 0) {return timeComparison;}

      long moves1 = getTotalMovesFromScore(s1);
      long moves2 = getTotalMovesFromScore(s2);
      
      return Long.compare(moves1, moves2);
    }

    private static long getTimeFromScore(String score) {
      String[] d = score.split(";");
      try {
        return Long.parseLong(d[6]);
      } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
        return Long.MAX_VALUE;
      }
    }

    private static long getTotalMovesFromScore(String score) {
      String[] d = score.split(";");

      if (d.length < 4){return Long.MAX_VALUE;}

      try {
        long smallMoves = Long.parseLong(d[2]);
        long bigMoves = Long.parseLong(d[3]);
        return smallMoves + bigMoves;

      } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {return Long.MAX_VALUE;}
    }
  }
}


