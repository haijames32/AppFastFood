package hainb21127.poly.appfastfood.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import hainb21127.poly.appfastfood.MainActivity;
import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.activity.Login;
import hainb21127.poly.appfastfood.activity.Register;
import hainb21127.poly.appfastfood.model.User_Register;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    TextView tvFullname, tvEmail, tvPhone, tvAddress;
    GoogleSignInClient mGoogleSignInClient;
    LinearLayout lo_profile, btn_logout;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lo_profile = view.findViewById(R.id.id_profile);
        btn_logout = view.findViewById(R.id.btn_logout);
        tvEmail = view.findViewById(R.id.tv_email_profile);
        tvFullname = view.findViewById(R.id.tv_fullname_profile);
        tvPhone = view.findViewById(R.id.tv_phone_profile);
        tvAddress = view.findViewById(R.id.tv_address_profile);

        // Tạo client Google Sign In
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.i("TAG", "onViewCreated: "+user.getUid());
        if(user != null){
            database = FirebaseDatabase.getInstance();
            String id = user.getUid();
            reference = database.getReference("users").child(id);
            tvEmail.setText(user.getEmail());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        User_Register userRegister = snapshot.getValue(User_Register.class);
                        String name = userRegister.getFullname();
                        int phone = userRegister.getPhone();
                        String address = userRegister.getAddress();
                        tvFullname.setText(name);
                        tvPhone.setText("0"+phone);
                        tvAddress.setText(address);
                    }else{
                        Log.i("TAG", "onDataChange: Không lấy được thông tin người dùng");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("TAG", "onCancelled: "+error.toString());
                }
            });
        }




//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_signin_google", Context.MODE_PRIVATE);
//        String idu = sharedPreferences.getString("idUser","");
//        String name = sharedPreferences.getString("displayname","");
//        String email = sharedPreferences.getString("email","");
//
//
//        tvEmail.setText(email);
//        tvFullname.setText(name);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                MainActivity.isLoggedIn = false;
                startActivity(new Intent(getActivity(), Login.class));
            }
        });
    }
}