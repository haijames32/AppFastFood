package hainb21127.poly.appfastfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import hainb21127.poly.appfastfood.MainActivity;
import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.fragment.ProfileFragment;

public class success extends AppCompatActivity {
    Button btn_next;
    int check;
    TextView tv_success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        btn_next = findViewById(R.id.btn_next);
        tv_success = findViewById(R.id.tv_success);
        Intent intent = getIntent();
        check = intent.getIntExtra("checkman", 0);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == 1) {
                    Intent intent1 = new Intent(success.this, Login.class);
                    tv_success.setText("Đăng ký tài khoản thành công");
                    startActivity(intent1);
                    finish();
                } else if (check == 2) {
                    Intent intent2 = new Intent(success.this, MainActivity.class);
                    startActivity(intent2);
                } else if (check == 3) {
                    Intent intent3 = new Intent(success.this, MainActivity.class);
                    startActivity(intent3);
                }
            }
        });
    }
}