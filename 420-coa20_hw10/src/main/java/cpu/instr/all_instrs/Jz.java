package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.MMU;
import cpu.alu.ALU;
import cpu.registers.EFlag;
import transformer.Transformer;

public class Jz implements Instruction{
    @Override
    public int exec(String eip, int opcode) {
        MMU mmu = MMU.getMMU();
        ALU alu = new ALU();
        EFlag eFlag = (EFlag) CPU_State.eflag;
        Transformer t = new Transformer();
        String segSelector = CPU_State.cs.read();
        String logic = segSelector + eip;
        String ins = new String(mmu.read(logic,16));

        String offset = ins.substring(8);
        if (eFlag.getZF()) {
            CPU_State.eip.write(alu.add(eip, offset));
            return 0;
        }
        else
            return 16;
    }
}
