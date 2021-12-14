package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.alu.ALU;
import memory.Memory;
import transformer.Transformer;

public class Pop implements Instruction{
    @Override
    public int exec(String eip, int opcode) {
        ALU alu = new ALU();
        Transformer t = new Transformer();
        Memory memory = Memory.getMemory();


        if (opcode==88)
            CPU_State.eax.write(memory.topOfStack(CPU_State.esp.read()));
        else if (opcode==89)
            CPU_State.ecx.write(memory.topOfStack(CPU_State.esp.read()));
        else
            CPU_State.edx.write(memory.topOfStack(CPU_State.esp.read()));

        CPU_State.esp.write(alu.add(t.intToBinary("4"),CPU_State.esp.read()));
        return 8;
    }
}
