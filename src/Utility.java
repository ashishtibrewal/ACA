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
   * Converts an integer to a 32-bit binary string representing it's corresponding binary value
   * @param number    The number to convert
   * @param groupSize The number of bits in a group
   * @return          The 32-bit long bit string containing the binary representation
   */
  public static String convertToBin(int number, int groupSize)
  {
    // Local variables
    StringBuilder binaryString;

    // Method functionality
    binaryString = new StringBuilder();
    for(int i = GlobalConstants.PROCESSOR_WORD_LENGTH - 1; i >= 0 ; i--)
    {
      int mask = 1 << i;
      binaryString.append((number & mask) != 0 ? "1" : "0");
      if (groupSize != 0)   // Need to check that the passed groupSize is not zero. If it is zero we avoid adding the spaces.
      {
        if (i % groupSize == 0)   // Note that this modulo calculation throws an Arithmetic Exception if groupSize if zero, hence the above check is used.
        {
          binaryString.append(" ");
        }
      }
    }
    if (groupSize != 0)
    {
      binaryString.replace(binaryString.length() - 1, binaryString.length(), "");   // This removes the last blank character after the binary string. Only required when the group size is not 0.
    }
    return binaryString.toString();
  } 

  /**
   * Convert 32-bit 2's complement binary string or a 32-bit unsigned binary string to an integer 
   * @param  input    32-bit 2's complement binary string or 32-bit unsigned binary string
   * @param  unsigned Boolean value stating if the binary string being passed is an unsigned number
   * @return          Converted integer value
   */
  public static int convertToInt(String binaryString, boolean unsigned)
  {
    int binaryStringLength = binaryString.length();
    char[] inputArray = new char[binaryStringLength];
    inputArray = binaryString.toCharArray();
    int intResult = 0;
    for (int i = 0; i < (binaryStringLength - 1); i++)
    {
      intResult+= (inputArray[((binaryStringLength - 1) - i)] - 48) * Math.pow(2, i);      // Subtracting 48 because inputArray is of char type and it returns an ASCII value
    }
    if (unsigned == false)       // If the binary string is a 2's complement number
    {
      if (inputArray[0] == '1')     // Check if the MSB is 1 (i.e. a negative number in 2's complement)
      {
        intResult-= (inputArray[0] - 48) * Math.pow(2, (binaryStringLength - 1));            // Subtracting 48 because inputArray is of char type and it returns an ASCII value
      }
    }
    else                      // If the binary string is an unsigned number
    {
      if (inputArray[0] == '1')     // Check if the MSB is 1 (i.e. still a positive number since it's an unsigned binary)
      {
        intResult+= (inputArray[0] - 48) * Math.pow(2, (binaryStringLength - 1));            // Subtracting 48 because inputArray is of char type and it returns an ASCII value
      }
    }
    return intResult;
  }

  /**
   * Mehtod to Sign Extend (SE) a binary string to a specified number of bits
   * @param  binaryString            String containing the binary number that needs to be sign extended
   * @return                         Sign extended version of the specified binary string
   */
  public static String signExtend(String binaryString)
  {
    char msb = binaryString.charAt(0);        // Obtain the MSB
    int extraBitsRequired = GlobalConstants.PROCESSOR_WORD_LENGTH - binaryString.length();
    StringBuilder signExtendedBinaryString = new StringBuilder(GlobalConstants.PROCESSOR_WORD_LENGTH);
    for (int i = 0; i < extraBitsRequired; i++)
    {
      signExtendedBinaryString.append(msb);                         // Append the MSB
    }
    signExtendedBinaryString.append(binaryString);                  // Append the original binary string                          
    return signExtendedBinaryString.toString();
  }
}