package interpreter.execution.handlers;

import interpreter.commons.ObjectData;
import interpreter.execution.model.InstructionPointer;
import interpreter.execution.service.ExecutionContextManager;
import interpreter.math.matrix.MatrixBuilder;
import interpreter.math.matrix.MatrixFactory;
import interpreter.math.scalar.DoubleScalar;
import interpreter.translate.model.instruction.InstructionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MatrixVerseInstructionHandler extends AbstractInstructionHandler {

    private MatrixFactory matrixFactory;
    private ExecutionContextManager executionContextManager;

    @Override
    public void handle(InstructionPointer instructionPointer) {
        process(executionContextManager.executionStackPop(executionContext, instructionPointer.current().getArgumentsNumber()));
        instructionPointer.increment();
    }

    private void process(List<ObjectData> objectDataList) {
        MatrixBuilder<Double> matrixBuilder = matrixFactory.createDoubleBuilder();
        objectDataList.forEach(objectData -> process(matrixBuilder, objectData));
        executionContext.executionStackPush(matrixBuilder.build());
    }

    @Override
    public InstructionCode getSupportedInstructionCode() {
        return InstructionCode.MATRIX_VERSE;
    }

    @Autowired
    public void setMatrixFactory(MatrixFactory matrixFactory) {
        this.matrixFactory = matrixFactory;
    }

    private void process(MatrixBuilder<Double> builder, ObjectData objectData) {
        if (objectData instanceof DoubleScalar) {
            builder.appendRight(((DoubleScalar) objectData).getValue());
        } else {
            throw new RuntimeException();
        }
    }

    @Autowired
    public void setExecutionContextManager(ExecutionContextManager executionContextManager) {
        this.executionContextManager = executionContextManager;
    }
}
