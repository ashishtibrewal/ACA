/**
 * @file Memory.java
 * @author Ashish Tibrewal
 * @date 12.10.2015
 * @details This file contains the Memory class that is used to emulate the physical memory for the simulation. It provides methods
 * to read and write to memory. It also provides functionality to define a custom-sized memory and a method to obtain the size of the
 * memory currently being used. The memory is essentially defined as an integer array of a specific length. Since an interger type is 
 * being used, by default each line in the memory would be 4 bytes wide since the primitive int type in Java has been designed to use 
 * 4 bytes. An alternative solution to using the int type would be to use a double-dimensonal array of the byte type with its first 
 * dimension specifying the number of lines in the memory and its second dimension specifying the number of bytes to store in each line
 * of the memory, which would by defualt be 4 bytes (i.e. 32 bits), since the processor's word length is of this size. The single-dimension
 * integer array approach has been taken since it's simpler to implement and doesn't requried the data to be masked and shifted for the 
 * different bits to be put into the correct byte element. The class also contains a memory dump method that dumps/outputs the current 
 * state of memory which can be useful for debugging purposes.
 */

// Import packages
import java.io.*;
import java.util.*;
import java.lang.*;

// Class declaration
/**
 * This class contains the memory (i.e. RAM) unit of the CPU Simulator.
 */
public class Memory
{
  // Class/Instance fields
  // TODO change all access specifiers to private and see if that works.
  protected int[] memoryArray;                        /** Memory array that emulates physical memory */
  protected final int defaultMemorySize = 64;         /** Default memory array size */
  //protected final int defaultMemorySize = 1024;       /** Default memory array size */
  protected int memoryArraySize;                      /** Store the custom-defined memory size for future reference in this class */
  protected Stack<Integer> memoryStack;               /** Memory/Program Stack that contains Integer objects*/
  private final int memoryInitializationValue = 0;    /** Initialization value for all the location in memory */

  // Class constructors
  /**
   * Default constructor
   * * @return No return value since this is a constructor
   */
  public Memory()
  {
    memoryArray = new int[defaultMemorySize];
    Arrays.fill(memoryArray, memoryInitializationValue);     // Fill the array with 0's
    memoryStack = new Stack<Integer>();                      // Instantiate a new Stack object
  }

  /**
   * Parameterized constructor that creates a custom-sized memory (i.e. memory array)
   * @param Size of the memory array
   * @return No return value since this is a constructor
   */
  public Memory(int memorySize)
  {
    if (memorySize < defaultMemorySize)
    {
      memorySize = defaultMemorySize;       // Prevent the array size from being less that the default array size
    }
    memoryArray = new int[memorySize];
    memoryArraySize = memorySize;
    Arrays.fill(memoryArray, memoryInitializationValue);    // Fill the array with 0's
    memoryStack = new Stack<Integer>();                     // Instantiate a new Stack object
  }

  // Class/Instance methods
  /**
   * Method to return the size of the memory 
   * @return Size of memory
   */
  public int memorySize()
  {
    return memoryArraySize;       // Return the size of the memory
    // return memoryArray.length;    // Return the size of the memory
  }

  /**
   * Method to handle memory reads
   * @param memoryLocation Memory location at which the value needs to be read
   * @return Value at the specefied memory location
   */
  public int readValue(int memoryLocation)
  {
    if (memoryLocation < 0 || memoryLocation >= memoryArray.length)
    {
      throw new MemIndexOutOfBoundsException("Illegal memory access (" + Integer.toHexString(memoryLocation) + 
                                             ")! Program attempting to read a value from an address/location in memory that does not exist.");
    }
    else
    {
      return memoryArray[memoryLocation];     // If the memory address is in range, read and return the value at this memory location
    }       
  }

  /**
   * Method to handle memory write 
   * @param memoryLocation Memory location at which the value needs to be writted to
   * @param newValue       Value that needs to be written at the speceifed memory location
   */
  public void writeValue(int memoryLocation, int newValue)
  {
    if (memoryLocation < 0 || memoryLocation >= memoryArray.length)
    {
      throw new MemIndexOutOfBoundsException("Illegal memory access (" + Integer.toHexString(memoryLocation) + 
                                             ")! Program attempting to write a value to an address/location in memory that does not exist.");
    }
    else
    {
      memoryArray[memoryLocation] = newValue;     // If the memory address is in range, write the value at this memory location
    }       
  }

