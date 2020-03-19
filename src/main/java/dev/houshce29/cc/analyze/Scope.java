package dev.houshce29.cc.analyze;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a scope within a symbol table. This contains basic
 * CRUD operations for symbols and their respective data.
 * @param <D> Data type for the data associated with the symbol.
 */
public class Scope<D> {
    private final Map<String, D> symbolsToData;
    private final int level;

    /**
     * Creates a new instance.
     * @param level Level of this scope.
     */
    public Scope(int level) {
        this.level = level;
        this.symbolsToData = new HashMap<>();
    }

    /**
     * Sets the symbol and its corresponding data.
     * @param symbol Symbol to collect.
     * @param data Data for the symbol.
     */
    public void put(String symbol, D data) {
        symbolsToData.put(symbol, data);
    }

    /**
     * Returns the data for the given symbol.
     * If the symbol is not found, `null` is returned.
     * @param symbol Symbol to get data for.
     * @return
     */
    public D get(String symbol) {
        return symbolsToData.get(symbol);
    }

    /**
     * Returns `true` if this scope contains the given symbol.
     * @param symbol Symbol to check for.
     * @return `true` if this scope contains the symbol.
     */
    public boolean contains(String symbol) {
        return symbolsToData.containsKey(symbol);
    }

    /**
     * Removes a symbol and its data.
     * @param symbol Symbol to remove.
     * @return The removed symbol data.
     */
    public D remove(String symbol) {
        return symbolsToData.remove(symbol);
    }

    /**
     * Returns this scope as a map, mapped from
     * symbol to data.
     * @return Mapped scope.
     */
    public Map<String, D> asMap() {
        return this.symbolsToData;
    }

    /**
     * @return The level of this scope.
     */
    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return this.symbolsToData.toString();
    }
}
