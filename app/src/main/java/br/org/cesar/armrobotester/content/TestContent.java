package br.org.cesar.armrobotester.content;

import br.org.cesar.armrobotester.model.MotionTest;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class TestContent {

    /**
     * A test item representing a piece of content.
     */
    public static class MotionTestItem extends MotionTest {
        public int _id;
        public int status;

        public MotionTestItem(int type) {
            super(type);
            this._id = -1;
            this.status = Status.NONE;
        }

        public MotionTestItem(int id, int status, int type) {
            super(type);
            this._id = id;
            this.status = status;
        }

        public String getStatus() {
            String strStatus;

            switch (status) {
                case Status.NONE:
                    strStatus = "None";
                    break;
                case Status.RUNNING:
                    strStatus = "Running";
                    break;
                case Status.PAUSE:
                    strStatus = "Paused";
                    break;
                case Status.COMPLETED:
                    strStatus = "Completed";
                    break;
                case Status.CANCELLED:
                    strStatus = "Cancelled";
                    break;
                default:
                    strStatus = "Unknown";
                    break;
            }
            return strStatus;
        }

        @Override
        public String toString() {
            return super.toString();
        }

        public interface Status {
            int NONE = 0;
            int RUNNING = 1;
            int PAUSE = 2;
            int CANCELLED = 3;
            int COMPLETED = 4;
        }
    }
}
