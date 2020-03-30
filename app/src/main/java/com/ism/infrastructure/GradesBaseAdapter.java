package com.ism.infrastructure;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ism.firstapp.R;
import com.ism.logic.GradeModel;

import java.util.List;

public class GradesBaseAdapter extends BaseAdapter {

    Activity activity;
    List<GradeModel> grades;

    public GradesBaseAdapter(Activity activity, List<GradeModel> grades) {
        this.activity = activity;
        this.grades = grades;
    }

    @Override
    public int getCount() {
        return grades.size();
    }

    @Override
    public Object getItem(int position) {
        return grades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            // inflate new grade group
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.grade_group, null);

            RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.gradeRadioGroup);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // set value to corresponding GradeModel
                    RadioButton radioButton = group.findViewById(checkedId);
                    String gradeValue = radioButton.getText().toString();
                    GradeModel grade = (GradeModel) getItem(position);
                    grade.setValue(Integer.valueOf(gradeValue));
                }
            });
            // set grade group label text
            TextView radioGroupET = view.findViewById(R.id.gradeLabelTV);
            radioGroupET.setTextColor(Color.RED);
            GradeModel grade = (GradeModel) getItem(position);
            radioGroupET.setText("ocena " + (position + 1));

        } else {
            view = convertView;
        }
        return view;
    }


}
