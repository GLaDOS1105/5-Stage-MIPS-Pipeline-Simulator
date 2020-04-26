package component;

import component.pipeline.InstructionDecode;
import component.pipeline.InstructionDecodeToExecutionRegister;
import component.pipeline.InstructionFetch;
import component.pipeline.InstructionFetchToInstructionDecodeRegister;
import controller.MainController;
import org.junit.jupiter.api.Test;
import signal.Instruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PipelineRegisterTest {

    @Test
    void testGetInstructionFetchToInstructionDecodeRegisterProperties() {
        int expectedProgramCounter = 8;
        Instruction expectedInstruction = new Instruction("00000000000000000000000000000000");

        InstructionFetch instructionFetch = mock(InstructionFetch.class);
        when(instructionFetch.getNewProgramCounter()).thenReturn(expectedProgramCounter);
        when(instructionFetch.getInstruction()).thenReturn(expectedInstruction);

        InstructionFetchToInstructionDecodeRegister ifId = new InstructionFetchToInstructionDecodeRegister(instructionFetch);
        ifId.update();

        assertEquals(expectedProgramCounter, ifId.getNewProgramCounter());
        assertEquals(expectedInstruction, ifId.getInstruction());
    }

    @Test
    void testGetInstructionDecodeToExecutionRegisterControlSignals() {
        MainController.RegisterDestination expectedRegisterDestination = MainController.RegisterDestination.RD;
        MainController.AluOperation expectedAluOperation = MainController.AluOperation.R_TYPE;
        MainController.AluSource expectedAluSource = MainController.AluSource.REGISTER;
        MainController.Branch expectedBranch = MainController.Branch.FALSE;
        MainController.MemoryRead expectedMemoryRead = MainController.MemoryRead.FALSE;
        MainController.MemoryWrite expectedMemoryWrite = MainController.MemoryWrite.FALSE;
        MainController.RegisterWrite expectedRegisterWrite = MainController.RegisterWrite.TRUE;
        MainController.MemoryToRegister expectedMemoryToRegister = MainController.MemoryToRegister.FROM_ALU_RESULT;

        InstructionDecode instructionDecode = mock(InstructionDecode.class);
        when(instructionDecode.getRegisterDestination()).thenReturn(expectedRegisterDestination);
        when(instructionDecode.getAluOperation()).thenReturn(expectedAluOperation);
        when(instructionDecode.getAluSource()).thenReturn(expectedAluSource);
        when(instructionDecode.getBranch()).thenReturn(expectedBranch);
        when(instructionDecode.getMemoryRead()).thenReturn(expectedMemoryRead);
        when(instructionDecode.getMemoryWrite()).thenReturn(expectedMemoryWrite);
        when(instructionDecode.getRegisterWrite()).thenReturn(expectedRegisterWrite);
        when(instructionDecode.getMemoryToRegister()).thenReturn(expectedMemoryToRegister);

        InstructionDecodeToExecutionRegister idExe = new InstructionDecodeToExecutionRegister(instructionDecode);
        idExe.update();

        assertEquals(expectedRegisterDestination, idExe.getRegisterDestination());
        assertEquals(expectedAluOperation, idExe.getAluOperation());
        assertEquals(expectedAluSource, idExe.getAluSource());
        assertEquals(expectedBranch, idExe.getBranch());
        assertEquals(expectedMemoryRead, idExe.getMemoryRead());
        assertEquals(expectedMemoryWrite, idExe.getMemoryWrite());
        assertEquals(expectedRegisterWrite, idExe.getRegisterWrite());
        assertEquals(expectedMemoryToRegister, idExe.getMemoryToRegister());
    }

    @Test
    void testGetInstructionDecodeToExecutionRegisterProperties() {
    }

    @Test
    void testGetExecutionToMemoryAccessRegisterProperties() {
    }

    @Test
    void testGetMemoryAccessToWriteBackRegisterProperties() {
    }
}