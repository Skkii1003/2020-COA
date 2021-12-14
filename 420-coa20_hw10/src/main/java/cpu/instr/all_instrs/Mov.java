package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.MMU;

public class Mov implements Instruction{
    @Override
    public int exec(String eip, int opcode) {
        MMU mmu = MMU.getMMU();
        String segSelector = CPU_State.cs.read();
        String logic = segSelector + eip;
        String ins = new String(mmu.read(logic,40));

        String imm32 = ins.substring(8);
        CPU_State.eax.write(imm32);
        return 40;
    }
}
