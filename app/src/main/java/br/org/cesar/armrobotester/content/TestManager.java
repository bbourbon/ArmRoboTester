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

    private TestManager(Context context) {
        mContext = context;
        mTestSuiteList = new ArrayList<>();
    }

    public static TestManager getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new TestManager(context);
        }
        return sInstance;
    }

    public TestSuite createTestSuite(String name) {
        TestSuite suite = new TestSuite(name);
        mTestSuiteList.add(suite);
        return suite;
    }

    public void addTest(String suiteName, TestCase testCase) {
        if (suiteName != null && suiteName.length() > 0) {
            for (TestSuite suite : mTestSuiteList) {
                if (suite.getName().equals(suiteName)) {
                    addTest(suite, testCase);
                }
            }
        }
    }

    public void addTest(TestSuite suite, TestCase testCase) {
        if (suite != null && !suite.containTest(testCase) &&
                suite.addTest(testCase)) {
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

    public TestSuite getSuiteByName(String suiteName) {
        if (suiteName != null && suiteName.length() > 0) {
            for (TestSuite suite : mTestSuiteList) {
                if (suite.getName().equals(suiteName)) {
                    return suite;
                }
            }
        }
        return null;
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

    // TODO - Implement it properly
    public List<TestSuite> getTestSuite() {
        return new ArrayList<>(mTestSuiteList);
    }

    public void removeSuite(TestSuite testSuite) {
        mTestSuiteList.remove(testSuite);
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

        public boolean containTest(TestCase testCase) {
            return mListTestCases.contains(testCase);
        }
    }
}
