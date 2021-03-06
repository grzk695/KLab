package com.klab.interpreter.execution.handlers;

import com.klab.common.EventService;
import com.klab.interpreter.commons.memory.MemorySpace;
import com.klab.interpreter.core.events.PrintEvent;
import com.klab.interpreter.execution.model.InstructionPointer;
import com.klab.interpreter.translate.model.InstructionCode;
import com.klab.interpreter.types.*;
import com.klab.interpreter.types.converters.ConvertersHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Modify1InstructionHandler extends AbstractInstructionHandler {
    private MemorySpace memorySpace;
    private ConvertersHolder convertersHolder;
    private EventService eventService;

    @Override
    public void handle(InstructionPointer instructionPointer) {
        // TODO type checks
        IdentifierObject target = (IdentifierObject) executionContext.executionStackPop();
        AddressIterator cells = ((Addressable) executionContext.executionStackPop()).getAddressIterator();
        NumericObject source = (NumericObject) executionContext.executionStackPop();
        NumericObject dest = (NumericObject) memorySpace.get(target.getAddress());
        if (cells.max() > dest.getCells()) {
            throw new RuntimeException(); // TODO
        }
        NumericType numericType = convertersHolder.promote(source.getNumericType(), dest.getNumericType());
        Editable<Number> editable = convertersHolder.convert(dest, numericType);
        EditSupportable<Number> supplier = convertersHolder.convert(source, numericType);
        editable.edit(cells, supplier.getEditSupplier());
        editable.setTemp(true);
        ObjectData objectData = memorySpace.set(target.getAddress(), editable, dest.getName());
        if (instructionPointer.currentInstruction().isPrint()) {
            eventService.publish(new PrintEvent(objectData, this));
        }
        instructionPointer.increment();
    }

    @Override
    public InstructionCode getSupportedInstructionCode() {
        return InstructionCode.MODIFY1;
    }

    @Autowired
    public void setMemorySpace(MemorySpace memorySpace) {
        this.memorySpace = memorySpace;
    }

    @Autowired
    public void setConvertersHolder(ConvertersHolder convertersHolder) {
        this.convertersHolder = convertersHolder;
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }
}
