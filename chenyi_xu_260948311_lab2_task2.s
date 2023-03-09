.global _start
.equ SW_MEMORY, 0xFF200040
.equ LED_MEMORY, 0xFF200000

.equ ADDR_7SEG1, 0xFF200020
.equ ADDR_7SEG2, 0xFF200030

.equ PB0, 0x00000001
.equ PB1, 0x00000002 
.equ PB2, 0x00000004
.equ PB3, 0x00000008 

.equ    PB_DATA, 0xFF200050       
.equ    PB_MASK, 0xFF200058        
.equ    PB_EDGE, 0xFF20005C

.equ    Load, 0xFFFEC600       
.equ    Counter, 0xFFFEC604       
.equ    Control, 0xFFFEC608      //IAE bit 
.equ    Interrupt_Status, 0xFFFEC60C //F bit  

TLoad: .word 20000000

_start:
initialization:
//counters for the 6 hex display
  MOV R4, #0         //has to be less than 10
  MOV R5, #0         //has to be less than 10
  MOV R6, #0         //has to be less than 6
  MOV R7, #0         //has to be less than 10
  MOV R8, #0         //has to be less than 6
  MOV R9, #0         //has to be less than 10
  
  //call hex write to intialize all display to 0
  MOV R0, #0          //<-integer input for hex display
  MOV R1, #0xFFFFFFFF //<- hex display, turn everything on
  BL HEX_write_ASM 

polling:
  //pushbuttons
  BL read_PB_edgecp_ASM  //<- output R0
  CMP R0, #0
  BLNE PB_clear_edgecp_ASM
  MOV R1, #0x00000001
  TST R0, R1
  BNE pb0_start
  MOV R1, #0x00000002 
  TST R0, R1
  BNE pb1_stop
  MOV R1, #0x00000004
  TST R0, R1
  BNE pb2_reset
  BL ARM_TIM_read_INT_ASM
  TST R0, #1
  BEQ polling 
  BL ARM_TIM_clear_INT_ASM  //F bit is 1 --> increase counter 
  
  ADD R4, R4, #1
  CMP R4, #10
  MOVGE R4, #0
  
  ADDEQ R5, R5, #1
  CMP R5, #10
  MOVGE R5, #0
  
  ADDEQ R6, R6, #1
  CMP R6, #6
  MOVGE R6, #0
  
  ADDEQ R7, R7, #1
  CMP R7, #10
  MOVGE R7, #0
  
  ADDEQ R8, R8, #1
  CMP R8, #6
  MOVGE R8, #0
  
  ADDEQ R9, R9, #1
  CMP R9, #10
  MOVGE R9, #0
  
timer_display:
  MOV R0, R4
  MOV R1, #0x00000001
  BL HEX_write_ASM      //R0-integer input, R1 hex input
  
  MOV R0, R5
  MOV R1, #0x00000002
  BL HEX_write_ASM      //R0-integer input, R1 hex input
  
  MOV R0, R6
  MOV R1, #0x00000004
  BL HEX_write_ASM      //R0-integer input, R1 hex input
  
  MOV R0, R7
  MOV R1, #0x00000008
  BL HEX_write_ASM      //R0-integer input, R1 hex input
  
  MOV R0, R8
  MOV R1, #0x00000010
  BL HEX_write_ASM      //R0-integer input, R1 hex input
  
  MOV R0, R9
  MOV R1, #0x00000020
  BL HEX_write_ASM      //R0-integer input, R1 hex input
  
  B polling
    

pb0_start:
  LDR R0, TLoad
  MOV R1, #0b011
  BL ARM_TIM_config_ASM 
  B polling 
pb1_stop:
  LDR R0, TLoad 
  MOV R1, #0b000
  BL ARM_TIM_config_ASM 
  B polling
pb2_reset: 
  LDR R0, TLoad
  MOV R1, #0b011
  BL ARM_TIM_config_ASM 
  B initialization
//drivers needed: pushbuttons, hex_write
//drivers for hex display
HEX_write_ASM:
//R0-integer input
//R1 hex input
  PUSH {R3-R5}
display_int:
//0
  MOV R2, #0x0
  CMP R2, R0
  MOVEQ R2, #0b00111111
  BEQ hex_display
//1
  MOV R2, #0x1
  CMP R2, R0
  MOVEQ R2, #0b00000110
  BEQ hex_display
 //2
  MOV R2, #0x2
  CMP R2, R0
  MOVEQ R2, #0b01011011
  BEQ hex_display
//int_3:
  MOV R2, #0x3
  CMP R2, R0
  MOVEQ R2, #0b01001111
  BEQ hex_display
//int_4:
  MOV R2, #0x4
  CMP R2, R0
  MOVEQ R2, #0b01100110
  BEQ hex_display
//int_5:
  MOV R2, #0x5
  CMP R2, R0
  MOVEQ R2, #0b01101101
  BEQ hex_display
//int_6:
  MOV R2, #0x6
  CMP R2, R0
  MOVEQ R2, #0b01111101
  BEQ hex_display
//int_7:
  MOV R2, #0x7
  CMP R2, R0
  MOVEQ R2, #0b00000111
  BEQ hex_display
//int_8:
  MOV R2, #0x8
  CMP R2, R0
  MOVEQ R2, #0b01111111
  BEQ hex_display
//int_9:
  MOV R2, #0x9
  CMP R2, R0
  MOVEQ R2, #0b01101111
  BEQ hex_display
//int_A:
  MOV R2, #0xA
  CMP R2, R0
  MOVEQ R2, #0b01110111
  BEQ hex_display
