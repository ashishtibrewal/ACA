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
import java.io.*;
import java.util.*;
import java.lang.*;

/**
 * This class controls the entire simulation of the processor.
 */
public class ProcessorSimulator
{
  // Class/Instance fields 
  public static final boolean debugMode;              /* Boolean variable that is set to true for debugging purposes */
  public static final boolean verboseMode;            /* Boolean variable that is set to true for debugging purposes that provides extra information */ 
  private Register cpuRegisters;
  private Memory cpuMemory;
  private Instruction currentInstruction;
  private final int totalRegisters = 16;
  private final int memorySize = 256;

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
    // Add functionality to read the binary file and load it to the memory (Using a method to initialize the contents of memory in the Memory class).
    // Add functionality to reset all variables/objects/components/units used in the simulator
    cpuMemory = new Memory(memorySize);
    cpuRegisters = new Register(totalRegisters, cpuMemory);

    //cpuMemory.initialize();   // Initialize contents of the main memory with the required instructions and data (Generated as an output from the Assembler)
    cpuMemory.testInitialize(); // Initializing contents of memory with hard-coded test instructions
  } 

  /**
   * Method that controls the flow of the simulation/processor execution
   */
  private void run()
  {
    // cpuRegisters.setPC(1025);      // Fails with an exception because this location doesn't physically exist in memory and the PC can never have it's value set to this
    //cpuRegisters.writePC(1000);
    //cpuRegisters.incrementPC();
    cpuMemory.dumpContents();
    //cpuRegisters.dumpContents();
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
      System.out.println("Running the Assembler to generate the executable binary file.");
      System.out.println("Input file: " + args[0]);
      cpu.generateBinary(args[0]);      // Call the generateBinary method that controls the Assembler
      System.out.println("Output executable: " + Assembler.outputFile);
      System.out.println("Assembler completed generating the executable binary file.");
    }
    catch (Exception ex)
    {
      System.err.println("An exception occurred while running the Assembler. The exception is as follows: " + ex.getMessage());
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
      System.out.println("Setting up the CPU simulator.");
      // this.setup();                  // Fails due to the following error: non-static variable this cannot be referenced from a static context
      cpu.setup();                     // Setup cpu for simulation (i.e. the simulation environment)
      System.out.println("CPU simulator set up complete.");
    }
    catch (Exception ex)
    {
      System.err.println("An exception occurred while setting up the simulator. The exception is as follows: " + ex.getMessage());
    }

    // Run the CPU simulator
    try
    {
      System.out.println();
      System.out.println("###########################################################");
      System.out.println("###              RUNNING THE CPU SIMULATOR              ###");
      System.out.println("###########################################################");
      System.out.println("CPU simulator starting program execution.");
      cpu.run();                        // Run the cpu simulator
      System.out.println("CPU simulator finished executing the program. \n");
    }
    catch (Exception ex)
    {
      System.err.println(" >>>>>>>>>>>> Dumping state of CPU <<<<<<<<<<<< ");
      cpu.dumpState();                  // Dump the state of the CPU to the standard output
      System.err.println("Warning: An exception occurred during program execution. The exception is as follows: " + ex.getMessage() +
                         " Check the CPU dump data shown above to locate possible issues/problems. \n");
      System.err.println(" >>>>>>>>>>>> End of simulation <<<<<<<<<<<< \n");
    }
  }
}