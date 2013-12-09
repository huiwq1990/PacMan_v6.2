package rl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;

/**
 * Utility methods.
 * 
 * @author zheyang@stanford.edu (Zhe Yang)
 */
public class Utils {
  
  public static int[] seeds = new int[] {
    7801, 3125, 234, 9340, 12369, 839158, 4912023, 333311, 87654, 234058
  };
  public static int getSeed(int i) {
    return seeds[i];
  }

  public static boolean getBit(int number, int i) {
    return (number & (1 << i)) != 0;
  }
  
  public static String printBits(int number, int n) {
    String s = "";
    for (int i = 0; i < n; i++) {
      s += getBit(number, i) ? "1" : "0";
    }
    return s;
  }
  
  public static String printArray(boolean[] array) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      if (i > 0) {
        sb.append(" ");
      }
      sb.append(array[i] ? "1" : "0");
    }
    return sb.reverse().toString();
  }
  
  public static String join(List<? extends Object> items, String separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < items.size(); i++) {
      if (i > 0) {
        sb.append(separator);
      }
      sb.append(items.get(i).toString());
    }
    return sb.toString();
  }
  
  public static String join(float[] items, String separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < items.length; i++) {
      if (i > 0) {
        sb.append(separator);
      }
      sb.append(String.format("%.6f", items[i]));
    }
    return sb.toString();
  }
  
  public static String join(int[] items, String separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < items.length; i++) {
      if (i > 0) {
        sb.append(separator);
      }
      sb.append(String.format("%d", items[i]));
    }
    return sb.toString();
  }
  
  public static boolean dump(String filename, String content) {
    Logger.println(1, "** Dumping to " + filename + " **");
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
      writer.write(content);
      writer.close();
      return true;
    } catch (Exception x) {
      System.err.println("Failed to write scores.");
    }
    return false;
  }
}
