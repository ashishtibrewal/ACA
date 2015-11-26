/**
 * @file ISA.java
 * @author Ashish Tibrewal
 * @date 17.10.2015
 * @details This file contains all the global constants defining the ISA and any other constants being used in the simulator. All constants are declared
 * public so that they are accessible by classes/objects outside of this class.
 */

// Import packages
import java.io.*;
import java.util.*;
import java.lang.*;

public class Isa
{
  // Constants for all the instructions specified in the ISA
  public static final int NOP   = 0;
  public static final int ADD   = 1;
  public static final int SUB   = 2;
  public static final int MULT  = 3;
  public static final int DIV   = 4;
  public static final int ADDI  = 5;
  public static final int SUBI  = 6;
  public static final int AND   = 7;
  public static final int OR    = 8;
  public static final int XOR   = 9;
  public static final int NOT   = 10;
  public static final int SLL   = 11;
  public static final int SLR   = 12;
  public static final int SLLV  = 13;
  public static final int SRA   = 14;
  public static final int LW    = 15;
  public static final int SW    = 16;
  public static final int MOVI  = 17;
  public static final int MOVR  = 18;
  public static final int BU    = 19;
  public static final int BL    = 20;
  public static final int BEQ   = 21;
  public static final int BNE   = 22;
  public static final int BLT   = 23;
  public static final int BGT   = 24;
  public static final int RET   = 25;

  // Other instruction constants
  public static final int ISA_TOTAL_INSTRUCTIONS = 26;
  public static final int INSTRUCTION_LENGTH = 32;
  public static final int OPCODE_LENGTH = 5;          // Same OpCode length in the entire ISA and for all different types of instructions. This makes the implementation a lot simpler.
  public static final String DEFAULT_IMM_VALUE = "";  //(TODO) Could actually change this to "0000000000000000" since all instructions with immediates have the same length immediate value (i.e. 16 bits)
  public static final int DEFAULT_REG_VALUE = 0;
  public static final int TOTAL_PROCESSOR_REGISTERS = 16;     // Total number of processor registers supported. This is the total number of general purpose registers available to the programmer/assembler. Total processor registers are more than or equal to the total number of physical architectural registers supported by the microarchitecture.

  public class InstructionType
  {
    public static final int NUMBER_OF_INSTRUCTION_TYPES = 5;

    public class RRR
    {
      public static final int S1_START = 24;
      public static final int S1_END = 28;
      public static final int S2_START = 20;
      public static final int S2_END = 24;
      public static final int D_START = 28;
      public static final int D_END = 32;
      public static final int NUMBER_OF_CYCLES = 1;       // Number of cycles required to execute an instruction. Currently all instructions are set to require one cycle. Can later be changed to more appropriate values for certain instructions.
    }

    public class RRI
    {
      public static final int IMM_START = 8;
      public static final int IMM_END = 24;
      public static final int S1_START = 24;
      public static final int S1_END = 28;
      public static final int D_START = 28;
      public static final int D_END = 32;
      public static final int NUMBER_OF_CYCLES = 1;       // Number of cycles required to execute an instruction. Currently all instructions are set to require one cycle. Can later be changed to more appropriate values for certain instructions.
    }

    public class RR
    {
      public static final int S1_START = 24;
      public static final int S1_END = 28;
      public static final int D_START = 28;
      public static final int D_END = 32;
      public static final int NUMBER_OF_CYCLES = 1;       // Number of cycles required to execute an instruction. Currently all instructions are set to require one cycle. Can later be changed to more appropriate values for certain instructions.
    }

    public class RI
    {
      public static final int IMM_START = 12;
      public static final int IMM_END = 28;
      public static final int D_START = 28;
      public static final int D_END = 32;
      public static final int NUMBER_OF_CYCLES = 1;       // Number of cycles required to execute an instruction. Currently all instructions are set to require one cycle. Can later be changed to more appropriate values for certain instructions.
    }

    public class I
    {
      public static final int IMM_START = 16;
      public static final int IMM_END = 32;
      public static final int NUMBER_OF_CYCLES = 1;       // Number of cycles required to execute an instruction. Currently all instructions are set to require one cycle. Can later be changed to more appropriate values for certain instructions.
    }
  }
}