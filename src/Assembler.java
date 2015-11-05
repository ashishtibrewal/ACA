/**
 * @file Assembler.java
 * @author Ashish Tibrewal
 * @date 29.10.2015
 * @details This file contains the Assembler. It expects to read in an assembly file that has been hand-written in correspondence to the custom-designed ISA.
 * The assembly file is passed to the Parser class to generate an executale binary file that can be used by the simulator.
 */

// Import packages
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * Assembler class that generates executable binary code file from an assembly file
 */
public class Assembler
{
  // Class/Instance fields
  public static String outputFile = "output.sim";     /* Name of the generated executable file */
  private String inputFile;

  // Class constructors 
  /**
   * Default constructor
   * @return No return value since this is a constructor
   */
  public Assembler()
  {
      this("No input file");  // Call the parameterized constructor
  }

  /**
   * Parameterized constructor that takes the assembly file as as parameter
   * @param  assemblyFile Input assembly file that needs to be converted to an executable binary file
   * @return              No return value since this is a constructor
   */
  public Assembler(String assemblyFile)
  {
    inputFile = assemblyFile;
  }

  // Class/Instance methods
  /**
   * Method to run the assembler and generate the executable file to be used by the simulator
   */
  public void run()
  {
    ArrayList<Integer> program;
    Scanner input, fileScanner;
    ObjectOutputStream output;
    Path source;

    if (inputFile.equals("No input file"))
    {
      System.err.println("The assembler object was instantiated without an input file. Fatal error. Exiting program ... \n");
      System.exit(1);               // Exit/Terminate the program/Java runtime with error code 1
    }
    else
    {
      program = new ArrayList<Integer>();
      input = new Scanner(System.in);
    
    // get path of source code file; load file; parse instructions into integer format; load
    // each into array; serialize array object into "binary" file.
      source = Paths.get(inputFile);
      fileScanner = null;
      try
      {
        fileScanner = new Scanner(source);
        while (fileScanner.hasNextLine()) 
        {
          String line = new String();
          line = fileScanner.nextLine();
          if (!program.add(this.parse(line)))
          { 
            System.err.println("Failed to add the parsed item to the complete list of program items. Note that this can cause unexpected results from the simulator to be generated");
          }
        } 
      }
      catch (Exception ex)
      {
        if (ex instanceof IOException)
        {
          System.err.println("Failed to open the assembly source file. Fatal error. Exiting program ... \n");
          // System.err.println("Failed to open the assembly source file. Please type \"quit\" and press the return key to stop the simulator");
          // while (!input.hasNext("quit"))
          // {
          //   input.next();
          // }
        }
        else if (ex instanceof ParsingException)
        {
          System.err.println("Parsing Error: " + ex.getMessage());
          //ex.printStackTrace();          
        }
        System.exit(1);         // Exit/Terminate the program/Java runtime with error code 1  
      }
      finally
      {
        if (fileScanner != null)
        {
          fileScanner.close();
        }
      }

      // take list of instructions and build serialized object file
      try
      {  
        output = new ObjectOutputStream(new FileOutputStream(outputFile));
        output.writeObject(program);
        if (output != null)
        {
            output.close();
        }
      } 
      catch (Exception ex)
      { 
        System.err.println("A problem occurred when saving the executable binary file due to the following exception: " + ex.getMessage());
        System.exit(1);             // Exit/Terminate the program/Java runtime with error code 1
      } 
    }
  }

