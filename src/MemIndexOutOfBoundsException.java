/**
 * @file MemIndexOutOfBoundsException.java
 * @author Ashish Tibrewal
 * @data 12.10.2015
 * @details This file contains the MemIndexOutOfBoundsException class that is used to create a custom-defined exception that is used/raised
 * during illegal memory operations.
 */

/**
 * MemIndexOutOfBoundsException exception class that defines a custom exception for illegal memory accesses 
 */
public class MemIndexOutOfBoundsException extends IndexOutOfBoundsException
{
  /**
   * Default constructor
   * @return No return value since this is a constructor
   */
  public MemIndexOutOfBoundsException()
  {
    super();                  // Call the base class constructor
  }
 
  /**
   * @param Parameterized constructor that accepts an expection message 
   * @return No return value since this is a constructor
   */
  public MemIndexOutOfBoundsException(String exceptionMessage)
  {
    super(exceptionMessage);  // Call the base class constructor with the specified message
  }
}