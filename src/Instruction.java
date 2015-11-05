/**
 * @file Instruction.java
 * @author Ashish Tibrewal
 * @date 17.10.2015
 * @details This file contains the declaration/definition for the Instruction enumeration type. The Instruction enum type contains all the possible
 * types of instructions specified in the ISA.
 */

// Import packages
import java.io.*;
import java.util.*;
import java.lang.*;

/**
 * Instruction enum containing all the possible instructions defined in the ISA that can be executed by the simulator.
 */
public enum Instruction
{
  ADD,
  SUB,
  MULT,
  DIV,
  ADDI,
  SUBI,

  AND,
  OR,
  XOR,
  NOR,
  ANDI,
  ORI,
  SLT,
  SLTI,
  SLL,
  SLR,
  SLLV,
  SRA,

  LW,
  SW,

  BR,
  BEQ,
  BNE,

  PUSH,
  POP,

  NOP,
}