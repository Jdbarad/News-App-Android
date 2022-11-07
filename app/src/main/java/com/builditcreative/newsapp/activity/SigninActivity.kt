package com.builditcreative.newsapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.builditcreative.newsapp.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.hbb20.CountryCodePicker

class SigninActivity : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance();
    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    var FBDB: DatabaseReference = firebaseDatabase.getReference("User")
    val GOOGLE_SIGN_IN_CODE = 100
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    private lateinit var buttonFacebookLogin: LoginButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        if (firebaseAuth.uid!=null) {
            val intent = Intent(this, HomeActivity::class.java)
            this.startActivity(intent)
        }

        val nameLayout = findViewById<TextInputLayout>(R.id.name_layout)
        val nameText = findViewById<TextInputEditText>(R.id.name_text)
        val countryLayout = findViewById<TextInputLayout>(R.id.country_layout)
        val countryText = findViewById<TextInputEditText>(R.id.country_text)
        val mobileLayout = findViewById<TextInputLayout>(R.id.mobile_layout)
        val mobileText = findViewById<TextInputEditText>(R.id.mobile_text)
        val emailLayout = findViewById<TextInputLayout>(R.id.mail_layout)
        val emailText = findViewById<TextInputEditText>(R.id.mail_text)
        val passwordLayout = findViewById<TextInputLayout>(R.id.password_layout)
        val passwordText = findViewById<TextInputEditText>(R.id.password_text)
        val mailLoginLayout = findViewById<TextInputLayout>(R.id.mail_login_layout)
        val mailLoginText = findViewById<TextInputEditText>(R.id.mail_login_text)
        val passwordLoginLayout = findViewById<TextInputLayout>(R.id.password_login_layout)
        val passwordLoginText = findViewById<TextInputEditText>(R.id.password_login_text)
        val agreeCheckBox = findViewById<CheckBox>(R.id.temps_checkbox)
        buttonFacebookLogin = findViewById(R.id.facebook_login)
        val googleButton = findViewById<ImageView>(R.id.google_button)
        val facebookButton = findViewById<ImageView>(R.id.facebook_button)
        val googleLoginButton = findViewById<ImageView>(R.id.google_login_button)
        val facebookLoginButton = findViewById<ImageView>(R.id.facebook_login_button)
        val registerButton = findViewById<MaterialButton>(R.id.register_button)
        val loginButton = findViewById<MaterialButton>(R.id.login_button)
        val reset_text = findViewById<TextView>(R.id.reset_text)

        val loginText = findViewById<TextView>(R.id.title)

        val cpp:CountryCodePicker = findViewById(R.id.ccp)

        var mCustomBottomSheet = findViewById<ConstraintLayout>(R.id.custom_bottom_sheet)
        var mHeaderLayout = findViewById<LinearLayout>(R.id.header_layout)
        var mBottomSheetBehavior = BottomSheetBehavior.from<ConstraintLayout>(mCustomBottomSheet)
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)

        mHeaderLayout.setOnClickListener(View.OnClickListener {
            if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        })
        mBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (0==slideOffset.toInt())
                    loginText.text = "Already have a account,Login"
                else
                    loginText.text = "Create a new Account,Sign-in"
            }
        })

        //GoogleSignIn
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        //FacebookSignIn
        callbackManager = CallbackManager.Factory.create()

        buttonFacebookLogin.setReadPermissions("email", "public_profile")
        buttonFacebookLogin.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d("TAG", "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d("TAG", "facebook:onError", error)
            }
        })

