package com.example.facy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn;
    TextView textView;
    ImageView imageView;

    private final static int REQUEST_IMAGE_CAPTURE = 124;
    InputImage firebaseVisionImage;
    FaceDetection visionFaceDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.cameraBtn);
        textView = findViewById(R.id.text1);
        imageView = findViewById(R.id.image);

        FirebaseApp.initializeApp(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFile();
            }
        });
        Toast.makeText(this, "App is started", Toast.LENGTH_SHORT).show();
    }

    private void OpenFile() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");
        FaceDetectionProcess(bitmap);
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }

    private void FaceDetectionProcess(Bitmap bitmap) {
        textView.setText("Face Detection");
        final StringBuilder stringBuilder = new StringBuilder();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        FaceDetectorOptions highAccuracyOpt = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .enableTracking().build();
        FaceDetector detector = FaceDetection.getClient(highAccuracyOpt);
        Task<List<Face>> result = detector.process(image);
        result.addOnSuccessListener(new OnSuccessListener<List<Face>>() {
            @Override
            public void onSuccess(List<Face> faces) {
                //tilting and rotation detection
                if (faces.size() != 0) {
                    if (faces.size() == 1) {
                        stringBuilder.append(faces.size() + "Faces detected\n\n");
                    } else if (faces.size() > 1) {
                        stringBuilder.append(faces.size() + "Faces detected");
                    }
                }
                for (Face face : faces) {
                    int id = face.getTrackingId();
                    float rotY = face.getHeadEulerAngleY();
                    float rotZ = face.getHeadEulerAngleZ();
                    stringBuilder.append("1. Face tracking ID [" + id + "]\n");
                    stringBuilder.append("2. Head rotation to right ["
                            + String.format("%.2f", rotY) + " deg. ]\n");
                    stringBuilder.append("3. Head tilted sideways ["
                            + String.format("%.2f", rotZ) + " deg. ]\n");
                    // smiling probability
                    if (face.getSmilingProbability() > 0) {
                        float smilingProbability = face.getSmilingProbability();
                        stringBuilder.append("4. Smiling probability ["
                                + String.format("%.2f", smilingProbability) + "]\n");
                    }
                    // left eye open probability
                    if (face.getLeftEyeOpenProbability() > 0) {
                        float leftEyeOpenProbability = face.getLeftEyeOpenProbability();
                        stringBuilder.append("4. Left eye open probability ["
                                + String.format("%.2f", leftEyeOpenProbability) + "]\n");
                    }
                    // right eye open probability
                    if (face.getRightEyeOpenProbability() > 0) {
                        float rightEyeOpenProbability = face.getRightEyeOpenProbability();
                        stringBuilder.append("4. Right eye open probability ["
                                + String.format("%.2f", rightEyeOpenProbability) + "]\n");
                    }
                    stringBuilder.append("\n");
                }
                ShowDetection("Face Detection", stringBuilder, true);
            }
        });
        result.addOnFailureListener(e -> {
            StringBuilder builderFailure = new StringBuilder();
            builderFailure.append("Face Detection Failed");
            ShowDetection("Face Detection", builderFailure, false);
        });
    }

    public void ShowDetection(final String title, final StringBuilder stringBuilder, final boolean isSuccess) {

    }
}