package org.example.service;

import java.nio.file.Path;
import java.util.Optional;

public interface IFileAnalyzer<T> {

    Optional<T> analyze(Path path);

}