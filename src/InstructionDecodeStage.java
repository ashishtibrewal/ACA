/**
 * @file InstructionDecodeStage.java
 * @author Ashish Tibrewal
 * @date 02.11.2015
 * @details This file contains the InstructionDecodeStage class that handles the second stage of the pipeline. It is used to decode instructions
 * that have been fetched from the main memory by the InstructionFetch class.
 */

// Import packages
// Custom packages
import pipeline.*;

// Pre-defined Java packages
import java.util.*;
import java.lang.*;

/**
 * This class implements the Instruction Decode (ID) stage of the processor.
 */
public class InstructionDecodeStage implements IProcessorPipelineStage
{
  // Class/Instance fields
  // private Register cpuRegisters;
  // private Memory cpuMemory;
  private int instruction;                      /** Instruction to decode */
  private String instructionBinary;
  private int opCode;                           /** Stores decoded OpCode for an instruction */
  private int sourceReg1;                       /** Source register 1 number - NOT THE VALUE STORED IN THIS REGISTER */            
  private int sourceReg2;                       /** Source register 2 number - NOT THE VALUE STORED IN THIS REGISTER */            
  private int destinationReg;                   /** Destination register number - NOT THE VALUE STORED IN THIS REGISTER */            
  private int signedImmediate;                  /** Stores the signed immediate value encoded/embedded in the instruction */
  private String instructionMnemonic;           /** Stores the instruction mnemonic */
  private String instructionType;               /** Stores the type of the instruction */
  private ExecutionUnit executionUnit;          /** Stores which EU should be used to execute the instruction */
  private Register cpuRegisters;                /** Reference to architectural registers */
  private ProcessorPipelineContext pContext;    /** Reference to the processor pipeline context */

  // public InstructionDecode(Register cpuRegisters, Memory cpuMemory)
  // {
  //     this.cpuRegisters = cpuRegisters;
  //     this.cpuMemory = cpuMemory;
  // }

  public void execute(IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type
    cpuRegisters = pContext.getCpuRegisters();                 // Obtain and store the reference to the primary cpu registers object from the pipeline context (Doing this to avoid having to type it over and over again)
    instruction = pContext.getCurrentIR();       // Read value from the (current) instruction register (IR)
    //instruction = cpuRegisters.readIR();       // Read the value currently stored in the instruction register (IR)
    //instructionBinary = Integer.toBinaryString(instruction);  
    instructionBinary = Utility.convertToBin(instruction, 0);   // Not using the Integer.toBinaryString() method because it truncates leading binary zero characters.
    opCode = (instruction >> (Isa.INSTRUCTION_LENGTH - Isa.OPCODE_LENGTH)) & (int)(Math.pow(2, Isa.OPCODE_LENGTH) - 1);     // Extract instruction OpCode (Logical AND with 31 since its 11111 in binary and opcode length is )
    if(opCode > (Isa.ISA_TOTAL_INSTRUCTIONS - 1))     // Check if the opCode is valid (i.e. check if it's a valid instruction)
    {
      throw new IllegalInstructionException("Illegal instruction (Instruction with OpCode \"" + Utility.convertToBin(opCode, 0).substring((Isa.INSTRUCTION_LENGTH - Isa.OPCODE_LENGTH), Isa.INSTRUCTION_LENGTH) + "\" is not specified in the ISA)."); 
    }
    this.printInstructionMnemonic(opCode);
    this.extractInformation(instructionMnemonic, instructionType);
    /*switch (opCode)
    {
      // NOP
      case Isa.NOP:
        this.extractInformation(instructionMnemonic, instructionType);
        break;
      
      // ADD dr, sr1, sr2
      case Isa.ADD:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // SUB dr, sr1, sr2
      case Isa.SUB:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // MULT dr, sr1, sr2
      case Isa.MULT:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // DIV dr, sr1, sr2
      case Isa.DIV:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // ADDI dr, sr1, Ix
      case Isa.ADDI:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // SUBI dr, sr1, Ix
      case Isa.SUBI:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // AND dr, sr1, sr2
      case Isa.AND:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // OR dr, sr1, sr2 
      case Isa.OR:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // XOR dr, sr1, sr2
      case Isa.XOR:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // NOT dr, sr1
      case Isa.NOT:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // SLL dr, sr1, Ix
      case Isa.SLL:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // SLR dr, sr1, Ix
      case Isa.SLR:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // SLLV dr, s1, sr2
      case Isa.SLLV:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // SRA dr, sr1, Ix
      case Isa.SRA:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // LW dr, sr1, Ix
      case Isa.LW:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // SW sr1, dr, Ix
      case Isa.SW:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // MOVI dr, Ix
      case Isa.MOVI:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // MOVR dr, sr1
      case Isa.MOVR:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // BU Ix
      case Isa.BU:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // BL Ix
      case Isa.BL:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // BEQ sr1, sr2, Ix
      case Isa.BEQ:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // BNE sr1, sr2, Ix
      case Isa.BNE:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // BLT sr1, sr2, Ix
      case Isa.BLT:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // BGT sr1, sr2, Ix
      case Isa.BGT:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // RET
      case Isa.RET:
        this.extractInformation(instructionMnemonic, instructionType);
        break;

      // Default case. This condition should never be reached (Primarily because an invalid instruction can never be written to the IR).
      default:
        System.out.println("Invalid Instruction! Instruction couldn't be decoded!");
        // TODO Find how to throw a new exception
        break;
    } */
  }

