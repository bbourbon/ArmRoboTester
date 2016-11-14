package br.org.cesar.armrobotester.content;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.database.DataSetObservable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.org.cesar.armrobotester.model.TestCase;

/**
 * Created by bcb on 14/08/16.
 */
public class TestManager extends DataSetObservable {

    private static TestManager sInstance;
    private List<TestSuite> mTestSuiteList;
    private BluetoothDevice mTargetDevice;
    private Context mContext;
    private TestSuite mPreferedSuite;

    private TestManager(Context context) {
        mContext = context;
        mTestSuiteList = new ArrayList<>();
        mPreferedSuite = null;
    }

    public static TestManager getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new TestManager(context);
        }
        return sInstance;
    }

    public TestSuite createTestSuite(String name) {
        TestSuite suite = new TestSuite(name);
        mPreferedSuite = suite;
        mTestSuiteList.add(suite);
        return suite;
    }

    public void addTest(TestCase testCase) {
        addTest(mPreferedSuite, testCase);
    }

    public void addTest(TestSuite suite, TestCase testCase) {
        if (suite != null && suite.addTest(testCase)) {
            notifyChanged();
        }
    }

    public void removeTest(TestSuite suite, TestCase testCase) {
        if (suite != null && suite.removeTest(testCase)) {
            notifyChanged();
        }
    }

    public void removeTest(TestCase testCase) {
        for (TestSuite suite : mTestSuiteList) {
            List<TestCase> testList = suite.getTests();
            for (TestCase testObject : testList ) {
                if (testObject.equals(testCase)) {
                    this.removeTest(suite, testCase);
                }
            }
        }
    }

    public TestCase getTestCase(TestSuite suite, int index) {
        return suite != null ? suite.getTest(index) : null;
    }

    public void executeTest(TestCase testCase) {
        //TODO: Start test intent service
        notifyChanged();
    }

    public BluetoothDevice getTargetDevice () {
        return mTargetDevice;
    }

    public void setTargetDevice (@NonNull BluetoothDevice device) {
        mTargetDevice = device;
    }

    public void setPreferedTestSuite(TestSuite suite) {
        mPreferedSuite = suite;
    }

    // TODO - Implement it properly
    public List<TestCase> getTestSuite() {
        return null;
    }

    public class TestSuite {
        private List<TestCase> mListTestCases;
        private String mTestSuiteName;

        public TestSuite(String name) {
            mTestSuiteName = name;
            mListTestCases = new ArrayList<>();
        }

        public String getName() {
            return mTestSuiteName;
        }

        public boolean addTest(TestCase test) {
            return mListTestCases.add(test);
        }

        public TestCase getTest(int index) {
            return index >= 0 && index < mListTestCases.size() ? mListTestCases.get(index) : null;
        }

        public boolean removeTest(TestCase test) {
            return mListTestCases.remove(test);
        }

        public final List<TestCase> getTests() {
            return new ArrayList<>(mListTestCases);
        }
    }
}
