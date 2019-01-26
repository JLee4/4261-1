package assignment1.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mLoginEmailView;
    private AutoCompleteTextView mRegisterEmailView;
    private EditText mLoginPasswordView;
    private EditText mRegisterPasswordView;

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize firebase auth with the current instance
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user is non-null so there is someone signed in
                    //eventually do something with this
                    Log.d("LoginActivity", "mAuthListener:onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    //user is signed out
                    Log.d("LoginActivity", "mAuthListener:onAuthStateChanged:signed_out");
                }
            }
        };
    }

    /**
     *Start the Activity
     */
    @Override
    public void onStart() {
        super.onStart();

        //attach listener to the firebase auth
        mAuth.addAuthStateListener(mAuthListener);
    }


    /**
     * Stop the Activity
     */
    @Override
    public void onStop() {
        super.onStop();

        //remove listener from firebase auth
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * called when user click the login button
     * @param v represents the Login button
     */
    public void onLoginButtonClicked(View v) {
        setContentView(R.layout.activity_login);
        mLoginEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        mLoginPasswordView = (EditText) findViewById(R.id.login_password);
    }

    /**
     * called when user click the register button
     * @param v represents the Register button
     */
    public void onRegisterButtonClicked(View v) {
        setContentView(R.layout.activity_register);
        mRegisterEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mRegisterPasswordView = (EditText) findViewById(R.id.register_password);
    }

    /**
     * runs after user click the create button
     * @param v represents the Register button
     */
    public void createAccount(View v) {
        mRegisterEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mRegisterPasswordView = (EditText) findViewById(R.id.register_password);
        mAuth.createUserWithEmailAndPassword(mRegisterEmailView.getText().toString(), mRegisterPasswordView.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("LoginActivity", "mAuth:createUser:onComplete::" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            //the creation failed
                            Toast.makeText(LoginActivity.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    /**
     * runs after user click the SignIn button
     * @param v represents the SignIn button
     */
    public void signIn(View v) {
        mLoginEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        mLoginPasswordView = (EditText) findViewById(R.id.login_password);
        mAuth.signInWithEmailAndPassword(mLoginEmailView.getText().toString(), mLoginPasswordView.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        Log.d("LoginActivity", "mAuth:signIn:onComplete::" + task.isSuccessful());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
