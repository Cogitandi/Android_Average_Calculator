package com.ism.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    final int GRADES_INTENT_CODE = 500;

    // Components declaration
    LinearLayout mainLayout;
    TextView averageTv;
    EditText firstNameET;
    EditText surnameET;
    EditText gradesAmountET;
    Button submitBTN;

    // values from Input
    String firstName;
    String surname;
    int gradesAmount;

    // flags
    boolean firstNameCorrect = false;
    boolean surnameCorrect = false;
    boolean gradesAmountCorrect = false;
    boolean displayButton = false;

    double average;
    boolean backFromIntent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialize();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            average = data.getExtras().getDouble("average");
            onSuccessGradesActivity();
        } else {
            new Toast(MainActivity.this).makeText(MainActivity.this, R.string.mainErrorGradesToast, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save data to bundle
        outState.putString("firstNameET", firstNameET.getText().toString());
        outState.putString("surnameET", surnameET.getText().toString());
        outState.putString("gradesAmountET", gradesAmountET.getText().toString());
        outState.putBoolean("backFromIntent", backFromIntent);
        outState.putBoolean("displayButton", displayButton);
        outState.putDouble("average", average);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore data from bundle
        backFromIntent = savedInstanceState.getBoolean("backFromIntent");
        average = savedInstanceState.getDouble("average");
        firstNameET.setText(savedInstanceState.getString("firstNameET"));
        surnameET.setText(savedInstanceState.getString("surnameET"));
        gradesAmountET.setText(savedInstanceState.getString("gradesAmountET"));
        displayButton = savedInstanceState.getBoolean("displayButton");

        // if average was counted generate previous view
        if (displayButton) {
            firstNameCorrect = true;
            surnameCorrect = true;
            gradesAmountCorrect = true;
        }
        if (backFromIntent) onSuccessGradesActivity();
        checkInputData();
    }

    // Initialize components
    private void initialize() {
        mainLayout = findViewById(R.id.main_mainLayout);
        averageTv = findViewById(R.id.main_averageTV);
        firstNameET = findViewById(R.id.main_firstNameET);
        surnameET = findViewById(R.id.main_surnameET);
        gradesAmountET = findViewById(R.id.main_gradesAmountET);
        submitBTN = findViewById(R.id.main_submitBTN);

        // onFocus input actions
        onClickSubmitButton();
        firstNameChangeFocus();
        surnameChangeFocus();
        gradesAmountChangeFocus();

        // add event on click key enter to last input field
        gradesAmountET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    gradesAmountET.clearFocus();
                    mainLayout.requestFocus();
                }
                return false;
            }
        });
    }

    // Listeners to buttons click
    private void onClickSubmitButton() {
        if (submitBTN.hasOnClickListeners()) return;
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGradesIntent();
            }
        });
    }

    // Action associated with button after come from grades activity
    private void changeButton() {
        submitBTN.setVisibility(View.VISIBLE);
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Different toast depends on average
        if (average >= 3) {
            submitBTN.setText(R.string.mainGoodTextBTN);
        } else {
            submitBTN.setText(R.string.mainBadBTextBTN);
        }

    }

    // firstName input
    private void firstNameChangeFocus() {
        firstNameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // surnameET lost focus
                    firstName = firstNameET.getText().toString();
                    if (firstName.isEmpty()) return; // Empty input - no action

                    if (firstName.length() >= 3) {
                        // Correct firstName
                        firstNameCorrect = true;
                        checkInputData();
                    } else {
                        // incorrect firstName
                        submitBTN.setVisibility(View.GONE);
                        firstNameCorrect = false;
                        new Toast(MainActivity.this).makeText(MainActivity.this, R.string.mainIncorretFirstName, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    // Surname input
    private void surnameChangeFocus() {
        surnameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // surnameET lost focus
                    surname = surnameET.getText().toString();
                    if (surname.isEmpty()) return; // Empty input - no action

                    if (surname.length() >= 3) {
                        // Correct firstName
                        surnameCorrect = true;
                        checkInputData();
                    } else {
                        // incorrect firstName
                        submitBTN.setVisibility(View.GONE);
                        surnameCorrect = false;
                        new Toast(MainActivity.this).makeText(MainActivity.this, R.string.mainIncorretSurname, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // gradesAmount input
    private void gradesAmountChangeFocus() {
        gradesAmountET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // surnameET lost focus
                    String gradesAmountString = gradesAmountET.getText().toString();
                    if (gradesAmountString.isEmpty()) return; // Empty input - no action

                    gradesAmount = new Integer(gradesAmountET.getText().toString());
                    if (gradesAmount >= 5 && gradesAmount <= 15) {
                        // Correct grades amount value
                        gradesAmountCorrect = true;
                        checkInputData();
                    } else {
                        // Incorrect grades amount value
                        submitBTN.setVisibility(View.GONE);
                        gradesAmountCorrect = false;
                        new Toast(MainActivity.this).makeText(MainActivity.this, R.string.mainIncorretGradesAmount, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // If all input data correct show button
    private void checkInputData() {
        if (firstNameCorrect && surnameCorrect && gradesAmountCorrect) {
            submitBTN.setVisibility(View.VISIBLE);
            displayButton = true;
        } else {
            submitBTN.setVisibility(View.GONE);
            displayButton = false;
        }
    }

    // Create intent and move to gradesActivity
    private void createGradesIntent() {
        Intent intent = new Intent(MainActivity.this, GradesActivity.class);
        gradesAmount = new Integer(gradesAmountET.getText().toString());
        intent.putExtra("gradesAmount", gradesAmount);
        startActivityForResult(intent, GRADES_INTENT_CODE);
    }

    // after successfull finish grades activity
    private void onSuccessGradesActivity() {
        backFromIntent = true;
        changeButton();
        // Disable input fields
        firstNameET.setEnabled(false);
        surnameET.setEnabled(false);
        gradesAmountET.setEnabled(false);

        // show average text view
        DecimalFormat df = new DecimalFormat("0.00");
        averageTv.setText("Twoja średnia to: " + df.format(average));
        averageTv.setVisibility(View.VISIBLE);

        // show message depens on average
        if (average >= 3) {
            new Toast(MainActivity.this).makeText(MainActivity.this, R.string.mainPassToast, Toast.LENGTH_SHORT).show();
        } else {
            new Toast(MainActivity.this).makeText(MainActivity.this, R.string.mainFailureToast, Toast.LENGTH_SHORT).show();
        }


    }
}
