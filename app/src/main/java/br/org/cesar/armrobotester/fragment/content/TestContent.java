package br.org.cesar.armrobotester.fragment.content;

import br.org.cesar.armrobotester.model.Test;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class TestContent {

    /**
     * An array of sample (test) items.
     */
//    public static final List<TestItem> ITEMS = new ArrayList<TestItem>();

    /**
     * A map of sample (test) items, by ID.
     */
//    public static final Map<String, TestItem> ITEM_MAP = new HashMap<String, TestItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
//            addItem(createTestItem(i));
        }
    }

    private static void addItem(TestItem item) {
//        ITEMS.add(item);
//        ITEM_MAP.put(item.id, item);
    }

    private static TestItem createTestItem(int position) {
        return new TestItem(String.valueOf(position),
                "Item " + position,
                makeDetails(position),
                Test.MOTION_A);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
//        builder.append("Details about Item: ").append(position);
//        for (int i = 0; i < position; i++) {
//            builder.append("\nMore details information here.");
//        }
        return builder.toString();
    }

    /**
     * A test item representing a piece of content.
     */
    public static class TestItem extends Test {
//        public final String id;
//        public final String content;
//        public final String details;


        public TestItem(int type) {
            super(type);
        }

        public TestItem(String id, String content, String details,
                        int type) {
            super(type);
//            this.id = id;
//            this.content = content;
//            this.details = details;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