//int_B:
  MOV R2, #0xB
  CMP R2, R0
  MOVEQ R3, #0b01111100
  BEQ hex_display
//int_C:
  MOV R2, #0xC
  CMP R2, R0
  MOVEQ R2, #0b00111001
  BEQ hex_display
//int_D:
  MOV R2, #0xD
  CMP R2, R0
  MOVEQ R2, #0b01011110
  BEQ hex_display
//int_E:
  MOV R2, #0xE
  CMP R2, R0
  MOVEQ R2, #0b1111001
  BEQ hex_display
//int_F:
  MOV R2, #0xF
  CMP R2, R0
  MOVEQ R2, #0b01110001
  BEQ hex_display
  
hex_display:
  //R2 integ input (from display_int), remember to shift
  //R1 hex display (input)
hex_0_w:
  MOV R3, #0x00000001
  TST R1, R3
  BEQ hex_1_w
  
  LDR R3, =ADDR_7SEG1
  LDR R4, [R3]
  MOV R5, #0xFFFFFF00
  AND R4, R4, R5
  ORR R4, R4, R2
  STR R4, [R3]
  

hex_1_w:
  MOV R3, #0x00000002
  TST R1, R3
  LSL R2, R2, #8
  BEQ hex_2_w
  
  LDR R3, =ADDR_7SEG1
  LDR R4, [R3]
  MOV R5, #0xFFFF00FF
  AND R4, R4, R5
  ORR R4, R4, R2
  STR R4, [R3]
  

hex_2_w:
  MOV R3, #0x00000004
  TST R1, R3
  LSL R2, R2, #8
  BEQ hex_3_w
  
  LDR R3, =ADDR_7SEG1
  LDR R4, [R3]
  MOV R5, #0xFF00FFFF
  AND R4, R4, R5
  ORR R4, R4, R2
  STR R4, [R3]
  
 
hex_3_w:
  MOV R3, #0x00000008
  TST R1, R3
  LSL R2, R2, #8
  BEQ hex_4_w
  
  LDR R3, =ADDR_7SEG1
  LDR R4, [R3]
  MOV R5, #0x00FFFFFF
  AND R4, R4, R5
  ORR R4, R4, R2
  STR R4, [R3]
  
hex_4_w:
  MOV R3, #0x00000010
  TST R1, R3
  LSR R2, R2, #24
  BEQ hex_5_w
  
  LDR R3, =ADDR_7SEG2
  LDR R4, [R3]
  MOV R5, #0xFF00
  AND R4, R4, R5
  ORR R4, R4, R2
  STR R4, [R3]
  
 hex_5_w:
  MOV R3, #0x00000020
  TST R1, R3
  LSL R2, R2, #8
  BEQ hex_write_end
 
  LDR R3, =ADDR_7SEG2
  LDR R4, [R3]
  MOV R5, #0x00FF
  AND R4, R4, R5
  ORR R4, R4, R2
  STR R4, [R3]

hex_write_end:
  POP {R3-R5}
  BX LR
  

//pushbutton drivers
read_PB_data_ASM:
//Data stored in R0 <--output
    LDR R1, =PB_DATA
	LDR R0, [R1]
	BX LR
	
PB_data_is_pressed_ASM:
// RO input argument
    LDR R1, =PB_DATA
	LDR R0, [R1]
	LDR R3, =PB0
	TST R0, R3
	BEQ pb1
	MOV R2, #0x00000001

pb1:
	LDR R3, =PB1
	TST R0, R3
	BEQ pb2
	MOV R2, #0x00000001

pb2:
	LDR R3, =PB2
	TST R0, R3
	BEQ pb3
    MOV R2, #0x00000001

pb3:
	LDR R3, =PB3
	TST R0, R3
    MOVEQ R2, #0x00000001
	BX LR

read_PB_edgecp_ASM:
//Data stored in R0 <-- output
	LDR R1, =PB_EDGE
	LDR R0, [R1]
	BX  LR
PB_edgecp_is_pressed_ASM:
	// RO input argument
	LDR R1, =PB_EDGE
	LDR R0, [R1]
	
	LDR R3, =PB0
	TST R0, R3
	BEQ pb1_E
	MOV R2, #0x00000001

pb1_E:
	LDR R3, =PB1
	TST R0, R3
	BEQ pb2_E
	MOV R2, #0x00000001

pb2_E:
	LDR R3, =PB2
	TST R0, R3
	BEQ pb3_E
    MOV R2, #0x00000001

pb3_E:
	LDR R3, =PB3
	TST R0, R3
    MOVEQ R2, #0x00000001
	BX LR
	
PB_clear_edgecp_ASM:
	LDR R1, =PB_EDGE
	LDR R2, [R1]
	STR R2, [R1]
	BX LR
//Ro input indices 
enable_PB_INT_ASM:
	LDR R1, =PB_MASK
	STR R0, [R1]
	BX LR
	
disable_PB_INT_ASM: 
    LDR R1, =PB_MASK
	LDR R2, [R1]
	BIC R2, R2, R0
	STR R2, [R1]
	BX LR
	
ARM_TIM_config_ASM:
//load value R0, store to load
//configuration bits R1, store to control 
	LDR R2, =Load
	STR R0, [R2]
	LDR R2, =Control
	STR R1, [R2] 
	BX LR

ARM_TIM_read_INT_ASM:

	LDR R1, =Interrupt_Status
	LDR R2, [R1]
	MOV R0, R2
    BX LR
	
ARM_TIM_clear_INT_ASM: 
// write a 1 into the isr
	LDR R1, =Interrupt_Status
	MOV R2, #1
	STR R2, [R1]
	BX LR
	