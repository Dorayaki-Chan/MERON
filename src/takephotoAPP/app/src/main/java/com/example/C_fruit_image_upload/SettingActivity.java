package com.example.C_fruit_image_upload;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {


    private TextView textView;
    private EditText editText;
    private File file;
    private global_variable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Context context = getApplicationContext();

        String fileName = "TestFile.txt";
        file = new File(context.getFilesDir(), fileName);

        textView = findViewById(R.id.text_view);
        editText = findViewById(R.id.edit_text);
        Button buttonSave = findViewById(R.id.button_save);

        // lambda
        buttonSave.setOnClickListener( v -> {
            // エディットテキストのテキストを取得
            String text = editText.getText().toString();

            saveFile(text);
            if(text.length() == 0){
                textView.setText("no_text");
            }
            else{
                textView.setText("saved");
            }
        });

        Button buttonRead = findViewById(R.id.button_read);
        // lambda
        buttonRead.setOnClickListener( v -> {
            String str = readFile(context);
            if (str != null) {
                textView.setText(str);
            } else {
                textView.setText("read_error");
            }
        });

        Button returnButton = findViewById(R.id.return_button);
        // lambda
        returnButton.setOnClickListener( v -> {
            gv = new global_variable();
            gv.ip_address = readFile(context);
            finish();
        });

    }

    // ファイルを保存
    public void saveFile(String str) {
        // try-with-resources
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(str);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ファイルを読み出し
    public String readFile(Context context) {
        String text = null;

        String fileName = "TestFile.txt";
        file = new File(context.getFilesDir(), fileName);

        // try-with-resources
        try(
                BufferedReader br = new BufferedReader(new FileReader(file))
        ){
            text = br.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

}
