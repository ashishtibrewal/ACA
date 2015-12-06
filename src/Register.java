/**
 * @file Register.java
 * @author Ashish Tibrewal
 * @date 12.10.2015
 * @details This file contains the Register class that is used to emulate the physical processor/architectural registers for the 
 * simulation. It provides methods to read and write to these registers, increment the PC register (including setting it to a 
 * specific value to handle branch instructions), etc. It also contains a register dump method that dumps/outputs the current state 
 * of registers which can be useful for debugging purposes.
 * 
 * //TODO Need to implement methods to be able to modify the stack pointer and link registers and all other/additional registers
 */

// Import packages
import java.io.*;
import java.util.*;
import java.lang.*;

// Class declaration
/**
 * This class contains the processor register units of the CPU Simulator.
 */
public class Register
{
  // Class/Instance fields
  private final int registerInitializationValue = GlobalConstants.REGISTER_INITIALIZATION_VALUE;  /** Initialization value for all the processor registers */
  private final int statusRegisterLength = GlobalConstants.STATUS_REGISTER_LENGTH;         /** Number of bits/flags stored in the status register: Z C N O */
  private int[] generalPurposeRegisters;              /** Array that holds all the processor register values, note R16 is a special purporse supervisor call register */
  private int accumulator;                            /** Temporary storage location for operations */
  private int programCounter;                         /** Program counter register: Holds address to the next instruction in memory */
  private int programCounterIncremented;              /** Program counter incremented register: Holds the temporary incremented program counter value */
  private int programCounterBranch;                   /** Program counter branch register: Holds the temporary branch program counter value */
  private int stackPointer;                           /** Stack pointer register: Holds address to the top of the stack */
  private int framePointer;                           /** Frame pointer register: Holds address of the start of the current frame in the stack */
  private int linkRegister;                           /** Link register: Holds return address values */
  private boolean[] statusRegister;                   /** Status register: Holds values of architectural status flags */
  private int instructionRegister;                    /** Instruction register: Holds the value of the last decoded instruction */
  private int memoryAddressRegister;                  /** Memory Address Register: Holds the memory address to which the data contained in the Memory Data Register needs to be stored */
  private int memoryDataRegister;                     /** Memory Data Register: Holds the data that needs to be stored to memory address contained in the Memory Address Register */
  private Memory cpuMemory;                           /** Reference to the CPU memory */
  private static int clockCounter;                    /** Variable that holds the number of cycles completed by the simulator - Declared as a static varaible since the processor should only contain a single clockCounter register no matter how many Register objects have been instantiated */
  private static int instructionCounter;              /** Could the number of instructions executed by the processor */
  private static int instructionCounterNOP;          /** Could the number of NOP instructions executed by the processor */

  // Initialize static variables
  static
  {
    clockCounter = GlobalConstants.CLOCK_REGISTER_INITIALIZATION_VALUE;
    instructionCounter = 0;
    instructionCounterNOP = 0;
  }

  // Class constructors
  /**
   * Default constructor
   * @return No return value since this is a constructor
   */
  public Register()
  {
    generalPurposeRegisters = new int[GlobalConstants.TOTAL_GP_REGISTERS];
    Arrays.fill(generalPurposeRegisters, registerInitializationValue);
    programCounter = registerInitializationValue;
    programCounterIncremented = registerInitializationValue;
    programCounterBranch = registerInitializationValue;
    stackPointer = registerInitializationValue;
    framePointer = stackPointer;
    linkRegister = registerInitializationValue;
    accumulator = registerInitializationValue;
    memoryAddressRegister = registerInitializationValue;
    memoryDataRegister = registerInitializationValue;
    statusRegister = new boolean[statusRegisterLength];
    for (int i = 0; i < statusRegister.length; i++)
    {
      statusRegister[i] = false;
    }
  }

