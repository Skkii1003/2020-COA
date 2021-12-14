package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.alu.ALU;
import memory.Memory;
import transformer.Transformer;

public class Push implements Instruction{
    @Override
    public int exec(String eip, int opcode) {
        ALU alu = new ALU();
        Transformer t = new Transformer();
        Memory memory = Memory.getMemory();

        CPU_State.esp.write(alu.sub(t.intToBinary("4"),CPU_State.esp.read()));
        memory.pushStack(CPU_State.esp.read(),CPU_State.ebx.read());
        return 8;
    }
}
