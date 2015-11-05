/**
 * @file RegisterAccessException.java
 * @author Ashish Tibrewal
 * @data 12.10.2015
 * @details This file contains the RegisterAccessException class that is used to create a custom-defined exception that is used/raised
 * during illegal register operations.
 */

/**
 * RegisterAccessException exception class that defines a custom exception for illegal register accesses 
 */
public class RegisterAccessException extends IndexOutOfBoundsException
{
  /**
   * Default constructor
   * @return No return value since this is a constructor
   */
  public RegisterAccessException()
  {
    super();                  // Call the base class constructor
  }
 
  /**
   * @param Parameterized constructor that accepts an expection message 
   * @return No return value since this is a constructor
   */
  public RegisterAccessException(String exceptionMessage)
  {
    super(exceptionMessage);  // Call the base class constructor with the specified message
  }
}