package com.mcmah113.mcmah113expensesiq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {
    //array holds the different languages the user can select
    private static final String languageArray[] = GlobalConstants.getLanguageArray();

    //array holds the currency name and its symbol
    private static final String currencyArray[] = GlobalConstants.getCurrencyArray();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //set toolbar properties
        final Toolbar toolbarCustom = findViewById(R.id.toolbarCustom);
        setSupportActionBar(toolbarCustom);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //set edit text properties
        final EditText editTextBankAccount = findViewById(R.id.editTextBankBalance);
        final EditText editTextCash = findViewById(R.id.editTextCash);
        final EditText editTextUsername = findViewById(R.id.editTextAccountName);
        final EditText editTextPassword = findViewById(R.id.editTextPassword);
        final EditText editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);

        //set users username and password they may have typed in the login activity
        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            editTextUsername.setText(bundle.getString("username").trim());
            editTextPassword.setText(bundle.getString("password"));
        }

        //set spinner properties
        final ArrayAdapter<String> arrayAdapterLanguage = new ArrayAdapter<>(this, R.layout.layout_spinner, languageArray);

        final Spinner spinnerLanguage = findViewById(R.id.spinnerLanguage);
        spinnerLanguage.setAdapter(arrayAdapterLanguage);

        final ArrayAdapter<String> arrayAdapterCurrency = new ArrayAdapter<>(this, R.layout.layout_spinner, currencyArray);

        final Spinner spinnerCurrency = findViewById(R.id.spinnerType);
        spinnerCurrency.setAdapter(arrayAdapterCurrency);

        //set sign up button properties
        final CustomOnTouchListener onTouchListener = new CustomOnTouchListener(getResources().getColor(R.color.colorPrimaryDark, getTheme()));

        final Button buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnTouchListener(onTouchListener);//ignore this warning...
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String username = editTextUsername.getText().toString().trim();
                final String password = editTextPassword.getText().toString();
                final String passwordConfirm = editTextPasswordConfirm.getText().toString();

                if(username.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                    //missing required form fields
                    Toast.makeText(SignUp.this, getResources().getString(R.string.Toast_Required), Toast.LENGTH_SHORT).show();
                }
                else {
                    //all form fields are present
                    if(!password.equals(passwordConfirm)) {
                        //passwords don't line up
                        Toast.makeText(SignUp.this, getResources().getString(R.string.passwords_no_match), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //all required form fields are filled out, check if valid numbers
                        String language = spinnerLanguage.getSelectedItem().toString();
                        String locale = spinnerCurrency.getSelectedItem().toString();

                        //only want the code inside the brackets
                        locale = locale.substring(locale.indexOf('(') + 1, locale.indexOf(')'));
                        language = language.substring(language.indexOf('(') + 1, language.indexOf(')'));

                        final String bankString = editTextBankAccount.getText().toString();
                        final String cashString = editTextCash.getText().toString();

                        if(bankString.isEmpty()) {
                            if(cashString.isEmpty()) {
                                //no optional fields filled out
                                createNewUser(username,password,locale,language,"0","0");
                            }
                            else {
                                //cashAccount is filled out, but not bankAccount
                                createNewUser(username,password,locale,language,"0",cashString);
                            }
                        }
                        else {
                            if(cashString.isEmpty()) {
                                //bankAccount is filled out, but not cashAccount
                                createNewUser(username,password,locale,language,bankString,"0");
                            }
                            else {
                                //both bankAccount and cashAccount is filled out
                                createNewUser(username,password,locale,language,bankString,cashString);
                            }
                        }
                    }
                }
            }
        });
    }

    public void createNewUser(String username, String password,
                                 String locale, String language,
                                 String bankString, String cashString) {

        //database SQL helper object
        final DatabaseHelper databaseHelper = new DatabaseHelper(this);

        try {
            //check to see if valid numbers
            double bankAccount = Double.parseDouble(bankString);
            double cashAccount = Double.parseDouble(cashString);

            if(databaseHelper.userSignUp(username, password, locale, language, bankAccount, cashAccount)) {
                //valid sign up, return to login activity
                Toast.makeText(this, getResources().getString(R.string.signup_good), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, Login.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                startActivity(intent);
            } else {
                //username is already being used or something else failed
                Toast.makeText(this, getResources().getString(R.string.uname_taken), Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception exception) {
            //invalid bank account or cash account input
            Toast.makeText(this, getResources().getString(R.string.toast_invalid_money), Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {
        if(this.getFragmentManager().getBackStackEntryCount() > 1) {
            //hide the keyboard if its showing since it counts as a fragment surprisingly
            this.getFragmentManager().popBackStack();
        }
        else {
            //keyboard is not showing, go back to the previous activity (login)
            NavUtils.navigateUpFromSameTask(this);
        }
    }
}