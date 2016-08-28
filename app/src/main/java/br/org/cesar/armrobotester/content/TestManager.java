package br.org.cesar.armrobotester.content;

import android.content.Context;
import android.database.DataSetObservable;

import java.util.ArrayList;
import java.util.List;

import br.org.cesar.armrobotester.model.TestCase;

/**
 * Created by bcb on 14/08/16.
 */
public class TestManager extends DataSetObservable {

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
        if (mTestSuite.listTestCases.add(testCase)) {
            notifyChanged();
        }
    }

    public void removeTest(TestCase testCase) {
        if (mTestSuite.listTestCases.contains(testCase)) {
            if (mTestSuite.listTestCases.remove(testCase)) {
                notifyChanged();
            }
        }
    }

    public TestCase getTestCase(int index) {
        return mTestSuite.listTestCases.get(index);
    }

    public void executeTest(TestCase testCase) {
        //TODO: Start test intent service
        notifyChanged();
    }

    public final List<TestCase> getTestSuite() {
        List<TestCase> tests = null;
        Object obj = mTestSuite.listTestCases.clone();
        if (obj instanceof List<?>) {
            tests = (List<TestCase>) obj;
        }

        return tests;
    }


    public class TestSuite {
        public final ArrayList<TestCase> listTestCases;
        public TestSuite() {
            listTestCases = new ArrayList<>(10);
        }
    }
}
