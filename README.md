Introduction

ExeScript Virtual Machine - Virtual Machine for the implementation of the possibility of using scripting languages in Andtoid applications. This project is intended for Android developers who want to enable users to dynamically manage their program content. For example, based on the virtual machine can create a database in which there is no pre-defined forms. The user creates a graphical fallback interface and defines the logic of his work (as in Microsoft Access) or may ask deliberately vague mathematical operations. You can build a browser with possibility of internal api, to create extensions like the one implemented in most desktop browsers. Applications are many, including the creation of an application development environment directly on the mobile device.

What needs to happen in the end

As a result, the work should get a virtual machine as an external library disseminate them to perform in a single-threaded program rehearse completely by appropriate criteria for functional programming with Java like programming language. Interaction with external code with respect to the virtual machine will be carried out by means of abstraction of the hardware drivers (which module VM is draver devices) are a programmer who wants to use this system will have to write your own.
The current state of development

At this point the system is under active development. Fully implemented class specification file, the system instruction execution and debugging.
Partially implemented a system interrupts and system calls. In general terms, at the moment sistmy it is fully functional, but has no "exit to the outside world" because it is not fully implemented system calls.

Plans

The plans already have a fork for the desktop virtual machine implementation. The difference of the fork lies in the fact that it will be used JIT compiler instead of an interpreter for Android (Andoid no support for direct access to the runtime kernel, so the only possible interpretation of bytecode). In addition, we would like to develop a system of division of time of execution, for the possibility of a multi-threaded program execution and multy-prcessing system. Necessary spend a considerable amount of work to optimize the system, to increase the speed of code execution.

Related projects

EsvmCompiler - Java compiler like programming language into bytecode virtual machine.
