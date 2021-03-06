package com.klab.interpreter.functions.external;

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import com.klab.common.EventService;
import com.klab.interpreter.core.ExecutionStartInitialization;
import com.klab.interpreter.core.code.ScriptFileService;
import com.klab.interpreter.core.events.CodeTranslatedEvent;
import com.klab.interpreter.core.events.ExecutionStartedEvent;
import com.klab.interpreter.core.events.ScriptChangeEvent;
import com.klab.interpreter.debug.BreakpointService;
import com.klab.interpreter.debug.BreakpointUpdatedEvent;
import com.klab.interpreter.execution.model.Code;
import com.klab.interpreter.translate.model.CallInstruction;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
class ExternalFunctionServiceImpl implements ExternalFunctionService, ExecutionStartInitialization {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalFunctionServiceImpl.class);
    private ScriptFileService scriptFileService;
    private BreakpointService breakpointService;
    private ExternalFunctionParser externalFunctionParser;
    private EventService eventService;
    private Map<FunctionMapKey, ExternalFunction> functionsCache = Maps.newHashMap();

    @Override
    public ExternalFunction loadFunction(CallInstruction cl) {
        FunctionMapKey key = new FunctionMapKey(cl.getName(), cl.getArgumentsNumber(), cl.getExpectedOutputSize());
        ExternalFunction externalFunction = functionsCache.get(key);
        return externalFunction != null ? externalFunction : read(key, cl);
    }

    @Override
    public Code loadFromCache(String name) {
        ExternalFunction fun = functionsCache.values().stream()
                .filter(externalFunction -> externalFunction.getName().equals(name))
                .findFirst().orElse(null);
        return fun == null ? null : fun.getCode();
    }

    private ExternalFunction read(FunctionMapKey key, CallInstruction cl) {
        try {
            String fileContent = scriptFileService.readScript(key.name);
            Optional<ExternalFunction> externalFunctionOptional = externalFunctionParser.parse(fileContent);
            if (externalFunctionOptional.isPresent()) {
                final ExternalFunction externalFunction = externalFunctionOptional.get();
                if (!externalFunction.getName().equals(key.name)) {
                    throw new RuntimeException("Function name does not match file name!");
                }

                EndFunctionInstruction ret = new EndFunctionInstruction(externalFunction.getArguments().size(), externalFunction.getReturns().size());
                ret.setExpectedOutput(cl.getExpectedOutputSize());
                externalFunction.getCode().add(ret, null);
                externalFunction.getCode().setSourceId(key.name);
                breakpointService.updateBreakpoints(externalFunction.getCode());
                functionsCache.put(key, externalFunction);
                eventService.publishAsync(new CodeTranslatedEvent(externalFunction.getCode(), this));
                return externalFunction;
            }
        } catch (IOException e) {
            LOGGER.error("Error reading function '{}'. {}", key.name, e);
        }
        throw new UnsupportedOperationException(String.format("Function '%s' not found or incorrect syntax", key.name)); // TODO exception
    }

    @Override
    public void initialize() {
        for (ExternalFunction externalFunction : functionsCache.values()) {
            externalFunction.getCode().forEach(instruction -> instruction.setProfilingData(null));
        }
    }

    @Subscribe
    public void onScriptChangedEvent(ScriptChangeEvent event) {
        String name = FilenameUtils.removeExtension(event.getData());
        boolean result = functionsCache.entrySet().removeIf(entry -> entry.getKey().name.equals(name));
        if (result) {
            LOGGER.info("Function '{}' removed from cache, cause: {}", name, event.getType());
        }
    }

    @Subscribe
    public void onBreakpointListChanged(BreakpointUpdatedEvent event) {
        functionsCache.values().stream()
                .filter(fun -> fun.getName().equals(event.getData().getScriptId()))
                .forEach(fun -> breakpointService.updateBreakpoints(fun.getCode()));
    }

    @Autowired
    public void setScriptFileService(ScriptFileService scriptFileService) {
        this.scriptFileService = scriptFileService;
    }

    @Autowired
    public void setExternalFunctionParser(ExternalFunctionParser externalFunctionParser) {
        this.externalFunctionParser = externalFunctionParser;
    }

    @Autowired
    public void setBreakpointService(BreakpointService breakpointService) {
        this.breakpointService = breakpointService;
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }
}
