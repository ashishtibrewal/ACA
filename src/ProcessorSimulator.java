/**
 * @file ProcessorSimulator.java
 * @author Ashish Tibrewal
 * @date 12.10.2015
 * @details This file contains the ProcessorSimulator class that is used to control the flow of the simulation. It contains the main entry
 * point to the simulator. It expects to read in an assembly file that has been hand-written in correspondence to the custom-designed ISA.
 * The assembly file is passed to the Assembler class/unit to generate an executale binary file. This executalbe file in then used by the simulator
 * to simulate the program.
 */

// Import packages
// Custom packages
import pipeline.*;

// Pre-defined Java packages
import java.io.*;
import java.util.*;
import java.lang.*;

/**
 * This class controls the entire simulation of the processor.
 */
public class ProcessorSimulator
{
  // Class/Instance fields 
  public static final boolean debugMode;                    /** Boolean variable that is set to true for debugging purposes */
  public static final boolean verboseMode;                  /** Boolean variable that is set to true for debugging purposes that provides extra information */ 
  private Register cpuRegisters;
  private Memory cpuMemory;
  private Instruction currentInstruction;
  private final int totalGeneralPurposeRegisters = GlobalConstants.TOTAL_GP_REGISTERS;
  private final int memorySize = GlobalConstants.MEMORY_SIZE;
  private final int pipelineLength = GlobalConstants.PIPELINE_LENGTH;                     /** Processor pipeline length */ // TODO check for the best possible length in the design

  private IPipeline processorPipeline;                       /** Reference to the processor pipeline */
  private IStage instructionFetchStage;                      /** Reference to the Instruction Fetch Stage of the pipeline */
  private IStage instructionDecodeStage;                     /** Reference to the Instruction Decode Stage of the pipeline */
  private IStage instructionIssueStage;                      /** Reference to the Instruction Issue Stage of the pipeline */
  private IStage instructionExecuteStage;                    /** Reference to the Instruction Execute Stage of the pipeline */
  private IStage instructionWritebackStage;                  /** Reference to the Instruction Writeback Stage of the pipeline */
  private ProcessorPipelineStatus pipelineStatus;            /** Reference to the utility/debug stage in the pipleline/simulator */
  private static IPipelineContext pipelineContext;           /** Reference to the sequential pipeline context */
  private BranchPredictor branchPredictor;                   /** Reference to the processor's branch prediction unit */

  // Initialize static variables
  static
  {
    // Set to true when debugging
    debugMode = false;
    verboseMode = false;
  }

  // Class constructors 
  /**
   * Default constructor
   * @return No return value since this is a constructor
   */
  public ProcessorSimulator()
  {
      // Currently empty
  }

  // Class/Instance methods 
  /**
   * Method to set up the simulation (i.e. Create/Instantiate and initialize all the resources required for the simulation)
   */
  private void setup()
  {
    // TODO Add functionality to read the binary file and load it to the memory (Using a method to initialize the contents of memory in the Memory class).
    // TODO Add functionality to reset all variables/objects/components/units used in the simulator
    //cpuMemory = new Memory(memorySize);
    cpuMemory = new Memory();
    cpuRegisters = new Register(totalGeneralPurposeRegisters, cpuMemory);
    cpuMemory.setCpuRegistersReference(cpuRegisters);           // Set the reference of the cpuRegisters object in an instance field in the cpuMemory object

    processorPipeline = new SequentialProcessorPipeline();     // Instantiate the sequential processor pipeline object.

    instructionFetchStage = new InstructionFetchStage();          // Instantiate the Instruction Fetch (IF) stage object
    instructionDecodeStage = new InstructionDecodeStage();        // Instantiate the Instruction Decode (ID) stage object
    instructionIssueStage = new InstructionIssueStage();          // Instantiate the Instruction Issue (II) stage object        // TODO The instruction issue class needs to implement register re-naming and reservation station, etc.  
    instructionExecuteStage = new InstructionExecuteStage();      // Instantiate the Instruction Execute (IE) stage object      // TODO Note this object should contain one or more execution units (EUs)
    instructionWritebackStage = new InstructionWritebackStage();  // Instantiate the Instruction Writeback (WB) stage objects   // TODO Need to implement a re-order buffer
    pipelineStatus = new ProcessorPipelineStatus();               // Instantiate the pipelineStatus object. This class provides a function to print the current status of the pipeline.
    branchPredictor = new BranchPredictor();                      // Instantiate the processor's branch prediction (BP) unit
    
    processorPipeline.addStage(instructionFetchStage);       // Add the IF stage to the pipeline
    processorPipeline.addStage(instructionDecodeStage);      // Add the ID stage to the pipeline
    processorPipeline.addStage(instructionIssueStage);       // Add the II stage to the pipeline
    processorPipeline.addStage(instructionExecuteStage);     // Add the IE stage to the pipeline
    processorPipeline.addStage(instructionWritebackStage);   // Add the WB stage to the pipeline

    pipelineContext = new ProcessorPipelineContext(cpuRegisters,
                                                  cpuMemory,
                                                  instructionFetchStage,
                                                  instructionDecodeStage,
                                                  instructionIssueStage,
                                                  instructionExecuteStage,
                                                  instructionWritebackStage,
                                                  branchPredictor);     // Instantiate the sequential pipeline context object. This is used to store data and share references between all stages in the pipeline. It can be thought of as the control unit (CU) of the cpu since it control all the values that are being updated.

    //cpuMemory.initialize();   // Initialize contents of the main memory with the required instructions and data (Generated as an output from the Assembler)
    cpuMemory.testInitialize(); // Initializing contents of memory with hard-coded test instructions
  } 

