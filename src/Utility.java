/**
 * @file Utility.java
 * @author Ashish Tibrewal
 * @date 02.11.2015
 * @details The file contains the Utility class that consists of all the required utility methods. Note that all these utility methods have been
 * declared static.
 */

// Class declaration
public class Utility
{
  /**
   * Converts an integer to a 32-bit binary string representing it's corresponding hexadecimal value
   * @param number    The number to convert
   * @param groupSize The number of bits in a group
   * @return          The 32-bit long bit string containing the hexadecimal representation
   */
  public static String convertToHex(int number, int groupSize)
  {
      // Local variables
      StringBuilder result;

      // Method functionality
      result = new StringBuilder();
      for(int i = 31; i >= 0 ; i--)
      {
          int mask = 1 << i;
          result.append((number & mask) != 0 ? "1" : "0");

          if (i % groupSize == 0)
          {
            result.append(" ");
          }
      }
      result.replace(result.length() - 1, result.length(), "");
      return result.toString();
  } 

  /**
   * Convert 32-bit 2's complement binary string to an integer 
   * @param  input 32-bit 2's complement binary string
   * @return       Converted integer value
   */
  public static int convertToInt(String binaryString)
  {
    char[] inputArray = new char[binaryString.length()];
    inputArray = binaryString.toCharArray();
    int intResult = 0;
    for (int i = 0; i < 31; i++)
    {
      intResult+= (inputArray[(31 - i)] - 48) * Math.pow(2,i);      // Subtracting 48 because inputArray is of char type and it returns an ASCII value
    }
    if (inputArray[0] == '1')     // Check if the MSB is 1 (i.e. a negative number in 2's complement)
    {
      intResult-= (inputArray[0] - 48) * Math.pow(2,31);            // Subtracting 48 because inputArray is of char type and it returns an ASCII value
    }
    return intResult;
  }
}