package com.example.simplecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTv, solutionTv;
    MaterialButton buttonC, buttonOpenBracket, buttonCloseBracket;
    MaterialButton buttonDivide, buttonMultiply, buttonAdd, buttonSubstract, buttonEquals;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonAC, buttonDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);

        assignId(buttonC, R.id.button_c);
        assignId(buttonOpenBracket, R.id.button_open_bracket);
        assignId(buttonCloseBracket, R.id.button_close_bracket);
        assignId(buttonDivide, R.id.button_divide);
        assignId(buttonMultiply, R.id.button_multiply);
        assignId(buttonAdd, R.id.button_add);
        assignId(buttonSubstract, R.id.button_substract);
        assignId(buttonEquals, R.id.button_equals);
        assignId(button0, R.id.button_0);
        assignId(button1, R.id.button_1);
        assignId(button2, R.id.button_2);
        assignId(button3, R.id.button_3);
        assignId(button4, R.id.button_4);
        assignId(button5, R.id.button_5);
        assignId(button6, R.id.button_6);
        assignId(button7, R.id.button_7);
        assignId(button8, R.id.button_8);
        assignId(button9, R.id.button_9);
        assignId(buttonAC, R.id.button_all_clear);
        assignId(buttonDot, R.id.button_dot);
    }

    void assignId(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    boolean checkFloatable(String data) {
        if(data.endsWith("+") || data.endsWith("-") || data.endsWith("*") || data.endsWith("/")) {
            data = data + "0";
        }

        String[] numbers = data.split("[+\\-*/]");

        return !numbers[numbers.length-1].contains(".");
    }

    boolean checkOperatorButton(String data) {
        if(data.length() == 0) return false;
        if(data.endsWith("+") || data.endsWith("-") || data.endsWith("*") || data.endsWith("/")) return false;
        return Character.isDigit(data.charAt(data.length() - 1));
    }

    @Override
    public void onClick(View v) {
        MaterialButton button = (MaterialButton) v;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTv.getText().toString();
        System.out.println(dataToCalculate.length());
        if (buttonText.equals("AC")) {
            solutionTv.setText("");
            resultTv.setText("0");
            return;
        }

        if (buttonText.equals("=")) {
            solutionTv.setText(resultTv.getText());
            return;
        }

        if (buttonText.equals("DEL") && (dataToCalculate.length() <= 1)) {
            solutionTv.setText("");
            resultTv.setText("0");
            return;
        } else if (buttonText.equals("DEL")) {
            dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
        } else if(buttonText.equals("0") && dataToCalculate.length() == 0) { // Prevent zero in front
            return;
        } else if(buttonText.equals(".") && !checkFloatable(dataToCalculate)) { // Check if there's already comma
            return;
        } else if((buttonText.equals("+") || buttonText.equals("-") || buttonText.equals("*") || buttonText.equals("/")) && !checkOperatorButton(dataToCalculate)) {
            return;
        } else {
            dataToCalculate = dataToCalculate + buttonText;
        }

        solutionTv.setText(dataToCalculate);

        String finalResult = getResult(dataToCalculate);

        if (!finalResult.equals("Error")) {
            resultTv.setText(finalResult);
        }

        System.out.println(dataToCalculate + " " + dataToCalculate.length());
    }

    String getResult(String data) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null).toString();
            if (finalResult.endsWith(".0")) {
                finalResult = finalResult.replace(".0", "");
            }

            return finalResult;

        } catch (Exception e) {
            return "Error";
        }
    }
}