  /**
   * Method to initialize the contents of memory with the instructions and data contained in the program that needs to be executed by the simulator.
   * It is essentially a list of Integer tokens that has been generated using the Assembler.
   * @param programList A list of Integer tokens that contain the program's instructions and data that's used to initialize the memory
   */
  public void initialize(ArrayList <Integer> programList)
  {
    int listIterator;
    for (int memoryLocation = 0; memoryLocation < programList.size(); memoryLocation++)
    {
      if (memoryLocation < memoryArray.length)
      {
        listIterator = memoryLocation;
        memoryArray[memoryLocation] = programList.get(listIterator).intValue();
      }
      else
      {
        throw new MemIndexOutOfBoundsException("Illegal memory access during initialization (" + Integer.toHexString(memoryLocation) + 
                                               ")! Simulator initializer attempting to write a value to an address/location in memory that does not exist.");
      }
    }
  }

  public void testInitialize()
  {
    // Random randomNumberGenerator = new Random();
    // for(int memoryLocation = 0; memoryLocation < 10; memoryLocation++)
    // {
    //   memoryArray[memoryLocation] = randomNumberGenerator.nextInt(100);
    // }
    /*
    memoryArray[0] = Integer.parseInt("01000100000000000000000000100001", 2); // MOVI R1, 2 (Move 2 into R1)
    memoryArray[1] = Integer.parseInt("01000100000000000000000001000010", 2); // MOVI R2, 3 (Move 3 into R2) 
    memoryArray[2] = Integer.parseInt("00000100000000000000000100100001", 2); // ADDR R1, R1, R2 (Add R1 and R2 and store the result in R1)
    memoryArray[3] = Integer.parseInt("-00000000000000000000000000000001", 2); // Should be -1 in decimal integer
    memoryArray[4] = 0xffffffff;   // Should be -1 in decimal integer
    */
    memoryArray[0] = Utility.convertToInt("10001000000000000000000000100001"); // MOVI R2, 3 (Move 3 into R2)
    memoryArray[1] = Utility.convertToInt("10001000000000000000000001000010"); // MOVI R2, 3 (Move 3 into R2)
    memoryArray[2] = Utility.convertToInt("00001000000000000000000100100001"); // ADDR R1, R1, R2 (Add R1 and R2 and store the result in R1)

  }

  /**
   * Method to dump contents of memory. Useful for debugging purposes.
   */
  public void dumpContents()
  {
    System.out.println("+----------------------------------------------------------------------------------+");
    System.out.println("| Dumping current state of memory                                                  |");
    System.out.println("+----------------------------------------------------------------------------------+");
    System.out.println(">>> Memory dump started <<< ");
    System.out.println("> Dumping .text and .data sections < ");
    System.out.println("+----------------------------------------------------------------------------------+");
    System.out.println("| Location                       Data (Content in 2's complement)                  |");
    System.out.println("+----------------------------------------------------------------------------------+");
    for (int memoryLocation = 0; memoryLocation < memoryArray.length; memoryLocation++)
    {
      //System.out.println("| 0x" + Integer.toHexString(memoryLocation) + "\t \t \t" + memoryArray[memoryLocation] + " |");
      System.out.format("| 0x%08x            0x%08x (%s)       |%n", memoryLocation, memoryArray[memoryLocation], Utility.convertToHex(memoryArray[memoryLocation],4));
    }
    System.out.println("+----------------------------------------------------------------------------------+");
    System.out.println("> Dumping stack < ");
    if (memoryStack.empty())
    {
      System.out.println("Stack is empty.");
    }
    else
    {
      System.out.println("Contents being printed in the reverse order. Top of stack is printed first (LIFO).");
      while (!memoryStack.empty())
      {
        Integer topOfStackElement = new Integer(memoryStack.pop());
        System.out.format("0x%08x (%s) \n", topOfStackElement, Utility.convertToHex(topOfStackElement.intValue(), 4));
      } 
    } 
    System.out.println(">>> Memory dump complete <<< \n");
  }
}