/**
 * @file GlobalConstants.java
 * @author Ashish Tibrewal
 * @date 12.10.2015
 * @details This file contains the GlobalConstants class that stores all the constant values used by the simulator
 */

public final class GlobalConstants
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

  public static final int ISA_TOTAL_INSTRUCTIONS = 25;

}