  /**
   * Method that controls the flow of the simulation/processor execution
   */
  private void step()
  {
    Register.incrementClockCounter();               // Increment the clock counter on every cycle run
    processorPipeline.execute(pipelineContext);     // Execute/run the pipeline for the current cycle
    pipelineStatus.print(pipelineContext);          // Execute the utility stage to print the current status of the pipeline (Executing it separately/manually since it's not been added to the actual pipeline)
    if (!((ProcessorPipelineContext) pipelineContext).getCorrectBranchPrediction())  // If the branch predictor mispredicted
    {
      System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
      System.out.println("+ ----->>>> Flushing pipeline (Branch was mispredicted) <<<<----- +");
      System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
      ((SequentialProcessorPipeline) processorPipeline).flush(pipelineContext);     // Flush the pipeline (i.e. flush each stage in the pipeline)
      ((ProcessorPipelineContext) pipelineContext).setCorrectBranchPrediction(GlobalConstants.CORRECT_BRANCH_PREDICTION_RESULT);     // Revert the variable back to it's default value after the pipeline has been flushed
    }
    ((InstructionFetchStage) ((ProcessorPipelineContext) pipelineContext).getIF_Stage()).resetBranchPredictorResult();     // Reset the branch predictor result in the IF stage back to its default value at the end of every cycle
    // this.dumpState();                             // Dump the state of the cpu every cycle
  }

  /**
   * Method to dump the current state of the CPU. Useful for debugging purposes
   */
  private void dumpState()
  {
    cpuMemory.dumpContents();         // Dump contents of memory to the standard output
    cpuRegisters.dumpContents();      // Dump contents of processor registers to the standard output
  }

  /**
   * Method that controls the Assembler and generates the CPU simulator executable binary file
   * @param assemblyFile Assembly file to be used by the Assembler to generate the executable binary
   */
  private void generateBinary(String assemblyFile)
  {
    Assembler assembler;                      /** Reference to the assembler object */

    assembler = new Assembler(assemblyFile);  // Instantiate the assembler object
    assembler.run();                          // Run the assembler
  }

  private void printResults()
  {
    //TODO Add other results
    System.out.println("###########################################################");
    System.out.println("###           SIMULATOR PERFORMANCE RESULTS             ###");
    System.out.println("###########################################################");
    System.out.println();
    System.out.println("--------------------------------------");
    System.out.println(">>>  Overall performance resutls   <<<");
    System.out.println("--------------------------------------");
    System.out.println("Total instructions executed: " + cpuRegisters.getInstructionCounter());
    System.out.println("Total NOP instructions executed: " + cpuRegisters.getInstructionCounterNOP());
    System.out.println("Total valid instructions executed: " + (cpuRegisters.getInstructionCounter() - cpuRegisters.getInstructionCounterNOP()));
    System.out.println("Total clock cycles simulated: " + cpuRegisters.readClockCounter());
    System.out.println("Instructions per cycle (IPC): " + ((double) (cpuRegisters.getInstructionCounter() - cpuRegisters.getInstructionCounterNOP()) / (double) (cpuRegisters.readClockCounter())));
    System.out.println();
    System.out.println("--------------------------------------");
    System.out.println(">>> BU & Branch prediction results <<<");
    System.out.println("--------------------------------------");
    System.out.println("Total branch instructions executed: " + cpuRegisters.getInstructionBranchCounter());
    System.out.println("Branches predicted correct: " + cpuRegisters.getBranchPredictionsCorrect());
    System.out.println("Branches predicted incorrect: " + cpuRegisters.getBranchPredictionsIncorrect());
    System.out.println();
    System.out.println("--------------------------------------");
    System.out.println(">>>          ALU results           <<<");
    System.out.println("--------------------------------------");
    System.out.println("Total ALU instructions executed: " + cpuRegisters.getInstructionAluCounter());
    System.out.println();
    System.out.println("--------------------------------------");
    System.out.println(">>>          LSU results           <<<");
    System.out.println("--------------------------------------");
    System.out.println("Total LSU instructions executed: " + (cpuRegisters.getInstructionLoadCounter() + cpuRegisters.getInstructionStoreCounter() + cpuRegisters.getInstructionEncodedLoadCounter()));
    System.out.println("Total loads executed: " + cpuRegisters.getInstructionLoadCounter());
    System.out.println("Total stores executed: " + cpuRegisters.getInstructionStoreCounter());
    System.out.println("Total instruction encoded loads (i.e. MOVIs): " + cpuRegisters.getInstructionEncodedLoadCounter());
    System.out.println();
  }

