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
  private int[] memoryArray;                        /** Memory array that emulates physical memory */
  private final int defaultMemorySize = GlobalConstants.MEMORY_SIZE;         /** Default memory array size */
  private int memorySize;                           /** Store the custom-defined memory size for future reference in this class */
  private Stack<Integer> memoryStack;               /** Memory/Program Stack that contains Integer objects*/
  private final int memoryInitializationValue = GlobalConstants.MEMORY_INITIALIZATION_VALUE;    /** Initialization value for all the locations in memory */
  private Register cpuRegisters;                    /** Reference to the CPU/architectural registers */

  // Class constructors
  /**
   * Default constructor
   * * @return No return value since this is a constructor
   */
  public Memory()
  {
    memoryArray = new int[defaultMemorySize];
    memorySize = defaultMemorySize;
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
      this.memorySize = defaultMemorySize;       // Prevent the array size from being less that the default array size
    }
    else
    {
      this.memorySize = memorySize;
    }
    memoryArray = new int[this.memorySize];
    Arrays.fill(memoryArray, memoryInitializationValue);    // Fill the array with 0's
    memoryStack = new Stack<Integer>();                     // Instantiate a new Stack object
  }

  // Class/Instance methods 
  /**
   * Method to set the reference stored for the primary cpu registers object. Being done as a separate method and not in the constructor since both Memory and Register
   * classes require a reference to each others objects, and the Register class contains a constructor that accepts a Memory object as a parameter, hence, ruling out 
   * this possibilty for the Memory class. An alternative solution would be to pass the Register object to each function in this class that needs to use/modify 
   * the contents of it, but that simply involves/adds more code and complexity, hence, this approach is being avoided.
   * @param cpuRegisters Reference to the primary cpu registers object
   */
  public void setCpuRegistersReference(Register _cpuRegisters)
  {
    cpuRegisters = _cpuRegisters;
  }

  /**
   * Method to obtain the size of main memory 
   * @return Size of main memory
   */
  public int getMemorySize()
  {
    return memorySize;               // Return the size of the memory
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
   * Method to push current register values onto the stack
   * @param value Item/value to be pushed onto the stack
   */
  public void stackPush(int value)
  {
    memoryStack.push(value);                // Push the value onto the stack
    cpuRegisters.incrementSP();             // Increment the stack pointer by one (This is done implicitly and not as a separate instruction, such as INCSP or using an already existing instruction in the ISA, such as ADDI for simplicity)
  }

  /**
   * Method to pop current register values off the stack
   * @return Item/value popped off the stack (i.e. the top-most item popped off the stack)
   */
  public int stackPop()
  {
    int poppedValue = memoryStack.pop();    // Pop the value from the stack
    cpuRegisters.decrementSP();             // Decrement the stack pointer by one (This is done implicitly and not as a separate instruction, such as DECSP or using an already existing instruction in the ISA, such as ADDI for simplicity)
    return poppedValue;
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
    
    /*
    // Have to explicity declare them as signed since they are 32 bit instructions (declared as binary strings) and need to stored in the main memory as a 2's complement 32 bit integer type
    memoryArray[0] = Utility.convertToInt("10001000000000000000000000100001", true); // MOVI R1, 2 (Move 2 into R1)
    memoryArray[1] = Utility.convertToInt("10001000000011111111111111000010", true); // MOVI R2, -4 (Move -4 into R2)
    //memoryArray[3] = Utility.convertToInt("00001000000000000000000100100001", true); // ADDR R1, R1, R2 (Add R1 and R2 and store the result in R1)
    //memoryArray[3] = Utility.convertToInt("10110000000000000001000000010010", true);  // BNE R1, R2, 16
    //memoryArray[3] = Utility.convertToInt("10011000000000000000000000010010", true);  // BU 18

    memoryArray[2] = Utility.convertToInt("10001000000000000000000100000011", true); // MOVI R3, 16 (Move 16 into R3)
    memoryArray[3] = Utility.convertToInt("10001000000000000000000100000100", true); // MOVI R4, 16 (Move 16 into R4)
    memoryArray[4] = Utility.convertToInt("00000000000000000000000000000000", true); // NOP (Inserting a NOP to avoid dependency hazard)
    memoryArray[5] = Utility.convertToInt("10101000000000000000111101000011", true); // BEQ R3, R4, 15 , therefore PC = 5 + 15
    //memoryArray[5] = Utility.convertToInt("10101000111111111111110101000011", true); // BEQ R3, R4, -3 , therefore PC = 5 + (- 3). This gets stuck in a infinite loop as expected.
    memoryArray[6] = Utility.convertToInt("00001000000000000000000100100001", true); // ADDR R1, R1, R2 (Add R1 and R2 and store the result in R1)
    memoryArray[7] = Utility.convertToInt("00001000000000000000001000100010", true); // ADDR R2, R2, R2 (Add R2 and R2 and store the result in R2)
    memoryArray[20] = Utility.convertToInt("00011000000000000000001000100010", true); // MULT R2, R2, R2 (Multiply R2 and R2 and store the result in R2)
    memoryArray[21] = Utility.convertToInt("10001000000000000000111111111111", true); // MOVI R16, 255 (Move 255 into R16)  - SVC call to suspend/halt the program being simulated
    */
   
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* N-th Fibonacci number */
    /*
    memoryArray[0] = Utility.convertToInt("10001000000000000000000000010001", true); // MOVI R1, 1 (Move 1 into R1)
    memoryArray[1] = Utility.convertToInt("10001000000000000000000000010010", true); // MOVI R2, 1 (Move 1 into R2)
    memoryArray[2] = Utility.convertToInt("10001000000000000000000010110011", true); // MOVI R3, 11 (Move 11 into R3)
    memoryArray[3] = Utility.convertToInt("10001000000000000000000000110100", true); // MOVI R4, 3 (Move 3 into R3)
    memoryArray[4] = Utility.convertToInt("00000000000000000000000000000000", true); // NOP (Inserting a NOP to avoid control dependency hazard)
    memoryArray[5] = Utility.convertToInt("10111000000000000000001001000011", true); // BLT R4, R3, 2 , therefore PC = 5 + 2
    memoryArray[6] = Utility.convertToInt("10001000000000000000111111111111", true); // MOVI R16, 255 (Move 255 into R16)  - SVC call to suspend/halt the program being simulated
    memoryArray[7] = Utility.convertToInt("00001000000000000000001000010101", true); // ADD R5, R1, R2 (Add R1 and R2 and store results in R5)
    memoryArray[8] = Utility.convertToInt("10010000000000000000000000100001", true); // MOVR R1, R2 (Move R2 into R1)
    memoryArray[9] = Utility.convertToInt("10010000000000000000000001010010", true); // MOVR R2, R5 (Move R5 into R2)
    memoryArray[10] = Utility.convertToInt("00101000000000000000000101000100", true); // ADDI R4, R4, 1 (Add 1 to R4 and store the result in R4)
    //memoryArray[11] = Utility.convertToInt("00000000000000000000000000000000", true); // NOP (Inserting a NOP to avoid control dependency hazard)
    //memoryArray[12] = Utility.convertToInt("10011000000000001111111111111001", true); // BU -7
    memoryArray[11] = Utility.convertToInt("10011000000000001111111111111010", true); // BU -6
    */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* N-th Fibonacci number correct way to write it in assembly */
    ///*
    memoryArray[0] = Utility.convertToInt("10001000000000000000000000010001", true); // MOVI R1, 1 (Move 1 into R1)
    memoryArray[1] = Utility.convertToInt("10001000000000000000000000010010", true); // MOVI R2, 1 (Move 1 into R2)
    memoryArray[2] = Utility.convertToInt("10001000000000000000000000110011", true); // MOVI R3, 3 (Move 3 into R3)
    memoryArray[3] = Utility.convertToInt("10001000000000000000000101000100", true); // MOVI R4, 20 (Move 20 into R4)
    memoryArray[4] = Utility.convertToInt("11000000000000000000011000110100", true); // BGT R3, R4, 6
    memoryArray[5] = Utility.convertToInt("00001000000000000000001000010101", true); // ADD R5, R1, R2 (Add R1 and R2 and store results in R5)
    memoryArray[6] = Utility.convertToInt("10010000000000000000000000100001", true); // MOVR R1, R2 (Move R2 into R1)
    memoryArray[7] = Utility.convertToInt("10010000000000000000000001010010", true); // MOVR R2, R5 (Move R5 into R2)
    memoryArray[8] = Utility.convertToInt("00101000000000000000000100110011", true); // ADDI R3, R3, 1 (Add 1 to R3 and store the result in R3)
    memoryArray[9] = Utility.convertToInt("10011000000000001111111111111011", true); // BU -5
    memoryArray[10] = Utility.convertToInt("10001000000000000000111111111111", true); // MOVI R16, 255 (Move 255 into R16)  - SVC call to suspend/halt the program being simulated 
    //*/
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* GCD of two numbers - GCD of 300,225 - Result should be 75 */
    /*
    memoryArray[0] = Utility.convertToInt("10001000000000000001001011000001", true); // MOVI R1, 1 (Move 300 into R1)
    memoryArray[1] = Utility.convertToInt("10001000000000000000111000010010", true); // MOVI R2, 1 (Move 225 into R2)   
    memoryArray[2] = Utility.convertToInt("00000000000000000000000000000000", true); // NOP (Inserting a NOP to avoid control dependency hazard) 
    memoryArray[3] = Utility.convertToInt("10101000000000000000011000010010", true); // BEQ R1, R2, 6
    memoryArray[4] = Utility.convertToInt("11000000000000000000001100010010", true); // BGT R1, R2, 3
    memoryArray[5] = Utility.convertToInt("00010000000000000000000100100010", true); // SUB R2, R2, R1
    memoryArray[6] = Utility.convertToInt("10011000000000000000000000000010", true); // BU 2
    memoryArray[7] = Utility.convertToInt("00010000000000000000001000010001", true); // SUB R1, R1, R2
    memoryArray[8] = Utility.convertToInt("10011000000000001111111111111011", true); // BU -5
    memoryArray[9] = Utility.convertToInt("10001000000000000000111111111111", true); // MOVI R16, 255 (Move 255 into R16)  - SVC call to suspend/halt the program being simulated
    */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
  
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Used to test the BL and RET instructions */
    /*
    memoryArray[0] = Utility.convertToInt("10001000000000000001001011000001", true); // MOVI R1, 1 (Move 300 into R1)
    memoryArray[1] = Utility.convertToInt("00000000000000000000000000000000", true); // NOP (Inserting a NOP to avoid control dependency hazard) 
    memoryArray[2] = Utility.convertToInt("00000000000000000000000000000000", true); // NOP (Inserting a NOP to avoid control dependency hazard) 
    memoryArray[3] = Utility.convertToInt("00000000000000000000000000000000", true); // NOP (Inserting a NOP to avoid control dependency hazard)
    // Note need at least three NOP instruction's before calling a method to make sure that all values are written to registers before pushing them onto the stack
    memoryArray[4] = Utility.convertToInt("10100000000000000000000000001000", true); // BL 8 (Branch with link to memory address 8)   
    memoryArray[5] = Utility.convertToInt("10001000000000000000111111111111", true); // MOVI R16, 255 (Move 255 into R16)  - SVC call to suspend/halt the program being simulated 
    memoryArray[12] = Utility.convertToInt("10001000000000000000000000111101", true); // MOVI R13, 3 (Move 3 into R13)
    memoryArray[13] = Utility.convertToInt("00000000000000000000000000000000", true); // NOP (Inserting a NOP to avoid control dependency hazard) 
    memoryArray[14] = Utility.convertToInt("00000000000000000000000000000000", true); // NOP (Inserting a NOP to avoid control dependency hazard) 
    memoryArray[15] = Utility.convertToInt("00000000000000000000000000000000", true); // NOP (Inserting a NOP to avoid control dependency hazard)
    // Note need at least three NOP instruction's before returning from the method.
    memoryArray[16] = Utility.convertToInt("11001000000000000000000000000000", true); // RET (Return from function call)
    */
   ////////////////////////////////////////////////////////////////////////////////////////////////////////
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
      System.out.format("| 0x%08x            0x%08x (%s)       |%n", memoryLocation, memoryArray[memoryLocation], Utility.convertToBin(memoryArray[memoryLocation], 4));
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
      Integer[] stackArray = new Integer[memoryStack.size()];
      memoryStack.toArray(stackArray);    // Convert the stack to an Integer array for printing
      for (int stackItem = (stackArray.length -1); stackItem >= 0; stackItem--)
      {
        System.out.format("0x%08x (%s) \n", stackArray[stackItem], Utility.convertToBin(stackArray[stackItem].intValue(), 4));
      }
      /*while (!memoryStack.empty())
      {
        Integer topOfStackElement = new Integer(memoryStack.pop());     // Cannot use this for printing because this removes/pops the element from the stack which is required later when returning from a method (i.e. by the RET instruction)
        System.out.format("0x%08x (%s) \n", topOfStackElement, Utility.convertToBin(topOfStackElement.intValue(), 4));
      }*/
    } 
    System.out.println(">>> Memory dump complete <<< \n");
  }
}