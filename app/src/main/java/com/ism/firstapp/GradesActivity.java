package com.ism.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ism.infrastructure.GradesBaseAdapter;
import com.ism.logic.GradeModel;

import java.util.ArrayList;
import java.util.List;

public class GradesActivity extends AppCompatActivity {

    // Declarate components
    LinearLayout mainLayout;
    Button doneBTN;
    ListView gradesLV;

    // Data from previous intent
    List<GradeModel> grades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialize();
        onClickDoneButton();
        createGrades();
        fillGradesLV();
    }

    // Prevent before back by button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            new Toast(GradesActivity.this).makeText(GradesActivity.this, R.string.mainErrorGradesToast, Toast.LENGTH_SHORT).show();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    // Initialize components and variables
    private void initialize() {
        mainLayout = findViewById(R.id.grades_mainLayout);
        doneBTN = findViewById(R.id.grades_doneBTN);
        gradesLV = findViewById(R.id.grades_gradesLV);
        grades = new ArrayList<>();
    }

    // On click doneBTN action
    private void onClickDoneButton() {
        doneBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllGradesChoosed()) {
                    finishActivity();
                } else {
                    new Toast(GradesActivity.this).makeText(GradesActivity.this, R.string.gradesNotChoosedGradesToast, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // crete grades
    private void createGrades() {
        int amount = getIntent().getIntExtra("gradesAmount", 1);
        for (int i = 1; i <= amount; i++) {
            grades.add(new GradeModel(i));
        }
    }

    // set adapter to grades listView
    private void fillGradesLV() {
        GradesBaseAdapter adapter = new GradesBaseAdapter(this, grades);
        gradesLV.setAdapter(adapter);
    }

    // send data and come to main Activity
    private void finishActivity() {
        Bundle data = new Bundle();
        data.putDouble("average", countAverage());
        Intent intent = getIntent();
        intent.putExtras(data);
        setResult(RESULT_OK, intent);
        finish();
    }

    // count average from grades
    private double countAverage() {
        int sum = 0;
        for (GradeModel item : grades) {
            sum += item.getValue();
        }
        return (double) sum / grades.size();
    }

    // check is all grades was choosed
    private boolean isAllGradesChoosed() {
        for (GradeModel item : grades) {
            if (item.getValue() == 0) return false;
        }
        return true;
    }
}