  /**
   * Method to extract information for each type of instruction
   * @param instructionType [description]
   */
  private void extractInformation(String instructionMnemonic, String instructionType)
  {
    switch(instructionType)
    {
      // RRR type
      case "RRR":
        sourceReg1 = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.RRR.S1_START, Isa.InstructionType.RRR.S1_END), false);
        sourceReg2 = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.RRR.S2_START, Isa.InstructionType.RRR.S2_END), false);
        destinationReg = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.RRR.D_START, Isa.InstructionType.RRR.D_END), false);
        signedImmediate = Isa.DEFAULT_IMM_VALUE;
        pContext.setNextInstruction(new Instruction(instructionType,
                                                    instructionMnemonic,
                                                    executionUnit,
                                                    opCode, 
                                                    pContext.getMemoryFetchLoc(),
                                                    instruction,
                                                    Isa.InstructionType.RRR.NUMBER_OF_CYCLES,
                                                    sourceReg1,
                                                    sourceReg2,
                                                    destinationReg,
                                                    signedImmediate));
        // TODO Check is the NOP instruction needs to be handled in a different manner when creating the Instruction object. Note that the way it's being done above should work for both. 
        // if (instructionMnemonic == "NOP")   // Check exceptional case for the instruction being NOP.
        // {
        //   System.out.println("Decoded instruction details: " + "None since this is a NOP instruction");
        // }
        // else
        // {
        //   System.out.println("Decoded instruction details: " + instructionMnemonic + " R" + destinationReg + ", R" + sourceReg1 + ", R" + sourceReg2);
        // }
        break;
      
      // RRI type
      case "RRI":
        sourceReg1 = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.RRI.S1_START, Isa.InstructionType.RRI.S1_END), false);
        sourceReg2 = Isa.DEFAULT_REG_VALUE;
        destinationReg = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.RRI.D_START, Isa.InstructionType.RRI.D_END), false);
        signedImmediate = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.RRI.IMM_START, Isa.InstructionType.RRI.IMM_END), true);
        pContext.setNextInstruction(new Instruction(instructionType,
                                                    instructionMnemonic,
                                                    executionUnit,
                                                    opCode, 
                                                    pContext.getMemoryFetchLoc(),
                                                    instruction,
                                                    Isa.InstructionType.RRI.NUMBER_OF_CYCLES,
                                                    sourceReg1,
                                                    sourceReg2,
                                                    destinationReg,
                                                    signedImmediate));
        // System.out.println("Decoded instruction details: " + instructionMnemonic + " R" + destinationReg + ", R" + sourceReg1 + ", I" + signedImmediate);
        break;

      // RR type
      case "RR":
        sourceReg1 = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.RR.S1_START, Isa.InstructionType.RR.S1_END), false);
        sourceReg2 = Isa.DEFAULT_REG_VALUE;
        destinationReg = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.RR.D_START, Isa.InstructionType.RR.D_END), false);
        signedImmediate = Isa.DEFAULT_IMM_VALUE;
        pContext.setNextInstruction(new Instruction(instructionType,
                                                    instructionMnemonic,
                                                    executionUnit,
                                                    opCode, 
                                                    pContext.getMemoryFetchLoc(),
                                                    instruction,
                                                    Isa.InstructionType.RR.NUMBER_OF_CYCLES,
                                                    sourceReg1,
                                                    destinationReg,
                                                    signedImmediate));
        // System.out.println("Decoded instruction details: " + instructionMnemonic + " R" + destinationReg + ", R" + sourceReg1);
        break;

      // RI type
      case "RI":
        sourceReg1 = Isa.DEFAULT_REG_VALUE;
        sourceReg2 = Isa.DEFAULT_REG_VALUE;
        destinationReg = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.RI.D_START, Isa.InstructionType.RI.D_END), false);
        signedImmediate = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.RI.IMM_START, Isa.InstructionType.RI.IMM_END), true);
        pContext.setNextInstruction(new Instruction(instructionType,
                                                    instructionMnemonic,
                                                    executionUnit,
                                                    opCode, 
                                                    pContext.getMemoryFetchLoc(),
                                                    instruction,
                                                    Isa.InstructionType.RI.NUMBER_OF_CYCLES,
                                                    sourceReg1,
                                                    destinationReg,
                                                    signedImmediate));
        // System.out.println("Decoded instruction details: " + instructionMnemonic + " R" + destinationReg + ", I" + signedImmediate);
        break;

      // I type
      case "I":
        sourceReg1 = Isa.DEFAULT_REG_VALUE;
        sourceReg2 = Isa.DEFAULT_REG_VALUE;
        destinationReg = Isa.DEFAULT_REG_VALUE;
        signedImmediate = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.I.IMM_START, Isa.InstructionType.I.IMM_END), true);
        pContext.setNextInstruction(new Instruction(instructionType,
                                                    instructionMnemonic,
                                                    executionUnit,
                                                    opCode, 
                                                    pContext.getMemoryFetchLoc(),
                                                    instruction,
                                                    Isa.InstructionType.I.NUMBER_OF_CYCLES,
                                                    signedImmediate));
        // System.out.println("Decoded instruction details: " + instructionMnemonic + " I" + signedImmediate);
        break;

      // Shoudn't get here since it's an invalid instruction type
      default:
        System.err.println("Invalid Instruction Type! Instruction information couldn't be extracted!");
        // TODO Find how to throw a new exception
        break;
    }
  }


  /**
   * * Method to print the instruction mnemonic depending on the OpCode
   * @param opCode Instruction OpCode
   */
  private void printInstructionMnemonic(int opCode)
  {
    switch (opCode)
    {
      // NOP
      case Isa.NOP:
        instructionMnemonic = "NOP";
        instructionType = "RRR";    // Note that for simplicity, the NOP operation is classified as a RRR type instruction
        executionUnit = ExecutionUnit.ALU;
        break;
      
      // ADD dr, sr1, sr2
      case Isa.ADD:
        instructionMnemonic = "ADD";
        instructionType = "RRR";
        executionUnit = ExecutionUnit.ALU;
        break;

      // SUB dr, sr1, sr2
      case Isa.SUB:
        instructionMnemonic = "SUB";
        instructionType = "RRR";
        executionUnit = ExecutionUnit.ALU;
        break;

      // MULT dr, sr1, sr2
      case Isa.MULT:
        instructionMnemonic = "MULT";
        instructionType = "RRR";
        executionUnit = ExecutionUnit.ALU;
        break;

      // DIV dr, sr1, sr2
      case Isa.DIV:
        instructionMnemonic = "DIV";
        instructionType = "RRR";
        executionUnit = ExecutionUnit.ALU;
        break;

      // ADDI dr, sr1, Ix
      case Isa.ADDI:
        instructionMnemonic = "ADDI";
        instructionType = "RRI";
        executionUnit = ExecutionUnit.ALU;
        break;

      // SUBI dr, sr1, Ix
      case Isa.SUBI:
        instructionMnemonic = "SUBI";
        instructionType = "RRI";
        executionUnit = ExecutionUnit.ALU;
        break;

      // AND dr, sr1, sr2
      case Isa.AND:
        instructionMnemonic = "AND";
        instructionType = "RRR";
        executionUnit = ExecutionUnit.ALU;
        break;

      // OR dr, sr1, sr2 
      case Isa.OR:
        instructionMnemonic = "OR";
        instructionType = "RRR";
        executionUnit = ExecutionUnit.ALU;
        break;

      // XOR dr, sr1, sr2
      case Isa.XOR:
        instructionMnemonic = "XOR";
        instructionType = "RRR";
        executionUnit = ExecutionUnit.ALU;
        break;

      // NOT dr, sr1
      case Isa.NOT:
        instructionMnemonic = "NOT";
        instructionType = "RR";
        executionUnit = ExecutionUnit.ALU;
        break;

      // SLL dr, sr1, Ix
      case Isa.SLL:
        instructionMnemonic = "SLL";
        instructionType = "RRI";
        executionUnit = ExecutionUnit.ALU;
        break;

      // SLR dr, sr1, Ix
      case Isa.SLR:
        instructionMnemonic = "SLR";
        instructionType = "RRI";
        executionUnit = ExecutionUnit.ALU;
        break;

      // SLLV dr, s1, sr2
      case Isa.SLLV:
        instructionMnemonic = "SLLV";
        instructionType = "RRR";
        executionUnit = ExecutionUnit.ALU;
        break;

      // SRA dr, sr1, Ix
      case Isa.SRA:
        instructionMnemonic = "SRA";
        instructionType = "RRI";
        executionUnit = ExecutionUnit.ALU;
        break;

      // LW dr, sr1, Ix
      case Isa.LW:
        instructionMnemonic = "LW";
        instructionType = "RRI";
        executionUnit = ExecutionUnit.LSU;
        break;

      // SW sr1, dr, Ix
      case Isa.SW:
        instructionMnemonic = "SW";
        instructionType = "RRI";
        executionUnit = ExecutionUnit.LSU;
        break;

      // MOVI dr, Ix
      case Isa.MOVI:
        instructionMnemonic = "MOVI";
        instructionType = "RI";
        executionUnit = ExecutionUnit.LSU;
        break;

      // MOVR dr, sr1
      case Isa.MOVR:
        instructionMnemonic = "MOVR";
        instructionType = "RR";
        executionUnit = ExecutionUnit.LSU;
        break;

      // BU Ix
      case Isa.BU:
        instructionMnemonic = "BU";
        instructionType = "I";
        executionUnit = ExecutionUnit.BU;
        break;

      // BL Ix
      case Isa.BL:
        instructionMnemonic = "BL";
        instructionType = "I";
        executionUnit = ExecutionUnit.BU;
        break;

      // BEQ sr1, sr2, Ix
      case Isa.BEQ:
        instructionMnemonic = "BEQ";
        instructionType = "RRI";
        executionUnit = ExecutionUnit.BU;
        break;

      // BNE sr1, sr2, Ix
      case Isa.BNE:
        instructionMnemonic = "BNE";
        instructionType = "RRI";
        executionUnit = ExecutionUnit.BU;
        break;

      // BLT sr1, sr2, Ix
      case Isa.BLT:
        instructionMnemonic = "BLT";
        instructionType = "RRI";
        executionUnit = ExecutionUnit.BU;
        break;

      // BGT sr1, sr2, Ix
      case Isa.BGT:
        instructionMnemonic = "BGT";
        instructionType = "RRI";
        executionUnit = ExecutionUnit.BU;
        break;

      // RET
      case Isa.RET:
        instructionMnemonic = "RET";
        instructionType = "RRR";
        executionUnit = ExecutionUnit.BU;
        break;

      // Default case. This condition should never be reached (Primarily because an invalid instruction can never be written to the IR).
      default:
        instructionMnemonic = "INVALID";
        System.err.println("Invalid Instruction! Instruction couldn't be decoded!");
        // TODO Find how to throw a new exception
        break;
    }
    // System.out.format("Current instruction: %s | OpCode: %s | Decoded instruction: %s | Instruction type: %s \n", instructionBinary, 
    //                       Utility.convertToBin(opCode, 0).substring((Isa.INSTRUCTION_LENGTH - Isa.OPCODE_LENGTH), Isa.INSTRUCTION_LENGTH), 
    //                       instructionMnemonic, instructionType);    
  }

  // TODO need to fill function contents accordingly
  public void flush(IPipelineContext context)
  {

  }

  /**
   * Method to obtain the current instruction that has been decoded by the ID stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current decoded instruction 
   */
  public int getCurrentInstruction()
  {
    return instruction;
  }

  /**
   * Method to obtain the current instruction mnemonic that has been decoded by the ID stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current decoded instruction mnemonic 
   */
  public String getCurrentInstructionMnemonic()
  {
    return instructionMnemonic;
  }

  /**
   * Method to obtain the current instruction type that has been decoded by the ID stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current decoded instruction type
   */
  public String getCurrentInstructionType()
  {
    return instructionType;
  }

  /**
   * Method to obtain the current source register 1 location that has been decoded by the ID stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current decoded source register 1 location
   */
  public int getCurrentSourceReg1()
  {
    return sourceReg1;
  }

  /**
   * Method to obtain the current source register 2 location that has been decoded by the ID stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current decoded source register 2 location
   */
  public int getCurrentSourceReg2()
  {
    return sourceReg2;
  }


  /**
   * Method to obtain the current destination register location that has been decoded by the ID stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current decoded destination register location
   */
  public int getCurrentDestinationReg()
  {
    return destinationReg;
  }

  /**
   * Method to obtain the current signed immediate value that has been decoded by the ID stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current decoded signed immediate value
   */
  public int getCurrentSignedImmediate()
  {
    return signedImmediate;
  }
}