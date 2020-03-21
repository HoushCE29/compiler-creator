package dev.houshce29.cc.extensions;

import dev.houshce29.cc.lex.Token;
import dev.houshce29.cc.parse.SymbolTree;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Engine that runs extensions.
 */
public final class ExtensionExecutionEngine {
    private static final Comparator<Hook> HOOK_COMPARATOR =
            Comparator.comparingInt(Hook::getSequence);
    private final Map<HookPoint, SortedSet<Hook>> sortedHookMap;

    private ExtensionExecutionEngine(Map<HookPoint, SortedSet<Hook>> sortedHookMap) {
        this.sortedHookMap = sortedHookMap;
    }

    public void run(HookPoint hookPoint, String input) {
        run(sortedHookMap.get(hookPoint), input);
    }

    public void run(HookPoint hookPoint, List<Token> tokens) {
        run(sortedHookMap.get(hookPoint), tokens);
    }

    public void run(HookPoint hookPoint, List<Token> tokens, SymbolTree symbolTree) {
        run(sortedHookMap.get(hookPoint), tokens, symbolTree);
    }

    public void runForFailure(Throwable throwable) {
        run(sortedHookMap.get(HookPoint.ON_FAILURE), throwable);
    }

    public boolean hasExtensions() {
        return !sortedHookMap.isEmpty();
    }

    public Collection<Hook> getExtensions() {
        return sortedHookMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static ExtensionExecutionEngine create(Collection<Hook> hooks) {
        Map<HookPoint, SortedSet<Hook>> sortedHookMap = new HashMap<>();
        for (Hook hook : hooks) {
            HookPoint hookPoint = hook.getHookPoint();
            if (!sortedHookMap.containsKey(hookPoint)) {
                sortedHookMap.put(hookPoint, new TreeSet<>(HOOK_COMPARATOR));
            }
            sortedHookMap.get(hookPoint).add(hook);
        }

        return new ExtensionExecutionEngine(sortedHookMap);
    }

    private void run(SortedSet<Hook> hooks, Object... objects) {
        if (hooks != null) {
            for (Hook hook : hooks) {
                hook.execute(objects);
            }
        }
    }
}
