/**
 * @file IllegalInstructionException.java
 * @author Ashish Tibrewal
 * @data 12.10.2015
 * @details This file contains the IllegalInstructionException class that is used to create a custom-defined exception that is used/raised
 * when an illegal instruction is encountered.
 */

/**
 * IllegalInstructionException exception class that defines a custom exception for an illegal instruction
 */
public class IllegalInstructionException extends IndexOutOfBoundsException
{
  /**
   * Default constructor
   * @return No return value since this is a constructor
   */
  public IllegalInstructionException()
  {
    super();                  // Call the base class constructor
  }
 
  /**
   * @param Parameterized constructor that accepts an expection message 
   * @return No return value since this is a constructor
   */
  public IllegalInstructionException(String exceptionMessage)
  {
    super(exceptionMessage);  // Call the base class constructor with the specified message
  }
}