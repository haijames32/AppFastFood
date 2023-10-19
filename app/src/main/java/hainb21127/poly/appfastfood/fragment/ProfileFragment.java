package hainb21127.poly.appfastfood.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import hainb21127.poly.appfastfood.MainActivity;
import hainb21127.poly.appfastfood.R;
import hainb21127.poly.appfastfood.activity.Login;
import hainb21127.poly.appfastfood.activity.Register;


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

    TextView tvFullname, tvEmail, tvUsername;
    GoogleSignInClient mGoogleSignInClient;
    LinearLayout lo_check, lo_profile, btn_logout;
    Button login, register;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lo_check = view.findViewById(R.id.checklogin_profile);
        lo_profile = view.findViewById(R.id.id_profile);
        btn_logout = view.findViewById(R.id.btn_logout);
        login = view.findViewById(R.id.btn_login_profile);
        register = view.findViewById(R.id.btn_register_profile);
        tvEmail = view.findViewById(R.id.tv_email_profile);
        tvFullname = view.findViewById(R.id.tv_fullname_profile);

        // Tạo client Google Sign In
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build());



//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), Login.class);
//                startActivity(intent);
//            }
//        });
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), Register.class);
//                startActivity(intent);
//            }
//        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                MainActivity.isLoggedIn = false;
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_signin_google", Context.MODE_PRIVATE);
        String idu = sharedPreferences.getString("idUser","");
        String name = sharedPreferences.getString("displayname","");
        String email = sharedPreferences.getString("email","");


        tvEmail.setText(email);
        tvFullname.setText(name);

    }
}