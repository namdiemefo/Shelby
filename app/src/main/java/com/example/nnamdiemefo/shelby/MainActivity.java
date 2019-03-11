package com.example.nnamdiemefo.shelby;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private final String DEVICE_ADDRESS = "";
    private final UUID PORT_UUID = UUID.fromString("");
    private TextToSpeech textToSpeech;
    private SpeechRecognizer speechRecognizer;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private String command;
    private Button forwardButton, leftButton, rightButton, reverseButton, bluetoothButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forwardButton = findViewById(R.id.forwardbutton);
        leftButton = findViewById(R.id.leftbutton);
        rightButton = findViewById(R.id.rightbutton);
        reverseButton = findViewById(R.id.reversebutton);
        bluetoothButton = findViewById(R.id.bluetoothbutton);

        forwardButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //when you hold a button down
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    command = "1";

                    try {
                        //transmits the value of the command to the bluetooth module
                        outputStream.write(command.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // when you release the button
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    command = "10";

                    try {
                        outputStream.write(command.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        reverseButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    command = "2";

                    try {
                        outputStream.write(command.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }  else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    command = "10";

                    try {
                        outputStream.write(command.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    command = "3";

                    try {
                        outputStream.write(command.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    command = "10";

                    try {
                        outputStream.write(command.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    command = "4";

                    try {
                        outputStream.write(command.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    command = "10";
                    try {
                        outputStream.write(command.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BTinit()) {
                    BTconnect();
                }
            }
        });



        ImageView microphone = findViewById(R.id.btnSpeak);
        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 4);
                speechRecognizer.startListening(intent);
            }
        });

        startRobotTalk();
        initializeSpeechRecognizer();
    }

    //initialize bluetooth
    private boolean BTinit() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //checks if bluetooth adapter is enabled
        if (bluetoothAdapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        }
    }


    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    List<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    processResult(results.get(0));
                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        }
    }

    //what to do with the result commands
    private void processResult(String command) {
        command = command.toLowerCase();
        //Move Forward
        if (command.indexOf("Forward") != -1) {
            speak("Moving Forward");
        }
        //Move Left
        if (command.indexOf("Left") != -1) {
            speak("Moving Left");
        }
        //Move Right
        if (command.indexOf("Right") != -1) {
            speak("Moving Right");
        }
        //Reverse
        if (command.indexOf("Reverse") != -1) {
            speak("Reversing");
        }
    }

    //make the app speak upon entry
    private void startRobotTalk() {
        textToSpeech =  new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (textToSpeech.getEngines().size() == 0) {
                    Toast.makeText(MainActivity.this, "No TextToSpeech Engine on your device", Toast.LENGTH_SHORT);
                    finish();
                } else {
                    textToSpeech.setLanguage(Locale.US);
                    speak("Hello My Name is Shelby, what direction should i move to");
                }
            }
        });
    }

    //shuts down app speech when the app is paused
    @Override
    protected void onPause() {
        super.onPause();
        textToSpeech.shutdown();
    }

    //enables app to speak
    private void speak(String message) {
        if (Build.VERSION.SDK_INT >= 21) {
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    //asks the user for speech and sends it to the speech converter and sends the output back as an intent
    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your device doesnt support this", Toast.LENGTH_SHORT).show();
        }
    }

    //what to do with the result 1.e the converted text from speech
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data !=  null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                }
        }
    }
}





    
