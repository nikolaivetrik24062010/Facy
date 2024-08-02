package com.example.facy;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private TextView textView;
    private ImageView imageView;

    private static final int REQUEST_IMAGE_CAPTURE = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация UI компонентов
        btn = findViewById(R.id.cameraBtn);
        textView = findViewById(R.id.text1);
        imageView = findViewById(R.id.image);

        // Инициализация Firebase
        FirebaseApp.initializeApp(this);

        // Установка обработчика нажатия на кнопку
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        Toast.makeText(this, "App is started", Toast.LENGTH_SHORT).show();
    }

    // Метод для открытия камеры
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "Failed to open camera", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap bitmap = (Bitmap) bundle.get("data");
                if (bitmap != null) {
                    processFaceDetection(bitmap);
                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
        }
    }

    // Метод для обработки обнаружения лиц
    private void processFaceDetection(Bitmap bitmap) {
        textView.setText("Face Detection");
        StringBuilder stringBuilder = new StringBuilder();
        imageView.setImageBitmap(bitmap);

        InputImage image = InputImage.fromBitmap(bitmap, 0);
        FaceDetectorOptions highAccuracyOpts = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .enableTracking()
                .build();

        FaceDetector detector = FaceDetection.getClient(highAccuracyOpts);
        detector.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                    @Override
                    public void onSuccess(List<Face> faces) {
                        handleSuccess(faces, stringBuilder);
                    }
                })
                .addOnFailureListener(e -> {
                    StringBuilder builderFailure = new StringBuilder("Face Detection Failed");
                    showDetectionResult("Face Detection", builderFailure, false);
                });
    }

    // Метод для обработки успешного обнаружения лиц
    private void handleSuccess(List<Face> faces, StringBuilder stringBuilder) {
        if (!faces.isEmpty()) {
            if (faces.size() == 1) {
                stringBuilder.append(faces.size()).append(" Face detected\n\n");
            } else {
                stringBuilder.append(faces.size()).append(" Faces detected\n\n");
            }
            for (Face face : faces) {
                int id = face.getTrackingId();
                float rotY = face.getHeadEulerAngleY();
                float rotZ = face.getHeadEulerAngleZ();

                stringBuilder.append("1. Face tracking ID [").append(id).append("]\n")
                        .append("2. Head rotation to right [").append(String.format("%.2f", rotY)).append(" deg.]\n")
                        .append("3. Head tilted sideways [").append(String.format("%.2f", rotZ)).append(" deg.]\n");

                if (face.getSmilingProbability() != null) {
                    float smilingProbability = face.getSmilingProbability();
                    stringBuilder.append("4. Smiling probability [")
                            .append(String.format("%.2f", smilingProbability)).append("]\n");
                }

                if (face.getLeftEyeOpenProbability() != null) {
                    float leftEyeOpenProbability = face.getLeftEyeOpenProbability();
                    stringBuilder.append("5. Left eye open probability [")
                            .append(String.format("%.2f", leftEyeOpenProbability)).append("]\n");
                }

                if (face.getRightEyeOpenProbability() != null) {
                    float rightEyeOpenProbability = face.getRightEyeOpenProbability();
                    stringBuilder.append("6. Right eye open probability [")
                            .append(String.format("%.2f", rightEyeOpenProbability)).append("]\n");
                }

                stringBuilder.append("\n");
            }
            showDetectionResult("Face Detection", stringBuilder, true);
        } else {
            stringBuilder.append("No faces detected");
            showDetectionResult("Face Detection", stringBuilder, false);
        }
    }

    // Метод для отображения результатов обнаружения лиц
    private void showDetectionResult(final String title, final StringBuilder stringBuilder, final boolean isSuccess) {
        textView.setText(null);
        textView.setMovementMethod(new ScrollingMovementMethod());

        if (isSuccess) {
            textView.append(stringBuilder);
            textView.append("\n(Hold the text to copy)");

            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText(title, stringBuilder);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(MainActivity.this, "Text copied", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        } else {
            textView.append(stringBuilder);
        }
    }
}
