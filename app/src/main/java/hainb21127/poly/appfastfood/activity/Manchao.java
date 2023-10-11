package hainb21127.poly.appfastfood.activity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import hainb21127.poly.appfastfood.MainActivity;
import hainb21127.poly.appfastfood.R;

public class Manchao extends AppCompatActivity {
Button btn_getstart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manchao);
        btn_getstart=findViewById(R.id.btn_getstart);
//        btn_getstart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//             Intent intent = new Intent(Manchao.this, MainActivity.class);
//             startActivity(intent);
//            }
//        });
    }
}