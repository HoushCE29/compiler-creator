package dev.houshce29.cc.analyze;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Analysis tool that tracks the scope of variables.
 * @param <D> Type of data stored per symbol.
 */
public class SymbolTable<D> {
    private static final int NO_SCOPE = -1;
    private final List<Scope<D>> scopes;
    private int currentScopeLevel;

    /**
     * Creates a new instance.
     */
    public SymbolTable() {
        scopes = new ArrayList<>();
        currentScopeLevel = NO_SCOPE;
    }

    /**
     * Opens a new scope.
     */
    public void openScope() {
        scopes.add(new Scope<>(++currentScopeLevel));
    }

    /**
     * Closes the latest scope.
     */
    public void closeScope() {
        if (!scopes.isEmpty()) {
            scopes.remove(currentScopeLevel--);
        }
    }

    /**
     * Puts the symbol and its data into the current scope.
     * If there are no scopes, a new scope will be opened.
     * @param symbol Symbol to add to the current scope.
     * @param data Data associated with the symbol.
     */
    public void put(String symbol, D data) {
        if (isEmpty()) {
            openScope();
        }
        scopes.get(currentScopeLevel).put(symbol, data);
    }

    /**
     * @return `true` if this table is empty (contains no scopes).
     */
    public boolean isEmpty() {
        return scopes.isEmpty();
    }

    /**
     * Returns an optional maybe containing the scope at the given level.
     * @param level Target scope level.
     * @return Scope of the given level, if found.
     */
    public Optional<Scope<D>> getScope(int level) {
        if (level < 0 || level > currentScopeLevel) {
            return Optional.empty();
        }
        return Optional.of(scopes.get(level));
    }

    /**
     * @return Optional maybe containing the current scope.
     */
    public Optional<Scope<D>> getCurrentScope() {
        return getScope(currentScopeLevel);
    }

    /**
     * @return The current scope level. -1 indicates no scopes present.
     */
    public int getCurrentScopeLevel() {
        return currentScopeLevel;
    }

    /**
     * Returns the latest scope containing the given symbol.
     * @param symbol Target symbol.
     * @return Optional maybe containing a matching scope.
     */
    public Optional<Scope<D>> findLatestScope(String symbol) {
        return stream()
                .filter(scope -> scope.contains(symbol))
                .findFirst();
    }

    /**
     * Returns the latest scope's data for the given symbol.
     * @param symbol Target symbol.
     * @return Optional maybe containing a matching scope.
     */
    public Optional<D> findLatest(String symbol) {
        return findLatestScope(symbol)
                .map(scope -> scope.get(symbol));
    }

    /**
     * Streams this table.
     * @return Stream of scopes.
     */
    public Stream<Scope<D>> stream() {
        return scopes.stream()
                .sorted(Comparator.comparing(Scope::getLevel, Comparator.reverseOrder()));
    }
}
