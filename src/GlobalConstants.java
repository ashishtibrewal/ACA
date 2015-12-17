/**
 * @file GlobalConstants.java
 * @author Ashish Tibrewal
 * @date 12.10.2015
 * @details This file contains the GlobalConstants class that stores all the constant values used by the simulator
 */

public final class GlobalConstants
{
  // Register contants
  public static final int TOTAL_GP_REGISTERS = 16;     // Total number of physical GP registers available in the microarchitecure.
  public static final int STATUS_REGISTER_LENGTH = 4;
  public static final int REGISTER_INITIALIZATION_VALUE = 0;
  public static final int CLOCK_REGISTER_INITIALIZATION_VALUE = 0;
  public static final int DEFAULT_PC = 0;

  // Memory constants
  // public static final int MEMORY_SIZE = 1024;
  public static final int MEMORY_SIZE = 32;
  public static final int MEMORY_INITIALIZATION_VALUE = 0;
  //public static final int STACK_SIZE = 1024;                    // Not used since by default the Stack class in Java doesn't support a constructor with a parameter specifying the size

  // Other processor constants
  public static final int PROCESSOR_WORD_LENGTH = 32;             // Word length the of the processor - the biggest piece of data that can be handled as a unit by the processor - This also specifies the data size that can be stored in the architectural registers, main memory and memory address sizes.
  public static final int PIPELINE_LENGTH = 5;
  public static final int INSTRUCTION_QUEUE_CAPACITY = 100;
  public static final int INSTRUCTION_LIST_START_INDEX = 0;
  public static final int BL_ITEMS_TO_PUSH = TOTAL_GP_REGISTERS + 1;    // Number of items to push to the stack when a BL, i.e. branch with link instruction, is found. Value = Total number of GP registers + 1, this + 1 is for the link register
  public static final int RET_ITEMS_TO_POP = TOTAL_GP_REGISTERS + 1;    // Number of items to pop off the stack when a RET, i.e. return instruction, is found. Value = Total number of GP registers + 1, this + 1 is for the link register
  public static final int NUM_ITERATIONS = 20;                          // Only used for testing/debugging purposes
  public static final int SVC_REGISTER = 15;                            // Reserve R15 as the SVC register.
  public static final int SVC_SUSPEND = 255;                            // Supervisor call to end/halt/terminate the simulation. Value needs to be moved to R15
  public static final int BUS_WIDTH = 4;                                // Processor bus width - Useful during the superscalar implementation
  // TODO Insert ALL constants to this class.

  // Processor default constants
  public static final int DEFAULT_INSTRUCTION = Isa.NOP;                // Default instruction is the NOP instruction specefied in the ISA
  public static final int DEFAULT_INSTRUCTION_OPCODE = (DEFAULT_INSTRUCTION >> (Isa.INSTRUCTION_LENGTH - Isa.OPCODE_LENGTH)) & (int)(Math.pow(2, Isa.OPCODE_LENGTH) - 1);   // Default instruction OpCode
  public static final int DEFAULT_MEM_FETCH_LOC = 0;                    // Default memory fetch location
  public static final boolean DEFAULT_BRANCH_PREDICTION = false;        // Default branch prediction value is false, i.e. by default branches are not taken
  public static final boolean CORRECT_BRANCH_PREDICTION_RESULT = true;  // Default branch prediction result value is true, i.e. by default the simulator assumes that all the branches are predicted correctly by the simulator
  public static final String DEFAULT_INSTRUCTION_TYPE = "RRR";          // Default instruction type
  public static final String DEFAULT_INSTRUCTION_MNEMONIC = "NOP";      // Default instruction mnemonic
  public static final ExecutionUnit DEFAULT_EXECUTION_UNIT = ExecutionUnit.ALU;       // Default execution unit
  public static final boolean DEFAULT_DEPENDENCY_FLAG = false;          // Default dependency flag value is set to false, i.e. By default an instruction doesn't have any dependencies
  
  // Process execution unit (EU) constants
  public static final int TOTAL_ALU = 1;    //TODO Would need at least 4
  public static final int TOTAL_LSU = 1;    //TODO Would need at least 2
  public static final int TOTAL_BU = 1;
}