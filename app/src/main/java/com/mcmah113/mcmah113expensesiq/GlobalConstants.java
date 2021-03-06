package com.mcmah113.mcmah113expensesiq;

import android.graphics.Color;

import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

class GlobalConstants {
    private static final String[] monthsArray = new String[]{
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    };

    static String[] getMonthsArray() {
        return monthsArray;
    }

    private static final int colorPalette[] = {
        Color.rgb(245, 199, 0),
        Color.rgb(106, 150, 31),  Color.rgb(179, 100, 53),
        Color.rgb(64, 89, 128),   Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),
        Color.rgb(191, 134, 134), Color.rgb(179, 48, 80),
        Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
        Color.rgb(140, 234, 255), Color.rgb(255, 140, 157),
        Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187),
        Color.rgb(118, 174, 175), Color.rgb(42, 109, 130),
        Color.rgb(217, 80, 138),  Color.rgb(254, 149, 7),   Color.rgb(254, 247, 120),
        Color.rgb(106, 167, 134), Color.rgb(53, 194, 209),Color.rgb(193, 37, 82),Color.rgb(255, 102, 0)
    };

    static int[] getColorPalette() {
        return colorPalette;
    }

    private static final String transactionTypeList[] = {
        "Accommodation",
        "Automobile",
        "Child Support",
        "Credit Cards",
        "Donation",
        "Entertainment",
        "Food",
        "Given Gifts",
        "Received Gifts",
        "Groceries",
        "Household",
        "Investment",
        "Medicare",
        "Personal Care",
        "Pets",
        "Self Improvement",
        "Shopping",
        "Sports and Recreation",
        "Tax",
        "Transportation",
        "Utilities",
        "Vacation",
        "Other"
    };

    private static final String localeArray[] = {
        "" + Currency.getInstance(Locale.US),
        "" + Currency.getInstance(Locale.CANADA),
        "" + Currency.getInstance(Locale.JAPAN),
        "" + Currency.getInstance(Locale.UK),
        "" + Currency.getInstance(Locale.FRANCE),
        "" + Currency.getInstance(Locale.CHINA)
    };

    static String[] getLocaleArray() {
        return localeArray;
    }

    private static HashMap<String, String> hashMapExchangeRates;

    private static boolean currencyExchangeFallBack = false;

    static void setHashMapExchangeRates(HashMap<String, String> hashMap) {
        hashMapExchangeRates = hashMap;
    }

    static HashMap<String, String> getHashMapExchangeRates() {
        return hashMapExchangeRates;
    }

    static boolean getCurrencyExchangeFallBack() {
        return currencyExchangeFallBack;
    }

    static void setCurrencyExchangeFallBack(boolean flag) {
        currencyExchangeFallBack = flag;
    }

    private static final String currencyArray[] = {
        Locale.US.getDisplayCountry() + " (" + Currency.getInstance(Locale.US) + ")",
        Locale.CANADA.getDisplayCountry() + " (" + Currency.getInstance(Locale.CANADA) + ")",
        Locale.UK.getDisplayCountry() + " (" + Currency.getInstance(Locale.UK) + ")",
        Locale.FRANCE.getDisplayCountry() + " (" + Currency.getInstance(Locale.FRANCE) + ")",
        Locale.CHINA.getDisplayCountry() + " (" + Currency.getInstance(Locale.CHINA) + ")",
        Locale.JAPAN.getDisplayCountry() + " (" + Currency.getInstance(Locale.JAPAN) + ")"
    };

    private static final String languageArray[] = {
        Locale.ENGLISH.getDisplayLanguage() + " (" + Locale.ENGLISH.getLanguage() + ")",
        Locale.FRENCH.getDisplayLanguage() + " (" + Locale.FRENCH.getLanguage() + ")",
        Locale.JAPANESE.getDisplayLanguage() + " (" + Locale.JAPANESE.getLanguage() + ")"
    };

    private static final String typesArray[] = {"Bank", "Cash"};

    private static final String transactionPeriods[] = {
        "All Time",
        "This Month",
        "This Week",
        "Today"
    };

    static String[] getTransactionTypeList() {
        return transactionTypeList;
    }

    static String[] getTypesArray() {
        return typesArray;
    }

    static String[] getLanguageArray() {
        return languageArray;
    }

    static String[] getCurrencyArray() {
        return currencyArray;
    }

    static String[] getTransactionPeriods() {
        return transactionPeriods;
    }
}