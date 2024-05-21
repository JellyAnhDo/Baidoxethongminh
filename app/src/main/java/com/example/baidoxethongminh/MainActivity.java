package com.example.baidoxethongminh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private ImageButton lightBtn;
    private Switch lightAutoBtn;
    private TextView slxe;
    private ImageView space1, space2, space3, space4;
    private boolean isButtonOn = false;
    // Biến đánh dấu trạng thái của nút (on/off)
    FirebaseDatabase database;
    DatabaseReference myRefLightBtn, myRefLightAuto, myRefSpaceCount, myRefSpace1, myRefSpace2, myRefSpace3, myRefSpace4,  myRefFire;

    // Khai báo biến toàn cục để lưu trữ AlertDialog
    private AlertDialog alertDialog;
    private MediaPlayer mediaFirePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Ánh xạ các phần tử
        inti();

        // trỏ tới cơ sở dữ liệu
        database = FirebaseDatabase.getInstance();
        myRefLightBtn = database.getReference("Light/LightBtn");
        myRefLightAuto = database.getReference("Light/LightAuto");
        myRefSpaceCount = database.getReference("ParkingSpace/ParkingSpaceCount");
        myRefSpace1 = database.getReference("ParkingSpace/Space1");
        myRefSpace2 = database.getReference("ParkingSpace/Space2");
        myRefSpace3 = database.getReference("ParkingSpace/Space3");
        myRefSpace4 = database.getReference("ParkingSpace/Space4");
        myRefFire = database.getReference("FireSensor");

        myRefFire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer fireValue = dataSnapshot.getValue(Integer.class);
                if (fireValue == 0) {
                    showFireAlert();
                } else {
                    dismissFireAlert();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });



        //Điều khiển đèn

        myRefLightBtn.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer lightValue = dataSnapshot.getValue(Integer.class);
                if(lightValue == 1) {
                    lightBtn.setImageResource(R.drawable.img_btnon);
                } else {
                    lightBtn.setImageResource(R.drawable.img_btnoff);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        myRefLightAuto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer lightAutoValue = dataSnapshot.getValue(Integer.class);
                if (lightAutoValue == 1){
                    lightAutoBtn.setChecked(true);
                } else {
                    lightAutoBtn.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đảo ngược trạng thái khi nút được ấn
                isButtonOn = !isButtonOn;

                // Thực hiện hành động tương ứng với trạng thái mới
                if (isButtonOn) {
                    // Thực hiện hành động khi nút là "on"
                    myRefLightBtn.setValue(1);
                    lightBtn.setImageResource(R.drawable.img_btnon);
                } else {
                    // Thực hiện hành động khi nút là "off"
                    myRefLightBtn.setValue(0);
                    lightBtn.setImageResource(R.drawable.img_btnoff);
                }

            }
        });

        lightAutoBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myRefLightAuto.setValue(1);
                }
                else {
                    myRefLightAuto.setValue(0);
                }
            }
        });




        //Tình trạng chỗ đỗ xe
        myRefSpaceCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer value = dataSnapshot.getValue(Integer.class);
                switch (value) {
                    case 1:
                        slxe.setText("1/4");
                        break;
                    case 2:
                        slxe.setText("2/4");
                        break;
                    case 3:
                        slxe.setText("3/4");
                        break;
                    case 4:
                        slxe.setText("4/4");
                        break;
                    default:
                        slxe.setText("0/4");
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }
        });



        //Hiển thị thông tin chỗ đỗ xe
        myRefSpace1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer spaceValue = dataSnapshot.getValue(Integer.class);
                // Thực hiện hành động tùy thuộc vào giá trị đọc được
                if (spaceValue == 1) {
                    // Đổi background của TextView1 khi giá trị là 1
                    space1.setBackgroundResource(R.drawable.img_4);
                } else {
                    // Đổi background của TextView1 khi giá trị không phải là 1
                    space1.setBackgroundResource(R.drawable.custom_background);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Failed to read value.", error.toException());
            }
        });
        myRefSpace2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer spaceValue = dataSnapshot.getValue(Integer.class);
                // Thực hiện hành động tùy thuộc vào giá trị đọc được
                if (spaceValue == 1) {
                    // Đổi background của TextView1 khi giá trị là 1
                    space2.setBackgroundResource(R.drawable.img_4);
                } else {
                    // Đổi background của TextView1 khi giá trị không phải là 1
                    space2.setBackgroundResource(R.drawable.custom_background);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Failed to read value.", error.toException());
            }
        });

        myRefSpace3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer spaceValue = dataSnapshot.getValue(Integer.class);
                // Thực hiện hành động tùy thuộc vào giá trị đọc được
                if (spaceValue == 1) {
                    // Đổi background của TextView1 khi giá trị là 1
                    space3.setBackgroundResource(R.drawable.img_4);
                } else {
                    // Đổi background của TextView1 khi giá trị không phải là 1
                    space3.setBackgroundResource(R.drawable.custom_background);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Failed to read value.", error.toException());
            }
        });
        myRefSpace4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer spaceValue = dataSnapshot.getValue(Integer.class);
                // Thực hiện hành động tùy thuộc vào giá trị đọc được
                if (spaceValue == 1) {
                    // Đổi background của TextView1 khi giá trị là 1
                    space4.setBackgroundResource(R.drawable.img_4);
                } else {
                    // Đổi background của TextView1 khi giá trị không phải là 1
                    space4.setBackgroundResource(R.drawable.custom_background);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Failed to read value.", error.toException());
            }
        });
    }



    private void inti(){
        lightBtn= findViewById(R.id.battatden);
        lightAutoBtn= findViewById(R.id.dieukhienden);
        slxe= findViewById(R.id.soluongxe);
        space1 = findViewById(R.id.xe1);
        space2 = findViewById(R.id.xe2);
        space3 = findViewById(R.id.xe3);
        space4 = findViewById(R.id.xe4);

    }


    // Hàm hiển thị layout thông báo cháy
    private void showFireAlert() {
        // Tạo AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Gán giao diện từ fire_alert_layout.xml
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.baochay, null);
        builder.setView(dialogView);

        // Thiết lập các thành phần giao diện cần thiết
        ImageButton closeBtn = dialogView.findViewById(R.id.closeButton);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Đóng AlertDialog khi nút Đóng được nhấn
                dismissFireAlert();
            }
        });

        //Phát âm thanh báo cháy
        playFireAlarmSound();

        // Hiển thị AlertDialog
        alertDialog = builder.create();
        alertDialog.show();
    }

    // Hàm phát âm thanh báo cháy
    private void playFireAlarmSound() {
        mediaFirePlayer = MediaPlayer.create(this, R.raw.fire_alarm);

        // Thêm lắng nghe sự kiện kết thúc để lặp lại âm thanh
        mediaFirePlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playFireAlarmSound(); // Khi kết thúc, bắt đầu lại từ đầu
            }
        });

        mediaFirePlayer.start();
    }

    // Hàm tắt thông báo
    private void dismissFireAlert() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        // Kiểm tra và dừng âm thanh nếu đang phát
        if (mediaFirePlayer != null && mediaFirePlayer.isPlaying()) {
            mediaFirePlayer.stop();
            mediaFirePlayer.release();
            mediaFirePlayer = null;
        }
    }
}