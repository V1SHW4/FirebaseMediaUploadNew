package lk.techseya.mediauploadfirebase;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {
    //Adding up activity result launcher
    ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //calling the TextView and ImageView
        ImageView uploadButton=findViewById(R.id.uploadButton);
        TextView dLink=findViewById(R.id.dLink);

        //Setting up ActivityResult Launcher
        //This is the new method istead of onActivityResult deprecated method
        mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        //Creating path in Firebase Storage
                        StorageReference Folder= FirebaseStorage.getInstance().getReference().child("images");
                        StorageReference imagename=Folder.child("image"+result.getLastPathSegment());
                        uploadButton.setImageURI(result);
                        imagename.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //Getting Download Link
                                imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        dLink.setText(uri.toString());
                                        //If you need you can send download link to Realtime Database
                                    }
                                });
                            }
                        });

                    }
                });

                //Setting up Image Onlick Listner
                uploadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mGetContent.launch("image/*");
                    }
                });
    }
}