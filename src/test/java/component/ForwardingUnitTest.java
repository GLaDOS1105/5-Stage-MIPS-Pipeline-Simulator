package component;

import component.pipeline.ExecutionToMemoryAccessRegister;
import component.pipeline.InstructionDecodeToExecutionRegister;
import component.pipeline.MemoryAccessToWriteBackRegister;
import controller.MainController;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static component.ForwardingUnit.ForwardingSignal.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ForwardingUnitTest {

    @NotNull
    private final InstructionDecodeToExecutionRegister idExe = mock(InstructionDecodeToExecutionRegister.class);
    @NotNull
    private final ExecutionToMemoryAccessRegister exeMem = mock(ExecutionToMemoryAccessRegister.class);
    @NotNull
    private final MemoryAccessToWriteBackRegister memWb = mock(MemoryAccessToWriteBackRegister.class);
    @NotNull
    private ForwardingUnit forwardingUnit;

    @BeforeEach
    void buildUp() {
        forwardingUnit = new ForwardingUnit(idExe, exeMem, memWb);
    }

    @Test
    void testForwardExeHazard() {
        final int hazardAddress = 2;
        when(exeMem.getRegisterWrite()).thenReturn(MainController.RegisterWrite.TRUE);
        when(exeMem.getWriteRegisterAddress()).thenReturn(hazardAddress);
        when(memWb.getRegisterWrite()).thenReturn(MainController.RegisterWrite.TRUE);
        when(memWb.getWriteRegisterAddress()).thenReturn(hazardAddress);
        when(idExe.getRs()).thenReturn(hazardAddress);
        when(idExe.getRt()).thenReturn(hazardAddress);
        assertEquals(FROM_EXE, forwardingUnit.getOperand1ForwardingSignal());
        assertEquals(FROM_EXE, forwardingUnit.getOperand2ForwardingSignal());
    }

    @Test
    void testForwardMemHazard() {
        final int hazardAddress = 2;
        when(exeMem.getRegisterWrite()).thenReturn(MainController.RegisterWrite.FALSE);
        when(exeMem.getWriteRegisterAddress()).thenReturn(hazardAddress);
        when(memWb.getRegisterWrite()).thenReturn(MainController.RegisterWrite.TRUE);
        when(memWb.getWriteRegisterAddress()).thenReturn(hazardAddress);
        when(idExe.getRs()).thenReturn(hazardAddress);
        when(idExe.getRt()).thenReturn(hazardAddress);
        assertEquals(FROM_MEM, forwardingUnit.getOperand1ForwardingSignal());
        assertEquals(FROM_MEM, forwardingUnit.getOperand2ForwardingSignal());
    }

    @Test
    void testNoHazard() {
        when(exeMem.getRegisterWrite()).thenReturn(MainController.RegisterWrite.TRUE);
        when(exeMem.getWriteRegisterAddress()).thenReturn(2);
        when(idExe.getRs()).thenReturn(3);
        when(idExe.getRt()).thenReturn(4);
        assertEquals(FROM_ID, forwardingUnit.getOperand1ForwardingSignal());
        assertEquals(FROM_ID, forwardingUnit.getOperand2ForwardingSignal());

        when(exeMem.getRegisterWrite()).thenReturn(MainController.RegisterWrite.FALSE);
        when(exeMem.getWriteRegisterAddress()).thenReturn(2);
        when(idExe.getRs()).thenReturn(2);
        when(idExe.getRt()).thenReturn(2);
        assertEquals(FROM_ID, forwardingUnit.getOperand1ForwardingSignal());
        assertEquals(FROM_ID, forwardingUnit.getOperand2ForwardingSignal());

        when(memWb.getRegisterWrite()).thenReturn(MainController.RegisterWrite.TRUE);
        when(memWb.getWriteRegisterAddress()).thenReturn(2);
        when(idExe.getRs()).thenReturn(3);
        when(idExe.getRt()).thenReturn(4);
        assertEquals(FROM_ID, forwardingUnit.getOperand1ForwardingSignal());
        assertEquals(FROM_ID, forwardingUnit.getOperand2ForwardingSignal());

        when(memWb.getRegisterWrite()).thenReturn(MainController.RegisterWrite.FALSE);
        when(memWb.getWriteRegisterAddress()).thenReturn(2);
        when(idExe.getRs()).thenReturn(2);
        when(idExe.getRt()).thenReturn(2);
        assertEquals(FROM_ID, forwardingUnit.getOperand1ForwardingSignal());
        assertEquals(FROM_ID, forwardingUnit.getOperand2ForwardingSignal());
    }
}