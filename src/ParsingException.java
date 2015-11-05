/**
 * @file ParsingException.java
 * @author Ashish Tibrewal
 * @data 29.10.2015
 * @details This file contains the ParsingException class that is used to create a custom-defined exception that is used/raised
 * if an illegal/unknown/unrecognized instruction or character is parsed by the assembler.
 */

/**
 * ParsingException exception class that defines a custom exception for an illegal/unknown instruction or character used in the assembly file
 */
public class ParsingException extends Exception
{
  /**
   * Default constructor
   * @return No return value since this is a constructor
   */
  public ParsingException()
  {
    super();                  // Call the base class constructor
  }
 
  /**
   * @param Parameterized constructor that accepts an expection message 
   * @return No return value since this is a constructor
   */
  public ParsingException(String exceptionMessage)
  {
    super(exceptionMessage);  // Call the base class constructor with the specified message
  }
}