  /**
   * Parameterized constructor that takes a custom-defined value of the total number of register that need to be created for the simulation.
   * Useful for creating special purpose and flag registers.
   * @param numberOfRegisters Total number of register to be created for the simulation
   * @return No return value since this is a constructor
   */
  public Register(int numberOfRegisters)
  {
    generalPurposeRegisters = new int[numberOfRegisters];
    Arrays.fill(generalPurposeRegisters, registerInitializationValue);
    programCounter = registerInitializationValue;
    programCounterIncremented = registerInitializationValue;
    programCounterBranch = registerInitializationValue;
    stackPointer = registerInitializationValue;
    framePointer = stackPointer;
    linkRegister = registerInitializationValue;
    accumulator = registerInitializationValue;
    memoryAddressRegister = registerInitializationValue;
    memoryDataRegister = registerInitializationValue;
    statusRegister = new boolean[4];
    for (int i = 0; i < statusRegister.length; i++)
    {
      statusRegister[i] = false;
    }
  }

  public Register(int numberOfRegisters, Memory _cpuMemory)
  {
    generalPurposeRegisters = new int[numberOfRegisters];
    Arrays.fill(generalPurposeRegisters, registerInitializationValue);
    programCounter = registerInitializationValue;
    programCounterIncremented = registerInitializationValue;
    programCounterBranch = registerInitializationValue;
    stackPointer = registerInitializationValue;
    framePointer = stackPointer;
    linkRegister = registerInitializationValue;
    accumulator = registerInitializationValue;
    memoryAddressRegister = registerInitializationValue;
    memoryDataRegister = registerInitializationValue;
    statusRegister = new boolean[4];
    for (int i = 0; i < statusRegister.length; i++)
    {
      statusRegister[i] = false;
    }
    cpuMemory = _cpuMemory;   // Store the actual cpu memory's reference for future use
  }

  // Class/Instance methods
  /**
   * Method to write a value to a general purpose (GP) register
   * @param registerNumber Register number for which the value needs to be read
   * @param newValue       Value that needs to be written to the specified register
   */
  public void writeGP(int registerNumber, int newValue)
  {
    if (registerNumber < 0 || registerNumber > generalPurposeRegisters.length)
    {
      throw new RegisterAccessException("Illegal register access (R" + Integer.toString(registerNumber) + " doesn't exist)."); 
    }
    else
    {
      if (registerNumber == 0)
      {
        throw new RegisterAccessException("Illegal register access (R" + Integer.toString(registerNumber) + " cannot be written to).");
      }
      else
      {
        generalPurposeRegisters[registerNumber] = newValue;
      }
    }
  }

  /**
   * Method to increment the program counter (PC) register to point to the next location in memory
   */
  public void incrementPC()
  {
    if((programCounterIncremented + 1) < 0 || (programCounterIncremented + 1) >= cpuMemory.getMemorySize()) // TODO change to check it doesnt enter the data region in memory
    {
      throw new RegisterAccessException("Illegal PC value (Location 0x" + Integer.toHexString(programCounterIncremented + 1) + " doesn't exist in memory)."); 
    }
    else
    {
      programCounterIncremented++;
    }
  }

  /**
   * Method to write a new value to the program counter (PC) register. Useful during branch and return instructions.
   * @param newValue Value that needs to be written to the PC register
   */
  public void writePC(int newValue)
  {
    if (newValue < 0 || newValue >= cpuMemory.getMemorySize())
    {
      throw new RegisterAccessException("Illegal PC value (Location 0x" + Integer.toHexString(newValue) + " doesn't exist in memory)."); 
    }
    else
    {
      programCounterBranch = newValue;
    }
  }

  /**
   * Method to update the value stored in the program counter (PC) register. Updated value is chosen based on whether or not a brach is taken. 
   * @param branchTaken Boolean value representing/stating whether a branch is taken or not
   */
  public void updatePC(boolean branchTaken)
  {
    if (branchTaken == false)   // Update the PC to the next instruction address
    {
      programCounter = programCounterIncremented;         // Set the actual program counter to the incremented program counter
    }
    else  // Update the PC to the branch target address
    {
      programCounter = programCounterBranch;              // Set the actual program counter to the branch location/address
      programCounterIncremented = programCounterBranch;   // Set the incremented program counter to programCounterBranch to avoid the incorrect value of programCounterIncremented being used in the next cycle
    }
  }

