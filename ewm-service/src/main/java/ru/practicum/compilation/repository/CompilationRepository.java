package ru.practicum.compilation.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilation.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

  List<Compilation> findAllByPinned(boolean pinned, Pageable pageable);

}
