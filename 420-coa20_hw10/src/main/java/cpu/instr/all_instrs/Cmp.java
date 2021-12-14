package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.MMU;
import cpu.alu.ALU;
import cpu.registers.EFlag;
import transformer.Transformer;

public class Cmp implements Instruction{
    @Override
    public int exec(String eip, int opcode) {
        MMU mmu = MMU.getMMU();
        ALU alu = new ALU();
        EFlag eFlag = (EFlag) CPU_State.eflag;
        Transformer t = new Transformer();
        String segSelector = CPU_State.cs.read();
        String logic = segSelector + eip;
        String ins = new String(mmu.read(logic,40));

        String imm32 = ins.substring(8);
        String des = CPU_State.eax.read();
        String res = alu.sub(imm32,des);
        eFlag.setZF(Integer.parseInt(res) == 0);
        eFlag.setSF(Integer.parseInt(t.binaryToInt(res)) < 0);
        CPU_State.eflag = eFlag;
        return 40;
    }
}
