package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StudentApprovedRequestActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<PassClass> arrayList;
    PassFirebaseAdapterTwo adapter;
    FirebaseFirestore fStore;

    TextView noDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_approved_request);

        noDataTextView = findViewById(R.id.noDataTextView);
        recyclerView = findViewById(R.id.stuPassView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        fStore = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();

            fStore.collection("Users")
                    .whereEqualTo("Email",userEmail)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                                DocumentSnapshot userDoc = task.getResult().getDocuments().get(0);
                                String rollNum = userDoc.getString("RollNum");
                                fetchPassesForUser(rollNum);
                            } else {
                                Toast.makeText(getApplicationContext(), "Error fetching data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private void fetchPassesForUser(String rollNum) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault());
        String currentDateTime = dateFormatter.format(calendar.getTime());

        fStore.collection("Pass")
                .whereEqualTo("RollNum",rollNum)
                .whereEqualTo("passStatus",true)
                .whereGreaterThan("DateTime", currentDateTime)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String rollNum = document.getString("RollNum");
                                String parentNo = document.getString("ParentNo");
                                String dateTime = document.getString("DateTime");
                                String reason = document.getString("Reason");
                                boolean passStatus = document.getBoolean("passStatus");
                                String approved;
                                if (passStatus) {
                                    approved = "Approved";
                                } else {
                                    approved = "Not Approved";
                                }

                                String qrCodeData = "Roll No: " + rollNum + "\nParent No: " + parentNo + "\nDate: " + dateTime + "\nStatus: " + approved;
                                Bitmap qrCodeImage = generateQRCode(qrCodeData);

                                PassClass showPass = new PassClass(rollNum, reason, parentNo, dateTime, passStatus,qrCodeImage);
                                //showPass.setQrCodeImage(qrCodeImage);

                                arrayList.add(showPass);
                            }

                            if (arrayList.isEmpty()) {
                                noDataTextView.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                adapter = new PassFirebaseAdapterTwo(getApplicationContext(), arrayList);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                noDataTextView.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error fetching Pass data.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private Bitmap generateQRCode(String data) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, 512, 512, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}