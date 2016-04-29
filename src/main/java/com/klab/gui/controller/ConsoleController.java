package com.klab.gui.controller;

import com.google.common.eventbus.Subscribe;
import com.klab.common.EventService;
import com.klab.gui.events.CommandSubmittedEvent;
import com.klab.gui.helpers.KeyboardHelper;
import com.klab.gui.model.CommandHistory;
import com.klab.interpreter.core.ExecutionCommand;
import com.klab.interpreter.core.Interpreter;
import com.klab.interpreter.core.events.PrintEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.CodeArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ConsoleController {
    private CommandHistory commandHistory = new CommandHistory();
    private Interpreter interpreter;
    private EventService eventService;

    @FXML
    private CodeArea commandInput;

    @FXML
    private CodeArea consoleOutput;

    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {
        if (KeyboardHelper.isEnterPressed(keyEvent)) {
            onEnter(keyEvent);
        } else if (KeyboardHelper.isArrowUpPressed(keyEvent)) {
            onArrowUp(keyEvent);
        } else if (KeyboardHelper.isArrowDownPressed(keyEvent)) {
            onArrowDown(keyEvent);
        } else if (KeyboardHelper.isEnterShift(keyEvent)) {
            commandInput.appendText("\n");
        }
    }

    @Subscribe
    public void onPrintEvent(PrintEvent printEvent) {
        Platform.runLater(() -> {
            String objectName = printEvent.getName();
            if (Objects.nonNull(objectName)) {
                consoleOutput.appendText(String.format("%s = ", objectName));
            }
            consoleOutput.appendText(String.format("%s\n\n", printEvent.getData().toString()));
        });
    }

    @Subscribe
    public void onCommandSubmittedEvent(CommandSubmittedEvent command) {
        consoleOutput.appendText(String.format(">> %s \n", command.getData()));
        commandHistory.add(command.getData());
        interpreter.startAsync(new ExecutionCommand(command.getData(), command.isProfiling()));
    }

    private void onArrowDown(KeyEvent keyEvent) {
        commandInput.clear();
        commandInput.appendText(commandHistory.prev());
        keyEvent.consume();
    }

    private void onArrowUp(KeyEvent keyEvent) {
        commandInput.clear();
        commandInput.appendText(commandHistory.next());
        keyEvent.consume();
    }

    private void onEnter(KeyEvent keyEvent) {
        CommandSubmittedEvent event = CommandSubmittedEvent.create()
                .data(commandInput.getText())
                .profiling(false)
                .build(this);
        eventService.publish(event);
        commandInput.clear();
        keyEvent.consume();
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Autowired
    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }
}