  /**
   * Main entry point to the simulator/program
   * @param args Command-line arguments passed to the program. It should include the name of the assembly file that needs to be read and executed by the simulator.
   */
  public static void main(String[] args)
  {
    // Local variable declarations/definitions
    ProcessorSimulator cpu;             /** Reference to the primary cpu object that is being simulated */

    // Local variable initializations
    cpu = new ProcessorSimulator();     // Instantiate the cpu object

    // Method functionality
    // Parse command line arguments that have been passed to the simulator/program
    if (args.length != 1)
    {
      System.err.println("Incorrect usage: Either no or more than one argument(s) have been passed to the program.");
      System.err.println("Correct Usage: java ProcessorSimulator <input assembly file>");
      System.exit(1);         // Exit/Terminate the program/Java runtime with error code 1
    }

    // Setup and run the assembler
    try
    {
      System.out.println();
      System.out.println("###########################################################");
      System.out.println("###               RUNNING THE ASSEMBLER                 ###");
      System.out.println("###########################################################");
      System.out.println("Running the Assembler to generate the executable binary file. \n");
      System.out.println("Input file: " + args[0]);
      cpu.generateBinary(args[0]);      // Call the generateBinary method that controls the Assembler
      System.out.println("Output executable: " + Assembler.outputFile + " \n");
      System.out.println("Assembler completed generating the executable binary file.");
    }
    catch (Exception ex)
    {
      System.err.println("An exception occurred while running the Assembler. The exception is as follows: " + ex.getMessage());
      System.err.println("\nPrinting stack trace ... \n");
      ex.printStackTrace();
      System.out.println();
      System.exit(1);               // Exit/Terminate the program/Java runtime with error code 1
    }

    // Setup the CPU simulator
    try
    {
      // Add functionality to read the binary file and load it to the memory (Using a method to initialize the contents of memory in the Memory class).
      // This functionality should be added to the setup() method.
      System.out.println();
      System.out.println("###########################################################");
      System.out.println("###            SETTING UP THE CPU SIMULATOR             ###");
      System.out.println("###########################################################");
      System.out.println("Setting up the CPU simulator. \n");
      // this.setup();                 // Fails due to the following error: non-static variable this cannot be referenced from a static context
      cpu.setup();                     // Setup cpu for simulation (i.e. the simulation environment)
      System.out.println("CPU simulator set up complete.");
    }
    catch (Exception ex)
    {
      System.err.println("An exception occurred while setting up the simulator. The exception is as follows: " + ex.getMessage());
      System.err.println("\nPrinting stack trace ... \n");
      ex.printStackTrace();
      System.out.println();
      System.exit(1);               // Exit/Terminate the program/Java runtime with error code 1
    }

    // Run the CPU simulator
    try
    {
      System.out.println();
      System.out.println("###########################################################");
      System.out.println("###              RUNNING THE CPU SIMULATOR              ###");
      System.out.println("###########################################################");
      System.out.println("CPU simulator starting program execution. \n");
      System.err.println(">>>>>>>>>>>> Dumping initial state of CPU <<<<<<<<<<<< ");
      cpu.dumpState();
      System.out.println();
      //for(int numCycles = 0; numCycles < GlobalConstants.NUM_ITERATIONS; numCycles++)
      while (((ProcessorPipelineContext) pipelineContext).getCpuRegisters().readGP(GlobalConstants.SVC_REGISTER) != GlobalConstants.SVC_SUSPEND)
      {  
        cpu.step();           // Step/Run the cpu simulator by one clock cycle
        // TODO Add code to print state of every stage for current cycle
        //cpu.dumpState();    // Dump the state of the cpu every cycle
      }
      cpu.dumpState();      // Print end state after having finished running the simulator
      System.out.println("CPU simulator finished executing the program. \n");
      cpu.printResults();     // Print the results of the simulation/simulator
    }
    catch (Exception ex)
    {
      System.err.println("\nThe simulator encounterd a problem. Refer to details given below ... \n");
      System.err.println(">>>>>>>>>>>> Dumping state of CPU <<<<<<<<<<<< ");
      cpu.dumpState();                  // Dump the state of the CPU to the standard output
      System.err.println("Warning: An exception occurred during program execution. The exception is as follows: " + ex.getMessage() +
                         " Check the CPU dump data shown above to locate possible issues/problems.");
      System.err.println("\nPrinting stack trace ... \n");
      ex.printStackTrace();
      System.out.println();
      //System.err.println(">>>>>>>>>>>> End of simulation <<<<<<<<<<<< \n");
    }
    finally
    {
      System.err.println(">>>>>>>>>>>> End of simulation <<<<<<<<<<<< \n");
    }
  }
}