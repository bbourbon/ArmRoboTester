package br.org.cesar.armrobotester.content;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.org.cesar.armrobotester.model.TestCase;

/**
 * Created by bcb on 14/08/16.
 */
public class TestManager {

    private static TestManager sInstance;
    private TestSuite mTestSuite;
    private Context mContext;

    private TestManager(Context context) {
        mTestSuite = new TestSuite();
        mContext = context;
    }

    public static TestManager getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new TestManager(context);
        }
        return sInstance;
    }

    public void addTest(TestCase testCase) {
        mTestSuite.listTestCases.add(testCase);
    }

    public void removeTest(TestCase testCase) {
        mTestSuite.listTestCases.remove(testCase);
    }

    public TestCase getTestCase(int index) {
        return mTestSuite.listTestCases.get(index);
    }

    public void executeTest(TestCase testCase) {
        //TODO: Start test intent service
    }

    public final List<TestCase> getTests() {
        List<TestCase> tests = null;
        Object obj = mTestSuite.listTestCases.clone();
        if (obj instanceof List<?>) {
            tests = (List<TestCase>) obj;
        }

        return tests;
    }

    public class TestSuite {
        public ArrayList<TestCase> listTestCases;

        public TestSuite() {
            listTestCases = new ArrayList<>();
        }
    }
}
