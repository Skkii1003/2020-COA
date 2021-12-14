## 作业目标

实现指令控制状态机ICC

## 具体要求

本次作业要求能够执行一连串的指令

为此需要实现：
1. 实现 *opcode=0xf4 hlt* 指令
2. 在上一次作业的基础上实现ICC状态机，允许对代码进行重构

在CPU中，指令的执行流一般是由ICC进行控制，如图所示，其中不需要考虑ICC=0b11的中断分支：
![](ICC.png)

## 补充说明

1. ICC状态机会不断重复获取、解析、执行指令，直到执行到hlt指令为止(实际上hlt是关机指令，为了避免实现复杂的中断(ICC=0b11)，本作业简单地将hlt指定为指令终止指令)
2. 我们不限定具体的实现方式，你可以使用一个最简单的while循环，也可以考虑状态设计模式，或者一个专门的控制器类。