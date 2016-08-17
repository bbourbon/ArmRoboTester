package br.org.cesar.armrobotester.model;

import java.util.List;

/**
 * Created by bcb on 10/08/16.
 */
public class TestCase {
    private List<Motion> mMotionList;
    private String mName;
    private int mId;


    public List<Motion> getMotionTestList() {
        return mMotionList;
    }

    public void setMotionList(List<Motion> motionList) {
        this.mMotionList = motionList;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }
}