  /**
   * Mehtod that parses the current line read from the assembly file to generate it's integer/machine code representation
   * @param  line             Current line read from the assembly file
   * @return                  Interer value/representation of the current assembly instruction
   * @throws ParsingException Exception for unknown/unrecognized instructions/characters
   */
  private int parse(String line) throws ParsingException
  {
    String[] stringTokens;
    int stringTokensLength;
    int[] intTokens;

    if (!line.startsWith("#") && !line.isEmpty())        // Check if the current line is a comment or a blank/empty line, only continue parsing it if it's not a comment or a blank/empty line
    {
      if(!line.contains("#"))                            // Check if inline comments are present in the file
      {
        stringTokens = line.split(" ");
        stringTokensLength = stringTokens.length;
        intTokens = new int[stringTokensLength];

        for (int i = 0; i < stringTokensLength; i++)
        {
          intTokens[i] = this.convert(stringTokens[i]);
        }
      }
      else
      {
        int startOfComments = line.indexOf("#");
        String lineWithoutComments = line.substring(0, startOfComments);    // Discard in-line comments
        stringTokens = lineWithoutComments.split(" ");
        stringTokensLength = stringTokens.length;
        intTokens = new int[stringTokensLength];

        for (int i = 0; i < stringTokensLength; i++)
        {
          intTokens[i] = this.convert(stringTokens[i]);
        }
      }
    }
    return 0;
  }

  /**
   * Method used to convert tokens extracted from a line in the assembly file
   * @param  stringToken      Token that needs to be analysed and converted
   * @return                  Converted value of a token
   * @throws ParsingException Exception for unknown/unrecognized instructions/characters
   */
  private int convert(String stringToken) throws ParsingException
  {
    if (ProcessorSimulator.debugMode == true)
    {
      if (ProcessorSimulator.verboseMode == true)
      {
        System.out.print("Current token: " + stringToken + ", ");
      }
      else
      {
        System.out.println("Current token: " + stringToken); 
      }
    }
    if (stringToken.startsWith("R"))
    {     
      int registerNumber = Integer.parseInt(stringToken.substring(1));
      if (ProcessorSimulator.debugMode == true && ProcessorSimulator.verboseMode == true)
      {
        System.out.println("Register token: R" + registerNumber);
      }
      return registerNumber;
    }
    else if (stringToken.startsWith("0x"))
    { 
      int immediateValue = Integer.parseInt(stringToken.substring(2));
      if (ProcessorSimulator.debugMode == true && ProcessorSimulator.verboseMode == true)
      {
        System.out.println("Immediate token: 0x" + immediateValue);
      }
      return immediateValue;
    }
    else if (stringToken.startsWith("label"))
    {
      // Enter code to handle labels
      // if (ProcessorSimulator.debugMode == true && ProcessorSimulator.verboseMode == true)
      // {
      //   System.out.println("Label token: " + labelValue);
      // }
      return -1;
    }
    /* // Dont need to include these conditions here since these are taken care of by the parse method. Only valid tokens are passed to this method.
     else if (stringToken.isEmpty())            // Checking if any unwanted spaces are present in the file. If yes, discard this toke and return -1
    {
      return -1;
    }
    else if (stringToken.startsWith("#"))      // Checking for in-line comments. If yes, discard these and return -1
    {
      return -1;
    }
    */
    else
    {    
      if (ProcessorSimulator.debugMode == true && ProcessorSimulator.verboseMode == true)
      {
        System.out.println("Instruction token: " + stringToken);
      }
      switch (stringToken)
      {
        case "add"    : return 1;
        case "sub"    : return 2;
        case "mul"    : return 3;
        case "div"    : return 4;
        case "and"    : return 5;
        case "or"     : return 6;
        case "xor"    : return 7;
        case "shl"    : return 8;
        case "slr"    : return 9;
        case "sar"    : return 10;
        case "ceq"    : return 11;
        case "cgr"    : return 12;
        case "clt"    : return 13;
        case "addc"   : return 17;
        case "subc"   : return 18;
        case "mulc"   : return 19;
        case "divc"   : return 20;
        case "andc"   : return 21;
        case "orc"    : return 22;
        case "xorc"   : return 23;
        case "shlc"   : return 24;
        case "slrc"   : return 25;
        case "sarc"   : return 26;
        case "ceqc"   : return 27;
        case "cgrc"   : return 28;
        case "cltc"   : return 29;
        case "ld"     : return 16;
        case "st"     : return 31;
        case "bez"    : return 32;
        case "bnz"    : return 33;
        case "eop"    : return 48;
        case "in"     : return 49;
        case "out"    : return 50;

        default: throw new ParsingException("Encountered an unknown instruction or an unexpected character while parsing the assembly file. Fatal error. Exiting program ... \n");
      }
    }    
  }
}