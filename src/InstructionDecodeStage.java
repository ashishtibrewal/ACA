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
  private int[] instruction;                      /** Instruction to decode */
  private String[] instructionBinary;
  private int[] opCode;                           /** Stores decoded OpCode for an instruction */
  private int[] sourceReg1;                       /** Source register 1 number - NOT THE VALUE STORED IN THIS REGISTER */            
  private int[] sourceReg2;                       /** Source register 2 number - NOT THE VALUE STORED IN THIS REGISTER */            
  private int[] destinationReg;                   /** Destination register number - NOT THE VALUE STORED IN THIS REGISTER */            
  private int[] signedImmediate;                  /** Stores the signed immediate value encoded/embedded in the instruction */
  private String[] instructionMnemonic;           /** Stores the instruction mnemonic */
  private String[] instructionType;               /** Stores the type of the instruction */
  private ExecutionUnit[] executionUnit;          /** Stores which EU should be used to execute the instruction */
  private Register cpuRegisters;                /** Reference to architectural registers */
  private ProcessorPipelineContext pContext;    /** Reference to the processor pipeline context */

  // public InstructionDecode(Register cpuRegisters, Memory cpuMemory)
  // {
  //     this.cpuRegisters = cpuRegisters;
  //     this.cpuMemory = cpuMemory;
  // }

  public InstructionDecodeStage()
  {
    this.instructionBinary = new String[GlobalConstants.BUS_WIDTH];           // Instantiate array to avoid a null pointer exception
    this.opCode = new int[GlobalConstants.BUS_WIDTH];                         // Instantiate array to avoid a null pointer exception
    this.sourceReg1 = new int[GlobalConstants.BUS_WIDTH];                     // Instantiate array to avoid a null pointer exception       
    this.sourceReg2 = new int[GlobalConstants.BUS_WIDTH];                     // Instantiate array to avoid a null pointer exception
    this.destinationReg = new int[GlobalConstants.BUS_WIDTH];                 // Instantiate array to avoid a null pointer exception
    this.signedImmediate = new int[GlobalConstants.BUS_WIDTH];;               // Instantiate array to avoid a null pointer exception
    this.instructionMnemonic = new String[GlobalConstants.BUS_WIDTH];         // Instantiate array to avoid a null pointer exception
    this.instructionType = new String[GlobalConstants.BUS_WIDTH];             // Instantiate array to avoid a null pointer exception
    this.executionUnit = new ExecutionUnit[GlobalConstants.BUS_WIDTH];        // Instantiate array to avoid a null pointer exception
  }

  public void execute(IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type
    cpuRegisters = pContext.getCpuRegisters();                 // Obtain and store the reference to the primary cpu registers object from the pipeline context (Doing this to avoid having to type it over and over again)
    instruction = pContext.getCurrentIR();       // Read value from the (current) instruction register (IR)
    //instruction = cpuRegisters.readIR();       // Read the value currently stored in the instruction register (IR)
    //instructionBinary = Integer.toBinaryString(instruction);
    Instruction[] nextInst = new Instruction[GlobalConstants.BUS_WIDTH];
    for (int i = 0; i < GlobalConstants.BUS_WIDTH; i++)
    {
      nextInst[i] = new Instruction(GlobalConstants.DEFAULT_INSTRUCTION_TYPE,
                                    GlobalConstants.DEFAULT_INSTRUCTION_MNEMONIC,
                                    ExecutionUnit.ALU,
                                    GlobalConstants.DEFAULT_INSTRUCTION_OPCODE, 
                                    GlobalConstants.DEFAULT_MEM_FETCH_LOC,
                                    GlobalConstants.DEFAULT_INSTRUCTION,
                                    Isa.InstructionType.RRR.NUMBER_OF_CYCLES,
                                    GlobalConstants.DEFAULT_BRANCH_PREDICTION,
                                    Isa.DEFAULT_REG_VALUE,
                                    Isa.DEFAULT_REG_VALUE,
                                    Isa.DEFAULT_REG_VALUE,
                                    Isa.DEFAULT_IMM_VALUE);       // Instantiate each Instruction object in the nextInst array to avoid null pointer exceptions
    }
    for (int i = 0; i < GlobalConstants.BUS_WIDTH; i++)
    {
      instructionBinary[i] = Utility.convertToBin(instruction[i], 0);   // Not using the Integer.toBinaryString() method because it truncates leading binary zero characters.
      opCode[i] = (instruction[i] >> (Isa.INSTRUCTION_LENGTH - Isa.OPCODE_LENGTH)) & (int)(Math.pow(2, Isa.OPCODE_LENGTH) - 1);     // Extract instruction OpCode (Logical AND with 31 since its 11111 in binary and opcode length is )
      if(opCode[i] > (Isa.ISA_TOTAL_INSTRUCTIONS - 1))     // Check if the opCode is valid (i.e. check if it's a valid instruction)
      {
        throw new IllegalInstructionException("Illegal instruction (Instruction with OpCode \"" + Utility.convertToBin(opCode[i], 0).substring((Isa.INSTRUCTION_LENGTH - Isa.OPCODE_LENGTH), Isa.INSTRUCTION_LENGTH) + "\" is not specified in the ISA)."); 
      }
      this.generateInstructionInformation(i);
      nextInst[i] = this.extractInformation(i);
    }
    pContext.setNextInstruction(nextInst);        // Set the next set of instrutions to be used by the II stage
  }

  /**
   * Method to extract information for each type of instruction
   * @param iteratorVal Iterator value
   * @return            Decoded instruction object  
   */
  private Instruction extractInformation(int iteratorVal)
  {
    switch (instructionType[iteratorVal])
    {
      // RRR type
      case "RRR":
        sourceReg1[iteratorVal] = Utility.convertToInt(instructionBinary[iteratorVal].substring(Isa.InstructionType.RRR.S1_START, Isa.InstructionType.RRR.S1_END), false);
        sourceReg2[iteratorVal] = Utility.convertToInt(instructionBinary[iteratorVal].substring(Isa.InstructionType.RRR.S2_START, Isa.InstructionType.RRR.S2_END), false);
        destinationReg[iteratorVal] = Utility.convertToInt(instructionBinary[iteratorVal].substring(Isa.InstructionType.RRR.D_START, Isa.InstructionType.RRR.D_END), false);
        signedImmediate[iteratorVal] = Isa.DEFAULT_IMM_VALUE;
        return new Instruction(instructionType[iteratorVal],
                               instructionMnemonic[iteratorVal],
                               executionUnit[iteratorVal],
                               opCode[iteratorVal], 
                               pContext.getCurrentMemoryFetchLoc()[iteratorVal],
                               instruction[iteratorVal],
                               Isa.InstructionType.RRR.NUMBER_OF_CYCLES,
                               pContext.getCurrentInstructionBranchPredictionResult(),
                               sourceReg1[iteratorVal],
                               sourceReg2[iteratorVal],
                               destinationReg[iteratorVal],
                               signedImmediate[iteratorVal]);
        // TODO Check is the NOP instruction needs to be handled in a different manner when creating the Instruction object. Note that the way it's being done above should work for both. 
        // if (instructionMnemonic == "NOP")   // Check exceptional case for the instruction being NOP.
        // {
        //   System.out.println("Decoded instruction details: " + "None since this is a NOP instruction");
        // }
        // else
        // {
        //   System.out.println("Decoded instruction details: " + instructionMnemonic + " R" + destinationReg + ", R" + sourceReg1 + ", R" + sourceReg2);
        // }
      
      // RRI type
      case "RRI":
        if (instructionMnemonic[iteratorVal] == "BEQ" || instructionMnemonic[iteratorVal] == "BNE" || instructionMnemonic[iteratorVal] == "BLT" || instructionMnemonic[iteratorVal] == "BGT")
        {
          sourceReg1[iteratorVal] = Utility.convertToInt(instructionBinary[iteratorVal].substring(Isa.InstructionType.RRI.S1_START, Isa.InstructionType.RRI.S1_END), false);
          sourceReg2[iteratorVal] = Utility.convertToInt(instructionBinary[iteratorVal].substring(Isa.InstructionType.RRI.D_START, Isa.InstructionType.RRI.D_END), false);
          destinationReg[iteratorVal] =  Isa.DEFAULT_REG_VALUE;
          signedImmediate[iteratorVal] = Utility.convertToInt(instructionBinary[iteratorVal].substring(Isa.InstructionType.RRI.IMM_START, Isa.InstructionType.RRI.IMM_END), true);
          // System.out.println("Decoded instruction details: " + instructionMnemonic + " R" + sourceReg1 + ", R" + sourceReg2 + ", I" + signedImmediate);
        }
        else
        {
          sourceReg1[iteratorVal] = Utility.convertToInt(instructionBinary[iteratorVal].substring(Isa.InstructionType.RRI.S1_START, Isa.InstructionType.RRI.S1_END), false);
          sourceReg2[iteratorVal] = Isa.DEFAULT_REG_VALUE;
          destinationReg[iteratorVal] = Utility.convertToInt(instructionBinary[iteratorVal].substring(Isa.InstructionType.RRI.D_START, Isa.InstructionType.RRI.D_END), false);
          signedImmediate[iteratorVal] = Utility.convertToInt(instructionBinary[iteratorVal].substring(Isa.InstructionType.RRI.IMM_START, Isa.InstructionType.RRI.IMM_END), true);
          // System.out.println("Decoded instruction details: " + instructionMnemonic + " R" + destinationReg + ", R" + sourceReg1 + ", I" + signedImmediate);
        }
        return new Instruction(instructionType[iteratorVal],
                               instructionMnemonic[iteratorVal],
                               executionUnit[iteratorVal],
                               opCode[iteratorVal], 
                               pContext.getCurrentMemoryFetchLoc()[iteratorVal],
                               instruction[iteratorVal],
                               Isa.InstructionType.RRI.NUMBER_OF_CYCLES,
                               pContext.getCurrentInstructionBranchPredictionResult(),
                               sourceReg1[iteratorVal],
                               sourceReg2[iteratorVal],
                               destinationReg[iteratorVal],
                               signedImmediate[iteratorVal]);

      // RR type
      case "RR":
        sourceReg1[iteratorVal] = Utility.convertToInt(instructionBinary[iteratorVal].substring(Isa.InstructionType.RR.S1_START, Isa.InstructionType.RR.S1_END), false);
        sourceReg2[iteratorVal] = Isa.DEFAULT_REG_VALUE;
        destinationReg[iteratorVal] = Utility.convertToInt(instructionBinary[iteratorVal].substring(Isa.InstructionType.RR.D_START, Isa.InstructionType.RR.D_END), false);
        signedImmediate[iteratorVal] = Isa.DEFAULT_IMM_VALUE;
        return new Instruction(instructionType[iteratorVal],
                               instructionMnemonic[iteratorVal],
                               executionUnit[iteratorVal],
                               opCode[iteratorVal], 
                               pContext.getCurrentMemoryFetchLoc()[iteratorVal],
                               instruction[iteratorVal],
                               Isa.InstructionType.RR.NUMBER_OF_CYCLES,
                               pContext.getCurrentInstructionBranchPredictionResult(),
                               sourceReg1[iteratorVal],
                               destinationReg[iteratorVal],
                               signedImmediate[iteratorVal]);
        // System.out.println("Decoded instruction details: " + instructionMnemonic + " R" + destinationReg + ", R" + sourceReg1);

      // RI type
      case "RI":
        sourceReg1[iteratorVal] = Isa.DEFAULT_REG_VALUE;
        sourceReg2[iteratorVal] = Isa.DEFAULT_REG_VALUE;
        destinationReg[iteratorVal] = Utility.convertToInt(instructionBinary[iteratorVal].substring(Isa.InstructionType.RI.D_START, Isa.InstructionType.RI.D_END), false);
        signedImmediate[iteratorVal] = Utility.convertToInt(instructionBinary[iteratorVal].substring(Isa.InstructionType.RI.IMM_START, Isa.InstructionType.RI.IMM_END), true);
        return new Instruction(instructionType[iteratorVal],
                               instructionMnemonic[iteratorVal],
                               executionUnit[iteratorVal],
                               opCode[iteratorVal],
                               pContext.getCurrentMemoryFetchLoc()[iteratorVal],
                               instruction[iteratorVal],
                               Isa.InstructionType.RI.NUMBER_OF_CYCLES,
                               pContext.getCurrentInstructionBranchPredictionResult(),
                               sourceReg1[iteratorVal],
                               destinationReg[iteratorVal],
                               signedImmediate[iteratorVal]);
        // System.out.println("Decoded instruction details: " + instructionMnemonic + " R" + destinationReg + ", I" + signedImmediate);

      // I type
      case "I":
        sourceReg1[iteratorVal] = Isa.DEFAULT_REG_VALUE;
        sourceReg2[iteratorVal] = Isa.DEFAULT_REG_VALUE;
        destinationReg[iteratorVal] = Isa.DEFAULT_REG_VALUE;
        signedImmediate[iteratorVal] = Utility.convertToInt(instructionBinary[iteratorVal].substring(Isa.InstructionType.I.IMM_START, Isa.InstructionType.I.IMM_END), true);
        return new Instruction(instructionType[iteratorVal],
                               instructionMnemonic[iteratorVal],
                               executionUnit[iteratorVal],
                               opCode[iteratorVal], 
                               pContext.getCurrentMemoryFetchLoc()[iteratorVal],
                               instruction[iteratorVal],
                               Isa.InstructionType.I.NUMBER_OF_CYCLES,
                               pContext.getCurrentInstructionBranchPredictionResult(),
                               signedImmediate[iteratorVal]);
        // System.out.println("Decoded instruction details: " + instructionMnemonic + " I" + signedImmediate);

      // Shoudn't get here since it's an invalid instruction type
      default:
        throw new IllegalInstructionException("Invalid instruction yype! Instruction information couldn't be extracted!");
    }
  }


  /**
   * * Method to generate instruction information depending on the OpCode
   * @param iteratorVal Iterator value
   */
  private void generateInstructionInformation(int iteratorVal)
  {
    switch (opCode[iteratorVal])
    {
      // NOP
      case Isa.NOP:
        instructionMnemonic[iteratorVal] = "NOP";
        instructionType[iteratorVal] = "RRR";    // Note that for simplicity, the NOP operation is classified as a RRR type instruction
        executionUnit[iteratorVal] = ExecutionUnit.ALU;
        break;
      
      // ADD dr, sr1, sr2
      case Isa.ADD:
        instructionMnemonic[iteratorVal]= "ADD";
        instructionType[iteratorVal] = "RRR";
        executionUnit[iteratorVal] = ExecutionUnit.ALU;
        break;

      // SUB dr, sr1, sr2
      case Isa.SUB:
        instructionMnemonic[iteratorVal] = "SUB";
        instructionType[iteratorVal] = "RRR";
        executionUnit[iteratorVal] = ExecutionUnit.ALU;
        break;

      // MULT dr, sr1, sr2
      case Isa.MULT:
        instructionMnemonic[iteratorVal] = "MULT";
        instructionType[iteratorVal] = "RRR";
        executionUnit[iteratorVal] = ExecutionUnit.ALU;
        break;

      // DIV dr, sr1, sr2
      case Isa.DIV:
        instructionMnemonic[iteratorVal] = "DIV";
        instructionType[iteratorVal] = "RRR";
        executionUnit[iteratorVal] = ExecutionUnit.ALU;
        break;

      // ADDI dr, sr1, Ix
      case Isa.ADDI:
        instructionMnemonic[iteratorVal] = "ADDI";
        instructionType[iteratorVal] = "RRI";
        executionUnit[iteratorVal] = ExecutionUnit.ALU;
        break;

      // SUBI dr, sr1, Ix
      case Isa.SUBI:
        instructionMnemonic[iteratorVal] = "SUBI";
        instructionType[iteratorVal] = "RRI";
        executionUnit[iteratorVal] = ExecutionUnit.ALU;
        break;

      // AND dr, sr1, sr2
      case Isa.AND:
        instructionMnemonic[iteratorVal] = "AND";
        instructionType[iteratorVal] = "RRR";
        executionUnit[iteratorVal] = ExecutionUnit.ALU;
        break;

      // OR dr, sr1, sr2 
      case Isa.OR:
        instructionMnemonic[iteratorVal] = "OR";
        instructionType[iteratorVal] = "RRR";
        executionUnit[iteratorVal] = ExecutionUnit.ALU;
        break;

      // XOR dr, sr1, sr2
      case Isa.XOR:
        instructionMnemonic[iteratorVal] = "XOR";
        instructionType[iteratorVal] = "RRR";
        executionUnit[iteratorVal] = ExecutionUnit.ALU;
        break;

      // NOT dr, sr1
      case Isa.NOT:
        instructionMnemonic[iteratorVal] = "NOT";
        instructionType[iteratorVal] = "RR";
        executionUnit[iteratorVal] = ExecutionUnit.ALU;
        break;

      // SLL dr, sr1, Ix
      case Isa.SLL:
        instructionMnemonic[iteratorVal] = "SLL";
        instructionType[iteratorVal] = "RRI";
        executionUnit[iteratorVal] = ExecutionUnit.ALU;
        break;

      // SLR dr, sr1, Ix
      case Isa.SLR:
        instructionMnemonic[iteratorVal] = "SLR";
        instructionType[iteratorVal] = "RRI";
        executionUnit[iteratorVal] = ExecutionUnit.ALU;
        break;

      // SLLV dr, s1, sr2
      case Isa.SLLV:
        instructionMnemonic[iteratorVal] = "SLLV";
        instructionType[iteratorVal] = "RRR";
        executionUnit[iteratorVal] = ExecutionUnit.ALU;
        break;

      // SRA dr, sr1, Ix
      case Isa.SRA:
        instructionMnemonic[iteratorVal] = "SRA";
        instructionType[iteratorVal] = "RRI";
        executionUnit[iteratorVal] = ExecutionUnit.ALU;
        break;

      // LW dr, sr1, Ix
      case Isa.LW:
        instructionMnemonic[iteratorVal] = "LW";
        instructionType[iteratorVal] = "RRI";
        executionUnit[iteratorVal] = ExecutionUnit.LSU;
        break;

      // SW sr1, dr, Ix
      case Isa.SW:
        instructionMnemonic[iteratorVal] = "SW";
        instructionType[iteratorVal] = "RRI";
        executionUnit[iteratorVal] = ExecutionUnit.LSU;
        break;

      // MOVI dr, Ix
      case Isa.MOVI:
        instructionMnemonic[iteratorVal] = "MOVI";
        instructionType[iteratorVal] = "RI";
        executionUnit[iteratorVal] = ExecutionUnit.LSU;
        break;

      // MOVR dr, sr1
      case Isa.MOVR:
        instructionMnemonic[iteratorVal] = "MOVR";
        instructionType[iteratorVal] = "RR";
        executionUnit[iteratorVal] = ExecutionUnit.LSU;
        break;

      // BU Ix
      case Isa.BU:
        instructionMnemonic[iteratorVal] = "BU";
        instructionType[iteratorVal] = "I";
        executionUnit[iteratorVal] = ExecutionUnit.BU;
        break;

      // BL Ix
      case Isa.BL:
        instructionMnemonic[iteratorVal] = "BL";
        instructionType[iteratorVal] = "I";
        executionUnit[iteratorVal] = ExecutionUnit.BU;
        break;

      // BEQ sr1, sr2, Ix
      case Isa.BEQ:
        instructionMnemonic[iteratorVal] = "BEQ";
        instructionType[iteratorVal] = "RRI";
        executionUnit[iteratorVal] = ExecutionUnit.BU;
        break;

      // BNE sr1, sr2, Ix
      case Isa.BNE:
        instructionMnemonic[iteratorVal] = "BNE";
        instructionType[iteratorVal] = "RRI";
        executionUnit[iteratorVal] = ExecutionUnit.BU;
        break;

      // BLT sr1, sr2, Ix
      case Isa.BLT:
        instructionMnemonic[iteratorVal] = "BLT";
        instructionType[iteratorVal] = "RRI";
        executionUnit[iteratorVal] = ExecutionUnit.BU;
        break;

      // BGT sr1, sr2, Ix
      case Isa.BGT:
        instructionMnemonic[iteratorVal] = "BGT";
        instructionType[iteratorVal] = "RRI";
        executionUnit[iteratorVal] = ExecutionUnit.BU;
        break;

      // RET
      case Isa.RET:
        instructionMnemonic[iteratorVal] = "RET";
        instructionType[iteratorVal] = "RRR";
        executionUnit[iteratorVal] = ExecutionUnit.BU;
        break;

      // Default case. This condition should never be reached (Primarily because an invalid instruction can never be written to the IR).
      default:
        instructionMnemonic[iteratorVal] = "INVALID";
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
    Arrays.fill(instruction, GlobalConstants.DEFAULT_INSTRUCTION);
    Arrays.fill(instructionBinary, Utility.convertToBin(GlobalConstants.DEFAULT_INSTRUCTION, 0));
    Arrays.fill(opCode, GlobalConstants.DEFAULT_INSTRUCTION_OPCODE);
    Arrays.fill(sourceReg1, Isa.DEFAULT_REG_VALUE);
    Arrays.fill(sourceReg2, Isa.DEFAULT_REG_VALUE);
    Arrays.fill(destinationReg, Isa.DEFAULT_REG_VALUE);
    Arrays.fill(signedImmediate, Isa.DEFAULT_IMM_VALUE);
    Arrays.fill(instructionMnemonic, GlobalConstants.DEFAULT_INSTRUCTION_MNEMONIC);
    Arrays.fill(instructionType, GlobalConstants.DEFAULT_INSTRUCTION_TYPE);
    Arrays.fill(executionUnit, GlobalConstants.DEFAULT_EXECUTION_UNIT);
  }

  /**
   * Method to obtain the current instruction that has been decoded by the ID stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current decoded instruction 
   */
  public int[] getCurrentInstruction()
  {
    return instruction;
  }

  /**
   * Method to obtain the current instruction mnemonic that has been decoded by the ID stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current decoded instruction mnemonic 
   */
  public String[] getCurrentInstructionMnemonic()
  {
    return instructionMnemonic;
  }

  /**
   * Method to obtain the current instruction type that has been decoded by the ID stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current decoded instruction type
   */
  public String[] getCurrentInstructionType()
  {
    return instructionType;
  }

  /**
   * Method to obtain the current source register 1 location that has been decoded by the ID stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current decoded source register 1 location
   */
  public int[] getCurrentSourceReg1()
  {
    return sourceReg1;
  }

  /**
   * Method to obtain the current source register 2 location that has been decoded by the ID stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current decoded source register 2 location
   */
  public int[] getCurrentSourceReg2()
  {
    return sourceReg2;
  }


  /**
   * Method to obtain the current destination register location that has been decoded by the ID stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current decoded destination register location
   */
  public int[] getCurrentDestinationReg()
  {
    return destinationReg;
  }

  /**
   * Method to obtain the current signed immediate value that has been decoded by the ID stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current decoded signed immediate value
   */
  public int[] getCurrentSignedImmediate()
  {
    return signedImmediate;
  }
}