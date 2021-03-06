package com.klab.gui.factories;

import com.klab.gui.model.ScriptContext;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public interface ScriptTabFactory {
    ScriptContext get(String scriptName, TabPane scriptPane);

    ScriptContext get(String scriptName, TabPane scriptPane, boolean noCreate);

    void initializeScriptPane(TabPane scriptPane);

    ScriptContext removeFromContext(String data);

    void stepInto();

    void stepOver();

    void resumeExecution();

    void runWithPause(TabPane scriptPane, Tab tab);

    void runScript(TabPane scriptPane, Tab tab, boolean profiling);
}