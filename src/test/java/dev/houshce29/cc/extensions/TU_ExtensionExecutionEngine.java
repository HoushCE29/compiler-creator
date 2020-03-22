package dev.houshce29.cc.extensions;

import dev.houshce29.cc.extensions.api.BeforeCompilationHook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TU_ExtensionExecutionEngine {
    private static final List<Integer> RUN_ORDER = new ArrayList<>();
    private List<Hook> hooks;

    @Before
    public void beforeEach() {
        RUN_ORDER.clear();
        hooks = Arrays.asList(
                new TestHook(8),
                new TestHook(6),
                new TestHook(7),
                new TestHook(5),
                new TestHook(3),
                new TestHook(0),
                new TestHook(9));
    }

    @Test
    public void test() {
        // Test empty engine
        ExtensionExecutionEngine engine = ExtensionExecutionEngine.create(Collections.emptyList());
        Assert.assertFalse(engine.hasExtensions());

        engine = ExtensionExecutionEngine.create(hooks);
        engine.run(HookPoint.BEFORE_COMPILE, "");
        // Should run in numeric order of sequences, not the order that they
        // were added in.
        Assert.assertEquals(Arrays.asList(0, 3, 5, 6, 7, 8, 9), RUN_ORDER);
    }


    private static final class TestHook extends BeforeCompilationHook {
        TestHook(int sequence) {
            super(sequence);
        }

        @Override
        public void execute(String rawInput) {
            // Push these values to the static list
            RUN_ORDER.add(getSequence());
        }
    }
}
