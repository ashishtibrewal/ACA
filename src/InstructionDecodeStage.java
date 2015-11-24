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
public class InstructionDecodeStage implements IStage
{
  // Class/Instance fields
  // private Register cpuRegisters;
  // private Memory cpuMemory;
  private int currentPC;
  private int currentInstruction;
  private String currentInstructionBinary;
  private ProcessorPipelineContext pContext;    /** Reference to the processor pipeline context */
  private int opCode;
  private int sourceReg1;
  private int sourceReg2;
  private int destinationReg;
  private int signedImmediate;
  private String instructionName;
  private String instructionType;

  // public InstructionDecode(Register cpuRegisters, Memory cpuMemory)
  // {
  //     this.cpuRegisters = cpuRegisters;
  //     this.cpuMemory = cpuMemory;
  // }

  public void execute(IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type
    currentInstruction = pContext.cpuRegisters.readIR();       // Read the value currently stored in the instruction register (IR)
    //currentInstructionBinary = Integer.toBinaryString(currentInstruction);  
    currentInstructionBinary = Utility.convertToBin(currentInstruction, 0);   // Not using the Integer.toBinaryString() method because it truncates leading binary zero characters.
    opCode = (currentInstruction >> (Isa.INSTRUCTION_LENGTH - Isa.OPCODE_LENGTH)) & (int)(Math.pow(2, Isa.OPCODE_LENGTH) - 1);     // Extract instruction OpCode (Logical AND with 31 since its 11111 in binary and opcode length is )
    if(opCode > (Isa.ISA_TOTAL_INSTRUCTIONS - 1))     // Check if the opCode is valid (i.e. check if it's a valid instruction)
    {
      throw new IllegalInstructionException("Illegal instruction (Instruction with OpCode \"" + Utility.convertToBin(opCode, 0).substring((Isa.INSTRUCTION_LENGTH - Isa.OPCODE_LENGTH), Isa.INSTRUCTION_LENGTH) + "\" is not specified in the ISA)."); 
    }
    this.printInstructionMnemonic(opCode);
    switch (opCode)
    {
      // NOP
      case Isa.NOP:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;
      
      // ADD dr, sr1, sr2
      case Isa.ADD:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // SUB dr, sr1, sr2
      case Isa.SUB:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // MULT dr, sr1, sr2
      case Isa.MULT:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // DIV dr, sr1, sr2
      case Isa.DIV:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // ADDI dr, sr1, Ix
      case Isa.ADDI:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // SUBI dr, sr1, Ix
      case Isa.SUBI:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // AND dr, sr1, sr2
      case Isa.AND:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // OR dr, sr1, sr2 
      case Isa.OR:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // XOR dr, sr1, sr2
      case Isa.XOR:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // NOT dr, sr1, sr2
      case Isa.NOT:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // SLL dr, sr1, Ix
      case Isa.SLL:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // SLR dr, sr1, Ix
      case Isa.SLR:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // SLLV dr, s1, sr2
      case Isa.SLLV:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // SRA dr, sr1, Ix
      case Isa.SRA:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // LW dr, sr1, Ix
      case Isa.LW:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // SW sr1, sr2, Ix
      case Isa.SW:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // MOVI dr, Ix
      case Isa.MOVI:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // MOVR dr, sr1
      case Isa.MOVR:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // BU Ix
      case Isa.BU:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // BL Ix
      case Isa.BL:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // BEQ sr1, sr2, Ix
      case Isa.BEQ:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // BNE sr1, sr2, Ix
      case Isa.BNE:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // BLT sr1, sr2, Ix
      case Isa.BLT:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // BGT sr1, sr2, Ix
      case Isa.BGT:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // RET
      case Isa.RET:
        this.extractInformation(instructionName, instructionType);
        // TODO Need to add instructions to the instruction issue unit's queue
        break;

      // Default case. This condition should never be reached (Primarily because an invalid instruction can never be written to the IR).
      default:
        System.out.println("Invalid Instruction! Instruction couldn't be decoded!");
        // TODO Find how to throw a new exception
        break;
    } 
    // The line below needs to be removed and put in the execute or the memory access stage
    pContext.cpuRegisters.updatePC(false);                     // Update primary PC register with the incremented shadow PC register value.
  }