  /**
   * Method to write a new value to the instruction register (IR)
   * @param newValue Value that needs to be written to the IR register
   */
  public void writeIR(int newValue)
  {
    int opCode = (newValue >> (Isa.INSTRUCTION_LENGTH - Isa.OPCODE_LENGTH)) & (int)(Math.pow(2, Isa.OPCODE_LENGTH) - 1);     // Extract instruction OpCode (Logical AND with 31 since its 11111 in binary and opcode length is )

    // Note this check for an invalid instruction is made in the Instruction Decode Stage class.
    // if(opCode > (Isa.ISA_TOTAL_INSTRUCTIONS - 1))
    // {
    //   throw new RegisterAccessException("Illegal IR value (Instruction with OpCode \"" + Utility.convertToBin(opCode, 0).substring((Isa.INSTRUCTION_LENGTH - Isa.OPCODE_LENGTH), Isa.INSTRUCTION_LENGTH) + "\" is not specified in the ISA)."); 
    // }
    // else
    // {
    //   instructionRegister = newValue;
    // }
    instructionRegister = newValue;
  }

  /**
   * Method to write a new value to the stack pointer (SP)
   * @param newValue Value that needs to be written to the SP register
   */
  public void writeSP(int newValue)
  {
    stackPointer = newValue;
  }

  /**
   * Method to increment the stack pointer (SP)
   */
  public void incrementSP()
  {
    // TODO Insert check to avoid stack overflow (i.e. value shouln't go higher than the size of the stack - 1)
    stackPointer++;
  }

  /**
   * Method to decrement the stack pointer (SP)
   */
  public void decrementSP()
  {
    // TODO Insert check to avoid stack overflow (i.e. value shouln't go less than 0)
    stackPointer--;
  }

  /**
   * Method to write a new value to the frame pointer (FP)
   * @param newValue Value that needs to be written to the FP register
   */
  public void writeFP(int newValue)
  {
    framePointer = newValue;
  }

  /**
   * Method to write a new value to the link register (LR)
   * @param newValue Value that needs to be written to the link register
   */
  public void writeLR(int newValue)
  {
    linkRegister = newValue;
  }

  /**
   * Method to write a new value to the status register (SR)
   * @param newValue Value that needs to be written to the status register
   */
  public void writeSR(boolean[] newValue)
  {
    if (newValue.length != statusRegisterLength)
    {
      throw new RegisterAccessException("Illegal SR value (Value that is being attempted to be written to the status register is not of the correct length).");
    }
    else
    {
      System.arraycopy(newValue, 0, statusRegister, 0, statusRegisterLength);   // Copy the contents of the new status value that has been passed to the function to the status register (SR)
    }
  }

  /**
   * Method to write a new value to the memory address register (MAR)
   * @param newValue Value that needs to be written to the memory address register
   */
  public void writeMAR(int newValue)
  {
    if (newValue < 0 || newValue >= cpuMemory.getMemorySize())
    {
      throw new RegisterAccessException("Illegal MAR value (Location 0x" + Integer.toHexString(newValue) + " doesn't exist in memory)."); 
    }
    else
    {
      memoryAddressRegister = newValue;
    }
  }

  /**
   * Method to write a new value to the memory data register (MDR)
   * @param newValue Value that needs to be written to the memory data register
   */
  public void writeMDR(int newValue)
  {
    memoryDataRegister = newValue;
  }

  /**
   * Method to read a value from a general purpose (GP) register
   * @param registerNumber Register number for which the value needs to be read
   * @return Value at the specefied register
   */
  public int readGP(int registerNumber)
  {
    if (registerNumber < 0 || registerNumber > generalPurposeRegisters.length)
    {
      throw new RegisterAccessException("Illegal register access (R" + Integer.toString(registerNumber) + " doesn't exist)."); 
    }
    else
    {
      return generalPurposeRegisters[registerNumber];     // If the register number is in range, read and return the value that is currently stored in this register
    }
  }

  /**
   * Method to read the current value stored in the instruction register (IR)
   * @return Value stored in the PC register
   */
  public int readIR()
  {
    return instructionRegister;
  }  

  /**
   * Method to read the current value stored in the program counter (PC) register 
   * @return Value stored in the PC register
   */
  public int readPC()
  {
    return programCounter;
  }

