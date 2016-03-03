package gui.controller;

import com.google.common.eventbus.Subscribe;
import common.EventService;
import gui.events.CommandSubmittedEvent;
import gui.events.OpenScriptEvent;
import gui.model.script.ScriptEditorPane;
import gui.model.script.ScriptTab;
import gui.service.ScriptViewService;
import interpreter.core.code.ScriptFileService;
import interpreter.debug.BreakpointEvent;
import interpreter.debug.BreakpointReachedEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static interpreter.debug.BreakpointEvent.Operation.ADD;
import static interpreter.debug.BreakpointEvent.Operation.REMOVE;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ScriptEditorController implements Initializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptEditorController.class);
    private ScriptFileService scriptFileService;
    private ScriptViewService scriptViewService;
    private EventService eventService;

    @FXML
    private ScriptEditorPane scriptPane;

    @FXML
    private Button releaseBreakpointButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scriptPane.setSaveScriptHandler(tab -> {
            try {
                scriptFileService.writeScript(tab.getText(), tab.getScriptContent());
            } catch (IOException e) {
                LOGGER.error("error", e);
            }
        });
        scriptPane.setRunScriptHandler(tab -> eventService.publish(new CommandSubmittedEvent(tab.getText(), this)));
    }

    @Subscribe
    public void openScript(OpenScriptEvent event) throws IOException {
        String scriptName = FilenameUtils.removeExtension(event.getData());
        ScriptTab tab = getScriptTab(scriptName);
        scriptPane.getSelectionModel().select(tab);
    }

    private ScriptTab getScriptTab(String scriptName) {
        ScriptTab tab = scriptPane.getScript(scriptName);
        if (tab == null) {
            tab = new ScriptTab(scriptName, scriptViewService.readScript(scriptName));
            scriptPane.addScript(scriptName, tab);
            tab.setOnRunHandler(t -> eventService.publish(new CommandSubmittedEvent(t.getScriptName(), this)));
            tab.setOnCloseHandler(t -> scriptPane.remove(t.getScriptName()));
            tab.getCodeArea().setBreakPointAddedHandler(number -> eventService.publish(BreakpointEvent.create(scriptName, number + 1, ADD, this)));
            tab.getCodeArea().setBreakPointRemovedHandler(number -> eventService.publish(BreakpointEvent.create(scriptName, number + 1, REMOVE, this)));
        }
        return tab;
    }

    @Subscribe
    public void onBreakpointReachedEvent(BreakpointReachedEvent event) {
        releaseBreakpointButton.setOnMouseClicked(ev -> {
            try {
                event.getLock().lock();
                event.getData().setReleased(true);
                event.getReleased().signalAll();
                releaseBreakpointButton.setDisable(true);
            } finally {
                event.getLock().unlock();
            }
        });
        releaseBreakpointButton.setDisable(false);
        Platform.runLater(() -> {
            ScriptTab tab = getScriptTab(event.getData().getSourceId());
            scriptPane.getSelectionModel().select(tab);
        });
    }

    @Autowired
    public void setScriptFileService(ScriptFileService scriptFileService) {
        this.scriptFileService = scriptFileService;
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Autowired
    public void setScriptViewService(ScriptViewService scriptViewService) {
        this.scriptViewService = scriptViewService;
    }
}
