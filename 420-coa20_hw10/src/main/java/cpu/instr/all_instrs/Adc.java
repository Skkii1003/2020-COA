package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.MMU;
import cpu.alu.ALU;
import cpu.registers.EFlag;

public class Adc implements Instruction{
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

        if (eFlag.getCF()){
            CPU_State.eax.write(alu.add(alu.add(imm32,des),"00000000000000000000000000000001"));
        }
        else
            CPU_State.eax.write(alu.add(imm32,des));
        return 40;
    }
}
