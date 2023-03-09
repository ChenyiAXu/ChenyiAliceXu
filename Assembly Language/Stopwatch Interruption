.section .vectors, "ax"
B _start
B SERVICE_UND       // undefined instruction vector
B SERVICE_SVC       // software interrupt vector
B SERVICE_ABT_INST  // aborted prefetch vector
B SERVICE_ABT_DATA  // aborted data vector
.word 0 // unused vector
B SERVICE_IRQ       // IRQ interrupt vector
B SERVICE_FIQ       // FIQ interrupt vector

.text

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
PB_int_flag: .word 0x0
tim_int_flag: .word 0x0
.global _start

_start:
    /* Set up stack pointers for IRQ and SVC processor modes */
    MOV    R1, #0b11010010      // interrupts masked, MODE = IRQ
    MSR    CPSR_c, R1           // change to IRQ mode
    LDR    SP, =0xFFFFFFFF - 3  // set IRQ stack to A9 onchip memory
    /* Change to SVC (supervisor) mode with interrupts disabled */
    MOV    R1, #0b11010011      // interrupts masked, MODE = SVC
    MSR    CPSR, R1             // change to supervisor mode
    LDR    SP, =0x3FFFFFFF - 3  // set SVC stack to top of DDR3 memory
    BL     CONFIG_GIC           // configure the ARM GIC
    // To DO: write to the pushbutton KEY interrupt mask register
    // Or, you can call enable_PB_INT_ASM subroutine from previous task
    // to enable interrupt for ARM A9 private timer, use ARM_TIM_config_ASM subroutine 
	
	MOV     R0, #0b1111
	BL      enable_PB_INT_ASM
	//R0 load value, R1, configuration bits
	LDR     R0, TLoad
	MOV     R1, #0b110
	BL      ARM_TIM_config_ASM
	
    LDR    R0, =0xFF200050      // pushbutton KEY base address
    MOV    R1, #0xF             // set interrupt mask bits
    STR    R1, [R0, #0x8]       // interrupt mask register (base + 8)
    //enable IRQ interrupts in the processor
    MOV    R0, #0b01010011      // IRQ unmasked, MODE = SVC
    MSR    CPSR_c, R0
IDLE:
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

interrupt_loop: 
  LDR R0, =PB_int_flag
  LDR R2, [R0]
  MOV R1, #0
  STR R1, [R0]
  
  //test each push buttons
  MOV R1, #0x00000001
  TST R2, R1
  BNE pb0_start
  MOV R1, #0x00000002 
  TST R2, R1
  BNE pb1_stop
  MOV R1, #0x00000004
  TST R2, R1
  BNE pb2_reset
  
  LDR R0, =tim_int_flag 
  LDR R2, [R0]
  TST R2, #1
  BEQ interrupt_loop
  
  MOV R1, #0
  STR R1, [R0]
  
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
  
  B interrupt_loop // This is where you write your objective task
pb0_start:
  LDR R0, TLoad
  MOV R1, #0b111
  BL ARM_TIM_config_ASM 
  B interrupt_loop
pb1_stop:
  LDR R0, TLoad 
  MOV R1, #0b100
  BL ARM_TIM_config_ASM 
  B interrupt_loop
pb2_reset: 
  LDR R0, TLoad
  MOV R1, #0b110
  BL ARM_TIM_config_ASM 
  B initialization 
	
/*--- Undefined instructions ---------------------------------------- */
SERVICE_UND:
    B SERVICE_UND
/*--- Software interrupts ------------------------------------------- */
SERVICE_SVC:
    B SERVICE_SVC
/*--- Aborted data reads -------------------------------------------- */
SERVICE_ABT_DATA:
    B SERVICE_ABT_DATA
/*--- Aborted instruction fetch ------------------------------------- */
SERVICE_ABT_INST:
    B SERVICE_ABT_INST
/*--- IRQ ----------------------------------------------------------- */
SERVICE_IRQ:
    PUSH {R0-R7, LR}
/* Read the ICCIAR from the CPU Interface */
    LDR R4, =0xFFFEC100
    LDR R5, [R4, #0x0C] // read from ICCIAR
	
	/* To Do: Check which interrupt has occurred (check interrupt IDs)
   Then call the corresponding ISR
   If the ID is not recognized, branch to UNEXPECTED 
   See the assembly example provided in the De1-SoC Computer_Manual on page 46 */   
Timer_check:
	CMP R5, #29
	BNE Pushbutton_check
	BL ARM_TIM_ISR
	B  EXIT_IRQ
	
Pushbutton_check:
    CMP R5, #73
UNEXPECTED:
    BNE UNEXPECTED      // if not recognized, stop here
    BL KEY_ISR
EXIT_IRQ:
/* Write to the End of Interrupt Register (ICCEOIR) */
    STR R5, [R4, #0x10] // write to ICCEOIR
    POP {R0-R7, LR}
SUBS PC, LR, #4
/*--- FIQ ----------------------------------------------------------- */
SERVICE_FIQ:
    B SERVICE_FIQ

CONFIG_GIC:
    PUSH {LR}
/* To configure the FPGA KEYS interrupt (ID 73):
* 1. set the target to cpu0 in the ICDIPTRn register
* 2. enable the interrupt in the ICDISERn register */
/* CONFIG_INTERRUPT (int_ID (R0), CPU_target (R1)); */
/* To Do: you can configure different interrupts
   by passing their IDs to R0 and repeating the next 3 lines */  
    MOV R0, #73            // KEY port (Interrupt ID = 73)
    MOV R1, #1             // this field is a bit-mask; bit 0 targets cpu0
    BL CONFIG_INTERRUPT 

	MOV R0, #29
	MOV R1, #1
	BL CONFIG_INTERRUPT 
	
/* configure the GIC CPU Interface */
    LDR R0, =0xFFFEC100    // base address of CPU Interface
/* Set Interrupt Priority Mask Register (ICCPMR) */
    LDR R1, =0xFFFF        // enable interrupts of all priorities levels
    STR R1, [R0, #0x04]
/* Set the enable bit in the CPU Interface Control Register (ICCICR).
* This allows interrupts to be forwarded to the CPU(s) */
    MOV R1, #1
    STR R1, [R0]
/* Set the enable bit in the Distributor Control Register (ICDDCR).
* This enables forwarding of interrupts to the CPU Interface(s) */
    LDR R0, =0xFFFED000
    STR R1, [R0]
    POP {PC}

/*
* Configure registers in the GIC for an individual Interrupt ID
* We configure only the Interrupt Set Enable Registers (ICDISERn) and
* Interrupt Processor Target Registers (ICDIPTRn). The default (reset)
* values are used for other registers in the GIC
* Arguments: R0 = Interrupt ID, N
* R1 = CPU target
*/
CONFIG_INTERRUPT:
    PUSH {R4-R5, LR}
/* Configure Interrupt Set-Enable Registers (ICDISERn).
* reg_offset = (integer_div(N / 32) * 4
* value = 1 << (N mod 32) */
    LSR R4, R0, #3    // calculate reg_offset
    BIC R4, R4, #3    // R4 = reg_offset
    LDR R2, =0xFFFED100
    ADD R4, R2, R4    // R4 = address of ICDISER
    AND R2, R0, #0x1F // N mod 32
    MOV R5, #1        // enable
    LSL R2, R5, R2    // R2 = value
/* Using the register address in R4 and the value in R2 set the
* correct bit in the GIC register */
    LDR R3, [R4]      // read current register value
    ORR R3, R3, R2    // set the enable bit
    STR R3, [R4]      // store the new register value
/* Configure Interrupt Processor Targets Register (ICDIPTRn)
* reg_offset = integer_div(N / 4) * 4
* index = N mod 4 */
    BIC R4, R0, #3    // R4 = reg_offset
    LDR R2, =0xFFFED800
    ADD R4, R2, R4    // R4 = word address of ICDIPTR
    AND R2, R0, #0x3  // N mod 4
    ADD R4, R2, R4    // R4 = byte address in ICDIPTR
/* Using register address in R4 and the value in R2 write to
* (only) the appropriate byte */
    STRB R1, [R4]
    POP {R4-R5, PC}

KEY_ISR:
//write the content of edgecapture register into pb 
	PUSH {LR}
    BL read_PB_edgecp_ASM
	LDR R1, =PB_int_flag
	STR R0, [R1]
	BL PB_clear_edgecp_ASM
	POP {LR}
    BX LR

ARM_TIM_ISR:
	PUSH {LR}
	LDR R1, =tim_int_flag 
	MOV R0, #1
	STR R0, [R1]
	BL ARM_TIM_clear_INT_ASM
	POP {LR}
	BX LR

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
