package com.klab.interpreter.core.code;

import com.klab.common.EventService;
import com.klab.interpreter.core.FileWatcher;
import com.klab.interpreter.core.events.ScriptChangeEvent;
import com.klab.interpreter.utils.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ScriptFileServiceImpl implements ScriptFileService, InitializingBean {
    private static final Logger LOGGER = getLogger(ScriptFileServiceImpl.class);
    private EventService eventService;
    private String scriptRegex = "[A-Za-z][A-Za-z0-9_]*[.]m$";
    private String workingDirectory;
    private String applicationName;
    private String extension = ".m";
    private String charsetName = "UTF-8";

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        watchForFilesChange();
    }

    @Override
    public List<Path> listScripts() throws IOException {
        return Files.list(Paths.get(workingDirectory))
                .filter(path -> path.getFileName().toString().matches(scriptRegex))
                .collect(Collectors.toList());
    }

    @Override
    public void writeScript(String scriptName, String content) throws IOException {
        scriptName = FilenameUtils.removeExtension(scriptName);
        Path path = Paths.get(workingDirectory, String.format("%s%s", scriptName, extension));
        LOGGER.info("Writing script: {}", path);
        Files.write(path, content.getBytes(Charset.forName(charsetName)));
    }

    @Override
    public boolean exists(String name) {
        name = FilenameUtils.removeExtension(name);
        Path path = Paths.get(workingDirectory, String.format("%s%s", name, extension));
        return Files.exists(path);
    }

    @Override
    public String readScript(String scriptName) throws IOException {
        scriptName = FilenameUtils.removeExtension(scriptName);
        Path path = Paths.get(workingDirectory, String.format("%s%s", scriptName, extension));
        return new String(Files.readAllBytes(path)).replaceAll("\\r\\n", "\n");
    }

    @Override
    public void rename(String oldName, String newName) throws IOException {
        oldName = StringUtils.appendIfMissing(oldName, extension);
        newName = StringUtils.appendIfMissing(newName, extension);
        Path oldPath = Paths.get(workingDirectory, oldName);
        Path newPath = Paths.get(workingDirectory, newName);
        Files.move(oldPath, newPath);
    }

    @Override
    public boolean removeScript(String value) throws IOException {
        value = StringUtils.appendIfMissing(value, extension);
        Path path = Paths.get(workingDirectory, value);
        return Files.deleteIfExists(path);
    }

    private void init() throws IOException {
        if (Objects.isNull(workingDirectory)) {
            Path path = Paths.get(FileUtils.getDocumentsPath(), applicationName);
            workingDirectory = path.toString();
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
        }
    }

    private void watchForFilesChange() {
        Consumer<WatchEvent<?>> consumer = (WatchEvent<?> ev) -> {
            if (ev.context().toString().matches(scriptRegex)) {
                eventService.publish(new ScriptChangeEvent(ev.context().toString(), this, ev.kind()));
            }
        };
        Thread thread = new Thread(new FileWatcher(workingDirectory, consumer));
        thread.setDaemon(true);
        thread.start();
    }

    @Value("${app.name}")
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }
}