//        loginText.setOnClickListener { view: View? ->
//            loginText.visibility = View.INVISIBLE
//            signinText.visibility = View.VISIBLE
//            loginButton.visibility = View.VISIBLE
//            registerButton.visibility = View.GONE
//            nameLayout.visibility = View.GONE
//            countryLayout.visibility = View.GONE
//            mobileLayout.visibility = View.GONE
//            reset_text.visibility = View.VISIBLE
//        }
//
//        signinText.setOnClickListener { view: View? ->
//            loginText.visibility = View.VISIBLE
//            signinText.visibility = View.INVISIBLE
//            loginButton.visibility = View.GONE
//            registerButton.visibility = View.VISIBLE
//            nameLayout.visibility = View.VISIBLE
//            countryLayout.visibility = View.VISIBLE
//            mobileLayout.visibility = View.VISIBLE
//            reset_text.visibility = View.GONE
//        }

        reset_text.setOnClickListener {
            if (mailLoginText.text.toString().isEmpty()) {
                Toast.makeText(this@SigninActivity, "Enter Mail Address", Toast.LENGTH_SHORT).show()
            } else {
                firebaseAuth.sendPasswordResetEmail(mailLoginText.text.toString())
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@SigninActivity,
                            "Reset Mail Sent Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }

        agreeCheckBox.text = Html.fromHtml(" I agree <a href=http://www.google.com>Terms & Conditions")
        agreeCheckBox.movementMethod = LinkMovementMethod.getInstance()
        mobileLayout.prefixText = cpp.defaultCountryCodeWithPlus
        countryText.setText(cpp.defaultCountryName.toString())

        countryText.setOnClickListener {
            cpp.launchCountrySelectionDialog()
        }

        cpp.setOnCountryChangeListener{
            mobileLayout.prefixText = cpp.selectedCountryCodeWithPlus
            countryText.setText(cpp.selectedCountryName.toString())
        }

        loginButton.setOnClickListener { view: View? ->
            if (mailLoginText.text.toString().isEmpty() ||
                passwordLoginText.text.toString().isEmpty()
            ) {
                Toast.makeText(this, "Enter Required Details", Toast.LENGTH_SHORT).show()
            } else {
                firebaseAuth.signInWithEmailAndPassword(
                    mailLoginText.text.toString(),
                    passwordLoginText.text.toString()
                )
                    .addOnSuccessListener {
                        startActivity(Intent(this@SigninActivity, HomeActivity::class.java))
                        finish()
                    }.addOnFailureListener { e ->
                        Toast.makeText(this@SigninActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }

        registerButton.setOnClickListener { view: View? ->
            if (nameText.text.toString().isEmpty() ||
                countryText.text.toString().isEmpty() ||
                mobileText.text.toString().isEmpty() ||
                emailText.text.toString().isEmpty() ||
                passwordText.text.toString().isEmpty() ||
                !agreeCheckBox.isChecked
            ) {
                Toast.makeText(this, "Enter Required Details", Toast.LENGTH_SHORT).show()
            } else {
                firebaseAuth.createUserWithEmailAndPassword(
                    emailText.text.toString(),
                    passwordText.text.toString()
                )
                    .addOnSuccessListener {
                        //Store data in database
                        val data =
                            HashMap<String, Any>()
                        data["Name"] = nameText.text.toString()
                        data["Surname"] = countryText.text.toString()
                        data["Telephone"] = mobileText.text.toString()
                        data["Email"] = emailText.text.toString()
                        data["UID"] = firebaseAuth.uid.toString()
                        FBDB.child(data["UID"].toString()).updateChildren(data)
                            .addOnSuccessListener(OnSuccessListener<Void?> { //Redirect into Home Screen
                                startActivity(
                                    Intent(
                                        this@SigninActivity,
                                        HomeActivity::class.java
                                    )
                                )
                                finish()
                            }).addOnFailureListener(OnFailureListener { e ->
                                Toast.makeText(
                                    this@SigninActivity,
                                    e.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                    }.addOnFailureListener { e ->
                        Toast.makeText(this@SigninActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }

        googleButton.setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN_CODE)
        }

        facebookButton.setOnClickListener{
            buttonFacebookLogin.performClick()
        }

        googleLoginButton.setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN_CODE)
        }

        facebookLoginButton.setOnClickListener{
            buttonFacebookLogin.performClick()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
        callbackManager.onActivityResult(requestCode, resultCode, data)

    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val data =
                            HashMap<String, Any>()
                        data["Name"] = account.displayName.toString()
                        data["Email"] = account.email.toString()
                        data["UID"] = firebaseAuth.uid.toString()
                        FBDB.child(data["UID"].toString()).updateChildren(data)
                            .addOnSuccessListener(OnSuccessListener<Void?> { //Redirect into Home Screen
                                startActivity(
                                    Intent(
                                        this@SigninActivity,
                                        HomeActivity::class.java
                                    )
                                )
                                finish()
                            }).addOnFailureListener(OnFailureListener { e ->
                                Toast.makeText(
                                    this@SigninActivity,
                                    e.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                    }
                }
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val data =
                        HashMap<String, Any>()
                    data["Name"] = task.result.user?.displayName.toString()
                    data["Email"] = task.result.user?.email.toString()
                    data["UID"] = firebaseAuth.uid.toString()
                    FBDB.child(data["UID"].toString()).updateChildren(data)
                        .addOnSuccessListener(OnSuccessListener<Void?> { //Redirect into Home Screen
                            startActivity(
                                Intent(
                                    this@SigninActivity,
                                    HomeActivity::class.java
                                )
                            )
                            finish()
                        }).addOnFailureListener(OnFailureListener { e ->
                            Toast.makeText(
                                this@SigninActivity,
                                e.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        })

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

}