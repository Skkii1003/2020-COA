package cpu;

import cpu.alu.ALU;
import cpu.instr.all_instrs.InstrFactory;
import cpu.instr.all_instrs.Instruction;
import cpu.registers.EIP;
import transformer.Transformer;

public class CPU {

    Transformer transformer = new Transformer();
    MMU mmu = MMU.getMMU();


    /**
     * execInstr specific numbers of instructions
     *
     * @param number numbers of instructions
     */
    public int execInstr(long number) {
        // 执行过的指令的总长度
        int totalLen = 0;
        while (number > 0) {
            // TODO 上次作业
            int len = execInstr();
            totalLen = totalLen + len;
            CPU_State.eip.write(new ALU().add(CPU_State.eip.read(),transformer.intToBinary(len+"")));
            number = number - 1L;
        }
        return totalLen;
    }

    /**
     * execInstr a single instruction according to eip value
     */
    private int execInstr() {
        String eip = CPU_State.eip.read();
        int len = decodeAndExecute(eip);
        return len;
    }

    private int decodeAndExecute(String eip) {
        int opcode = instrFetch(eip, 1);
        Instruction instruction = InstrFactory.getInstr(opcode);
        assert instruction != null;

        //exec the target instruction
        int len = instruction.exec(eip, opcode);
        return len;


    }

    /**
     * @param eip
     * @param length opcode的字节数，本作业只使用单字节opcode
     * @return
     */
    private int instrFetch(String eip, int length) {
        // TODO X   FINISHED √
        String segSelector = CPU_State.cs.read();
        String logic = segSelector + eip;
        char[] data = mmu.read(logic,length*8);
        return Integer.parseInt(transformer.binaryToInt(String.valueOf(data)));
    }

    public void execUntilHlt(){
        // TODO ICC
        EIP eip = (EIP) CPU_State.eip;
        int len = execInstr();
        CPU_State.ICC = 0;
        while(true){
            if (len==-1)
                break;
            else{
                eip.plus(len);
                CPU_State.eip = eip;
            }
            len = execInstr();
        }
        CPU_State.ICC = 0;
        eip.plus(8);
        CPU_State.eip = eip;
    }

}

