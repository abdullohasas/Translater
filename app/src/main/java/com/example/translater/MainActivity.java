package com.example.translater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class MainActivity extends AppCompatActivity {

    private Spinner fromSpinner, toSpinner;
    private EditText sourceEdit;
    private Button button;
    private TextView translatedTV;

    // Source Array of Strings - Spinner's Data
    String[] fromLanguage = {
            "from", "English", "Arabic", "Russian", "Japanse", "Hindi", "Spanish", "Urdu", "Turkey"
    };
    String[] toLanguage = {
            "to", "English", "Arabic", "Russian", "Hindi", "Japanse", "Spanish", "Urdu", "Turkey"
    };


    // Permission
    private static final int REQUEST_CODE = 1;
    String languageCode, fromLanguageCode, toLanguageCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromSpinner = findViewById(R.id.idFromSpinner);
        toSpinner = findViewById(R.id.idToSpinner);
        sourceEdit = findViewById(R.id.idEditText);
        button = findViewById(R.id.button_translate);
        translatedTV = findViewById(R.id.idTranslated);

        // 1
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromLanguageCode = getLanguageCode(fromLanguage[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // spinner da chiqadigan  variantlar uchun
        ArrayAdapter fromAdapter = new ArrayAdapter(this, R.layout.spinner_item, fromLanguage);

        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);


        // 2
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLanguageCode = getLanguageCode(toLanguage[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter toAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, toLanguage);

        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);


        // 3
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translatedTV.setText("");
                if (sourceEdit.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter your text !", Toast.LENGTH_SHORT).show();
                } else if (fromLanguageCode.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter Language... ", Toast.LENGTH_SHORT).show();
                } else if (toLanguageCode.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter Language", Toast.LENGTH_SHORT).show();
                } else {
                    TranslateText(fromLanguageCode, toLanguageCode, sourceEdit.getText().toString());
                }
            }
        });
    }

    private void TranslateText(String fromLanguageCode, String toLanguageCode, String src) {

        translatedTV.setText("Please wait.! Downloading Modal...");


        try {
            TranslatorOptions options = new TranslatorOptions.Builder()
                    .setSourceLanguage(fromLanguageCode)//Tarjima qilinadigan til
                    .setTargetLanguage(toLanguageCode)// Tarjima qilingan til
                    .build();                        // yaratish()

//
            Translator translator = Translation.getClient(options);

            DownloadConditions conditions = new DownloadConditions
                    .Builder()
                    .build();

            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    translatedTV.setText("Translating...");

                    translator.translate(src).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            translatedTV.setText(s);
                        }
                    })

                       .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to translate..!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            })


                .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Failed to download the language...", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // 1
    private String getLanguageCode(String language) {
        String languageCode;

        switch (language) {
            case "English":
                languageCode = TranslateLanguage.ENGLISH;
                break;
            case "Spanish":
                languageCode = TranslateLanguage.SPANISH;
                break;
            case "Japanse":
                languageCode = TranslateLanguage.JAPANESE;
                break;
            case "Russian":
                languageCode = TranslateLanguage.RUSSIAN;
                break;
            case "Turkey":
                languageCode = TranslateLanguage.TURKISH;
                break;
            case "Arabic":
                languageCode = TranslateLanguage.ARABIC;
                break;
            case "Hindi":
                languageCode = TranslateLanguage.HINDI;
                break;
            case "Urdu":
                languageCode = TranslateLanguage.URDU;
                break;
            default:
                languageCode = "";
        }
        return languageCode;

    }

}