package lk.kdu.buslocator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikepenz.materialdrawer.Drawer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {

    private EditText mRoute, mStart, mDest;
    private Button mConfirm, mChange;
    private FirebaseAuth mAuth;
    private DatabaseReference mPassengerDatabase;
    private String userId, routeNo, routeStart, routeEnd;
    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        NavDrawer nd = new NavDrawer();
        result = nd.getDrawer(this, savedInstanceState);

        mRoute = findViewById(R.id.route);
        mStart = findViewById(R.id.depart);
        mDest = findViewById(R.id.dest);
        mConfirm = findViewById(R.id.settingsConfirm);
        mChange = findViewById(R.id.change);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mPassengerDatabase = FirebaseDatabase.getInstance().getReference().child("Buses").child(userId);

        getBusInfo();

        mChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp=mStart.getText().toString();
                mStart.setText(mDest.getText().toString());
                mDest.setText(temp);
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
                Toast.makeText(getApplicationContext(), "Details updated", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getBusInfo() {
        mPassengerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("routeNo") != null) {
                        routeNo = map.get("routeNo").toString();
                        mRoute.setText(routeNo);
                    }
                    if (map.get("depart") != null) {
                        routeStart = map.get("depart").toString();
                        mStart.setText(routeStart);
                    }
                    if (map.get("destination") != null) {
                        routeEnd = map.get("destination").toString();
                        mDest.setText(routeEnd);;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveUserInfo() {
        routeNo= mRoute.getText().toString();
        routeStart = mStart.getText().toString();
        routeEnd = mDest.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("routeNo", routeNo);
        userInfo.put("depart", routeStart);
        userInfo.put("destination", routeEnd);
        mPassengerDatabase.updateChildren(userInfo);
    }
}
