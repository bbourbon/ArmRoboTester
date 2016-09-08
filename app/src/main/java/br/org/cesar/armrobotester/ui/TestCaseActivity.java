package br.org.cesar.armrobotester.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.fragments.TestCaseFragment;

/**
 * Created by bcb on 28/08/16.
 */

public class TestCaseActivity extends AppCompatActivity {

    public static final String ACTION_NEW_TEST = "br.org.cesar.armrobotester.action_new_test";
    public static final String ACTION_EDIT_TEST = "br.org.cesar.armrobotester.action_edit_test";
    private static final String TAG_TEST_CASE_FRAG = "test_case_fragment";
    private TestCaseFragment mTestCaseFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Test Case");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button buttonAction = (Button) findViewById(R.id.button_action);
        buttonAction.setVisibility(View.GONE);
        buttonAction.setText("Add Test");
        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionButtonClicked();
            }
        });

        Button buttonNext = (Button) findViewById(R.id.button_next);
        buttonNext.setVisibility(View.GONE);
        buttonNext.setText("Cancel");
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextButtonClicked();
            }
        });
    }

    @Override
    protected void onResume() {
        this.mTestCaseFragment = new TestCaseFragment();
        mTestCaseFragment.setOnTestCaseListener(new TestCaseFragment.OnTestCaseListener() {
            @Override
            public void onCancel() {
                TestCaseActivity.this.finish();
            }

            @Override
            public void onAdd() {
                // TODO: ???
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_placement, mTestCaseFragment, TAG_TEST_CASE_FRAG)
                .commit();

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_case, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_clean:
                onCleanTestCase();
                break;
            case R.id.action_remove:
                onRemoveTestCase();
                break;
            default:
                // Nothing
        }

        return super.onOptionsItemSelected(item);
    }

    private void onRemoveTestCase() {
        Toast.makeText(this, "Test Case removed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostResume() {
        String action = getIntent().getAction();
        if (ACTION_NEW_TEST.equals(action)) {
            Toast.makeText(this, "Create a new Test Case", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Loading Test Case", Toast.LENGTH_SHORT).show();
        }
        super.onPostResume();
    }

    private void onCleanTestCase() {
        Toast.makeText(this, "Test Case cleaned", Toast.LENGTH_SHORT).show();
    }

    private void onNextButtonClicked() {
        this.onBackPressed();
    }

    private void onActionButtonClicked() {
        mTestCaseFragment.addTestCase();
        Toast.makeText(this, "Test Case added", Toast.LENGTH_SHORT).show();
    }

}
