package ru.practicum.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilation.model.Compilation;

import java.util.List;


public interface RepositoryOfCompilation extends JpaRepository<Compilation, Long> {

    Compilation findCompilationById(Long compId);

    List<Compilation> findCompilationByPinnedIs(Boolean pinned, Pageable pageable);

    void removeCompilationById(Long compId);
}