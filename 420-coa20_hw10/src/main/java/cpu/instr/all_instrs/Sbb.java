package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.MMU;
import cpu.alu.ALU;
import cpu.registers.EFlag;

public class Sbb implements Instruction{
    @Override
    public int exec(String eip, int opcode) {
        MMU mmu = MMU.getMMU();
        ALU alu = new ALU();
        EFlag eFlag = (EFlag) CPU_State.eflag;
        String segSelector = CPU_State.cs.read();
        String logic = segSelector + eip;
        String ins = new String(mmu.read(logic,40));

        String imm32 = ins.substring(8);
        String des = CPU_State.eax.read();

        if (eFlag.getCF())
            CPU_State.eax.write(alu.sub("00000000000000000000000000000001",alu.sub(imm32,des)));
        else
            CPU_State.eax.write(alu.sub(imm32,des));

        return 40;
    }
}
