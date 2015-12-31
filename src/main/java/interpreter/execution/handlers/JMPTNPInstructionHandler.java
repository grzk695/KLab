package interpreter.execution.handlers;

import interpreter.execution.model.InstructionPointer;
import interpreter.translate.model.InstructionCode;
import interpreter.translate.model.JumperInstruction;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JMPTNPInstructionHandler extends AbstractInstructionHandler {
	@Override
	public InstructionCode getSupportedInstructionCode() {
		return InstructionCode.JMPTNP;
	}

	@Override
	public void handle(InstructionPointer instructionPointer) {
		JumperInstruction jmp = (JumperInstruction) instructionPointer.currentInstruction();
		boolean result = executionContext.executionStackPeek().isTrue();
		if (result) {
			instructionPointer.jumpTo(jmp.getJumpIndex());
		} else {
			instructionPointer.increment();
		}
	}
}