  /**
   * Method to read the current value stored in the stack pointer (SP) register 
   * @return Value stored in the SP register
   */
  public int readSP()
  {
    return stackPointer;
  }

  /**
   * Method to read the current value stored in the link register (LR)
   * @return Value stored in the link register
   */
  public int readLR()
  {
    return linkRegister;
  }

  /**
   * Method to read the current value stored in the status register (SR)
   * @return Value stored in the status register
   */
  public boolean[] readSR()
  {
    return statusRegister;
  }

  /**
   * Method to read the current value stored in the memory address register (MAR)
   * @return Value stored in the memory address register
   */
  public int readMAR()
  {
    return memoryAddressRegister;
  }

  /**
   * Method to read the current value stored in the memory data register (MDR)
   * @return Value stored in the memory data register
   */
  public int readMDR()
  {
    return memoryDataRegister;
  }

  /**
   * Method to increment the clock counter by one - Declared as static since it modifies a static (class) variable
   */
  public static void incrementClockCounter()
  {
    clockCounter++;           // Increment the clock by one (Should be done for each loop iteration) 
  }

  /**
   * Method to read the current clock counter value - Declared as static since it returns a static (class) variable
   * @return Current clock counter value
   */
  public static int readClockCounter()
  {
    return clockCounter;       // Increment the clock by one (Should be done for each loop iteration) 
  }

  /**
   * Method to increment the instruction counter
   */
  public void incrementInstructionCounter()
  {
    instructionCounter++;
  }

  /**
   * Method to increment the NOP instruction counter
   */
  public void incrementInstructionCounterNOP()
  {
    instructionCounterNOP++;
  }

  /**
   * Method to obtain the value stored in the instruction counter register, i.e. total number of instructions executed during the simulation
   * @return Total number of instructions executed by/during the simualtor
   */
  public int getInstructionCounter()
  {
    return instructionCounter;
  }

  /**
   * Method to obtain the value stored in the NOP instruction counter register, i.e. total number of NOP instructions executed during the simulation
   * @return Total number of NOP instructions executed by/during the simualtor
   */
  public int getInstructionCounterNOP()
  {
    return instructionCounterNOP;
  } 


  /**
   * Method to dump contents of all the architectural registers. 
   */
  public void dumpContents()
  {
    System.out.println("+---------------------------------------------------------------------------------+");
    System.out.println("| Dumping current state of processor registers                                    |");
    System.out.println("+---------------------------------------------------------------------------------+");
    System.out.println(">>> Register dump started <<< ");
    System.out.println("+---------------------------------------------------------------------------------+");
    System.out.println("| Register                      Data (Content in 2's complement)                  |");
    System.out.println("+---------------------------------------------------------------------------------+");
    for (int registerNumber = 0; registerNumber < generalPurposeRegisters.length; registerNumber++)
    {
      System.out.format("| R%02d   \t      0x%08x (%s)        |%n", registerNumber, generalPurposeRegisters[registerNumber], Utility.convertToBin(generalPurposeRegisters[registerNumber], 4));
    }
    System.out.format("| IR     \t      0x%08x (%s)        |%n", instructionRegister, Utility.convertToBin(instructionRegister, 4));
    System.out.format("| PC     \t      0x%08x (%s)        |%n", programCounter, Utility.convertToBin(programCounter, 4));
    System.out.format("| SP     \t      0x%08x (%s)        |%n", stackPointer, Utility.convertToBin(stackPointer, 4));
    System.out.format("| LR     \t      0x%08x (%s)        |%n", linkRegister, Utility.convertToBin(linkRegister, 4));
    System.out.print("| SR      \t      ");
    for (int statusRegisterIndex = 0; statusRegisterIndex < statusRegister.length; statusRegisterIndex++)
    {
      int tempStatusRegisterBitValue;
      if (statusRegister[statusRegisterIndex] == true)
      {
        tempStatusRegisterBitValue = 1;
      }
      else
      {
        tempStatusRegisterBitValue = 0;
      }
      System.out.format("%d  ", tempStatusRegisterBitValue);
    }
    System.out.println("                                                |");
    System.out.println("+---------------------------------------------------------------------------------+");
    System.out.println(">>> Register dump complete <<< \n");
  }
}