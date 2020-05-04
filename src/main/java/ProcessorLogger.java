import component.pipeline.ExecutionToMemoryAccessRegister;
import component.pipeline.InstructionDecodeToExecutionRegister;
import component.pipeline.InstructionFetchToInstructionDecodeRegister;
import component.pipeline.MemoryAccessToWriteBackRegister;
import org.jetbrains.annotations.NotNull;

import java.util.SortedSet;
import java.util.TreeSet;

public class ProcessorLogger {

    private final StringBuilder stringBuilder = new StringBuilder();

    private int clockCycle = 1;

    public void onClockCycleFinished(
            @NotNull Processor processor,
            @NotNull InstructionFetchToInstructionDecodeRegister ifId,
            @NotNull InstructionDecodeToExecutionRegister idExe,
            @NotNull ExecutionToMemoryAccessRegister exeMem,
            @NotNull MemoryAccessToWriteBackRegister memWb
    ) {
        appendLine("CC" + clockCycle + ":");
        appendLine("");
        logRegister(processor);
        appendLine("");
        logDataMemory(processor);
        appendLine("");
        logIfId(ifId);
        appendLine("");
        logIdExe(idExe);
        appendLine("");
        logExeMem(exeMem);
        appendLine("");
        logMemWb(memWb);
        appendLine("=================================================================");
        clockCycle++;
    }

    private void logRegister(@NotNull Processor processor) {
        SortedSet<Integer> sortedAddresses = new TreeSet<>(processor.getWrittenRegisterAddresses());
        appendLine("Registers:");
        sortedAddresses.forEach((address) -> appendLine("$" + address + ": " + processor.readRegister(address)));
    }

    private void logDataMemory(@NotNull Processor processor) {
        SortedSet<Integer> sortedAddresses = new TreeSet<>(processor.getWrittenDataMemoryAddresses());
        appendLine("Data memory:");
        sortedAddresses.forEach((address) ->
                appendLine(String.format("0x%02X: %d", address, processor.readDataMemory(address)))
        );
    }

    private void logIfId(@NotNull InstructionFetchToInstructionDecodeRegister ifId) {
        appendLine("IF/ID:");
        appendLine("PC\t" + ifId.getProgramCounter());
        appendLine("Instruction\t" + ifId.getInstruction());
    }

    private void logIdExe(@NotNull InstructionDecodeToExecutionRegister idExe) {
        appendLine("ID/EX:");
        appendLine("ReadData1\t" + idExe.getRegisterData1());
        appendLine("ReadData2\t" + idExe.getRegisterData2());
        appendLine("sign_ext\t" + idExe.getImmediate());
        appendLine("Rs\t" + idExe.getRs());
        appendLine("Rt\t" + idExe.getRt());
        appendLine("Rd\t" + idExe.getRd());
        appendLine("Control Signals\t"
                + idExe.getRegisterDestination()
                + idExe.getAluOperation()
                + idExe.getAluSource()
                + idExe.getBranch()
                + idExe.getMemoryRead()
                + idExe.getMemoryWrite()
                + idExe.getRegisterWrite()
                + idExe.getMemoryToRegister());
    }

    private void logExeMem(@NotNull ExecutionToMemoryAccessRegister exeMem) {
        appendLine("EX/MEM:");
        appendLine("ALUout\t" + exeMem.getAluResult());
        appendLine("WriteData\t" + exeMem.getRegisterData2());
        appendLine("Rt/Rd\t" + exeMem.getWriteRegisterAddress());
        appendLine("Control Signals\t"
                + exeMem.getBranch()
                + exeMem.getMemoryRead()
                + exeMem.getMemoryWrite()
                + exeMem.getRegisterWrite()
                + exeMem.getMemoryToRegister());
    }

    private void logMemWb(@NotNull MemoryAccessToWriteBackRegister memWb) {
        appendLine("MEM/WB:");
        appendLine("ReadData\t" + memWb.getMemoryReadData());
        appendLine("ALUout\t" + memWb.getAluResult());
        appendLine("Rt/Rd\t" + memWb.getWriteRegisterAddress());
        appendLine("Control Signals\t"
                + memWb.getRegisterWrite()
                + memWb.getMemoryToRegister());
    }

    @NotNull
    public String getLog() {
        return stringBuilder.toString();
    }

    private void appendLine(String str) {
        stringBuilder.append(str).append(System.lineSeparator());
    }
}