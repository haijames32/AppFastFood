package hainb21127.poly.appfastfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import hainb21127.poly.appfastfood.MainActivity;
import hainb21127.poly.appfastfood.R;

public class Success extends AppCompatActivity {
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

        if (check == 1) {
            tv_success.setText("Đăng ký thành công");
            btn_next.setText("Back To Login");
        } else if (check == 2) {
            tv_success.setText("Thay đổi mật khẩu thành công");
            btn_next.setText("Complete");
        } else if (check == 3) {
            tv_success.setText("Thay đổi thông tin thành công");
            btn_next.setText("Complete");
        } else if (check == 4) {
            tv_success.setText("Đặt hàng thành công");
            btn_next.setText("Complete");
        }
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == 1) {
                    startActivity(new Intent(Success.this, Login.class));
                    finish();
                } else if (check == 2) {
                    startActivity(new Intent(Success.this, MainActivity.class));
                    finish();
                } else if (check == 3) {
                    startActivity(new Intent(Success.this, MainActivity.class));
                    finish();
                } else if (check == 4) {
                    startActivity(new Intent(Success.this, MainActivity.class));
                    finish();
                }
            }
        });
    }
}