  /**
   * Method to extract information for each type of instruction
   * @param instructionType [description]
   */
  private void extractInformation(String instructionName, String instructionType)
  {
    switch(instructionType)
    {
      // RRR type
      case "RRR":
        sourceReg1 = Utility.convertToInt(currentInstructionBinary.substring(Isa.InstructionType.RRR.S1_START, Isa.InstructionType.RRR.S1_END), true);
        sourceReg2 = Utility.convertToInt(currentInstructionBinary.substring(Isa.InstructionType.RRR.S2_START, Isa.InstructionType.RRR.S2_END), true);
        destinationReg = Utility.convertToInt(currentInstructionBinary.substring(Isa.InstructionType.RRR.D_START, Isa.InstructionType.RRR.D_END), true);
        pContext.currentInstruction = new Instruction(instructionType,
                                                      opCode, 
                                                      pContext.cpuRegisters.readPC(),
                                                      Isa.InstructionType.RRR.NUMBER_OF_CYCLES,
                                                      pContext.cpuRegisters.readGP(sourceReg1),
                                                      pContext.cpuRegisters.readGP(sourceReg2),
                                                      destinationReg,
                                                      Isa.DEFAULT_IMM_VALUE);
        // TODO Check is the NOP instruction needs to be handled in a different manner when creating the Instruction object. Note that the way it's being done above should work for both. 
        if (instructionName == "NOP")   // Check exceptional case for the instruction being NOP.
        {
          System.out.println("Decoded instruction details: " + "None since this is a NOP instruction");
        }
        else
        {
          System.out.println("Decoded instruction details: " + instructionName + " R" + destinationReg + ", R" + sourceReg1 + ", R" + sourceReg2);
        }
        break;
      
      // RRI type
      case "RRI":
        signedImmediate = Utility.convertToInt(currentInstructionBinary.substring(Isa.InstructionType.RRI.IMM_START, Isa.InstructionType.RRI.IMM_END), true);
        sourceReg1 = Utility.convertToInt(currentInstructionBinary.substring(Isa.InstructionType.RRI.S1_START, Isa.InstructionType.RRI.S1_END), true);
        destinationReg = Utility.convertToInt(currentInstructionBinary.substring(Isa.InstructionType.RRI.D_START, Isa.InstructionType.RRI.D_END), true);
        pContext.currentInstruction = new Instruction(instructionType,
                                                      opCode, 
                                                      pContext.cpuRegisters.readPC(),
                                                      Isa.InstructionType.RRI.NUMBER_OF_CYCLES,
                                                      pContext.cpuRegisters.readGP(sourceReg1),
                                                      Isa.DEFAULT_REG_VALUE,
                                                      destinationReg,
                                                      signedImmediate);
        System.out.println("Decoded instruction details: " + instructionName + " R" + destinationReg + ", R" + sourceReg1 + ", I" + signedImmediate);
        break;

      // RR type
      case "RR":
        sourceReg1 = Utility.convertToInt(currentInstructionBinary.substring(Isa.InstructionType.RR.S1_START, Isa.InstructionType.RR.S1_END), true);
        destinationReg = Utility.convertToInt(currentInstructionBinary.substring(Isa.InstructionType.RR.D_START, Isa.InstructionType.RR.D_END), true);
        pContext.currentInstruction = new Instruction(instructionType,
                                                      opCode, 
                                                      pContext.cpuRegisters.readPC(),
                                                      Isa.InstructionType.RR.NUMBER_OF_CYCLES,
                                                      pContext.cpuRegisters.readGP(sourceReg1),
                                                      destinationReg,
                                                      Isa.DEFAULT_IMM_VALUE);
        System.out.println("Decoded instruction details: " + instructionName + " R" + destinationReg + ", R" + sourceReg1);
        break;

      // RI type
      case "RI":
        signedImmediate = Utility.convertToInt(currentInstructionBinary.substring(Isa.InstructionType.RI.IMM_START, Isa.InstructionType.RI.IMM_END), true);
        destinationReg = Utility.convertToInt(currentInstructionBinary.substring(Isa.InstructionType.RI.D_START, Isa.InstructionType.RI.D_END), true);
        pContext.currentInstruction = new Instruction(instructionType,
                                                      opCode, 
                                                      pContext.cpuRegisters.readPC(),
                                                      Isa.InstructionType.RI.NUMBER_OF_CYCLES,
                                                      Isa.DEFAULT_REG_VALUE,
                                                      destinationReg,
                                                      signedImmediate);
        System.out.println("Decoded instruction details: " + instructionName + " R" + destinationReg + ", I" + signedImmediate);
        break;

      // I type
      case "I":
        signedImmediate = Utility.convertToInt(currentInstructionBinary.substring(Isa.InstructionType.I.IMM_START, Isa.InstructionType.I.IMM_END), true);
        pContext.currentInstruction = new Instruction(instructionType,
                                                      opCode, 
                                                      pContext.cpuRegisters.readPC(),
                                                      Isa.InstructionType.I.NUMBER_OF_CYCLES,
                                                      destinationReg,
                                                      signedImmediate);
        System.out.println("Decoded instruction details: " + instructionName + " I" + signedImmediate);
        break;

      // Shoudn't get here since it's an invalid instruction type
      default:
        System.out.println("Invalid Instruction Type! Instruction information couldn't be extracted!");
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
        instructionName = "NOP";
        instructionType = "RRR";    // Note that for simplicity, the NOP operation is classified as a RRR type instruction
        break;
      
      // ADD dr, sr1, sr2
      case Isa.ADD:
        instructionName = "ADD";
        instructionType = "RRR";
        break;

      // SUB dr, sr1, sr2
      case Isa.SUB:
        instructionName = "SUB";
        instructionType = "RRR";
        break;

      // MULT dr, sr1, sr2
      case Isa.MULT:
        instructionName = "MULT";
        instructionType = "RRR";
        break;

      // DIV dr, sr1, sr2
      case Isa.DIV:
        instructionName = "DIV";
        instructionType = "RRR";
        break;

      // ADDI dr, sr1, Ix
      case Isa.ADDI:
        instructionName = "ADDI";
        instructionType = "RRI";
        break;

      // SUBI dr, sr1, Ix
      case Isa.SUBI:
        instructionName = "SUBI";
        instructionType = "RRI";
        break;

      // AND dr, sr1, sr2
      case Isa.AND:
        instructionName = "AND";
        instructionType = "RRR";
        break;

      // OR dr, sr1, sr2 
      case Isa.OR:
        instructionName = "OR";
        instructionType = "RRR";
        break;

      // XOR dr, sr1, sr2
      case Isa.XOR:
        instructionName = "XOR";
        instructionType = "RRR";
        break;

      // NOT dr, sr1, sr2
      case Isa.NOT:
        instructionName = "NOT";
        instructionType = "RRR";
        break;

      // SLL dr, sr1, Ix
      case Isa.SLL:
        instructionName = "SLL";
        instructionType = "RRI";
        break;

      // SLR dr, sr1, Ix
      case Isa.SLR:
        instructionName = "SLR";
        instructionType = "RRI";
        break;

      // SLLV dr, s1, sr2
      case Isa.SLLV:
        instructionName = "SLLV";
        instructionType = "RRR";
        break;

      // SRA dr, sr1, Ix
      case Isa.SRA:
        instructionName = "SRA";
        instructionType = "RRI";
        break;

      // LW dr, sr1, Ix
      case Isa.LW:
        instructionName = "LW";
        instructionType = "RRI";
        break;

      // SW sr1, sr2, Ix
      case Isa.SW:
        instructionName = "SW";
        instructionType = "RRI";
        break;

      // MOVI dr, Ix
      case Isa.MOVI:
        instructionName = "MOVI";
        instructionType = "RI";
        break;

      // MOVR dr, sr1
      case Isa.MOVR:
        instructionName = "MOVR";
        instructionType = "RR";
        break;

      // BU Ix
      case Isa.BU:
        instructionName = "BU";
        instructionType = "I";
        break;

      // BL Ix
      case Isa.BL:
        instructionName = "BL";
        instructionType = "I";
        break;

      // BEQ sr1, sr2, Ix
      case Isa.BEQ:
        instructionName = "BEQ";
        instructionType = "RRI";
        break;

      // BNE sr1, sr2, Ix
      case Isa.BNE:
        instructionName = "BNE";
        instructionType = "RRI";
        break;

      // BLT sr1, sr2, Ix
      case Isa.BLT:
        instructionName = "BLT";
        instructionType = "RRI";
        break;

      // BGT sr1, sr2, Ix
      case Isa.BGT:
        instructionName = "BGT";
        instructionType = "RRI";
        break;

      // RET
      case Isa.RET:
        instructionName = "RET";
        instructionType = "RRR";
        break;

      // Default case. This condition should never be reached (Primarily because an invalid instruction can never be written to the IR).
      default:
        instructionName = "INVALID";
        System.out.println("Invalid Instruction! Instruction couldn't be decoded!");
        // TODO Find how to throw a new exception
        break;
    }
    System.out.format("Current instruction: %s | OpCode: %s | Decoded instruction: %s | Instruction type: %s \n", currentInstructionBinary, 
                          Utility.convertToBin(opCode, 0).substring((Isa.INSTRUCTION_LENGTH - Isa.OPCODE_LENGTH), Isa.INSTRUCTION_LENGTH), 
                          instructionName, instructionType);    
  }
}