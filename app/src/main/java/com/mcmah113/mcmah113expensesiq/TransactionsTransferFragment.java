package com.mcmah113.mcmah113expensesiq;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class TransactionsTransferFragment extends Fragment {
    private double exchangeRate;

    private TextView textViewRate;
    private TextView textViewRefresh;

    public TransactionsTransferFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transactions_transfer, container, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        final DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

        final int userId = Overview.getUserId();
        final int accountId = getArguments().getInt("accountId");

        final Account[] accountsList = databaseHelper.getAccountList(userId);
        final String spinnerString[] = new String[accountsList.length];

        //creating array that will be used for the spinners
        for(int i = 0; i < accountsList.length; i ++) {
            spinnerString[i] = accountsList[i].getName() + " (" + accountsList[i].getLocale() + ")";
        }

        //setting up TextView properties
        textViewRate = view.findViewById(R.id.textViewExchangeRate);
        textViewRefresh = view.findViewById(R.id.textViewRefreshDate);

        //setting up the editText properties
        final EditText editTextAmount = view.findViewById(R.id.editTextAmount);
        final EditText editTextNote = view.findViewById(R.id.editTextNote);
        final EditText editTextDate = view.findViewById(R.id.editTextDate);

        final Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") final String today = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

        editTextDate.setText(today);

        //setting the spinners properties
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.layout_spinner, spinnerString);

        final Spinner spinnerAccountFrom = view.findViewById(R.id.spinnerFromAccount);
        final Spinner spinnerAccountTo = view.findViewById(R.id.spinnerToAccount);

        spinnerAccountFrom.setAdapter(arrayAdapter);
        spinnerAccountFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerAccountFrom.getSelectedItem() != null && spinnerAccountTo.getSelectedItem() != null) {
                    updateExchangeRate(spinnerAccountFrom.getSelectedItem().toString(),
                                       spinnerAccountTo.getSelectedItem().toString());
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerAccountTo.setAdapter(arrayAdapter);
        spinnerAccountTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerAccountFrom.getSelectedItem() != null && spinnerAccountTo.getSelectedItem() != null) {
                    updateExchangeRate(spinnerAccountFrom.getSelectedItem().toString(),
                                       spinnerAccountTo.getSelectedItem().toString());
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //setting the default selection as the accountId passed in
        //accountsList and spinnerString[] have the same indexes
        for(int i = 0; i < accountsList.length; i ++) {
            if(accountId == accountsList[i].getId()) {
                spinnerAccountFrom.setSelection(i);
                spinnerAccountTo.setSelection(i);
                break;
            }
        }

        //make the default call to get the currency conversion
        if(spinnerAccountFrom.getSelectedItem() != null && spinnerAccountTo.getSelectedItem() != null) {
            updateExchangeRate(spinnerAccountFrom.getSelectedItem().toString(),
                               spinnerAccountTo.getSelectedItem().toString());
        }

        //set sign up button properties
        final CustomOnTouchListener onTouchListener = new CustomOnTouchListener(getResources().getColor(R.color.colorPrimaryDark, getContext().getTheme()));

        final Button buttonTransaction = view.findViewById(R.id.buttonTransaction);
        buttonTransaction.setOnTouchListener(onTouchListener);//ignore this warning...
        buttonTransaction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //find out which accounts were selected and get their Ids
                final String dateString = editTextDate.getText().toString();

                int accountFromId = -1;
                int accountToId = -1;

                for(int i = 0; i < spinnerString.length; i ++) {
                    if(spinnerString[i].equals(spinnerAccountFrom.getSelectedItem())) {
                        accountFromId = accountsList[i].getId();
                        break;
                    }
                }

                for(int i = 0; i < spinnerString.length; i ++) {
                    if(spinnerString[i].equals(spinnerAccountTo.getSelectedItem())) {
                        accountToId = accountsList[i].getId();
                        break;
                    }
                }

                if(accountFromId > 0 && accountToId > 0) {
                    if(!spinnerAccountFrom.getSelectedItem().toString().equals(spinnerAccountTo.getSelectedItem().toString())) {
                        //2 different accounts selected, good
                        if(!editTextAmount.getText().toString().isEmpty() && !dateString.isEmpty()) {
                            try {
                                //check to see if the inputted date is valid, will throw exception on illegal date
                                final Calendar calendar = Calendar.getInstance();
                                @SuppressLint("SimpleDateFormat") final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                simpleDateFormat.setLenient(false);

                                calendar.setTime(simpleDateFormat.parse(dateString));

                                //amount is filled out, try and convert it
                                try {
                                    double amount = Double.parseDouble(editTextAmount.getText().toString());

                                    final Bundle args = new Bundle();
                                    args.putInt("accountFromId", accountFromId);
                                    args.putInt("accountToId", accountToId);
                                    args.putDouble("amount", amount);
                                    args.putDouble("exchangeRate", exchangeRate);
                                    args.putString("note", editTextNote.getText().toString());
                                    args.putString("date", dateString);

                                    hideKeyboard(buttonTransaction);

                                    TransactionsTransferDialogFragment dialogFragment = new TransactionsTransferDialogFragment();
                                    dialogFragment.setArguments(args);
                                    dialogFragment.show(getFragmentManager(), "Account Dialog");
                                }
                                catch(Exception exception) {
                                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_invalid_money), Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (Exception exception) {
                                Toast.makeText(getContext(), getContext().getResources().getString(R.string.invalid_date), Toast.LENGTH_SHORT).show();

                            }
                        }
                        else {
                            Toast.makeText(getContext(), getContext().getResources().getString(R.string.Toast_Required), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        //same account selected, don;t let them transfer funds
                        Toast.makeText(getContext(), getContext().getResources().getString(R.string.same_account_fail), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.Toast_Required), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("DefaultLocale")
    public void updateExchangeRate(String spinnerFrom, String spinnerTo) {
        if(!spinnerFrom.isEmpty() && !spinnerTo.isEmpty()) {
            //only want the locale for the two bank accounts
            String localeFrom = spinnerFrom.substring(spinnerFrom.indexOf('(') +1,spinnerFrom.indexOf(')'));
            String localeTo = spinnerTo.substring(spinnerTo.indexOf('(') +1,spinnerTo.indexOf(')'));
            String refreshDate;
            String rateString;

            if(!GlobalConstants.getCurrencyExchangeFallBack()) {
                //make the API request to get the information
                HashMap<String, String> dataAPI = GlobalConstants.getHashMapExchangeRates();

                //rates are in EUR, common base, division to find
                //cross currency ratio
                final double currency1 = Double.parseDouble(dataAPI.get(localeFrom));
                final double currency2 = Double.parseDouble(dataAPI.get(localeTo));
                exchangeRate = currency1 / currency2;

                refreshDate = "Refreshed on " + dataAPI.get("Date");
                rateString = String.format("%.2f", exchangeRate) + " " + localeFrom + " = 1.00 " + localeTo;
            }
            else {
                exchangeRate = 1.00;
                rateString = getContext().getResources().getString(R.string.fixer_io_unavailable);
                refreshDate = getContext().getResources().getString(R.string.unavailable_fail);
            }

            textViewRate.setText(rateString);
            textViewRefresh.setText(refreshDate);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if(inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}